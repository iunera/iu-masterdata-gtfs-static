# GTFS indexing service and GTFS Masterdata API

A service to process static GTFS data into a Postgres DB.
The data model has the extension that a GTFS file can be stored in different revisions what makes it possible to extract historic schedules.
Key reason for developing this service was that we were not able to not find any ready to use GTFS software to easily import GTFS files form folders or URLs and to query with geo based and provider based slicing easily. Additionally, we required historic GTFS data, where one can store different providers and different revisions of the same data at the same time.
 
Imagine the timetable of a provider changes and the departure times shift by 5 minutes later in winter than in summer. 
Furthermore, different providers might have overlapping data. By storing different metadata keys in the GTFS model, one can now do the following with this service:
- Query for historic data 
- Compare GTFS data changes
- Merge data of multiple providers and then slice it by a coordinates
- Partition consuming services of the data based on query parameters

#Features

## Indexing capabilities
Ultimately, this spring boot based service can index GTFS data in the following ways:

- Read a specified directory regularly
- A rest API call with a URL with a ZIP file to download and index
- Import the gtfs-data directory in the resources folder

All of that can be configured via the application properties. 
Furthermore, the service indexes into UTC by a time zone specification in the settings or in the indexing API, ensuring localization can be done.

## Query capabilities
Different REST endpoints are offered.
The results of all end points can be limited the following parameters to enable the partitioning of clients:

- A longitude/latitude based box (minlon, minlat, maxlon, maxlat)
- The provider of the data
- The agency executing the trips

Endpoints:

- /stops/all/geotimekeykey_lineids?bucketsize=XX 
-> returns a map of fuzzy time geokeys (see domain objects for the key generation) with the time interval (bucket) size specified in the parameter and maps those keys to the most likely associated lines with the line id.
- /stops/all/geotimekeykey_stopids??bucketsize=XX -> same like the line mapping, but for stops in form of the stop id.
- /lines -> returns the line details. Can be used to associate the mappings with the details.
- /stops -> returns the planned stop details. Can be used to associate the mappings with the details.


## Data Model
See the data.sql for the data model.
In case you uncomment the create statements the boot up of the service will create the data model automatically.

The key extension to ordinary GFTS are the following field extensions:
- import process -> The filename or the task name of the indexing to allow the later removal of the data sets easily
- provider -> the provider which offered the GTFS data.
- insertion date -> the day of the adding of the Gtfs data
- valid_from/ valid_to -> used to express if some validity was given by the provider that is not reflected in the data. Often the dates are not curated and where there is a new schedule there is just new GTFS data. This way one can easily slice it. 
- timezone The i18n (e.g. Europe/Berlin) time zone for which the GTFS data is specified. Allows this way to generate UTC schedules. 

# TODO
Should also offer historic and real-time GTFS data from EFA systems.

# Howto build it

```
docker build --no-cache -t iu-gtfs-rest:local .
```

# Inspirations and other credits go to:

* https://developers.google.com/transit/gtfs/reference
* https://github.com/derhuerst/gtfs-via-postgres/tree/be807f1d358a9fbaca93990c46bc37ee367f3c8f/lib
* https://github.com/trein/gtfs-java
* http://developer.onebusaway.org/modules/onebusaway-gtfs-modules/current/onebusaway-gtfs-hibernate-cli.html
* https://project-awesome.org/CUTR-at-USF/awesome-transit
* https://github.com/jamesrwilliams/gtfs-to-sql/blob/master/sql/load-gtfs.sql
* https://github.com/markusvalo/HSLtraffic
* https://github.com/de-stops

# License
[Open Compensation Token License, Version 0.20](https://github.com/open-compensation-token-license/license/blob/main/LICENSE.md)
