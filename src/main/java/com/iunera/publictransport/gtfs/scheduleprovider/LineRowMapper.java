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

import com.iunera.publictransport.domain.route.Route;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LineRowMapper implements RowMapper<Route> {

  LineRowMapper() {}

  @Override
  public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
    Route line = new Route();
    line.line_routeid = rs.getString("route_id");
    line.line_long_name = rs.getString("route_long_name");
    line.lineName = rs.getString("route_short_name");
    line.line = rs.getString("route_id");
    line.line_id = rs.getString("route_id");
    line.lineLabel = rs.getString("route_short_name");
    line.lineRef = rs.getString("route_id");
    line.color = rs.getString("route_color");
    line.route_text_color = rs.getString("route_text_color");
    line.route_type = rs.getString("route_type");

    return line;
  }
}
