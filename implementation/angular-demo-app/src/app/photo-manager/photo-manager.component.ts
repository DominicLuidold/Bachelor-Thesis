import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Photo } from '@app/_models';
import { PhotoService } from '@app/_services';
import { FileUploadComponent } from '@app/file-upload/file-upload.component';
import { environment } from '@environments/environment';

@Component({
  selector: 'app-photo-manager',
  templateUrl: './photo-manager.component.html',
  styleUrls: ['./photo-manager.component.css']
})
export class PhotoManagerComponent implements OnInit, AfterViewInit {
  @ViewChild(FileUploadComponent)
  fileUploadComponent: any;

  photos: Photo[] = [];
  photoUrl = `${ environment.apiUrl }/photos/`;

  constructor(private photoService: PhotoService) {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.fetchData();
  }

  ngAfterViewInit(): void {
    this.fileUploadComponent.photoUpload.subscribe(() => {
      this.fetchData();
    });
  }

  private fetchData(): void {
    this.photoService.getAll().subscribe(photos => this.photos = photos);
  }
}
