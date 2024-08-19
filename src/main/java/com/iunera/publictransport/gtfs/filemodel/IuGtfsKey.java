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

import java.util.Date;

public class IuGtfsKey implements GTFSProperty {

  private String iu_importprocess;

  private String provider_name;

  private Date insertion_date;

  private Date valid_from_date;

  private Date valid_to_date;

  private String timezone;

  @Override
  public String getiu_importprocess() {
    return iu_importprocess;
  }

  @Override
  public void setiu_importprocess(String iu_importprocess) {
    this.iu_importprocess = iu_importprocess;
  }

  public String getProvider_name() {
    return provider_name;
  }

  public void setProvider_name(String provider_name) {
    this.provider_name = provider_name;
  }

  public Date getInsertion_date() {
    return insertion_date;
  }

  public void setInsertion_date(Date insertion_date) {
    this.insertion_date = insertion_date;
  }

  public Date getValid_from_date() {
    return valid_from_date;
  }

  public void setValid_from_date(Date valid_from_date) {
    this.valid_from_date = valid_from_date;
  }

  public Date getValid_to_date() {
    return valid_to_date;
  }

  public void setValid_to_date(Date valid_to_date) {
    this.valid_to_date = valid_to_date;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }
}
