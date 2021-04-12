import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OfflinePhoto, Photo } from '@app/_models/photo';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PhotoService {

  constructor(private http: HttpClient) {
    // Intentionally empty
  }

  /**
   * Returns all references to uploaded {@link Photo}s.
   */
  getAll(): Observable<Photo[]> {
    return this.http.get<Photo[]>(`${ environment.apiUrl }/photos`);
  }

  /**
   * Uploads a single photo.
   *
   * @param formData The {@link FormData} containing the photo
   */
  uploadPhoto(formData: FormData): Observable<any> {
    return this.http.post(`${ environment.apiUrl }/photos`, formData, {
      reportProgress: true,
      observe: 'events'
    });
  }

  /**
   * Syncs {@link OfflinePhoto}s to the backend.
   *
   * @param offlinePhotos Array of offline photos
   */
  sync(offlinePhotos: OfflinePhoto[]): Observable<any> {
    return this.http.post(`${ environment.apiUrl }/photos/sync`, offlinePhotos);
  }
}
