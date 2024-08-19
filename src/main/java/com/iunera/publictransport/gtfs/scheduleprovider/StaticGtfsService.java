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
import com.iunera.publictransport.domain.route.Route;
import com.iunera.publictransport.domain.stop.PlannedStop;
import com.iunera.publictransport.gtfs.scheduleprovider.queryresultmodels.RegularTrip;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class StaticGtfsService {
  @Autowired private JdbcTemplate jdbcTemplate;

  // static ZoneId timeZone = ZoneId.of("Europe/Berlin");

  ////////////////////////////////////////////////////////////////////////////////////
  /////////////// Departure from stations
  //////////////////////////////////////////////////////////////////////////////////////

  private static String regularDayDepartureQuerySimple =
      "SELECT distinct r.route_id,\r\n"
          + "start_st.departure_time,start_st.arrival_time, start_s.stop_name as departure_stop,\r\n"
          + "start_s.stop_lat as stop_lat, start_s.stop_lon as stop_lon, key.timezone, agency.agency_name FROM\r\n"
          + "trips t INNER JOIN routes r ON t.route_id = r.route_id\r\n"
          + "    r.route_type,  start_st.stop_sequence,"
          + "INNER JOIN stop_times start_st ON t.trip_id = start_st.trip_id\r\n"
          + "INNER JOIN stops start_s ON start_st.stop_id = start_s.stop_id\r\n"
          + "INNER JOIN iu_importprocess key ON r.iu_importprocess = key.iu_importprocess\r\n"
          + "INNER JOIN agency agency_id ON r.agency_id = agency.agency_id\r\n"
          + "WHERE key.provider_name = ?\r\n";

  String regularDayDepartureQueryWithFullDetails =
      "SELECT distinct r.route_id, t.trip_id,\r\n"
          + "       t.trip_short_name, start_st.departure_time,start_st.arrival_time,\r\n"
          + "       start_s.stop_name as departure_stop, start_st.stop_headsign,\r\n"
          + "       start_s.zone_id, direction_id as direction, r.route_short_name,\r\n"
          + "       r.route_long_name, start_s.stop_id as departure_stop_id,\r\n"
          + "    r.route_type,  start_st.stop_sequence,"
          + "       c.start_date, c.end_date, c.monday, c.tuesday,\r\n"
          + "       c.wednesday, c.thursday, c.friday, c.saturday,\r\n"
          + "       c.sunday, start_s.stop_lat as stop_lat, start_s.stop_lon as stop_lon, key.timezone , agency.agency_name FROM\r\n"
          + "trips t INNER JOIN routes r ON t.route_id = r.route_id\r\n"
          + "		   INNER JOIN calendar c ON t.service_id = c.service_id\r\n"
          + "        INNER JOIN stop_times start_st ON t.trip_id = start_st.trip_id\r\n"
          + "        INNER JOIN stops start_s ON start_st.stop_id = start_s.stop_id\r\n"
          + "        INNER JOIN iu_importprocess key ON r.iu_importprocess = key.iu_importprocess\r\n"
          + "INNER JOIN agency agency ON r.agency_id = agency.agency_id\r\n"
          + "WHERE key.provider_name = ?\r\n"
          + "AND c.start_date IS NOT NULL AND c.end_date IS NOT NULL\r\n"
          + "AND c.monday|c.tuesday| c.wednesday| c.thursday| c.friday| c.saturday|c.sunday <> 0 \r\n";

  String agency_name_wherecondition = "AND agency.agency_name= ? \r\n";

  public List<RegularTrip> getRegularTrips(String provider, String agencyName) {
    List<RegularTrip> rows = null;
    if (agencyName != null) {
      rows =
          jdbcTemplate.query(
              regularDayDepartureQuerySimple + agency_name_wherecondition,
              new RegularTripRowMapper2(
                  "route_id",
                  "departure_time",
                  "arrival_time",
                  "departure_stop",
                  "stop_lat",
                  "stop_lon",
                  "timezone"),
              provider,
              agencyName);
    } else {
      rows =
          jdbcTemplate.query(
              regularDayDepartureQuerySimple,
              new RegularTripRowMapper2(
                  "route_id",
                  "departure_time",
                  "arrival_time",
                  "departure_stop",
                  "stop_lat",
                  "stop_lon",
                  "timezone"),
              provider);
    }

    System.out.println(
        "All-----regularDayDepartureQuerySimple " + regularDayDepartureQuerySimple + rows.size());

    return rows;
  }

  public List<RegularTrip> getRegularTripsWithDetails(
      String provider, String agencyName, Float minlon, Float maxlon, Float minlat, Float maxlat) {
    List<RegularTrip> rows = null;
    if (agencyName != null) {
      if (minlon != null && maxlon != null && minlat != null && maxlat != null) {

        rows =
            jdbcTemplate.query(
                regularDayDepartureQueryWithFullDetails + agency_name_wherecondition + bboxwhere,
                new RegularTripRowMapper2(
                    "route_id",
                    "trip_id",
                    "trip_short_name",
                    "departure_time",
                    "arrival_time",
                    "departure_stop",
                    "stop_headsign",
                    "zone_id",
                    "direction",
                    "route_short_name",
                    "route_type",
                    "stop_sequence",
                    "start_date",
                    "end_date",
                    "monday",
                    "tuesday",
                    "wednesday",
                    "thursday",
                    "friday",
                    "saturday",
                    "sunday",
                    "stop_lat",
                    "stop_lon",
                    "timezone"),
                provider,
                agencyName,
                minlon,
                maxlon,
                minlat,
                maxlat);

      } else {
        rows =
            jdbcTemplate.query(
                regularDayDepartureQueryWithFullDetails + agency_name_wherecondition,
                new RegularTripRowMapper2(
                    "route_id",
                    "trip_id",
                    "trip_short_name",
                    "departure_time",
                    "arrival_time",
                    "departure_stop",
                    "stop_headsign",
                    "zone_id",
                    "direction",
                    "route_short_name",
                    "route_long_name",
                    "departure_stop_id",
                    "route_type",
                    "stop_sequence",
                    "start_date",
                    "end_date",
                    "monday",
                    "tuesday",
                    "wednesday",
                    "thursday",
                    "friday",
                    "saturday",
                    "sunday",
                    "stop_lat",
                    "stop_lon",
                    "timezone"),
                provider,
                agencyName);
      }
    } else {
      if (minlon != null && maxlon != null && minlat != null && maxlat != null) {

        rows =
            jdbcTemplate.query(
                regularDayDepartureQueryWithFullDetails + bboxwhere,
                new RegularTripRowMapper2(
                    "route_id",
                    "trip_id",
                    "trip_short_name",
                    "departure_time",
                    "arrival_time",
                    "departure_stop",
                    "stop_headsign",
                    "zone_id",
                    "direction",
                    "route_short_name",
                    "route_long_name",
                    "departure_stop_id",
                    "route_type",
                    "stop_sequence",
                    "start_date",
                    "end_date",
                    "monday",
                    "tuesday",
                    "wednesday",
                    "thursday",
                    "friday",
                    "saturday",
                    "sunday",
                    "stop_lat",
                    "stop_lon",
                    "timezone"),
                provider,
                minlon,
                maxlon,
                minlat,
                maxlat);
      } else {
        rows =
            jdbcTemplate.query(
                regularDayDepartureQueryWithFullDetails,
                new RegularTripRowMapper2(
                    "route_id",
                    "trip_id",
                    "trip_short_name",
                    "departure_time",
                    "arrival_time",
                    "departure_stop",
                    "stop_headsign",
                    "zone_id",
                    "direction",
                    "route_short_name",
                    "route_long_name",
                    "departure_stop_id",
                    "route_type",
                    "stop_sequence",
                    "start_date",
                    "end_date",
                    "monday",
                    "tuesday",
                    "wednesday",
                    "thursday",
                    "friday",
                    "saturday",
                    "sunday",
                    "stop_lat",
                    "stop_lon",
                    "timezone"),
                provider);
      }
    }
    System.out.println("R-----" + regularDayDepartureQueryWithFullDetails + rows.size());
    return rows;
  }

  private static String specialDayDepartureQueryWithFullDetails =
      "SELECT distinct r.route_id, t.trip_id,\r\n"
          + "       t.trip_short_name, start_st.departure_time,start_st.arrival_time,\r\n"
          + "       start_s.stop_name as departure_stop, start_st.stop_headsign,\r\n"
          + "       start_s.zone_id, direction_id as direction, r.route_short_name,\r\n"
          + "       r.route_long_name, start_s.stop_id as departure_stop_id,\r\n"
          + "    r.route_type,  start_st.stop_sequence,"
          + "       cd.date, cd.exception_type, (start_s.stop_lat as stop_lat) as stop_lat ,\r\n"
          + "       start_s.stop_lon as stop_lon, key.timezone FROM trips t INNER JOIN routes r ON t.route_id = r.route_id\r\n"
          + "       INNER JOIN stop_times start_st ON t.trip_id = start_st.trip_id\r\n"
          + "       INNER JOIN stops start_s ON start_st.stop_id = start_s.stop_id\r\n"
          + "       INNER JOIN calendar_dates cd ON t.service_id = cd.service_id\r\n"
          + "       INNER JOIN iu_importprocess key ON r.iu_importprocess = key.iu_importprocess\r\n"
          + "INNER JOIN agency agency_id ON r.agency_id = agency.agency_id\r\n"
          + "       WHERE key.provider_name = ?\r\n";

  /**
   * Returns the special trips which add extra service. That means that a bus might not run normally
   * on Wednesday but on the special trips it runs. There is also a marking of excluded trips when
   * service is not available. Check the respective method for that.
   */
  public List<RegularTrip> getExtraSpecialTrips(String provider, String agencyName) {
    List<RegularTrip> rows = null;
    if (agencyName != null) {
      rows =
          jdbcTemplate.query(
              specialDayDepartureQueryWithFullDetails + agency_name_wherecondition,
              new RegularTripRowMapper2(
                  "route_id",
                  "trip_id",
                  "trip_short_name",
                  "departure_time",
                  "arrival_time",
                  "departure_stop",
                  "stop_headsign",
                  "zone_id",
                  "direction",
                  "route_short_name",
                  "route_long_name",
                  "departure_stop_id",
                  "route_type",
                  "stop_sequence",
                  "date",
                  "stop_lat",
                  "stop_lon",
                  "timezone"),
              provider,
              agencyName);
    } else {
      rows =
          jdbcTemplate.query(
              specialDayDepartureQueryWithFullDetails,
              new RegularTripRowMapper2(
                  "route_id",
                  "trip_id",
                  "trip_short_name",
                  "departure_time",
                  "arrival_time",
                  "departure_stop",
                  "stop_headsign",
                  "zone_id",
                  "direction",
                  "route_short_name",
                  "route_long_name",
                  "departure_stop_id",
                  "route_type",
                  "stop_sequence",
                  "date",
                  "stop_lat",
                  "stop_lon",
                  "timezone"),
              provider);
    }
    System.out.println("S-----" + rows.size());

    return rows;
  }

  ////////////////////////////////////////////////////////////////////////////////////
  /////////////// Line/Route information - how the route runs and so on
  ////////////////////////////////////////////////////////////////////////////////////
  String routeDetailsQuery =
      "SELECT distinct r.route_id, r.route_short_name,\r\n"
          + "       r.route_long_name, r.route_type, r.route_color,\r\n"
          + "       r.route_text_color, key.timezone\r\n"
          + "FROM routes r JOIN iu_importprocess key ON r.iu_importprocess = key.iu_importprocess\r\n"
          + "WHERE key.provider_name = ?\r\n";

  // this is to fetch Route/Line details from gtfs based on provider and assign to
  // Line
  public Map<String, Route> listAllLines(String provider, String agencyName) {
    List<Route> rows = null;
    if (agencyName != null) {
      rows =
          jdbcTemplate.query(
              routeDetailsQuery + agencyName, new LineRowMapper(), provider, agencyName);
    } else {
      rows = jdbcTemplate.query(routeDetailsQuery, new LineRowMapper(), provider);
    }
    return rows.stream().collect(Collectors.toMap(e -> e.lineRef, e -> e));
  }

  ////////////////////////////////////////////////////////////////////////////////////
  /////////////// Stop information - which lines run at wich stop
  ////////////////////////////////////////////////////////////////////////////////////

  String stopsQuery =
      "SELECT distinct start_s.stop_id, start_s.stop_name, start_s.stop_lat as stop_lat,\r\n"
          + "       start_s.stop_lon as stop_lon,start_s.stop_code,start_s.location_type,start_s.parent_station,key.timezone\r\n"
          + "FROM stops start_s JOIN iu_importprocess key ON start_s.iu_importprocess = key.iu_importprocess\r\n"
          + "       WHERE key.provider_name = ?\r\n";

  String bboxwhere =
      " start_s.stop_lon>= ?"
          + "AND start_s.stop_lon<= ?"
          + "AND start_s.stop_lon>= ?"
          + "AND start_s.stop_lon <= ?";
  // this is to fetch stop details from gtfs based on provider and assign to Stop

  public List<PlannedStop> listStops(
      String provider, Float minlon, Float maxlon, Float minlat, Float maxlat) {

    List<PlannedStop> rows = null;
    if (minlon != null && maxlon != null && minlat != null && maxlat != null) {

      rows =
          jdbcTemplate.query(
              stopsQuery + " AND " + bboxwhere,
              new StopRowMapper(),
              provider,
              minlon,
              maxlon,
              minlat,
              maxlat);
    } else {
      rows = jdbcTemplate.query(stopsQuery, new StopRowMapper(), provider);
    }
    return rows;
  }

  String stopsWithRouteDetailsQuery1 =
      "SELECT distinct start_s.stop_id, start_s.stop_name, start_s.stop_lat as stop_lat"
          + ", start_s.stop_lon as stop_lon, r.route_id,\r\n"
          + "r.route_short_name, key.timezone, count(*) as count FROM\r\n"
          + "trips t JOIN routes r ON t.route_id = r.route_id\r\n"
          + "        JOIN stop_times start_st ON t.trip_id = start_st.trip_id\r\n"
          + "        JOIN stops start_s ON start_st.stop_id = start_s.stop_id\r\n"
          + "        JOIN iu_importprocess key ON r.iu_importprocess = key.iu_importprocess WHERE key.provider_name = ?\r\n";
  String stopsWithRouteDetailsQuery2 =
      "GROUP BY start_s.stop_id, start_s.stop_name, start_s.stop_lat, start_s.stop_lon, r.route_id, r.route_short_name, key.timezone";

  /** Determines the details for all stops - so which lines depart at the stop */
  public Map<String, LineStop> listLineDetails(
      @RequestParam String provider,
      String agencyName,
      Float minlon,
      Float maxlon,
      Float minlat,
      Float maxlat) {

    List<LineStop> rows = null;
    if (agencyName != null) {
      rows =
          jdbcTemplate.query(
              stopsWithRouteDetailsQuery1
                  + agency_name_wherecondition
                  + stopsWithRouteDetailsQuery2,
              new LineWithStopRowMapper(),
              provider,
              agencyName);
    } else {
      rows =
          jdbcTemplate.query(
              stopsWithRouteDetailsQuery1 + stopsWithRouteDetailsQuery2,
              new LineWithStopRowMapper(),
              provider);
    }
    return rows.stream().collect(Collectors.toMap(e -> e.lineRef, e -> e));
  }
}
