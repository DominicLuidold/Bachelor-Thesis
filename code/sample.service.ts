[...]

@Injectable()
export class SampleService {
    constructor(private http: HttpClient) {}

    getSampleData() {
        return this.http.get<Array<Sample>>(/* URL to server or API endpoint */);
    }
}

[...]
