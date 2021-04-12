import { HttpEventType } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PhotoService } from '@app/_services';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

/**
 * Logic based on https://blog.angular-university.io/angular-file-upload/
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
  photoUpload: EventEmitter<any> = new EventEmitter<any>();

  uploadProgress: number;
  uploadSubscription: Subscription;

  constructor(private photoService: PhotoService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  onFileSelected(event): void {
    const file: File = event.target.files[0];

    if (file) {
      // Limit file size to 10MB
      if (file.size > 10485760) {
        this.snackBar.open('Datei ist zu groß! Maximal 10MB/Foto..', 'Close', {
          duration: 5000
        });
        this.inputElement.nativeElement.value = null;
        return;
      }

      const formData = new FormData();
      formData.append('userUpload', file);

      const upload$ = this.photoService.uploadPhoto(formData).pipe(finalize(() => {
        this.reset();
        // Tell parent component to display photo
        this.photoUpload.emit();
      }));
      this.uploadSubscription = upload$.subscribe(uploadEvent => {
        if (uploadEvent.type === HttpEventType.UploadProgress) {
          this.uploadProgress = Math.round(100 * (uploadEvent.loaded / uploadEvent.total));
        }
      });

      // Reset input element to allow uploading same image twice
      this.inputElement.nativeElement.value = null;
    }
  }

  cancelUpload(): void {
    this.uploadSubscription.unsubscribe();
    this.reset();
  }

  reset(): void {
    this.uploadProgress = null;
    this.uploadSubscription = null;
  }
}
