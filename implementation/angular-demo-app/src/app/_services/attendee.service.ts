import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Attendee } from '@app/_models';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AttendeeService {

  constructor(private http: HttpClient) {
    // Intentionally empty
  }

  /**
   * Returns all {@link Attendee}s without any previous status change for the given id.
   *
   * @param eventId Event to get all attendees for
   */
  getAllForEvent(eventId: string): Observable<Attendee[]> {
    return this.http.get<Attendee[]>(`${ environment.apiUrl }/events/${ eventId }/attendees`);
  }

  /**
   * Returns all {@link Attendee}s with a previous status change for the given id and status.
   *
   * @param eventId Event to get all attendees for
   * @param status  Status to get all attendees for
   */
  getAllForEventByStatus(eventId: string, status: 'ENTERED' | 'EXITED'): Observable<Attendee[]> {
    return this.http.get<Attendee[]>(`${ environment.apiUrl }/events/${ eventId }/attendees/status/${ status.toLowerCase() }`);
  }

  /**
   * Marks {@link Attendee}s either as entered or exited.
   *
   * @param eventId   Event for which attendees get marked
   * @param attendees Updated attendees
   */
  markAttendeesAs(eventId: string, attendees: Attendee[]): Observable<any> {
    return this.http.put(`${ environment.apiUrl }/attendees/event/${ eventId }`, attendees);
  }
}
