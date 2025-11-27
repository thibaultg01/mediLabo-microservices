package com.medilabo.assessment.dto;

import lombok.Data;

@Data
public class NoteDto {
  private String id;
  private Long patId;
  private String note;
  private String createdAt;
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
public String getNote() {
	return note;
}
public void setNote(String note) {
	this.note = note;
}
public String getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
}
  
  
}