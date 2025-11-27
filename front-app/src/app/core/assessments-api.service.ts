import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Assessment {
  patientId: number;
  risk: 'None' | 'Borderline' | 'InDanger' | 'EarlyOnset';
  triggers?: number;
  lastUpdated?: string;
}

@Injectable({ providedIn: 'root' })
export class AssessmentsApiService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiBaseUrl;

  getByPatientId(patientId: number): Observable<Assessment> {
    return this.http.get<Assessment>(`${this.baseUrl}/assess/patient/${patientId}`);
  }
}
