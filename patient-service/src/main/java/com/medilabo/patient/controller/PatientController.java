package com.medilabo.patient.controller;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService service;
  public PatientController(PatientService service) { this.service = service; }

  @GetMapping
  public List<Patient> all() { return service.findAll(); }

  @GetMapping("/{id}")
  public Patient byId(@PathVariable Long id) { return service.findById(id); }

  @PostMapping
  public ResponseEntity<Patient> create(@Valid @RequestBody Patient body) {
    Patient saved = service.create(body);
    return ResponseEntity
      .created(URI.create("/patients/" + saved.getId()))
      .body(saved);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable Long id, @Valid @RequestBody Patient body) {
    service.update(id, body);
  }
}