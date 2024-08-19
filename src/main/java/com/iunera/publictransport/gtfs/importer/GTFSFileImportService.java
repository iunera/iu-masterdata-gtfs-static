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

import com.iunera.publictransport.gtfs.filemodel.Agency;
import com.iunera.publictransport.gtfs.filemodel.Calendar;
import com.iunera.publictransport.gtfs.filemodel.CalendarDates;
import com.iunera.publictransport.gtfs.filemodel.FeedInfo;
import com.iunera.publictransport.gtfs.filemodel.GTFSProperty;
import com.iunera.publictransport.gtfs.filemodel.IuGtfsKey;
import com.iunera.publictransport.gtfs.filemodel.Routes;
import com.iunera.publictransport.gtfs.filemodel.StopTimes;
import com.iunera.publictransport.gtfs.filemodel.Stops;
import com.iunera.publictransport.gtfs.filemodel.Transfers;
import com.iunera.publictransport.gtfs.filemodel.Trips;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Unzip the input folder GTFS, transform the CSV files into DTOs and then storing them in the
 * database
 */
@Service
public class GTFSFileImportService {

  @Autowired private GtfsRepository gtfsRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(GTFSFileImportService.class);

  private static final String splitOnCommaIgnoringCommaInQuotes =
      ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

  /** The default timezone of the GTFS imports. Defaults to Europe/Berlin */
  @Value("${gtfs.default.timezone:Europe/Berlin}")
  private String timeZone;

