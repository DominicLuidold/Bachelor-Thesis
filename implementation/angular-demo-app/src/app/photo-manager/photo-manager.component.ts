import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FileUploadComponent } from '@app/file-upload/file-upload.component';

@Component({
  selector: 'app-photo-manager',
  templateUrl: './photo-manager.component.html',
  styleUrls: ['./photo-manager.component.css']
})
export class PhotoManagerComponent implements OnInit, AfterViewInit {
  @ViewChild(FileUploadComponent)
  fileUploadComponent: any;

  constructor() {
    // Intentionally empty
  }

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    this.fileUploadComponent.photoUpload.subscribe(photo => {
      console.log(photo);
    });
  }
}
