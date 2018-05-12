DROP KEYSPACE IF EXISTS myretail;
CREATE KEYSPACE myretail WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE myretail;

DROP TABLE IF EXISTS pricing_metadata;

CREATE TABLE pricing_metadata (
    ID text,
    VALUE double,
    CURRENCY_CODE text,
    UPDT_TS timestamp,
    PRIMARY KEY (ID)
);

INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '13860428', 29.99, 'USD', dateof(now()));


INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '15117729', 19.99, 'USD', dateof(now()));


INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '16483589', 9.99, 'USD', dateof(now()));


INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '16696652', 42.99, 'USD', dateof(now()));


INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '16752456', 37.99, 'USD', dateof(now()));


INSERT INTO pricing_metadata (id, value, currency_code, updt_ts) VALUES (
 '15643793', 29.99, 'USD', dateof(now()));



