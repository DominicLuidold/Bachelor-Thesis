import { Attendee } from './attendee.interface';
import { Event } from './event.interface';

export interface LogEntry {
  id: string;
  event: Event;
  attendee: Attendee;
  status: 'ENTERED' | 'EXITED';
  timestamp: Date;
}
