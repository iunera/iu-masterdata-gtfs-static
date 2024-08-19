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

public class Agency implements GTFSProperty {

  private String agency_id;
  private String agency_name;
  private String agency_url;
  private String agency_timezone;
  private String agency_lang;
  private String agency_phone;
  private String agency_fare_url;
  private String agency_email;
  private String iu_importprocess;

  public String getAgency_id() {
    return agency_id;
  }

  public void setAgency_id(String agency_id) {
    this.agency_id = agency_id;
  }

  public String getAgency_name() {
    return agency_name;
  }

  public void setAgency_name(String agency_name) {
    this.agency_name = agency_name;
  }

  public String getAgency_url() {
    return agency_url;
  }

  public void setAgency_url(String agency_url) {
    this.agency_url = agency_url;
  }

  public String getAgency_timezone() {
    return agency_timezone;
  }

  public void setAgency_timezone(String agency_timezone) {
    this.agency_timezone = agency_timezone;
  }

  public String getAgency_lang() {
    return agency_lang;
  }

  public void setAgency_lang(String agency_lang) {
    this.agency_lang = agency_lang;
  }

  public String getAgency_phone() {
    return agency_phone;
  }

  public void setAgency_phone(String agency_phone) {
    this.agency_phone = agency_phone;
  }

  public String getAgency_fare_url() {
    return agency_fare_url;
  }

  public void setAgency_fare_url(String agency_fare_url) {
    this.agency_fare_url = agency_fare_url;
  }

  public String getAgency_email() {
    return agency_email;
  }

  public void setAgency_email(String agency_email) {
    this.agency_email = agency_email;
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
