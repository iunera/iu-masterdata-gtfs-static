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

public class Transfers implements GTFSProperty {
  private String from_route_id;
  private String to_route_id;
  private String from_trip_id;
  private String to_trip_id;
  private String from_stop_id;
  private String to_stop_id;
  private String transfer_type;
  private int min_transfer_time;
  private String iu_importprocess;

  public String getFrom_route_id() {
    return from_route_id;
  }

  public void setFrom_route_id(String from_route_id) {
    this.from_route_id = from_route_id;
  }

  public String getTo_route_id() {
    return to_route_id;
  }

  public void setTo_route_id(String to_route_id) {
    this.to_route_id = to_route_id;
  }

  public String getFrom_trip_id() {
    return from_trip_id;
  }

  public void setFrom_trip_id(String from_trip_id) {
    this.from_trip_id = from_trip_id;
  }

  public String getTo_trip_id() {
    return to_trip_id;
  }

  public void setTo_trip_id(String to_trip_id) {
    this.to_trip_id = to_trip_id;
  }

  public String getFrom_stop_id() {
    return from_stop_id;
  }

  public void setFrom_stop_id(String from_stop_id) {
    this.from_stop_id = from_stop_id;
  }

  public String getTo_stop_id() {
    return to_stop_id;
  }

  public void setTo_stop_id(String to_stop_id) {
    this.to_stop_id = to_stop_id;
  }

  public String getTransfer_type() {
    return transfer_type;
  }

  public void setTransfer_type(String transfer_type) {
    this.transfer_type = transfer_type;
  }

  public int getMin_transfer_time() {
    return min_transfer_time;
  }

  public void setMin_transfer_time(int min_transfer_time) {
    this.min_transfer_time = min_transfer_time;
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
