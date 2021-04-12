import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { ServiceWorkerModule } from '@angular/service-worker';
import { ErrorInterceptor } from '@app/_helpers';
import { AppRoutingModule } from '@app/app-routing.module';
import { AppComponent } from '@app/app.component';
import { AttendeeColumnComponent } from '@app/attendee-column/attendee-column.component';
import { EntranceControlComponent } from '@app/entrance-control/entrance-control.component';
import { EventSelectionComponent } from '@app/event-selection/event-selection.component';
import { FileUploadComponent } from '@app/file-upload/file-upload.component';
import { HomeComponent } from '@app/home/home.component';
import { MaterialModule } from '@app/material-module';
import { NavbarComponent } from '@app/navbar/navbar.component';
import { PhotoManagerComponent } from '@app/photo-manager/photo-manager.component';
import { environment } from '@environments/environment';
import { SidenavListComponent } from './sidenav-list/sidenav-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AttendeeColumnComponent,
    EntranceControlComponent,
    EventSelectionComponent,
    FileUploadComponent,
    HomeComponent,
    NavbarComponent,
    PhotoManagerComponent,
    SidenavListComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    RouterModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the app is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  // Intentionally empty
}
