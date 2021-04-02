export interface Attendee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  status?: 'ENTERED' | 'EXITED';
}
