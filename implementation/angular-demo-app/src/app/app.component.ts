import { Component } from '@angular/core';
import { SwUpdate } from '@angular/service-worker';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private updates: SwUpdate) {
    // Automatically refreshes browser page to reflect updated to PWA
    updates.available.subscribe(() => {
      updates.activateUpdate().then(() => document.location.reload());
    });
  }
}
