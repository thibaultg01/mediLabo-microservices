import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { PatientApiService } from '../../core/patient-api.service';
import { Patient } from '../../core/patient.model';

import { NotesApiService } from '../../core/notes-api.service';
import { Note } from '../../core/note.model';

import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './patient-detail.html',
  styleUrls: ['./patient-detail.scss'],
})
export class PatientDetailComponent {
  private route = inject(ActivatedRoute);
  private api = inject(PatientApiService);
  private notesApi = inject(NotesApiService);

  // Patient
  patient?: Patient;
  loading = true;
  error?: string;

  // Edition patient
  editMode = false;
  form: Partial<Patient> = {};
  saving = false;
  saveError?: string;

  // Notes
  notes?: Note[];
  notesLoading = true;
  notesError?: string;

  // Ajout de note
  newNote = '';
  addLoading = false;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.api.get(id).subscribe({
      next: (p: Patient) => {
        this.patient = p;
        this.loading = false;
        this.loadNotes(id);
      },
      error: (e: unknown) => {
        this.error = 'Patient introuvable';
        this.loading = false;
        console.error(e);
      },
    });
  }

  startEdit() {
    if (!this.patient) return;
    this.form = { ...this.patient }; // copie de travail
    this.editMode = true;
    this.saveError = undefined;
  }

  cancelEdit() {
    this.editMode = false;
    this.form = {};
    this.saveError = undefined;
  }

  save(f: any) {
    if (!this.patient || f.invalid) return;

    this.saving = true;
    this.saveError = undefined;

    this.api
      .update(this.patient.id!, {
        id: this.patient.id,
        firstName: this.form.firstName!.trim(),
        lastName: this.form.lastName!.trim(),
        birthDate: this.form.birthDate!,
        gender: this.form.gender!,
        address: this.form.address?.toString().trim() || null,
        phone: this.form.phone?.toString().trim() || null,
      } as Patient)
      .subscribe({
        next: (updated: Patient) => {
          this.patient = updated;
          this.saving = false;
          this.editMode = false;
        },
        error: (e: unknown) => {
          const he = e as HttpErrorResponse;
          this.saveError =
            (he.error && (he.error.message || he.error.error || he.error.details)) ||
            `Échec de l’enregistrement (HTTP ${he.status})`;
          this.saving = false;
          console.error('PUT /patients error', he);
        },
      });
  }

  private loadNotes(patientId: number) {
    this.notesLoading = true;
    this.notesError = undefined;

    this.notesApi.getByPatientId(patientId).subscribe({
      next: (ns: Note[]) => {
        this.notes = ns;
        this.notesLoading = false;
      },
      error: (e: unknown) => {
        this.notesError = 'Impossible de charger les notes';
        this.notesLoading = false;
        console.error(e);
      },
    });
  }

  addNote() {
    if (!this.patient || !this.newNote?.trim()) return;

    this.addLoading = true;

    const patientLabel =
      [(this.patient as any).lastName, (this.patient as any).firstName]
        .filter(Boolean)
        .join(' ')
        .trim() ||
      (this.patient as any).fullName ||
      (this.patient as any).name ||
      'Patient';

    this.notesApi
      .add(Number((this.patient as any).id), patientLabel, this.newNote.trim())
      .subscribe({
        next: (created) => {
          this.notes = [created, ...(this.notes ?? [])];
          this.newNote = '';
          this.addLoading = false;
        },
        error: (err) => {
          console.error(err);
          this.notesError = 'Erreur lors de l’ajout de la note';
          this.addLoading = false;
        },
      });
  }
  getContent(n: Note): string {
    return (n as any).content ?? (n as any).note ?? (n as any).text ?? (n as any).comment ?? '';
  }
}
