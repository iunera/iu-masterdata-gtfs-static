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

public class Stops implements GTFSProperty {
  private String stop_id;
  private String stop_code;
  private String stop_name;
  private float stop_lat;
  private float stop_lon;
  private String location_type;
  private String parent_station;
  private String iu_importprocess;
  private String platform_code;
  private String level_id;

  public String getPlatform_code() {
    return platform_code;
  }

  public void setPlatform_code(String platform_code) {
    this.platform_code = platform_code;
  }

  public String getLevel_id() {
    return level_id;
  }

  public void setLevel_id(String level_id) {
    this.level_id = level_id;
  }

  private String wheelchair_boarding;

  public String getWheelchair_boarding() {
    return wheelchair_boarding;
  }

  public void setWheelchair_boarding(String wheelchair_boarding) {
    this.wheelchair_boarding = wheelchair_boarding;
  }

  private String stop_desc;

  public String getStop_desc() {
    return stop_desc;
  }

  public void setStop_desc(String stop_desc) {
    this.stop_desc = stop_desc;
  }

  public String getStop_id() {
    return stop_id;
  }

  public void setStop_id(String stop_id) {
    this.stop_id = stop_id;
  }

  public String getStop_code() {
    return stop_code;
  }

  public void setStop_code(String stop_code) {
    this.stop_code = stop_code;
  }

  public String getStop_name() {
    return stop_name;
  }

  public void setStop_name(String stop_name) {
    this.stop_name = stop_name;
  }

  public float getStop_lat() {
    return stop_lat;
  }

  public void setStop_lat(String stop_lat) {

    this.stop_lat = (Float.parseFloat(stop_lat));
  }

  public float getStop_lon() {
    return stop_lon;
  }

  public void setStop_lon(String stop_lon) {
    this.stop_lon = (Float.parseFloat(stop_lon));
  }

  public String getLocation_type() {
    return location_type;
  }

  public void setLocation_type(String location_type) {
    this.location_type = location_type;
  }

  public String getParent_station() {
    return parent_station;
  }

  public void setParent_station(String parent_station) {
    this.parent_station = parent_station;
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
