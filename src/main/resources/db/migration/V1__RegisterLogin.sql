CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

CREATE TABLE user_tbl(
     id VARCHAR(100) NOT NULL PRIMARY KEY,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     gender gender,
     phone_number VARCHAR(15) NOT NULL,
     occupation VARCHAR(50),
     pic VARCHAR(50),
     date TIMESTAMP NOT NULL
);

CREATE TABLE login_tbl(
     id SERIAL NOT NULL PRIMARY KEY,
     email VARCHAR(50) NOT NULL UNIQUE,
     password VARCHAR(100) NOT NULL,
     active BOOLEAN NOT NULL,
     token VARCHAR(100),
     expire_time VARCHAR(50) NOT NULL,
     date_created TIMESTAMP NOT NULL
);

CREATE TABLE role_tbl(
     id SERIAL NOT NULL PRIMARY KEY,
     name VARCHAR(50) NOT NULL
);
