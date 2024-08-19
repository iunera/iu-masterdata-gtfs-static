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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/** Initializer class used to load GTFS files during startup from a folder */
@Component
public class FolderScanningImportService {

  @Autowired GTFSFileImportService gTFSFileImportService;

  private static final Logger LOGGER = LoggerFactory.getLogger(FolderScanningImportService.class);
  /**
   * "classpath" pseudo URL, a "file:" URL, or a plain file path. Default: classpath:gtfs-data.
   * Specify "none" for no loading of files at the startup none deactivates the regular folder
   * scanning
   */
  @Value("${gtfs.folderimport.source:classpath:gtfs-data/}")
  private String gtfsfolder;

  /**
   * * Subfolder of the gtfs data where processed files are moved after the import. Default:
   * imported
   */
  @Value("${gtfs.folderimport.importedfolder:imported}")
  private String importedfolder;

  @Value("${gtfs.folderimport.revertonerrors:false}")
  private boolean revertOnInsertErrors;

  /**
   * If the processing is continued regardless if there was an error or not. Use this with extreme
   * caution, it can lead to DB inconsistencies. Default is false.
   */
  @Value("${gtfs.folderimport.skiperrors:false}")
  private boolean skipErrors;

  @EventListener(ApplicationReadyEvent.class)
  public void ensurefolders() throws IOException {
    // ensure destination exists

    File destDir = new File(getImportedFolder());
    if (!destDir.exists()) {
      destDir.mkdir();
    }
  }

  // avoid parsing files with errors twice
  private Set<String> errorfiles = new HashSet<>();

  // scan the folder each 10 seconds
  @Scheduled(fixedDelay = 10000, initialDelay = 10000)
  public void loadfiles() throws IOException {

    if (gtfsfolder.equals("none")) return;

    for (File file : ResourceUtils.getFile(gtfsfolder).listFiles()) {
      String fileNameWithSuffix = file.getName();

      // avoid parsing files that had an error before multiple times
      // reason is that currently there is no cleanup mechanism of the database yet
      // and the partial entries of a failed file need to be removed manually by the
      // unique iu_importprocess that is specified for each import
      // avoiding double insertion prevents multiple cleanups and double entries for
      // one file
      if (errorfiles.contains(fileNameWithSuffix)) continue;

      String fileName = fileNameWithSuffix;
      // used to preserve the file ending
      String filesuffix = "";
      if (fileNameWithSuffix.endsWith(".zip")) {
        fileName = fileName.substring(0, fileName.lastIndexOf(".zip"));
        filesuffix = ".zip";
      }
      String importProcess = fileName + "_IP-" + Instant.now().toString().replace(":", "-");
      if (file.isFile()) {
        try {

          gTFSFileImportService.importGtfsData(
              file.getAbsolutePath(),
              fileName,
              null,
              importProcess,
              revertOnInsertErrors,
              skipErrors);

          // move the imported file
          Files.move(
              file.toPath(),
              Path.of(getImportedFolder() + importProcess + filesuffix),
              REPLACE_EXISTING);
        } catch (Exception e) {
          LOGGER.error("File insertion error", e);
          errorfiles.add(fileNameWithSuffix);
        }
      }
    }
  }

  // used to ensure the imported folder is constructed everywhere in a uniform way
  private String getImportedFolder() throws FileNotFoundException {
    return ResourceUtils.getFile(this.gtfsfolder).getPath()
        + File.separator
        + this.importedfolder
        + File.separator;
  }
}
