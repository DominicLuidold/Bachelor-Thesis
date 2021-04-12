import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Photo } from '@app/_models';
import { OfflinePhoto } from '@app/_models/photo';
import { PhotoService } from '@app/_services';
import { FileUploadComponent } from '@app/file-upload/file-upload.component';
import { environment } from '@environments/environment';
import { fromEvent, Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-photo-manager',
  templateUrl: './photo-manager.component.html',
  styleUrls: ['./photo-manager.component.css']
})
export class PhotoManagerComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild(FileUploadComponent)
  fileUploadComponent: any;

  currentlyOnline: boolean;
  onlineEvent: Observable<Event>;
  offlineEvent: Observable<Event>;
  subscriptions: Subscription[] = [];

  photos: Photo[] = [];
  photoUrl = `${ environment.apiUrl }/photos/`;
  currentOfflinePhotos: OfflinePhoto[];

  constructor(private photoService: PhotoService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.fetchData();
    this.currentOfflinePhotos = JSON.parse(localStorage.getItem('offlinePhotos')) || [];

    // Always fetch latest online/offline status
    this.handleAppConnectivityChanges();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  ngAfterViewInit(): void {
    // Subscribes to ongoing uploads and executes action based on online status.
    this.fileUploadComponent.photoUpload.subscribe(isOnlineUpload => {
      if (isOnlineUpload) {
        this.fetchData();
      } else {
        this.currentOfflinePhotos = JSON.parse(localStorage.getItem('offlinePhotos')) || [];
      }
    });
  }

  /**
   * Syncs the currently available offline photos to the backend by getting them from the LocalStorage.
   * Subsequently clears the LocalStorage.
   */
  syncOfflinePhotos(): void {
    this.photoService.sync(this.currentOfflinePhotos).subscribe(() => {
      localStorage.clear();
      this.currentOfflinePhotos = [];
      this.fetchData();
    }, error => this.snackBar.open(`Fehler während dem Synchronisieren: ${ error }`, 'Close', {
      duration: 5000,
    }));
  }

  /**
   * Fetches all photos.
   */
  fetchData(): void {
    this.photoService.getAll().subscribe(photos => this.photos = photos);
  }

  /**
   * Handles online/offline status changes.
   * <p>
   * Code copied from
   * https://levelup.gitconnected.com/how-to-detect-and-handle-offline-mode-in-angular-without-using-any-library-36a108d015b9
   */
  handleAppConnectivityChanges(): void {
    this.onlineEvent = fromEvent(window, 'online');
    this.offlineEvent = fromEvent(window, 'offline');

    this.subscriptions.push(this.onlineEvent.subscribe(() => this.currentlyOnline = true));
    this.subscriptions.push(this.offlineEvent.subscribe(() => this.currentlyOnline = false));
  }
}
