import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PatientApiService } from '../../core/patient-api.service';
import { Patient } from '../../core/patient.model';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './patient-detail.html',
  styleUrls: ['./patient-detail.scss'],
})
export class PatientDetailComponent {
  private route = inject(ActivatedRoute);
  private api = inject(PatientApiService);

  patient?: Patient;
  loading = true;
  error?: string;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.get(id).subscribe({
      next: (p) => {
        this.patient = p;
        this.loading = false;
      },
      error: (e) => {
        this.error = 'Patient introuvable';
        this.loading = false;
        console.error(e);
      },
    });
  }
}
