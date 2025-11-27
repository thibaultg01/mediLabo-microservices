package com.medilabo.assessment.dto;

import com.medilabo.assessment.model.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AssessmentDto {
	private String patient;
	private int age;
	private RiskLevel riskLevel;
	private int triggerCount;

	public AssessmentDto() {
	}

	public AssessmentDto(String patient, int age, RiskLevel riskLevel, int triggerCount) {
		this.patient = patient;
		this.age = age;
		this.riskLevel = riskLevel;
		this.triggerCount = triggerCount;
	}

	public String getPatient() {
		return patient;
	}

	public int getAge() {
		return age;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public int getTriggerCount() {
		return triggerCount;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public void setTriggerCount(int triggerCount) {
		this.triggerCount = triggerCount;
	}
}
