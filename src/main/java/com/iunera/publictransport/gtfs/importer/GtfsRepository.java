package com.iunera.publictransport.gtfs.importer;

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

import com.iunera.publictransport.gtfs.filemodel.Agency;
import com.iunera.publictransport.gtfs.filemodel.Calendar;
import com.iunera.publictransport.gtfs.filemodel.CalendarDates;
import com.iunera.publictransport.gtfs.filemodel.FeedInfo;
import com.iunera.publictransport.gtfs.filemodel.GTFSProperty;
import com.iunera.publictransport.gtfs.filemodel.IuGtfsKey;
import com.iunera.publictransport.gtfs.filemodel.Routes;
import com.iunera.publictransport.gtfs.filemodel.Shapes;
import com.iunera.publictransport.gtfs.filemodel.StopTimes;
import com.iunera.publictransport.gtfs.filemodel.Stops;
import com.iunera.publictransport.gtfs.filemodel.Transfers;
import com.iunera.publictransport.gtfs.filemodel.Trips;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** Inserting gtfs data using jdbc template */
@Component
public class GtfsRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public int insert(GTFSProperty gtfsObj) throws GTFSInsertionException {
    // TODO: replace with a proper multimethods way to have it more clean. E.g.
    // https://gist.github.com/norswap/5236080
    try {
      if (gtfsObj instanceof Agency) return insert((Agency) gtfsObj);
      if (gtfsObj instanceof Calendar) return insert((Calendar) gtfsObj);
      if (gtfsObj instanceof CalendarDates) return insert((CalendarDates) gtfsObj);
      if (gtfsObj instanceof FeedInfo) return insert((FeedInfo) gtfsObj);
      if (gtfsObj instanceof IuGtfsKey) return insert((IuGtfsKey) gtfsObj);
      if (gtfsObj instanceof Routes) return insert((Routes) gtfsObj);
      if (gtfsObj instanceof Shapes) return insert((Shapes) gtfsObj);
      if (gtfsObj instanceof Stops) return insert((Stops) gtfsObj);
      if (gtfsObj instanceof StopTimes) return insert((StopTimes) gtfsObj);
      if (gtfsObj instanceof Transfers) return insert((Transfers) gtfsObj);
      if (gtfsObj instanceof Trips) return insert((Trips) gtfsObj);
    } catch (DataAccessException | GTFSInsertionException | ParseException e) {
      throw new GTFSInsertionException("insert error " + gtfsObj.getClass(), e);
    }
    return -1;
  }

  private static String[] tables = {
    "agency",
    "calendar",
    "calendar_dates",
    "fare_attributes",
    "fare_rules",
    "fares",
    "feed_info",
    "frequencies",
    "iu_importprocess",
    "routes",
    "shapes",
    "spatial_ref_sys",
    "stop_times",
    "stops",
    "transfers",
    "trips",
    "trips_shapes"
  };

  /** Erases a complete import from the database */
  public void deleteImportprocess(String iu_importprocess) {
    String sql = "DELETE FROM ? WHERE iu_importprocess = ?";

    Arrays.stream(tables).forEach(e -> jdbcTemplate.update(sql, e, iu_importprocess));
  }

  public int insert(IuGtfsKey iuGtfsKey) throws GTFSInsertionException {

    String sql =
        "INSERT INTO iu_importprocess (iu_importprocess, provider_name, insertion_date, valid_from_date, valid_to_date, timezone) "
            + "VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (iu_importprocess) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              iuGtfsKey.getiu_importprocess(),
              iuGtfsKey.getProvider_name(),
              iuGtfsKey.getInsertion_date(),
              iuGtfsKey.getValid_from_date(),
              iuGtfsKey.getValid_to_date(),
              iuGtfsKey.getTimezone()
            });

    return result;
  }

  public int insert(Agency agency) throws GTFSInsertionException {

    String sql =
        "INSERT INTO agency (agency_id, agency_name, agency_url, agency_timezone, agency_lang, agency_phone, agency_fare_url, agency_email, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (agency_id) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              agency.getAgency_id(),
              agency.getAgency_name(),
              agency.getAgency_url(),
              agency.getAgency_timezone(),
              agency.getAgency_lang(),
              agency.getAgency_phone(),
              agency.getAgency_fare_url(),
              agency.getAgency_email(),
              agency.getiu_importprocess()
            });

    return result;
  }

  public int insert(Calendar calendar) throws GTFSInsertionException {
    String sql =
        "INSERT INTO calendar (service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date, iu_importprocess)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (service_id) DO NOTHING ";
    int result = 0;
    try {
      result =
          jdbcTemplate.update(
              sql,
              new Object[] {
                calendar.getService_id(),
                calendar.getMonday(),
                calendar.getTuesday(),
                calendar.getWednesday(),
                calendar.getThursday(),
                calendar.getFriday(),
                calendar.getSaturday(),
                calendar.getSunday(),
                (calendar.getStart_date() != null)
                    ? new SimpleDateFormat("yyyyMMdd").parse(calendar.getStart_date())
                    : null,
                (calendar.getEnd_date() != null)
                    ? new SimpleDateFormat("yyyyMMdd").parse(calendar.getEnd_date())
                    : null,
                calendar.getiu_importprocess()
              });
    } catch (DataAccessException | ParseException e) {
      throw new GTFSInsertionException("GTFS insert failed", e);
    }
    return result;
  }

  public int insert(CalendarDates calendarDate) throws GTFSInsertionException {
    String sql =
        "INSERT INTO calendar_dates (service_id,date,exception_type, iu_importprocess) VALUES (?, ?, ?, ?)";

    int result = 0;
    try {
      result =
          jdbcTemplate.update(
              sql,
              new Object[] {
                calendarDate.getService_id(),
                (calendarDate.getDate() != null)
                    ? new SimpleDateFormat("yyyyMMdd").parse(calendarDate.getDate())
                    : null,
                calendarDate.getException_type(),
                calendarDate.getiu_importprocess()
              });
    } catch (DataAccessException | ParseException e) {
      throw new GTFSInsertionException("GTFS insert failed", e);
    }
    return result;
  }

  public int insert(FeedInfo feedInfo)
      throws DataAccessException, ParseException, GTFSInsertionException {
    String sql =
        "INSERT INTO feed_info ("
            + "feed_publisher_name,"
            + "feed_publisher_url,"
            + "feed_lang,"
            + "feed_start_date,"
            + "feed_end_date,"
            + "feed_version,"
            + "iu_importprocess,"
            + "feed_contact_email,"
            + "feed_contact_url ) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    int result = 0;

    result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              feedInfo.getFeed_publisher_name(),
              feedInfo.getFeed_publisher_url(),
              feedInfo.getFeed_lang(),
              new SimpleDateFormat("yyyyMMdd").parse(feedInfo.getFeed_start_date()),
              new SimpleDateFormat("yyyyMMdd").parse(feedInfo.getFeed_end_date()),
              feedInfo.getFeed_version(),
              feedInfo.getiu_importprocess(),
              feedInfo.getFeed_contact_email(),
              feedInfo.getFeed_contact_url()
            });

    return result;
  }

  public int insert(Routes routes) throws GTFSInsertionException {
    String sql =
        "INSERT INTO routes (route_id,agency_id,route_short_name,route_long_name,route_type,route_color,route_text_color, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (route_id) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              routes.getRoute_id(),
              routes.getAgency_id(),
              routes.getRoute_short_name(),
              routes.getRoute_long_name(),
              routes.getRoute_type(),
              routes.getRoute_color(),
              routes.getRoute_text_color(),
              routes.getiu_importprocess()
            });
    return result;
  }

  public int insert(Shapes shapes) throws GTFSInsertionException {
    String sql =
        "INSERT INTO shapes (shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence,shape_dist_traveled, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (shape_id) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              shapes.getShape_id(),
              shapes.getShape_pt_lat(),
              shapes.getShape_pt_lon(),
              shapes.getShape_pt_sequence(),
              shapes.getShape_dist_traveled(),
              shapes.getiu_importprocess()
            });
    return result;
  }

  public int insert(StopTimes stopTimes) throws GTFSInsertionException {
    String sql =
        "INSERT INTO stop_times (trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              stopTimes.getTrip_id(),
              stopTimes.getArrival_time(),
              stopTimes.getDeparture_time(),
              stopTimes.getStop_id(),
              stopTimes.getStop_sequence(),
              stopTimes.getStop_headsign(),
              stopTimes.getPickup_type(),
              stopTimes.getDrop_off_type(),
              stopTimes.getShape_dist_traveled(),
              stopTimes.getiu_importprocess()
            });
    return result;
  }

  public int insert(Stops stops) throws GTFSInsertionException {
    String sql =
        "INSERT INTO stops (stop_id,stop_code,stop_name,stop_lat,stop_lon,location_type,parent_station, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (stop_id) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              stops.getStop_id(),
              stops.getStop_code(),
              stops.getStop_name(),
              stops.getStop_lat(),
              stops.getStop_lon(),
              stops.getLocation_type(),
              stops.getParent_station(),
              stops.getiu_importprocess()
            });
    return result;
  }

  public int insert(Transfers transfers) throws GTFSInsertionException {
    String sql =
        "INSERT INTO transfers (from_stop_id,to_stop_id,transfer_type,min_transfer_time, iu_importprocess) VALUES (?, ?, ?, ?, ?) ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              transfers.getFrom_stop_id(),
              transfers.getTo_stop_id(),
              transfers.getTransfer_type(),
              transfers.getMin_transfer_time(),
              transfers.getiu_importprocess()
            });
    return result;
  }

  public int insert(Trips trips) throws GTFSInsertionException {
    String sql =
        "INSERT INTO trips (route_id,service_id,trip_id,shape_id,trip_headsign,trip_short_name,direction_id,block_id, iu_importprocess) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (trip_id) DO NOTHING ";

    int result =
        jdbcTemplate.update(
            sql,
            new Object[] {
              trips.getRoute_id(),
              trips.getService_id(),
              trips.getTrip_id(),
              trips.getShape_id(),
              trips.getTrip_headsign(),
              trips.getTrip_short_name(),
              trips.getDirection_id(),
              trips.getBlock_id(),
              trips.getiu_importprocess()
            });
    return result;
  }
}
