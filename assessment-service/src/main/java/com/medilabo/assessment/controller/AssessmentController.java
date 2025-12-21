package com.medilabo.assessment.controller;

import com.medilabo.assessment.dto.AssessmentDto;
import com.medilabo.assessment.service.AssessmentService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/assess")
public class AssessmentController {

	/**
	 * Constructor injecting the assessment service.
	 *
	 * @param service service handling diabetes risk assessment logic
	 */
	private final AssessmentService service;

	public AssessmentController(AssessmentService service) {
		this.service = service;
	}

	/**
	 * Computes the diabetes risk assessment for a given patient.
	 *
	 * @param patientId unique identifier of the patient
	 * @return an {@link AssessmentDto} containing the risk level and related data
	 */
	@GetMapping("/patient/{patientId}")
	@ResponseStatus(HttpStatus.OK)
	public AssessmentDto assess(@PathVariable long patientId) {
		return service.assessByPatientId(patientId);
	}
}
