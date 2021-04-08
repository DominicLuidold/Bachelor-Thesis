import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Event } from '@app/_models';
import { EventService } from '@app/_services';

@Component({
  selector: 'app-event-selection',
  templateUrl: './event-selection.component.html',
  styleUrls: ['./event-selection.component.css']
})
export class EventSelectionComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ['id', 'name', 'location', 'startTime', 'endTime', 'description'];
  dataSource = new MatTableDataSource<Event>();

  constructor(private eventService: EventService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.eventService.getAll().subscribe(events => {
      this.dataSource.data = events;
      this.dataSource.sort = this.sort;
    }, error => {
      this.snackBar.open(error, 'Close', {
        duration: 3000,
      });
    });
  }
}
