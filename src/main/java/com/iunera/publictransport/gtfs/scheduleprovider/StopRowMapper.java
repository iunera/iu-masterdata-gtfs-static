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

import com.iunera.publictransport.domain.stop.PlannedStop;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class StopRowMapper implements RowMapper<PlannedStop> {

  StopRowMapper() {}

  @Override
  public PlannedStop mapRow(ResultSet rs, int rowNum) throws SQLException {
    PlannedStop stop = new PlannedStop();
    if (!(rs.getString("stop_lat").equals("")) && !(rs.getString("stop_lon").equals(""))) {
      stop.latitude = Double.parseDouble(rs.getString("stop_lat"));
      stop.longitude = Double.parseDouble(rs.getString("stop_lon"));
    }

    stop.parentStationIFOPT = rs.getString("parent_station");
    stop.name = rs.getString("stop_name");
    stop.IFOPT = rs.getString("stop_id");
    String code = rs.getString("stop_code");
    String location_type = rs.getString("location_type");
    if (location_type != null && !location_type.isEmpty()) stop.isParent = true;
    return stop;
  }
}
