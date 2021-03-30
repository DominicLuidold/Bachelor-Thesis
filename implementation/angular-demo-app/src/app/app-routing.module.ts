import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  // Otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const AppRoutingModule = RouterModule.forRoot(routes);
