import { Routes } from '@angular/router';
import { PatientListComponent } from './pages/patient-list/patient-list';
import { PatientDetailComponent } from './pages/patient-detail/patient-detail';

export const routes: Routes = [
  { path: '', redirectTo: 'patients', pathMatch: 'full' },
  { path: 'patients', component: PatientListComponent },
  { path: 'patients/:id', component: PatientDetailComponent },
  { path: '**', redirectTo: 'patients' },
];
