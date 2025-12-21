package com.medilabo.assessment.service;

import com.medilabo.assessment.dto.AssessmentDto;

public interface AssessmentService {
	AssessmentDto assessByPatientId(long patientId);
}
