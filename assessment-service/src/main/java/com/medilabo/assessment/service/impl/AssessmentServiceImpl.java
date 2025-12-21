package com.medilabo.assessment.service.impl;

import com.medilabo.assessment.controller.AssessmentController;
import com.medilabo.assessment.dto.AssessmentDto;
import com.medilabo.assessment.dto.NoteDto;
import com.medilabo.assessment.dto.PatientDto;
import com.medilabo.assessment.model.RiskLevel;
import com.medilabo.assessment.service.AssessmentService;
import com.medilabo.assessment.utils.TriggerAnalyzer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AssessmentServiceImpl implements AssessmentService {

	private final RestClient http;
	private final String patientsBaseUrl;
	private final String notesBaseUrl;

	/**
	 * Constructs the service with required dependencies and configuration.
	 *
	 * @param http RestClient used to call external microservices
	 * @param patientsBaseUrl base URL of the patient microservice
	 * @param notesBaseUrl base URL of the notes microservice
	 */
	public AssessmentServiceImpl(RestClient http, @Value("${app.patients-base-url}") String patientsBaseUrl,
			@Value("${app.notes-base-url}") String notesBaseUrl) {
		this.http = http;
		this.patientsBaseUrl = patientsBaseUrl;
		this.notesBaseUrl = notesBaseUrl;
	}

	/**
	 * Computes the diabetes risk assessment for a given patient ID.
	 *
	 * The method:
	 * - retrieves patient demographic data
	 * - retrieves all medical notes related to the patient
	 * - computes patient age
	 * - counts trigger terms in medical notes
	 * - determines the diabetes risk level
	 *
	 * @param patientId unique identifier of the patient
	 * @return an AssessmentDto containing the assessment result
	 * @throws IllegalArgumentException if the patient cannot be found
	 */
	@Override
	public AssessmentDto assessByPatientId(long patientId) {
		PatientDto patient = http.get().uri(patientsBaseUrl + "/" + patientId).retrieve().body(PatientDto.class);
		if (patient == null)
			throw new IllegalArgumentException("Patient not found: " + patientId);

		List<NoteDto> notes = http.get().uri(notesBaseUrl + "/patient/{id}", patientId).retrieve()
				.body(new ParameterizedTypeReference<List<NoteDto>>() {
				});
		if (notes == null)
			notes = List.of();
		int age = computeAge(patient.getBirthdate());
		int triggers = TriggerAnalyzer.countTriggers(notes);
		RiskLevel level = computeRisk(age, safeSex(patient.getSex()), triggers);

		return new AssessmentDto(patient.getLastName(), age, level, triggers);
	}

	/**
	 * Computes the patient's age from an ISO-8601 birthdate string.
	 *
	 * @param birthdateIso birthdate in ISO format (yyyy-MM-dd)
	 * @return age in years, or 0 if birthdate is missing or invalid
	 */
	private int computeAge(String birthdateIso) {
		if (birthdateIso == null || birthdateIso.isBlank()) {
			return 0;
		}
		LocalDate dob = LocalDate.parse(birthdateIso, DateTimeFormatter.ISO_DATE);
		return Period.between(dob, LocalDate.now()).getYears();
	}

	/**
	 * Safely normalizes the patient's sex value.
	 *
	 * @param sex raw sex value
	 * @return trimmed sex value or empty string if null
	 */
	private String safeSex(String sex) {
		if (sex == null)
			return "";
		return sex.trim();
	}

	/**
	 * Determines the diabetes risk level based on age, sex, and trigger count.
	 *
	 * @param age patient age
	 * @param sex patient sex
	 * @param triggerCount number of detected trigger terms
	 * @return computed RiskLevel
	 */
	private RiskLevel computeRisk(int age, String sex, int triggerCount) {
		if (triggerCount == 0)
			return RiskLevel.NONE;

		boolean over30 = age > 30;
		boolean male = "M".equalsIgnoreCase(sex);
		boolean female = "F".equalsIgnoreCase(sex);

		if (over30) {
			if (triggerCount >= 8)
				return RiskLevel.EARLY_ONSET;
			if (triggerCount >= 6)
				return RiskLevel.IN_DANGER;
			if (triggerCount >= 2 && triggerCount <= 5)
				return RiskLevel.BORDERLINE;
			return RiskLevel.NONE;
		} else {
			if (male) {
				if (triggerCount >= 5)
					return RiskLevel.EARLY_ONSET;
				if (triggerCount >= 3)
					return RiskLevel.IN_DANGER;
				return RiskLevel.NONE;
			}
			if (female) {
				if (triggerCount >= 7)
					return RiskLevel.EARLY_ONSET;
				if (triggerCount >= 4)
					return RiskLevel.IN_DANGER;
				return RiskLevel.NONE;
			}
			return RiskLevel.NONE;
		}
	}
}
