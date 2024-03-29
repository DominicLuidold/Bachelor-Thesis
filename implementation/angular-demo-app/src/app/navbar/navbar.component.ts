import { Component, EventEmitter, OnInit, Output } from '@angular/core';

/**
 * Source code based on https://code-maze.com/angular-material-navigation/
 *
 * @see <a href="https://code-maze.com/angular-material-navigation/">Angular Material Navigation Menu - Complete Responsive Navigation</a>
 */
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output()
  sidenavToggle: EventEmitter<any> = new EventEmitter<any>();

  constructor() {
    // Intentionally empty
  }

  ngOnInit(): void {
    // Intentionally empty
  }

  onToggleSidenav(): void {
    this.sidenavToggle.emit();
  }
}
