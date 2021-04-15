[...]

@Injectable()
export class SampleService {
    constructor(private http: HttpClient) {}

    getSampleData(): Observable<Sample[]> {
        return this.http.get<Sample[]>(/* URL to server or API endpoint */);
    }
}

[...]
