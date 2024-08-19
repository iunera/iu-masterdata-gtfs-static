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

public class Trips implements GTFSProperty {

  private String wheelchair_accessible;
  private String bikes_allowed;
  private String route_id;
  private String service_id;
  private String trip_id;
  private String shape_id;
  private String trip_headsign;
  private String trip_short_name;
  private int direction_id;
  private String block_id;
  private String iu_importprocess;

  public String getWheelchair_accessible() {
    return wheelchair_accessible;
  }

  public void setWheelchair_accessible(String wheelchair_accessible) {
    this.wheelchair_accessible = wheelchair_accessible;
  }

  public String getBikes_allowed() {
    return bikes_allowed;
  }

  public void setBikes_allowed(String bikes_allowed) {
    this.bikes_allowed = bikes_allowed;
  }

  public String getRoute_id() {
    return route_id;
  }

  public void setRoute_id(String route_id) {
    this.route_id = route_id;
  }

  public String getService_id() {
    return service_id;
  }

  public void setService_id(String service_id) {
    this.service_id = service_id;
  }

  public String getTrip_id() {
    return trip_id;
  }

  public void setTrip_id(String trip_id) {
    this.trip_id = trip_id;
  }

  public String getShape_id() {
    return shape_id;
  }

  public void setShape_id(String shape_id) {
    this.shape_id = shape_id;
  }

  public String getTrip_headsign() {
    return trip_headsign;
  }

  public void setTrip_headsign(String trip_headsign) {
    this.trip_headsign = trip_headsign;
  }

  public String getTrip_short_name() {
    return trip_short_name;
  }

  public void setTrip_short_name(String trip_short_name) {
    this.trip_short_name = trip_short_name;
  }

  public int getDirection_id() {
    return direction_id;
  }

  public void setDirection_id(int direction_id) {
    this.direction_id = direction_id;
  }

  public String getBlock_id() {
    return block_id;
  }

  public void setBlock_id(String block_id) {
    this.block_id = block_id;
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
