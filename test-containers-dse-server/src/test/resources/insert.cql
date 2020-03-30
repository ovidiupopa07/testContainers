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
INSERT INTO car(brand,uuid,model,horse_power,fuel,torque) VALUES ('VW', now(), 'Passat',200,'Diesel','420Nm');

DROP SEARCH INDEX on car;

CREATE SEARCH INDEX IF NOT EXISTS ON car WITH COLUMNS model;

ALTER SEARCH INDEX SCHEMA ON car
   ADD types.fieldType [
      @name='TextField_keyword' ,
      @class='org.apache.solr.schema.TextField' ]
   WITH  $${
      "analyzer": [ {
         "type": "index",
         "tokenizer": { "class": "solr.KeywordTokenizerFactory" },
         "filter": [ { "class": "solr.LowerCaseFilterFactory" },
              { "class": "solr.ASCIIFoldingFilterFactory" }
            ]
          },
          {
            "type": "search",
            "tokenizer": { "class": "solr.KeywordTokenizerFactory" },
            "filter": [
              { "class": "solr.LowerCaseFilterFactory" },
              { "class": "solr.ASCIIFoldingFilterFactory" }
            ]
          }
        ]
      }$$;


      ALTER SEARCH INDEX SCHEMA ON car
        SET field[@name='model']@type = 'TextField_keyword';


      RELOAD SEARCH INDEX ON car;
      REBUILD SEARCH INDEX ON car;
