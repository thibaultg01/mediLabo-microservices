package com.medilabo.assessment.service.impl;

import com.medilabo.assessment.controller.AssessmentController;
import com.medilabo.assessment.dto.AssessmentDto;
import com.medilabo.assessment.dto.NlpExtractRequest;
import com.medilabo.assessment.dto.NlpExtractResponse;
import com.medilabo.assessment.dto.NoteDto;
import com.medilabo.assessment.dto.PatientDto;
import com.medilabo.assessment.model.RiskLevel;
import com.medilabo.assessment.service.AssessmentService;
import com.medilabo.assessment.utils.TriggerAnalyzer;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AssessmentServiceImpl implements AssessmentService {

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Value("${app.nlp-base-url}")
	private String nlpBaseUrl;
	
	private final RestClient http;
	private final String patientsBaseUrl;
	private final String notesBaseUrl;
	private static final Logger logger = LogManager.getLogger(AssessmentServiceImpl.class);

	public AssessmentServiceImpl(RestClient http, @Value("${app.patients-base-url}") String patientsBaseUrl,
			@Value("${app.notes-base-url}") String notesBaseUrl) {
		this.http = http;
		this.patientsBaseUrl = patientsBaseUrl;
		this.notesBaseUrl = notesBaseUrl;
	}

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
		// Construit la requete NLP
				List<NlpExtractRequest.NoteItem> noteItems = notes.stream()
				        .map(n -> new NlpExtractRequest.NoteItem(n.getId(), n.getNote()))
				        .toList();

				NlpExtractRequest nlpRequest = new NlpExtractRequest(noteItems);

				// Appele le service Python
				NlpExtractResponse nlpResponse = webClientBuilder.build()
				        .post()
				        .uri(nlpBaseUrl + "/nlp/extract-triggers")
				        .bodyValue(nlpRequest)
				        .retrieve()
				        .bodyToMono(NlpExtractResponse.class)
				        .block();

				int triggerCount = nlpResponse.getTotalTriggers();
				int age = computeAge(patient.getBirthdate());
				RiskLevel level = computeRisk(age, safeSex(patient.getSex()), triggerCount);

				return new AssessmentDto(patient.getLastName(), age, level, triggerCount);
			}

	private int computeAge(String birthdateIso) {
		if (birthdateIso == null || birthdateIso.isBlank()) {
			return 0;
		}
		LocalDate dob = LocalDate.parse(birthdateIso, DateTimeFormatter.ISO_DATE);
		return Period.between(dob, LocalDate.now()).getYears();
	}

	private String safeSex(String sex) {
		if (sex == null)
			return "";
		return sex.trim();
	}

	private RiskLevel computeRisk(int age, String sex, int triggerCount) {
		if (triggerCount == 0)
			return RiskLevel.NONE;

		boolean over30 = age > 30;
		boolean male = "M".equalsIgnoreCase(sex);
		logger.info("sex : " + male);
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
