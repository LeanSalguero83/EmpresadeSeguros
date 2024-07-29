--liquibase formatted sql
--changeset jpa_dev:001-ddl-definition.sql splitStatements:false
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE COUNTRIES(
	country_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(64) NOT NULL,
	population int
);

CREATE TABLE STATES(
	state_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(64) NOT NULL,
	population int,
	country_id UUID NOT NULL,
	CONSTRAINT states_countries_fk FOREIGN KEY (country_id) REFERENCES COUNTRIES(country_id)
);

CREATE TABLE CITIES(
	city_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	country_id UUID NOT NULL,
	state_id UUID,
	name varchar(64) NOT NULL,
	population int,
	CONSTRAINT cities_countries_fk FOREIGN KEY (country_id) REFERENCES COUNTRIES(country_id),
	CONSTRAINT cities_states_fk FOREIGN KEY (state_id) REFERENCES STATES(state_id)
);

CREATE TABLE ADDRESSES(
	address_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	country_id UUID NOT NULL,
	city_id UUID NOT NULL,
	state_id UUID,
	address varchar(128) NOT NULL,
	postal_code varchar(16),
	CONSTRAINT addresses_countries_fk FOREIGN KEY (country_id) REFERENCES COUNTRIES(country_id),
	CONSTRAINT addresses_cities_fk FOREIGN KEY (city_id) REFERENCES CITIES(city_id),
	CONSTRAINT addresses_states_fk FOREIGN KEY (state_id) REFERENCES STATES(state_id)
);

CREATE TABLE CUSTOMERS(
	customer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name VARCHAR(64) NOT NUll,
	surname VARCHAR(64) NOT NULL,
	email VARCHAR(320) NOT NULL UNIQUE,
	telephone_number VARCHAR(20),
	created TIMESTAMP WITH TIME ZONE NOT NULL,
	updated TIMESTAMP WITH TIME ZONE,
	address_id UUID,
	CONSTRAINT customers_addresses_fk FOREIGN KEY (address_id) REFERENCES ADDRESSES(address_id)
);

CREATE TABLE POLICIES(
	policy_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(16) NOT NULL UNIQUE,
	description TEXT,
	price numeric(6,2) NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL,
	updated TIMESTAMP WITH TIME ZONE
);

CREATE TABLE SUBSCRIPTIONS(
	policy_id UUID NOT NULL,
	customer_id UUID NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL,
	paid_price numeric(6,2) NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL,
	updated TIMESTAMP WITH TIME ZONE,
	CONSTRAINT subscriptions_pk PRIMARY KEY(policy_id, customer_id),
	CONSTRAINT subscriptions_policies_fk FOREIGN KEY (policy_id) REFERENCES POLICIES(policy_id),
	CONSTRAINT subscriptions_customers_fk FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(customer_id)
);

CREATE TABLE COVERAGES(
	coverage_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(16) NOT NULL,
	description TEXT
);

CREATE TABLE POLICIES_COVERAGES (
	coverage_id UUID NOT NULL,
	policy_id UUID NOT NULL,
	CONSTRAINT pc_policies_coverages PRIMARY KEY(coverage_id, policy_id),
	CONSTRAINT pc_coverages_fk FOREIGN KEY (coverage_id) REFERENCES COVERAGES(coverage_id),
	CONSTRAINT pc_policies_fk FOREIGN KEY (policy_id) REFERENCES POLICIES(policy_id)
);
