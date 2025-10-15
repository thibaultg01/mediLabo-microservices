export interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: 'M' | 'F';
  address?: string | null;
  phone?: string | null;
}
