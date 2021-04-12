import { HttpEventType } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { OfflinePhoto } from '@app/_models/photo';
import { PhotoService } from '@app/_services';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { v4 as uuidv4 } from 'uuid';

/**
 * Source code partially based on https://blog.angular-university.io/angular-file-upload/
 *
 * @see <a href="https://blog.angular-university.io/angular-file-upload/">Angular File Upload - Complete Guide</a>
 */
@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  @ViewChild('fileUpload')
  inputElement: ElementRef;
  @Input()
  requiredFileType: string;
  @Output()
  photoUpload: EventEmitter<boolean> = new EventEmitter<boolean>();

  uploadProgress: number;
  uploadSubscription: Subscription;

  constructor(private photoService: PhotoService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  /**
   * Handles selecting a file via the custom upload element.
   * <p>
   * Source code inspired by https://blog.angular-university.io/angular-file-upload/
   *
   * @param event The event that occurs when selecting a file
   * @see <a href="https://blog.angular-university.io/angular-file-upload/">Angular File Upload - Complete Guide</a>
   */
  onFileSelected(event): void {
    const file: File = event.target.files[0];

    if (file) {
      // Limit file size to 3MB
      if (file.size > 3145730) {
        this.snackBar.open('Datei ist zu groß! Maximal 3MB/Foto..', 'Close', {
          duration: 5000
        });
        this.inputElement.nativeElement.value = null;
        return;
      }

      // Only upload to backend if client is actually online, otherwise save photo in LocalStorage
      if (window.navigator.onLine) {
        this.uploadToBackend(file);
      } else {
        this.saveInLocalStorage(file);
      }

      // Reset input element to allow uploading same photo twice
      this.inputElement.nativeElement.value = null;
    }
  }

  /**
   * Uploads a photo to the backend.
   * <p>
   * Source code inspired by https://blog.angular-university.io/angular-file-upload/
   *
   * @param file the photo to upload
   * @see <a href="https://blog.angular-university.io/angular-file-upload/">Angular File Upload - Complete Guide</a>
   */
  uploadToBackend(file: File): void {
    const formData = new FormData();
    formData.append('userUpload', file);

    const upload$ = this.photoService.uploadPhoto(formData).pipe(finalize(() => {
      this.reset();
      // Tell parent component to display photo
      this.photoUpload.emit(true);
    }));
    this.uploadSubscription = upload$.subscribe(uploadEvent => {
      // Show progress bar based on current upload status
      if (uploadEvent.type === HttpEventType.UploadProgress) {
        this.uploadProgress = Math.round(100 * (uploadEvent.loaded / uploadEvent.total));
      }
    });
  }

  /**
   * Saves a photo to the LocalStorage if the client is currently offline. Allows syncing later on.
   *
   * @param file The photo to save locally
   */
  saveInLocalStorage(file: File): void {
    this.readUploadedFileAsBase64(file).then(result => {
      const currentOfflinePhotos: OfflinePhoto[] = JSON.parse(localStorage.getItem('offlinePhotos')) || [];
      currentOfflinePhotos.push({
        id: uuidv4(),
        fileName: file.name,
        uploadedAt: new Date(),
        imageData: btoa(result)
      });
      localStorage.setItem('offlinePhotos', JSON.stringify(currentOfflinePhotos));
      this.photoUpload.emit(false);
    });
  }

  /**
   * Encodes a file to base64 to allow easier storage.
   *
   * @param file The file to encode
   */
  readUploadedFileAsBase64(file: File): Promise<string> {
    const reader = new FileReader();

    return new Promise<string>((resolve) => {
      reader.onload = () => {
        resolve(reader.result.toString());
      };
      reader.readAsBinaryString(file);
    });
  }

  /**
   * Cancels an ongoing upload.
   * <p>
   * Source code copied from https://blog.angular-university.io/angular-file-upload/
   *
   * @see <a href="https://blog.angular-university.io/angular-file-upload/">Angular File Upload - Complete Guide</a>
   */
  cancelUpload(): void {
    this.uploadSubscription.unsubscribe();
    this.reset();
  }

  /**
   * Resets the progress bar and HTTP subscription.
   * <p>
   * Source code copied from https://blog.angular-university.io/angular-file-upload/
   *
   * @see <a href="https://blog.angular-university.io/angular-file-upload/">Angular File Upload - Complete Guide</a>
   */
  reset(): void {
    this.uploadProgress = null;
    this.uploadSubscription = null;
  }
}
