CREATE TABLE passengers_table  ( tuid integer,    first_initial varchar(1),    middle_initial varchar(1),    last_name varchar(60),    phone_number varchar(60),    primary key (tuid)  );
CREATE TABLE planes_table(  tuid integer,  plane_id varchar(5),  max_vip integer,  max_luxury integer,  primary key (tuid));
CREATE TABLE fees_table(  tuid integer,  seat_type varchar(1),  fee integer,  primary key (tuid));
CREATE TABLE flights_table(  tuid integer,  plane_tuid integer,  airport_code varchar(3),  depart_gate integer,  depart_time varchar(60),  primary key (tuid),  foreign key (plane_tuid) references planes_table);
CREATE TABLE schedules_table(  tuid integer,  passenger_tuid integer,  flight_tuid integer,  flight_date varchar(60),  seat_section varchar(1),  seat_number integer,  primary key (tuid),  foreign key (passenger_tuid) references passengers_table,  foreign key (flight_tuid) references flights_table);
