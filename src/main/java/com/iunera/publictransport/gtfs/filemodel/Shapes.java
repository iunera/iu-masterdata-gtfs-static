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

public class Shapes implements GTFSProperty {

  private String shape_id;
  private String shape_pt_lat;
  private String shape_pt_lon;
  private String shape_pt_sequence;
  private String shape_dist_traveled;
  private String iu_importprocess;

  public String getShape_id() {
    return shape_id;
  }

  public void setShape_id(String shape_id) {
    this.shape_id = shape_id;
  }

  public String getShape_pt_lat() {
    return shape_pt_lat;
  }

  public void setShape_pt_lat(String shape_pt_lat) {
    this.shape_pt_lat = shape_pt_lat;
  }

  public String getShape_pt_lon() {
    return shape_pt_lon;
  }

  public void setShape_pt_lon(String shape_pt_lon) {
    this.shape_pt_lon = shape_pt_lon;
  }

  public String getShape_pt_sequence() {
    return shape_pt_sequence;
  }

  public void setShape_pt_sequence(String shape_pt_sequence) {
    this.shape_pt_sequence = shape_pt_sequence;
  }

  public String getShape_dist_traveled() {
    return shape_dist_traveled;
  }

  public void setShape_dist_traveled(String shape_dist_traveled) {
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
