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

import com.iunera.publictransport.domain.route.LineStop;
import com.iunera.publictransport.domain.stop.PlannedStop;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public final class LineWithStopRowMapper implements RowMapper<LineStop> {

  LineWithStopRowMapper() {}

  @Override
  public LineStop mapRow(ResultSet rs, int rowNum) throws SQLException {
    LineStop line = new LineStop();
    line.line_routeid = rs.getString("route_id");
    line.lineName = rs.getString("route_short_name");
    line.line = rs.getString("route_id");
    line.line_id = rs.getString("route_id");
    line.lineLabel = rs.getString("route_short_name");
    line.lineRef = rs.getString("route_id");
    PlannedStop stop = new PlannedStop();
    //		TODO: org.postgresql.util.PSQLException: Bad value for type double
    if (!(rs.getString("stop_lat").equals("")) && !(rs.getString("stop_lon").equals(""))) {
      stop.latitude = Double.parseDouble(rs.getString("stop_lat"));
      stop.longitude = Double.parseDouble(rs.getString("stop_lon"));
    }
    stop.name = rs.getString("stop_name");
    line.stop = stop;

    return line;
  }
}
