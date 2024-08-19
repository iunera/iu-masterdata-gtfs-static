package com.iunera.publictransport.gtfs.importer;

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

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/apiv1/import")
public class ImportGtfsCsvZipApi {

  @Autowired GTFSFileImportService object;

  /**
   * To import gtfs data from zip file format and push to database Example input's are => urls or
   * files.
   *
   * @param gtfsFile the folder with the GTFS files
   * @param fileIdentifier the source of the GTFS files. E.g.: filename
   * @param timeZone the timeZone of this GTFS file. Defaults to the configuration value.
   * @param stopOnInsertErrors stops on insertion erros when inserting
   */
  @RequestMapping(value = "/gtfs/import", method = RequestMethod.GET, consumes = "application/json")
  public ResponseEntity<String> importGtfsCSV(
      @RequestParam String gtfsFile,
      @RequestParam String fileIdentifier,
      @RequestParam(required = false) String timeZone,
      @RequestParam(required = false) Boolean revertOnInsertErros,
      @RequestParam(required = false) Boolean skipErrors) {
    try {
      String importProcess = fileIdentifier + "_IP-" + Instant.now();
      object.importGtfsData(
          gtfsFile, fileIdentifier, timeZone, importProcess, revertOnInsertErros, skipErrors);
      return new ResponseEntity<>(
          "Insert successfull - Import process:" + importProcess, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(
      value = "/gtfs/import/{importProcess}/delete",
      method = RequestMethod.POST,
      consumes = "application/json")
  public ResponseEntity<String> deleteImport(@PathVariable String importProcess) {
    try {

      object.deleteImportprocess(importProcess);
      return new ResponseEntity<>(
          "Insert successfull - Import process:" + importProcess, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
  }
}
