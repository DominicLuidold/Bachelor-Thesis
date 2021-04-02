import { SelectionModel } from '@angular/cdk/collections';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Attendee } from '@app/_models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-attendee-column',
  templateUrl: './attendee-column.component.html',
  styleUrls: ['./attendee-column.component.css']
})
export class AttendeeColumnComponent implements OnInit {
  @Input()
  header: string;
  @Input()
  explanation: string;
  @Input()
  buttonText: string;
  @Input()
  observable: Observable<Attendee[]>;

  @ViewChild(MatSort) sort: MatSort;
  selection: SelectionModel<Attendee>;

  displayedColumns: string[] = ['select', 'firstName', 'lastName', 'email'];
  dataSource = new MatTableDataSource<Attendee>();

  constructor() {
    // Intentionally empty
  }

  ngOnInit(): void {
    const initialSelection = [];
    const allowMultiSelect = true;
    this.selection = new SelectionModel<Attendee>(allowMultiSelect, initialSelection);

    this.observable.subscribe(attendees => {
      this.dataSource.data = attendees;
      this.dataSource.sort = this.sort;
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
}