  /** The import process is the unique identifier for the import of the file */
  public void importGtfsData(
      String gtfsZipFile,
      String fileIdentifier,
      String timeZone,
      String importProcess,
      boolean revertOnInsertErros,
      boolean skipErrors)
      throws GTFSInsertionException {

    boolean inserterroroccured = false;
    try {

      if (timeZone == null) timeZone = this.timeZone;

      String temppath = fileIdentifier + "-extracted";

      Path tempFileDir = Files.createTempDirectory(temppath);
      String unzipFolder = tempFileDir.toString();

      ZipInputStream zipIn = new ZipInputStream(new FileInputStream(gtfsZipFile));
      ZipEntry entry = zipIn.getNextEntry();
      while (entry != null) {

        String filePath = unzipFolder + File.separator + entry.getName();
        if (!entry.isDirectory()) {
          extractFile(zipIn, filePath);
        } else {
          File dir = new File(filePath);
          dir.mkdirs();
        }
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      }
      zipIn.close();

      IuGtfsKey iuGtfsKey = new IuGtfsKey();
      // allows later to erase specific inserts of files where some parts failed and
      // all file contents shall be removed
      iuGtfsKey.setiu_importprocess(importProcess);
      iuGtfsKey.setProvider_name(fileIdentifier);
      iuGtfsKey.setInsertion_date(new Date());
      iuGtfsKey.setValid_from_date(null);
      iuGtfsKey.setValid_from_date(null);
      iuGtfsKey.setTimezone(timeZone);
      gtfsRepository.insert(iuGtfsKey);

      File folder = new File(unzipFolder);
      File[] fileNames = folder.listFiles();
      for (File file : fileNames) {
        try {
          LOGGER.info("inserting " + file);

          Class<? extends GTFSProperty> rowClazz = null;
          String gtfsfilename = file.getName();
          if (gtfsfilename.equals("feed_info.txt")) {
            rowClazz = FeedInfo.class;
          } else if (gtfsfilename.equals("calendar_dates.txt")) {
            rowClazz = CalendarDates.class;
          } else if (gtfsfilename.equals("calendar.txt")) {
            rowClazz = Calendar.class;
          } else if (gtfsfilename.equals("agency.txt")) {
            rowClazz = Agency.class;
          } else if (gtfsfilename.equals("trips.txt")) {
            rowClazz = Trips.class;
          } else if (gtfsfilename.equals("transfers.txt")) {
            rowClazz = Transfers.class;
          } else if (gtfsfilename.equals("feedInfo.txt")) {
            rowClazz = FeedInfo.class;
          } else if (gtfsfilename.equals("routes.txt")) {
            rowClazz = Routes.class;
          } else if (gtfsfilename.equals("shapes.txt")) {
            LOGGER.debug("skipping shapes - no insertion");

            continue;
          } else if (gtfsfilename.equals("stops.txt")) {
            rowClazz = Stops.class;
          } else if (gtfsfilename.equals("stop_times.txt")) {
            rowClazz = StopTimes.class;
          }

          if (rowClazz == null) continue;

          boolean erroroccured = false;

          FileReader fileReader = new FileReader(file);

          try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            // get the header from the file
            String header = bufferedReader.readLine();
            String columnheaders[] = header.split(splitOnCommaIgnoringCommaInQuotes, -1);

            // remove quotes from header if there are any
            removeQuotes(columnheaders);

            // parse line by line
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {

              try {
                GTFSProperty bean = rowClazz.getDeclaredConstructor().newInstance();
                // get the columns in the line
                String[] tokens = line.split(splitOnCommaIgnoringCommaInQuotes, -1);
                removeQuotes(tokens);

                // map all columns to the bean
                int i = 0;
                for (String column : tokens) {
                  // remote quotes from the string

                  String columnName = columnheaders[i];
                  try {
                    Field field = rowClazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    Class<?> fieldclazz = field.getType();

                    // ensure the proper mapping for the different fields and convert to the correct
                    // type
                    // currently, only float and int are supported
                    if (!(column == null || column.isEmpty() || column.isBlank())) {
                      if (fieldclazz.equals(Integer.class) || fieldclazz.getName().equals("int")) {

                        field.set(bean, Integer.parseInt(column));
                      } else if (fieldclazz.equals(Float.class)
                          || fieldclazz.getName().equals("float")) {

                        field.set(bean, Float.parseFloat(column));
                      } else if (fieldclazz.equals(String.class)) {
                        // if no other type matches, assume it is a string
                        field.set(bean, column);
                      } else {
                        // the type conversion is not planned to happen, yet
                        throw new NoSuchFieldError(
                            "Reading"
                                + gtfsfilename
                                + "problem: "
                                + rowClazz.getName()
                                + " field: "
                                + fieldclazz.getName()
                                + " not supported in "
                                + "Processing line:"
                                + line);
                      }
                    }
                  } catch (NoSuchFieldException e) {
                    LOGGER.error(
                        "SKIPPED GTFS insertion errror. Process "
                            + iuGtfsKey.getiu_importprocess()
                            + " failed to insert",
                        e);
                    if (!skipErrors) throw new IllegalStateException(e);
                  }
                  i++;
                }

                // set the import process id
                bean.setiu_importprocess(iuGtfsKey.getiu_importprocess());

                // insert the row
                GTFSFileImportService.this.gtfsRepository.insert(bean);

              } catch (GTFSInsertionException e) {
                LOGGER.error(
                    "GTFS insertion errror. Process "
                        + iuGtfsKey.getiu_importprocess()
                        + " failed to insert",
                    e);
                erroroccured = true;
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }

          if (erroroccured) {
            inserterroroccured = true;
            throw new GTFSInsertionException("CSV row processing failure");
          }
          LOGGER.debug("file insertion finished");
        } catch (DataAccessException | GTFSInsertionException e) {
          LOGGER.error(
              "GTFS insertion errror. Process "
                  + iuGtfsKey.getiu_importprocess()
                  + " failed to insert",
              e);
          throw e;
        }
      }

      // clean the tmp directory
      File filesList[] = tempFileDir.toFile().listFiles();
      for (File file : filesList) {
        file.delete();
      }
      Files.deleteIfExists(tempFileDir);
    } catch (DataAccessException | IOException e) {
      LOGGER.error("cleanup of tmp files failed", e);
    } finally {
      if (revertOnInsertErros) {
        if (inserterroroccured && revertOnInsertErros)
          this.gtfsRepository.deleteImportprocess(importProcess);
      }
    }
  }

  private void removeQuotes(String[] columns) {
    for (int k = 0; k < columns.length; k++) columns[k] = columns[k].replace("\"", "");
  }

  private static final int BUFFER_SIZE = 4096;

  private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[BUFFER_SIZE];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }

  public void deleteImportprocess(String importProcess) {
    gtfsRepository.deleteImportprocess(importProcess);
  }
}
