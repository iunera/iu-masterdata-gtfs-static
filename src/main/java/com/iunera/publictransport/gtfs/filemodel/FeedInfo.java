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

public class FeedInfo implements GTFSProperty {

  private String feed_publisher_name;
  private String feed_publisher_url;
  private String feed_lang;
  private String feed_start_date;
  private String feed_end_date;
  private String feed_version;
  private String iu_importprocess;

  private String feed_contact_email = "";
  private String feed_contact_url = "";

  public String getFeed_contact_url() {
    return feed_contact_url;
  }

  public void setFeed_contact_url(String feed_contact_url) {
    this.feed_contact_url = feed_contact_url;
  }

  public String getFeed_contact_email() {
    return feed_contact_email;
  }

  public void setFeed_contact_email(String feed_contact_email) {
    this.feed_contact_email = feed_contact_email;
  }

  public String getFeed_publisher_name() {
    return feed_publisher_name;
  }

  public void setFeed_publisher_name(String feed_publisher_name) {
    this.feed_publisher_name = feed_publisher_name;
  }

  public String getFeed_publisher_url() {
    return feed_publisher_url;
  }

  public void setFeed_publisher_url(String feed_publisher_url) {
    this.feed_publisher_url = feed_publisher_url;
  }

  public String getFeed_lang() {
    return feed_lang;
  }

  public void setFeed_lang(String feed_lang) {
    this.feed_lang = feed_lang;
  }

  public String getFeed_start_date() {
    return feed_start_date;
  }

  public void setFeed_start_date(String feed_start_date) {
    this.feed_start_date = feed_start_date;
  }

  public String getFeed_end_date() {
    return feed_end_date;
  }

  public void setFeed_end_date(String feed_end_date) {
    this.feed_end_date = feed_end_date;
  }

  public String getFeed_version() {
    return feed_version;
  }

  public void setFeed_version(String feed_version) {
    this.feed_version = feed_version;
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
