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

public class Routes implements GTFSProperty {

  private String route_id;
  private String agency_id;
  private String route_short_name;
  private String route_long_name;
  private String route_type;
  private String route_color;
  private String route_text_color;
  private String iu_importprocess;
  private String route_desc;

  public String getRoute_desc() {
    return route_desc;
  }

  public void setRoute_desc(String route_desc) {
    this.route_desc = route_desc;
  }

  public String getRoute_id() {
    return route_id;
  }

  public void setRoute_id(String route_id) {
    this.route_id = route_id;
  }

  public String getAgency_id() {
    return agency_id;
  }

  public void setAgency_id(String agency_id) {
    this.agency_id = agency_id;
  }

  public String getRoute_short_name() {
    return route_short_name;
  }

  public void setRoute_short_name(String route_short_name) {
    this.route_short_name = route_short_name;
  }

  public String getRoute_long_name() {
    return route_long_name;
  }

  public void setRoute_long_name(String route_long_name) {
    this.route_long_name = route_long_name;
  }

  public String getRoute_type() {
    return route_type;
  }

  public void setRoute_type(String route_type) {
    this.route_type = route_type;
  }

  public String getRoute_color() {
    return route_color;
  }

  public void setRoute_color(String route_color) {
    this.route_color = route_color;
  }

  public String getRoute_text_color() {
    return route_text_color;
  }

  public void setRoute_text_color(String route_text_color) {
    this.route_text_color = route_text_color;
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
