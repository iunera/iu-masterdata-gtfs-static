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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Calendar implements GTFSProperty {

  private String service_id;
  private int monday;
  private int tuesday;
  private int wednesday;
  private int thursday;
  private int friday;
  private int saturday;
  private int sunday;
  private String start_date;
  private String end_date;
  private String iu_importprocess;

  public String getService_id() {
    return service_id;
  }

  public void setService_id(String service_id) {
    this.service_id = service_id;
  }

  public int getMonday() {
    return monday;
  }

  public void setMonday(int monday) {
    this.monday = monday;
  }

  public int getTuesday() {
    return tuesday;
  }

  public void setTuesday(int tuesday) {
    this.tuesday = tuesday;
  }

  public int getWednesday() {
    return wednesday;
  }

  public void setWednesday(int wednesday) {
    this.wednesday = wednesday;
  }

  public int getThursday() {
    return thursday;
  }

  public void setThursday(int thursday) {
    this.thursday = thursday;
  }

  public int getFriday() {
    return friday;
  }

  public void setFriday(int friday) {
    this.friday = friday;
  }

  public int getSaturday() {
    return saturday;
  }

  public void setSaturday(int saturday) {
    this.saturday = saturday;
  }

  public int getSunday() {
    return sunday;
  }

  public void setSunday(int sunday) {
    this.sunday = sunday;
  }

  public String getStart_date() {
    return start_date;
  }

  public void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  public String getEnd_date() {
    return end_date;
  }

  public void setEnd_date(String end_date) {
    this.end_date = end_date;
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
