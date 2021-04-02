import { RouterModule, Routes } from '@angular/router';
import { EntranceControlComponent } from '@app/entrance-control/entrance-control.component';
import { EventSelectionComponent } from '@app/event-selection/event-selection.component';
import { HomeComponent } from '@app/home/home.component';
import { PhotoManagerComponent } from '@app/photo-manager/photo-manager.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'entrance-control', component: EventSelectionComponent },
  { path: 'entrance-control/:eventId', component: EntranceControlComponent },
  { path: 'photo-manager', component: PhotoManagerComponent },

  // Otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const AppRoutingModule = RouterModule.forRoot(routes);
