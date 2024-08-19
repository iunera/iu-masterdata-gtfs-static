/*drop schema public cascade;
create schema public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public; 

CREATE TABLE iu_importprocess(iu_importprocess text  PRIMARY KEY, 
provider_name text ,
insertion_date date ,
valid_from_date date, 
valid_to_date date,
timezone text 
);

CREATE TABLE public.agency(agency_id text  PRIMARY KEY,
agency_name text ,
agency_url text ,
agency_timezone text ,
agency_lang text ,
agency_phone text ,
agency_fare_url text ,
agency_email text ,
iu_importprocess text );

CREATE TABLE public.calendar (
	service_id text  PRIMARY KEY,
	monday int4 ,
	tuesday int4 ,
	wednesday int4 ,
	thursday int4 ,
	friday int4 ,
	saturday int4 ,
	sunday int4 ,
	start_date date ,
	end_date date ,
	iu_importprocess text 
);

CREATE TABLE public.fares (
	fare_id varchar(255)  PRIMARY KEY,
	iu_importprocess text 
);

CREATE TABLE public.feed_info (
	feed_publisher_name text ,
	feed_publisher_url text ,
	feed_lang text ,
	default_lang text NULL,
	feed_start_date date NULL,
	feed_end_date date NULL,
	feed_version text NULL,
	feed_contact_email text NULL,
	feed_contact_url text NULL,
	iu_importprocess text
	
);

CREATE TABLE public.shapes (
	id serial  PRIMARY KEY,
	shape_id text NULL,
	shape_pt_sequence int4 NULL,
	shape_pt_loc text NULL,
	shape_dist_traveled float4 NULL,
	shape_pt_lat float8 NULL,
	shape_pt_lon float8 NULL,
	iu_importprocess text 
);

CREATE TABLE public.spatial_ref_sys (
	srid int4  PRIMARY KEY,
	auth_name varchar(256) NULL,
	auth_srid int4 NULL,
	srtext varchar(2048) NULL,
	proj4text varchar(2048) NULL,
	iu_importprocess text 
);

CREATE TABLE public.calendar_dates (
	service_id text ,
	"date" date ,
	exception_type text ,
	iu_importprocess text 
);

CREATE TABLE public.fare_attributes (
	id int8  PRIMARY KEY,
	currency_type int4 NULL,
	payment_type int4 NULL,
	price float8 ,
	transfer_duration float8 NULL,
	transfer_type int4 NULL,
	fare varchar(255) NULL,
	iu_importprocess text 
);

CREATE TABLE public.routes (
	route_id text  PRIMARY KEY,
	agency_id text NULL,
	route_short_name text NULL,
	route_long_name text NULL,
	route_desc text NULL,
	route_type text ,
	route_url text NULL,
	route_color text NULL,
	route_text_color text NULL,
	route_sort_order int4 NULL,
	agency varchar(255) NULL,
	iu_importprocess text 
);

CREATE TABLE public.stops (
	stop_id text  PRIMARY KEY,
	stop_code text NULL,
	stop_name text NULL,
	stop_desc text NULL,
	stop_loc text NULL,
	zone_id text NULL,
	stop_url text NULL,
	location_type text NULL,
	parent_station text NULL,
	stop_timezone text NULL,
	wheelchair_boarding text NULL,
	level_id text NULL,
	platform_code text NULL,
	stop_lat real NULL,
	stop_lon real NULL,
	iu_importprocess text 
);

CREATE TABLE public.transfers (
	id serial  PRIMARY KEY,
	from_stop_id text NULL,
	to_stop_id text NULL,
	transfer_type text NULL,
	min_transfer_time int4 NULL,
	from_route_id text NULL,
	to_route_id text NULL,
	from_trip_id text NULL,
	to_trip_id text NULL,
	iu_importprocess text 
);

CREATE TABLE public.trips (
	trip_id text,
	route_id text ,
	service_id text ,
	trip_headsign text NULL,
	trip_short_name text NULL,
	direction_id int4 NULL,
	block_id text NULL,
	shape_id text NULL,
	wheelchair_accessible text NULL,
	bikes_allowed text NULL,
	iu_importprocess text 
);

CREATE TABLE public.trips_shapes (
	trips_trip_id varchar(255) ,
	shapes_id int8 ,
	shape_id int4 ,
	iu_importprocess text ,
	PRIMARY KEY (trips_trip_id, shape_id)
);

CREATE TABLE public.fare_rules (
	id int8  PRIMARY KEY,
	contains_zone varchar(255) NULL,
	destination_zone varchar(255) NULL,
	origin_zone varchar(255) NULL,
	fare varchar(255) NULL,
	route varchar(255) ,
	iu_importprocess text 
);

CREATE TABLE public.frequencies (
	id int8  PRIMARY KEY,
	end_time time ,
	exact_time int4 NULL,
	headway_secs int8 NULL,
	start_time time ,
	trip varchar(255) ,
	iu_importprocess text 
);

CREATE TABLE public.stop_times (
	trip_id text ,
	arrival_time text NULL,
	departure_time text NULL,
	stop_id text ,
	stop_sequence int4 ,
	stop_sequence_consec int4 NULL,
	stop_headsign text NULL,
	stop_desc text NULL,
	pickup_type text NULL,
	drop_off_type text NULL,
	shape_dist_traveled float4 NULL,
	timepoint text NULL,
	shape_distance_traveled float8 NULL,
	iu_importprocess text 
);

CREATE INDEX iu_importprocess_iu_importprocess_idx ON iu_importprocess(iu_importprocess);
CREATE INDEX iu_importprocess_provider_name_idx ON iu_importprocess(provider_name);
CREATE INDEX calendar_dates_service_id_idx ON calendar_dates(service_id);
CREATE INDEX calendar_service_id_idx ON calendar(service_id);
CREATE INDEX routes_route_id_idx ON routes(route_id);
CREATE INDEX trips_trip_id_idx ON trips(trip_id);
CREATE INDEX stops_stop_id_idx ON stops(stop_id);
CREATE INDEX stops_lat_idx ON stops(stop_lat);
CREATE INDEX stops_lon_idx ON stops(stop_lon);
CREATE INDEX stop_times_stop_id_idx ON stop_times(stop_id);
*/