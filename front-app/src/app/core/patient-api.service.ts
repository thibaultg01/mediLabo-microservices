import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from './patient.model';

@Injectable({ providedIn: 'root' })
export class PatientApiService {
  private http = inject(HttpClient);

  list(): Observable<Patient[]> {
    return this.http.get<Patient[]>('/patients');
  }

  get(id: number): Observable<Patient> {
    return this.http.get<Patient>(`/patients/${id}`);
  }
}
