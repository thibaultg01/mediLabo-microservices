package com.medilabo.patient.controller;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*; 

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/patients")
@Validated
public class PatientController {
  private final PatientService service;
  public PatientController(PatientService service) { this.service = service; }
  
  private static final Logger logger = LogManager.getLogger(PatientController.class);

  @GetMapping
  public ResponseEntity<List<Patient>> getAll() {
      return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Patient> getById(@PathVariable @Min(1) Long id) {
      Patient p = service.findById(id);
      return ResponseEntity.ok(p);
  }

  @PostMapping
  public ResponseEntity<Patient> create(@Valid @RequestBody Patient payload) {
      Patient created = service.create(payload);
      URI location = URI.create("/patients/" + created.getId());
      return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Patient> update(@PathVariable @Min(1) Long id,
                                        @Valid @RequestBody Patient payload) {
	  logger.info("tentative mis à jour");
      Patient updated = service.update(id, payload);
      logger.info("mis à jour");
      return ResponseEntity.ok(updated);
  }
}