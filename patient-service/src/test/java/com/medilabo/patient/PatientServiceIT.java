package com.medilabo.patient;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PatientServiceIT {

	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8").withDatabaseName("patientsdb").withUsername("app")
			.withPassword("app");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@Autowired
	private PatientRepository patientRepository;

	@Test
	void testInsertAndFindPatient() {
		Patient p = new Patient();
		p.setFirstName("Wich");
		p.setLastName("John");
		p.setGender("M");
	    p.setBirthDate(LocalDate.of(1980, 1, 1));

		Patient saved = patientRepository.save(p);

		assertThat(saved.getId()).isNotNull();

		Patient found = patientRepository.findById(saved.getId()).orElseThrow();
		assertThat(found.getFirstName()).isEqualTo("Wich");
	}
}
