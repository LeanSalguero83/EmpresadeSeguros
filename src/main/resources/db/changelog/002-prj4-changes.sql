--liquibase formatted sql
--changeset jpa_dev:002-prj4-changes.sql splitStatements:false

ALTER TABLE CUSTOMERS ADD COLUMN birth_date DATE NOT NULL;