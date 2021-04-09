import { Injectable } from '@angular/core';
import { RealTimeData } from '@app/_models/realtime-data';
import { environment } from '@environments/environment';
import { Observable, Observer } from 'rxjs';
import { io, Socket } from 'socket.io-client';
import { v4 as uuidv4 } from 'uuid';

@Injectable({ providedIn: 'root' })
export class RealTimeService {
  clientUuid: uuidv4;
  socket: Socket;
  observer: Observer<any>;

  constructor() {
    this.clientUuid = uuidv4();
    this.socket = io(environment.apiUrl);
  }

  publishRealTimeUpdate(): void {
    console.log('blubb');
    this.socket.emit('clientRealTimeUpdate', {
      senderId: this.clientUuid
    });
  }

  getRealTimeUpdates(): Observable<boolean> {
    this.socket.on('serverRealTimeUpdate', (response: RealTimeData) => {
      // True if realtime update was caused by other client
      this.observer.next(response.senderId !== this.clientUuid);
      console.log(response);
    });

    return new Observable(observer => this.observer = observer);
  }
}
