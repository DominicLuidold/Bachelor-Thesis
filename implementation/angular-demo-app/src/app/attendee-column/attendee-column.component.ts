import { SelectionModel } from '@angular/cdk/collections';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Attendee } from '@app/_models';
import { AttendeeService } from '@app/_services';

@Component({
  selector: 'app-attendee-column',
  templateUrl: './attendee-column.component.html',
  styleUrls: ['./attendee-column.component.css']
})
export class AttendeeColumnComponent implements OnInit {
  @Input()
  tableId: string;
  @Input()
  eventId: string;
  @Input()
  header: string;
  @Input()
  explanation: string;
  @Input()
  buttonText: string;
  @Output()
  dataUpdate: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild(MatSort) sort: MatSort;
  selection: SelectionModel<Attendee>;

  displayedColumns: string[] = ['select', 'firstName', 'lastName', 'email'];
  dataSource = new MatTableDataSource<Attendee>();

  constructor(private attendeeService: AttendeeService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  ngOnInit(): void {
    const initialSelection = [];
    const allowMultiSelect = true;
    this.selection = new SelectionModel<Attendee>(allowMultiSelect, initialSelection);
    this.fetchData();
  }

  /**
   * Fetches data based on table column.
   */
  fetchData(): void {
    if (this.tableId === 'default') {
      this.attendeeService.getAllForEvent(this.eventId).subscribe(attendees => {
        this.dataSource.data = attendees;
        this.dataSource.sort = this.sort;
      }, error => this.openSnackBar(error));
    } else if (this.tableId === 'entered') {
      this.attendeeService.getAllForEventByStatus(this.eventId, 'ENTERED').subscribe(attendees => {
        this.dataSource.data = attendees;
        this.dataSource.sort = this.sort;
      }, error => this.openSnackBar(error));
    } else if (this.tableId === 'exited') {
      this.attendeeService.getAllForEventByStatus(this.eventId, 'EXITED').subscribe(attendees => {
        this.dataSource.data = attendees;
        this.dataSource.sort = this.sort;
      }, error => this.openSnackBar(error));
    }
  }

  /**
   * Moves attendees to the specified column, initiates backend update and causes tables to re-fetch updated data.
   */
  moveAttendees(): void {
    let updatedStatus;
    this.selection.selected.forEach(attendee => {
      switch (this.tableId) {
        case 'entered':
          attendee.status = 'EXITED';
          updatedStatus = 'EXITED';
          break;
        case 'default':
        case 'exited':
          attendee.status = 'ENTERED';
          updatedStatus = 'ENTERED';
          break;
      }
    });

    this.attendeeService.markAttendeesAs(this.eventId, this.selection.selected).subscribe(() => {
      this.selection.clear();
      this.dataUpdate.emit();
    });
  }

  /**
   * Whether the number of selected elements matches the total number of rows.
   * <p>
   * Source code copied from https://material.angular.io/components/table/overview#selection
   *
   * @see <a href="https://material.angular.io/components/table/overview#selection">Table | Angular Material</a>
   */
  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /**
   * Selects all rows if they are not all selected; otherwise clear selection.
   * <p>
   * Source code copied from https://material.angular.io/components/table/overview#selection
   *
   * @see <a href="https://material.angular.io/components/table/overview#selection">Table | Angular Material</a>
   */
  masterToggle(): void {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  /**
   * Opens a {@link MatSnackBar}.
   *
   * @param message Message to display
   * @param action  Confirmation button text
   */
  openSnackBar(message: string, action: string = 'Close'): void {
    this.snackBar.open(message, action, {
      duration: 3000,
    });
  }
}
