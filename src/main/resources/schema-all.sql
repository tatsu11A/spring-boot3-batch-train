DROP TABLE IF EXISTS USERS CASCADE;

CREATE TABLE USERS (
  ID INT NOT NULL,
  NAME VARCHAR(100),
  DEPARTMENT VARCHAR(100),
  CREATED_AT DATE,
  PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS member_registration;

CREATE TABLE member_registration (
    contract_id VARCHAR(100),
    member_id VARCHAR(100),
    product_id VARCHAR(100),
    card_number VARCHAR(100),
    registration_date DATE
);