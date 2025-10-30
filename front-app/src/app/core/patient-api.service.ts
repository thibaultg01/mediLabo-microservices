import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Patient } from './patient.model';

@Injectable({ providedIn: 'root' })
export class PatientApiService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl; // ex: http://localhost:8080/api

  list(): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.base}/patients`);
  }

  get(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.base}/patients/${id}`);
  }

  update(id: number, body: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.base}/patients/${id}`, body);
  }
}
