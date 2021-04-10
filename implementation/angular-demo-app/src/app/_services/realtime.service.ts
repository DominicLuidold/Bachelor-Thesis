import { Injectable } from '@angular/core';
import { RealTimeData } from '@app/_models/realtime-data';
import { environment } from '@environments/environment';
import { Observable, Observer } from 'rxjs';
import { io, Socket } from 'socket.io-client';

@Injectable({ providedIn: 'root' })
export class RealTimeService {
  eventId: string;
  socket: Socket;
  observer: Observer<any>;

  constructor() {
    this.socket = io(environment.apiUrl);
  }

  /**
   * Publishes a real-time data update (caused by moving attendees into another column) via WebSockets (socket.io).
   */
  publishRealTimeUpdate(): void {
    console.log('Publishing client real-time update..');
    this.socket.emit('clientRealTimeUpdate', {
      eventId: this.eventId
    });
  }

  /**
   * Subscribes to real-time data updates (caused by moving attendees into another column) via WebSockets (socket.io).
   * The event subsequently causes a data update on a client, if the client itself is not the original sender and only
   * if the event matches the event on which the change happened.
   *
   * @param eventId The event to which the client wants to receive real-time updates
   */
  getRealTimeUpdates(eventId: string): Observable<boolean> {
    this.eventId = eventId;
    this.socket.on('serverRealTimeUpdate', (response: RealTimeData) => {
      console.log('Incoming server real-time update..');
      // True if real-time update concerns subscribed event
      this.observer.next(response.eventId === this.eventId);
    });

    return new Observable(observer => this.observer = observer);
  }
}
