import { HttpEventType } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Photo } from '@app/_models';
import { PhotoService } from '@app/_services';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

/**
 * Logic based/inspired on https://blog.angular-university.io/angular-file-upload/
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
  photoUpload: EventEmitter<Photo> = new EventEmitter<Photo>();

  uploadProgress: number;
  uploadSubscription: Subscription;

  constructor(private photoService: PhotoService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  onFileSelected(event): void {
    const file: File = event.target.files[0];

    if (file) {
      const formData = new FormData();
      formData.append('userUpload', file);

      const upload$ = this.photoService.uploadPhoto(formData).pipe(finalize(() => this.reset()));
      this.uploadSubscription = upload$.subscribe(uploadEvent => {
        if (uploadEvent.type === HttpEventType.UploadProgress) {
          this.uploadProgress = Math.round(100 * (uploadEvent.loaded / uploadEvent.total));
        }
      });

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
