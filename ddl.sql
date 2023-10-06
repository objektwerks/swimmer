DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA PUBLIC;

CREATE TABLE account (
  id BIGSERIAL PRIMARY KEY,
  license VARCHAR(36) UNIQUE NOT NULL,
  email_address VARCHAR UNIQUE NOT NULL,
  pin VARCHAR(7) NOT NULL,
  activated BIGINT NOT NULL,
  deactivated BIGINT NOT NULL
);

CREATE TABLE swimmer (
  id BIGSERIAL PRIMARY KEY,
  license VARCHAR(36) REFERENCES account(license),
  name VARCHAR(24) NOT NULL,
  height INT NOT NULL,
  weight INT NOT NULL
);

CREATE TABLE session (
  id BIGSERIAL PRIMARY KEY,
  pool_id BIGINT REFERENCES pool(id),
  lap INT NOT NULL,
  laps INT NOT NULL,
  stoke VARCHAR(6) NOT NULL,
  kickboard BOOL NOT NULL,
  fins BOOL NOT NULL,
  to BIGINT NOT NULL,
  from BIGINT NOT NULL
);