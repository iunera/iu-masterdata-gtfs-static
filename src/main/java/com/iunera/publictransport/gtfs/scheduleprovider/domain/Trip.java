package com.iunera.publictransport.gtfs.scheduleprovider.domain;

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

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Trip does currently not have a place in LineRideDetails or Ride event. Clearly a trip is a
 * connection between two points and we need it somehow TODO: Check how trip fits into our logical
 * application model
 */
public class Trip {
  public String tripId;
  public String departureStopName;
  //	public String arrivalStopName;
  public String zoneId; // timeZone

  public String direction;
  public String routeId;
  public String routeShortName;
  public String routeLongName;
  public String departureStopId;
  //	public String arrivalStopId;

  public String stopLatitude;
  public String stopLongitude;

  public String stopHeadsign;

  public LocalTime plannedDepartureTime;
  public LocalTime plannedArrivalTime;

  // First time when this Trip is done
  public LocalDateTime plannedDepartureStartDateTime;
  public LocalDateTime plannedArrivalStartDateTime;

  // Last time when this Trip is done
  public LocalDateTime plannedDepartureEndDateTime;
  public LocalDateTime plannedArrivalEndDateTime;

  public int routeType;

  /**
   * Always ascending - BEWARE: It does not mean that numbers are incremented by one- it can be
   * more. However, a drop to a lower number means it is a new line
   */
  public int stopSequence;

  public int count;

  public String timezone;
}
