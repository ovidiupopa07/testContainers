DROP KEYSPACE IF EXISTS test;

CREATE KEYSPACE test
WITH REPLICATION = {
'class':'SimpleStrategy',
'replication_factor':1
};

use test;

DROP TABLE IF EXISTS car;

CREATE TABLE IF NOT EXISTS car (
	brand text, /*PK*/
	uuid TimeUUID,
    model text,
    horse_power int,
    fuel text,
    torque text,

	PRIMARY KEY (brand, uuid));
	
	
INSERT INTO car(brand,uuid,model,horse_power,fuel,torque) VALUES ('VW', now(), 'Tiguan',200,'Petrol','300Nm');