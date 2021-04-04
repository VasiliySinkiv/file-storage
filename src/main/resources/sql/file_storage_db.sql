DROP DATABASE IF EXISTS file_storage_db;
CREATE DATABASE file_storage_db;
USE file_storage_db;
SET GLOBAL max_allowed_packet = 15728640;


CREATE TABLE media_data (
    id      int        NOT NULL AUTO_INCREMENT,
    content mediumblob NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB
    DEFAULT CHARSET = utf8;

CREATE TABLE files (
    id                int          NOT NULL AUTO_INCREMENT,
    name              varchar(200) NOT NULL,
    type              varchar(200) NOT NULL,
    size              long         NOT NULL,
    date_uploading    DATETIME     NOT NULL,
    date_modification DATETIME,
    link              varchar(200),
    media_data_id     int          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name),
    FOREIGN KEY (media_data_id) REFERENCES media_data (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;