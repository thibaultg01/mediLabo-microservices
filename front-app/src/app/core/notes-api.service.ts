import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { Note } from './note.model';
import { Patient } from './patient.model';

@Injectable({ providedIn: 'root' })
export class NotesApiService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiBaseUrl;

  getByPatientId(patientId: number): Observable<Note[]> {
    return this.http.get<Partial<Note>[]>(`${this.baseUrl}/notes/patient/${patientId}`).pipe(
      map((list: Partial<Note>[]) =>
        list.map((raw) => ({
          id: raw.id,
          patientId: Number((raw as any).patientId ?? (raw as any).patId ?? patientId),
          content:
            (raw as any).content ??
            (raw as any).note ??
            (raw as any).text ??
            (raw as any).comment ??
            '',
          createdAt: (raw as any).createdAt as string | undefined,
        }))
      )
    );
  }

  add(patId: number, patient: string, note: string): Observable<Note> {
    const body = {
      patId: Number(patId),
      patient: (patient ?? '').trim(),
      note: (note ?? '').trim(),
    };
    return this.http.post<Note>(`${this.baseUrl}/notes`, body);
  }
}
