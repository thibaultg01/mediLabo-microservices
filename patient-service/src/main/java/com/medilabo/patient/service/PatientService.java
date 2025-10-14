package com.medilabo.patient.service;

import java.util.List;
import com.medilabo.patient.model.Patient;

public interface PatientService {
  List<Patient> findAll();
  Patient findById(Long id);
  Patient create(Patient p);
  Patient update(Long id, Patient p);
}