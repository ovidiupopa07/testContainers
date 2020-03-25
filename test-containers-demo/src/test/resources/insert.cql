DROP KEYSPACE IF EXISTS test;

CREATE KEYSPACE test
WITH REPLICATION = {
'class':'SimpleStrategy',
'replication_factor':1
};

use test;

DROP TABLE IF EXISTS test_table;

CREATE TABLE IF NOT EXISTS test_table (
	name text, /*PK*/
	uuid TimeUUID,
    last_name text,

	PRIMARY KEY (name, uuid));
	
	
INSERT INTO test_table(name,uuid,last_name) VALUES ('FIRSTNAME', now(), 'lastname');