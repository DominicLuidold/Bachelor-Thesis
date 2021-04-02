import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from '@app/app-routing.module';
import { AppComponent } from '@app/app.component';
import { EntranceControlComponent } from '@app/entrance-control/entrance-control.component';
import { EventSelectionComponent } from '@app/event-selection/event-selection.component';
import { HomeComponent } from '@app/home/home.component';
import { MaterialModule } from '@app/material-module';
import { NavbarComponent } from '@app/navbar/navbar.component';
import { PhotoManagerComponent } from '@app/photo-manager/photo-manager.component';
import { AttendeeColumnComponent } from './attendee-column/attendee-column.component';

@NgModule({
  declarations: [
    AppComponent,
    EntranceControlComponent,
    EventSelectionComponent,
    HomeComponent,
    NavbarComponent,
    PhotoManagerComponent,
    AttendeeColumnComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  // Intentionally empty
}
