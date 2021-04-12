import { Component, EventEmitter, OnInit, Output } from '@angular/core';

/**
 * Source code based on https://code-maze.com/angular-material-navigation/
 *
 * @see <a href="https://code-maze.com/angular-material-navigation/">Angular Material Navigation Menu - Complete Responsive Navigation</a>
 */
@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnInit {
  @Output()
  sidenavClose: EventEmitter<any> = new EventEmitter<any>();

  constructor() {
    // Intentionally empty
  }

  ngOnInit(): void {
    // Intentionally empty
  }

  onSidenavClose(): void {
    this.sidenavClose.emit();
  }
}
