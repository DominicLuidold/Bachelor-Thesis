import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Event } from '@app/_models';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EventService {

  constructor(private http: HttpClient) {
    // Intentionally empty
  }

  getAll(): Observable<Event[]> {
    return this.http.get<Event[]>(`${ environment.apiUrl }/events`);
  }
}
