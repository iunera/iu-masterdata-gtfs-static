package com.iunera.publictransport.gtfs.filemodel;

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

public class StopTimes implements GTFSProperty {

  private String trip_id;
  private String arrival_time;
  private String departure_time;
  private String stop_id;
  private int stop_sequence;
  private String stop_headsign;
  private String pickup_type;
  private String drop_off_type;
  private double shape_dist_traveled;
  private String iu_importprocess;

  public String getTrip_id() {
    return trip_id;
  }

  public void setTrip_id(String trip_id) {
    this.trip_id = trip_id;
  }

  public String getArrival_time() {
    return arrival_time;
  }

  public void setArrival_time(String arrival_time) {
    this.arrival_time = arrival_time;
  }

  public String getDeparture_time() {
    return departure_time;
  }

  public void setDeparture_time(String departure_time) {
    this.departure_time = departure_time;
  }

  public String getStop_id() {
    return stop_id;
  }

  public void setStop_id(String stop_id) {
    this.stop_id = stop_id;
  }

  public int getStop_sequence() {
    return stop_sequence;
  }

  public void setStop_sequence(int stop_sequence) {
    this.stop_sequence = stop_sequence;
  }

  public String getStop_headsign() {
    return stop_headsign;
  }

  public void setStop_headsign(String stop_headsign) {
    this.stop_headsign = stop_headsign;
  }

  public String getPickup_type() {
    return pickup_type;
  }

  public void setPickup_type(String pickup_type) {
    this.pickup_type = pickup_type;
  }

  public String getDrop_off_type() {
    return drop_off_type;
  }

  public void setDrop_off_type(String drop_off_type) {
    this.drop_off_type = drop_off_type;
  }

  public double getShape_dist_traveled() {
    return shape_dist_traveled;
  }

  public void setShape_dist_traveled(double shape_dist_traveled) {
    this.shape_dist_traveled = shape_dist_traveled;
  }

  @Override
  public String getiu_importprocess() {
    return iu_importprocess;
  }

  @Override
  public void setiu_importprocess(String iu_importprocess) {
    this.iu_importprocess = iu_importprocess;
  }
}
