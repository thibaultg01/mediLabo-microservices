import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PatientApiService } from '../../core/patient-api.service';
import { Patient } from '../../core/patient.model';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './patient-list.html',
  styleUrls: ['./patient-list.scss'],
})
export class PatientListComponent {
  private api = inject(PatientApiService);
  patients: Patient[] = [];
  loading = true;
  error?: string;

  ngOnInit() {
    this.api.list().subscribe({
      next: (d) => {
        this.patients = d;
        this.loading = false;
      },
      error: (e) => {
        this.error = 'Échec du chargement';
        this.loading = false;
        console.error(e);
      },
    });
  }
}
