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

import com.iunera.publictransport.gtfs.scheduleprovider.domain.Trip;
import com.iunera.publictransport.gtfs.scheduleprovider.queryresultmodels.RegularTrip;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;

public class RegularTripRowMapper2 implements RowMapper<RegularTrip> {

  private Set<String> mappedColumns;
  private ZoneId timeZone;

  public RegularTripRowMapper2(String... columns) {
    this.mappedColumns = new HashSet<>(Arrays.asList(columns));
  }

  @Override
  public RegularTrip mapRow(ResultSet rs, int rowNum) throws SQLException {

    RegularTrip regulartrip = new RegularTrip();
    if (mappedColumns.contains("start_date"))
      regulartrip.startDate = (LocalDate.parse(rs.getString("start_date"), datetimeformatterDay));

    if (mappedColumns.contains("end_date"))
      regulartrip.endDate = (LocalDate.parse(rs.getString("end_date"), datetimeformatterDay));

    Trip trip = new Trip();
    if (mappedColumns.contains("timezone")) {
      trip.timezone = rs.getString("timezone");
      timeZone = ZoneId.of(trip.timezone);
    }
    if (mappedColumns.contains("departure_stop_id"))
      trip.departureStopId = rs.getString("departure_stop_id");

    if (mappedColumns.contains("route_type")) trip.routeType = rs.getInt("route_type");

    if (mappedColumns.contains("stop_sequence")) trip.stopSequence = rs.getInt("stop_sequence");

    if (mappedColumns.contains("stop_headsign")) trip.stopHeadsign = rs.getString("stop_headsign");
    if (mappedColumns.contains("departure_stop"))
      trip.departureStopName = rs.getString("departure_stop");
    if (mappedColumns.contains("direction")) trip.direction = rs.getString("direction");

    if (mappedColumns.contains("departure_time") && mappedColumns.contains("start_date"))
      trip.plannedDepartureStartDateTime =
          LocalDateTime.ofInstant(
              toDate(rs.getString("departure_time"), regulartrip.startDate.toString(), timeZone),
              timeZone);

    if (mappedColumns.contains("departure_time") && mappedColumns.contains("end_date"))
      trip.plannedDepartureEndDateTime =
          LocalDateTime.ofInstant(
              toDate(rs.getString("departure_time"), regulartrip.endDate.toString(), timeZone),
              timeZone);

    if (mappedColumns.contains("departure_time"))
      trip.plannedDepartureTime = toLocalTime(rs.getString("departure_time"));
    if (mappedColumns.contains("arrival_time") && mappedColumns.contains("start_date"))
      trip.plannedArrivalStartDateTime =
          LocalDateTime.ofInstant(
              toDate(rs.getString("arrival_time"), regulartrip.startDate.toString(), timeZone),
              timeZone);
    if (mappedColumns.contains("arrival_time") && mappedColumns.contains("end_date"))
      trip.plannedArrivalEndDateTime =
          LocalDateTime.ofInstant(
              toDate(rs.getString("arrival_time"), regulartrip.endDate.toString(), timeZone),
              timeZone);
    if (mappedColumns.contains("arrival_time"))
      trip.plannedArrivalTime = toLocalTime(rs.getString("arrival_time"));
    if (mappedColumns.contains("route_id")) trip.routeId = rs.getString("route_id");
    if (mappedColumns.contains("route_long_name"))
      trip.routeLongName = rs.getString("route_long_name");
    if (mappedColumns.contains("route_short_name"))
      trip.routeShortName = rs.getString("route_short_name");
    if (mappedColumns.contains("stop_lat")) trip.stopLatitude = rs.getString("stop_lat");
    if (mappedColumns.contains("stop_lon")) trip.stopLongitude = rs.getString("stop_lon");

    if (mappedColumns.contains("trip_id")) trip.tripId = rs.getString("trip_id");
    if (mappedColumns.contains("zone_id")) {
      if (rs.getString("zone_id") != null) trip.zoneId = rs.getString("zone_id");
      else trip.zoneId = timeZone.toString();
    }

    regulartrip.trip = trip;

    if (mappedColumns.contains("monday")) if (rs.getInt("monday") == 1) regulartrip.monday = true;
    if (mappedColumns.contains("tuesday"))
      if (rs.getInt("tuesday") == 1) regulartrip.tuesday = true;
    if (mappedColumns.contains("wednesday"))
      if (rs.getInt("wednesday") == 1) regulartrip.wednesday = true;
    if (mappedColumns.contains("thursday"))
      if (rs.getInt("thursday") == 1) regulartrip.thursday = true;
    if (mappedColumns.contains("friday")) if (rs.getInt("friday") == 1) regulartrip.friday = true;
    if (mappedColumns.contains("saturday"))
      if (rs.getInt("saturday") == 1) regulartrip.saturday = true;
    if (mappedColumns.contains("sunday")) if (rs.getInt("sunday") == 1) regulartrip.sunday = true;

    return regulartrip;
  }

  static DateTimeFormatter datetimeformattertime = DateTimeFormatter.ofPattern("HH:mm:ss");
  static DateTimeFormatter datetimeformatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // this is to get time when larger than 24 hours then add end day to the
  // operation date
  private Instant toDate(String time, String day, ZoneId timezone) {
    int hour = Integer.parseInt((time.toString().split(":")[0])) * 60 * 60; // hours
    int minutes = Integer.parseInt((time.toString().split(":")[1])) * 60; // minutes
    int seconds = hour + minutes;
    return LocalDate.parse(day, datetimeformatterDay)
        .atStartOfDay()
        .plusSeconds(seconds)
        .atZone(timezone)
        .toInstant();
  }

  // this is to get time when larger than 24 hours
  private static LocalTime toLocalTime(String time) {
    String[] splittime = time.toString().split(":");
    int hour = Integer.parseInt(splittime[0]);
    int minutes = Integer.parseInt(splittime[1]);
    int seconds = Integer.parseInt(splittime[2]);
    if (hour >= 24) hour = hour - 24;
    return LocalTime.of(hour, minutes, seconds);
  }
}
