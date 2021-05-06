[...]

@Injectable({ providedIn: 'root' })
export class AttendeeService {

  constructor(private http: HttpClient) {}

  getAllForEvent(eventId: string): Observable<Attendee[]> {
    return this.http.get<Attendee[]>(
      `${ environment.apiUrl }/events/${ eventId }/attendees`
    );
  }
  
  [...]
}
