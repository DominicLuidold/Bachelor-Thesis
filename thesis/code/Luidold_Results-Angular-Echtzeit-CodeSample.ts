getRealTimeUpdates(eventId: string): Observable<boolean> {
  [...]
  this.socket.on('serverRealTimeUpdate', (response: RealTimeData) => {
    this.observer.next(response.eventId === this.eventId);
  });

  return new Observable(observer => this.observer = observer);
}
