import { AfterViewInit, Component, OnDestroy, OnInit, ViewChildren } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { RealTimeService } from '@app/_services';
import { AttendeeColumnComponent } from '@app/attendee-column/attendee-column.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-entrance-control',
  templateUrl: './entrance-control.component.html',
  styleUrls: ['./entrance-control.component.css']
})
export class EntranceControlComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChildren(AttendeeColumnComponent) attendeeColumns;
  eventId: string;
  webSocketSubscription: Subscription;

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

  /* Miscellaneous */
  realTimeUpdateNotification = 'Die Teilnehmer:innenlisten wurde aufgrund externer Änderungen aktualisiert';

  constructor(private route: ActivatedRoute, private realTimeService: RealTimeService, private snackBar: MatSnackBar) {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.eventId = this.route.snapshot.paramMap.get('eventId');

    // Subscribe to real-time updates via WebSockets
    this.webSocketSubscription = this.realTimeService.getRealTimeUpdates(this.eventId).subscribe(
      hasRealTimeChangeHappenedForEvent => {
        if (hasRealTimeChangeHappenedForEvent) {
          this.snackBar.open(this.realTimeUpdateNotification, 'Schließen', {
            duration: 5000,
          });
          this.attendeeColumns.forEach(columnToUpdate => columnToUpdate.fetchData());
        }
      }
    );
  }

  ngAfterViewInit(): void {
    this.attendeeColumns.forEach(column => column.dataUpdate.subscribe(() => {
      // Publish real-time update to notify other clients
      this.realTimeService.publishRealTimeUpdate();
      // Tell all tables to fetch updated data when change has happened
      this.attendeeColumns.forEach(columnToUpdate => {
        columnToUpdate.selection.clear();
        columnToUpdate.fetchData();
      });
    }));
  }

  ngOnDestroy(): void {
    this.webSocketSubscription.unsubscribe();
  }
}
