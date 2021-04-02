import { Event } from './event.interface';

export interface Attendee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  events: Event[];
}
