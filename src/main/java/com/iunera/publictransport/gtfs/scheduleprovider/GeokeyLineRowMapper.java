package com.iunera.publictransport.gtfs.scheduleprovider;

/*-
 * #%L
 * iu-masterdata-gtfs-static
 * %%
 * Copyright (C) 2024 Tim Frey, Christian Schmitt
 * %%
 * Licensed under the OPEN COMPENSATION TOKEN LICENSE (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * <https://github.com/open-compensation-token-license/license/blob/main/LICENSE.md>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @octl.sid: 1b6f7a5d-8dcf-44f1-b03a-77af04433496
 * #L%
 */

import com.iunera.publictransport.domain.TransportProductDTO;
import com.iunera.publictransport.keys.RideKeysGeneration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.jdbc.core.RowMapper;

public class GeokeyLineRowMapper implements RowMapper<Collection<Map.Entry<String, String>>> {

  private Set<String> mappedColumns;
  private ZoneId timeZone;
  private int bucketsize;

  public GeokeyLineRowMapper(ZoneId timeZone, int bucketsize, String... columns) {
    this.mappedColumns = new HashSet<>(Arrays.asList(columns));
    this.timeZone = timeZone;
    this.bucketsize = bucketsize;
  }

  @Override
  public Collection<Map.Entry<String, String>> mapRow(ResultSet rs, int rowNum)
      throws SQLException {

    Coordinate stopccordinate = new Coordinate();
    stopccordinate.x = rs.getDouble("stop_lon");
    stopccordinate.y = rs.getDouble("stop_lat");

    // generate the nearest buckets for the departure stop
    String[] nearestbuckets =
        RideKeysGeneration.getNearestTimeBucketsKeys(
            RideKeysGeneration.CoordinateFunctionStop,
            stopccordinate.x,
            stopccordinate.y,
            7,
            StaticGtfsApi.toLocalTime(rs.getString("departure_time")),
            null,
            TransportProductDTO.BUS,
            RideKeysGeneration.directionUNKNOWN,
            this.bucketsize);

    String route_id = rs.getString("route_id");
    List<Map.Entry<String, String>> ret = new ArrayList(3);
    for (String bucket : nearestbuckets) {
      ret.add(Map.entry(bucket, route_id));
    }

    return ret;
  }
}
