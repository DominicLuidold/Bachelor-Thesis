import { AfterViewInit, Component, OnInit, ViewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AttendeeColumnComponent } from '@app/attendee-column/attendee-column.component';

@Component({
  selector: 'app-entrance-control',
  templateUrl: './entrance-control.component.html',
  styleUrls: ['./entrance-control.component.css']
})
export class EntranceControlComponent implements OnInit, AfterViewInit {
  @ViewChildren(AttendeeColumnComponent) attendeeColumns;
  eventId: string;

  /* Default column */
  defaultTableId = 'default';
  defaultHeader = 'Allgemeine Teilnehmer:innenliste';
  defaultExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events. Diese können ausgewählt, und mittels Button in die jeweils passende Spalte ("Im Veranstaltungsort" und "Außerhalb des "Veranstaltungsorts") verschoben werden.';
  defaultButton = 'Verschieben zu: Im Veranstaltungsort';

  /* Entered column */
  enteredTableId = 'entered';
  enteredHeader = 'Im Veranstaltungsort';
  enteredExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events, die zum aktuellen Zeitpunkt innerhalb des Veranstaltungsorts sind und mittels Button in die Spalte "Außerhalb des Veranstaltungsorts" verschoben werden können.';
  enteredButton = 'Verschieben zu: Außerhalb des Veranstaltungsorts';

  /* Exited column */
  exitedTableId = 'exited';
  exitedHeader = 'Außerhalb des Veranstaltungsorts';
  exitedExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events, die zum aktuellen Zeitpunkt außerhalb des Veranstaltungsorts sind und mittels Button in die Spalte "Im Veranstaltungsort" verschoben werden können.';
  exitedButton = 'Verschieben zu: Im Veranstaltungsort';

  constructor(private route: ActivatedRoute) {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.eventId = this.route.snapshot.paramMap.get('eventId');
  }

  ngAfterViewInit(): void {
    // Tell all tables to fetch updated data when change has happened
    this.attendeeColumns.forEach(column => column.dataUpdate.subscribe(() => {
      this.attendeeColumns.forEach(columnToUpdate => columnToUpdate.fetchData());
    }));
  }
}
