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

  private final AssessmentService service;
  private static final Logger logger = LogManager.getLogger(AssessmentController.class);

  public AssessmentController(AssessmentService service) {
    this.service = service;
  }

@GetMapping("/patient/{patientId}")
@ResponseStatus(HttpStatus.OK)
public AssessmentDto assess(@PathVariable long patientId)  {
	  logger.info("tentative recuperition");
    return service.assessByPatientId(patientId);
  }
}
