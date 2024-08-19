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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.iunera.publictransport.domain.TransportProductDTO;
import com.iunera.publictransport.domain.route.LineStop;
import com.iunera.publictransport.domain.route.PlannedDeparture;
import com.iunera.publictransport.domain.route.Route;
import com.iunera.publictransport.domain.stop.PlannedStop;
import com.iunera.publictransport.gtfs.scheduleprovider.domain.OperationDates;
import com.iunera.publictransport.gtfs.scheduleprovider.queryresultmodels.RegularTrip;
import com.iunera.publictransport.keys.RideKeysGeneration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/apiv1")
public class StaticGtfsApi {

  @Autowired private StaticGtfsService staticGtfsService;

  /**
   * * this is to fetch the gtfs trip, route, stop and departure time data based on the provider
   * beware this does not include operationDates.additionalTrips =
   * staticGtfsService.getExtraSpecialTrips(provider);
   */
  @RequestMapping(value = "/stops/all/geotimekeykey_lineids", method = RequestMethod.GET)
  public Map<String, Collection<String>> getGeoKeysWithLines(
      @RequestParam int bucketsize,
      @RequestParam String provider,
      @RequestParam(required = false) String agency,
      @RequestParam(required = false) Float minlon,
      @RequestParam(required = false) Float maxlon,
      @RequestParam(required = false) Float minlat,
      @RequestParam(required = false) Float maxlat) {
    Multimap<String, String> timeStopGeo_line = MultimapBuilder.hashKeys().hashSetValues().build();
    try {

      OperationDates operationDates = new OperationDates();
      // fetch all trips (rides from stop a to stop b), operating on a regular fashion
      operationDates.regulartrips =
          staticGtfsService.getRegularTripsWithDetails(
              provider, agency, minlon, maxlon, minlat, maxlon);

      // out of memory - TODO check how it can be done
      // operationDates.additionalTrips = staticGtfsService.getExtraSpecialTrips(provider);

      // generate the keys for the regular trips
      for (RegularTrip departure : operationDates.regulartrips) {
        // compute the geo time based key for a stop and associate with the line id

        if (!(departure.trip.stopLongitude.equals(""))
            && !(departure.trip.stopLatitude.equals(""))) {
          Coordinate stopccordinate = new Coordinate();
          stopccordinate.x = Double.parseDouble(departure.trip.stopLongitude);
          stopccordinate.y = Double.parseDouble(departure.trip.stopLatitude);

          // generate the nearest buckets for the departure stop
          String[] nearestbuckets =
              RideKeysGeneration.getNearestTimeBucketsKeys(
                  RideKeysGeneration.CoordinateFunctionStop,
                  stopccordinate.x,
                  stopccordinate.y,
                  7,
                  departure.trip.plannedDepartureTime,
                  null,
                  TransportProductDTO.getFromGTFSType(departure.trip.routeType),
                  RideKeysGeneration.directionUNKNOWN,
                  bucketsize);

          // add the bucket type
          timeStopGeo_line.put(
              "type=lower#" + nearestbuckets[0],
              departure.trip.routeId + "#" + departure.trip.stopSequence);
          timeStopGeo_line.put(
              "type=concrete#" + nearestbuckets[1],
              departure.trip.routeId + "#" + departure.trip.stopSequence);
          timeStopGeo_line.put(
              "type=upper#" + nearestbuckets[2],
              departure.trip.routeId + "#" + departure.trip.stopSequence);
        }
      }

      // TODO: Add the special trips

    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeStopGeo_line.asMap();
  }

  /** The same like for lines, just for stops */
  @RequestMapping(value = "/stops/all/geotimekeykey_stopids", method = RequestMethod.GET)
  public Map<String, Collection<String>> getGeoKeysWithStops(
      @RequestParam int bucketsize,
      @RequestParam String provider,
      @RequestParam(required = false) String agency,
      @RequestParam(required = false) Float minlon,
      @RequestParam(required = false) Float maxlon,
      @RequestParam(required = false) Float minlat,
      @RequestParam(required = false) Float maxlat) {
    Multimap<String, String> timeStopGeo_line = MultimapBuilder.hashKeys().hashSetValues().build();
    try {

      OperationDates operationDates = new OperationDates();
      // fetch all trips (rides from stop a to stop b), operating on a regular fashion
      operationDates.regulartrips =
          staticGtfsService.getRegularTripsWithDetails(
              provider, agency, minlon, maxlon, minlat, maxlon);

      // out of memory - TODO check how it can be done
      // operationDates.additionalTrips = staticGtfsService.getExtraSpecialTrips(provider);

      // generate the keys for the regular trips
      for (RegularTrip departure : operationDates.regulartrips) {
        // compute the geo time based key for a stop and associate with the line id

        if (!(departure.trip.stopLongitude.equals(""))
            && !(departure.trip.stopLatitude.equals(""))) {
          Coordinate stopccordinate = new Coordinate();
          stopccordinate.x = Double.parseDouble(departure.trip.stopLongitude);
          stopccordinate.y = Double.parseDouble(departure.trip.stopLatitude);

          // generate the nearest buckets for the departure stop
          String[] nearestbuckets =
              RideKeysGeneration.getNearestTimeBucketsKeys(
                  RideKeysGeneration.CoordinateFunctionStop,
                  stopccordinate.x,
                  stopccordinate.y,
                  7,
                  departure.trip.plannedDepartureTime,
                  null,
                  TransportProductDTO.BUS,
                  RideKeysGeneration.directionUNKNOWN,
                  bucketsize);

          for (String key : nearestbuckets) {
            timeStopGeo_line.put(key, departure.trip.departureStopId);
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeStopGeo_line.asMap();
  }

  /**
   * Returns all lines for a provider. TODO: Add filter for timeframe
   *
   * @return the line id and the details of the line
   */
  @RequestMapping(value = "/lines", method = RequestMethod.GET)
  public Map<String, Route> lineSchedule(
      @RequestParam String provider, @RequestParam(required = false) String agencyName) {

    return staticGtfsService.listAllLines(provider, agencyName);
  }

  /** @returns all stops ids and stop information */
  @RequestMapping(value = "/stops", method = RequestMethod.GET)
  public List<PlannedStop> listStops(
      @RequestParam String provider,
      @RequestParam(required = false) String agency,
      @RequestParam(required = false) Float minlon,
      @RequestParam(required = false) Float maxlon,
      @RequestParam(required = false) Float minlat,
      @RequestParam(required = false) Float maxlat) {
    // TODO: solve this in one query
    List<PlannedStop> stops = staticGtfsService.listStops(provider, minlon, maxlon, minlat, maxlon);
    Map<String, PlannedStop> stopmap =
        stops.stream()
            .collect(Collectors.toMap(e -> e.IFOPT, e -> e, (oldvalue, newvalue) -> newvalue));

    OperationDates operationDates = new OperationDates();
    // fetch all trips (rides from stop a to stop b), operating on a regular fashion
    operationDates.regulartrips =
        staticGtfsService.getRegularTripsWithDetails(
            provider, agency, minlon, maxlon, minlat, maxlon);
    // operationDates.additionalTrips = staticGtfsService.getExtraSpecialTrips(provider);

    for (RegularTrip departure : operationDates.regulartrips) {
      PlannedStop s = stopmap.get(departure.trip.departureStopId);

      if (s != null) {
        if (s.plannedDepartures == null) s.plannedDepartures = new ArrayList<>();
        LocalTime d = null;
        if (departure.trip.plannedDepartureStartDateTime != null)
          d = departure.trip.plannedDepartureStartDateTime.toLocalTime();
        else {
          System.out.print("ddd");
        }
        s.plannedDepartures.add(
            new PlannedDeparture(
                d, departure.trip.direction, departure.trip.routeId, departure.trip.stopHeadsign));
      }
    }

    // TODO: add the additional trips and come up with ideas to prevent memory overloads

    return stops;
  }

  /**
   * This method gives details which lines leave when on which stop
   *
   * @return stops and the information which line leaves when
   *     <p>TODO: change that to compatible objects to the rest (Line, Stop and so on)
   */
  @RequestMapping(value = "/stops/all/details", method = RequestMethod.GET)
  public Map<String, LineStop> listLineDetails(
      @RequestParam String provider,
      @RequestParam(required = false) String agencyName,
      @RequestParam(required = false) Float minlon,
      @RequestParam(required = false) Float maxlon,
      @RequestParam(required = false) Float minlat,
      @RequestParam(required = false) Float maxlat) {

    return staticGtfsService.listLineDetails(provider, agencyName, maxlat, maxlat, maxlat, maxlat);
  }

  // this is to get time when larger than 24 hours
  public static LocalTime toLocalTime(String time) {
    String[] splittime = time.toString().split(":");
    int hour = Integer.parseInt(splittime[0]);
    int minutes = Integer.parseInt(splittime[1]);
    int seconds = Integer.parseInt(splittime[2]);
    if (hour >= 24) hour = hour - 24;
    return LocalTime.of(hour, minutes, seconds);
  }
}
