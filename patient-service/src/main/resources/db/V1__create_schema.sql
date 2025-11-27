CREATE TABLE patients (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name  VARCHAR(100) NOT NULL,
  birth_date DATE NOT NULL,
  gender     VARCHAR(1) NOT NULL,
  address    VARCHAR(255),
  phone      VARCHAR(50) NOT NULL,
  CONSTRAINT uk_patients_phone UNIQUE (phone)
);
CREATE INDEX idx_patients_lastname ON patients(last_name);
