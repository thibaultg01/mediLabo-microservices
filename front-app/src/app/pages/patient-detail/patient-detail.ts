import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { PatientApiService } from '../../core/patient-api.service';
import { Patient } from '../../core/patient.model';

import { NotesApiService } from '../../core/notes-api.service';
import { Note } from '../../core/note.model';

import { HttpErrorResponse } from '@angular/common/http';

import { AssessmentsApiService, Assessment } from '../../core/assessments-api.service';
import { switchMap, finalize } from 'rxjs/operators';

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
  private assessmentsApi = inject(AssessmentsApiService);

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

  // Assessment
  assessment?: Assessment | null;
  assessmentLoading = false;
  assessmentError: string | null = null;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.api.get(id).subscribe({
      next: (p: Patient) => {
        this.patient = p;
        this.loading = false;
        this.loadNotes(id);
        this.loadAssessment(id);
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
    this.form = { ...this.patient };
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

  private loadAssessment(patientId: number) {
    this.assessment = null;
    this.assessmentError = null;
    this.assessmentLoading = true;

    this.assessmentsApi
      .getByPatientId(patientId)
      .pipe(finalize(() => (this.assessmentLoading = false)))
      .subscribe({
        next: (a) => (this.assessment = a),
        error: (err) => {
          console.error(err);
          this.assessmentError = 'Impossible de récupérer l’évaluation.';
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
          this.loadAssessment(this.patient!.id!);
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

  riskOf(a: any): string {
    const raw =
      a?.risk ??
      a?.riskLevel ??
      a?.risk_level ??
      a?.result ??
      a?.assessment ??
      a?.diabetesRisk ??
      a?.diabetes_assessment ??
      a?.category ??
      '';
    return (raw ?? '').toString();
  }

  normalizedRisk(a: any): string {
    const v = this.riskOf(a).toLowerCase().trim();
    if (!v) return '';

    if (v.includes('early')) return 'EarlyOnset';
    if (v.includes('danger') || v.includes('in danger')) return 'InDanger';
    if (v.includes('border')) return 'Borderline';
    if (v.includes('none') || v.includes('aucun') || v.includes('no risk')) return 'None';

    if (['earlyonset', 'indanger', 'borderline', 'none'].includes(v.replace(/\s+/g, ''))) {
      const map: any = {
        earlyonset: 'EarlyOnset',
        indanger: 'InDanger',
        borderline: 'Borderline',
        none: 'None',
      };
      return map[v.replace(/\s+/g, '')];
    }
    return this.riskOf(a);
  }

  triggersOf(a: any): number | null {
    const t =
      a?.triggers ?? a?.triggerCount ?? a?.totalTriggers ?? a?.nbTriggers ?? a?.count ?? null;
    return typeof t === 'number' ? t : t != null ? Number(t) : null;
  }
}
