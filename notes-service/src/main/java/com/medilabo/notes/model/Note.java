package com.medilabo.notes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "notes")
public class Note {
	@Id
	private String id;

	@Indexed
	private Long patId;

	private String patient;
	private String note;
	private Instant createdAt = Instant.now();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getPatId() {
		return patId;
	}
	public void setPatId(Long patId) {
		this.patId = patId;
	}
	public String getPatient() {
		return patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
