import { Component, OnInit } from '@angular/core';
import { Attendee } from '@app/_models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-entrance-control',
  templateUrl: './entrance-control.component.html',
  styleUrls: ['./entrance-control.component.css']
})
export class EntranceControlComponent implements OnInit {

  /* Default column */
  defaultHeader = 'Allgemeine Teilnehmer:innenliste';
  defaultExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events. Diese können ausgewählt, und mittels Button & Drag\'n\'Drop in die jeweils passende Spalte ("Im Veranstaltungsort" und "Außerhalb des "Veranstaltungsorts") verschoben werden.';
  defaultButton = 'Verschieben zu: Im Veranstaltungsort';

  defaultObservable: Observable<Attendee[]>;

  /* Entered column */
  enteredHeader = 'Im Veranstaltungsort';
  enteredExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events, die zum aktuellen Zeitpunkt innerhalb des Veranstaltungsorts sind und mittels Button & Drag\'n\'Drop in die Spalte "Außerhalb des Veranstaltungsorts" verschoben werden können.';
  enteredButton = 'Verschieben zu: Außerhalb des Veranstaltungsorts';

  enteredObservable: Observable<Attendee[]>;

  /* Exited column */
  exitedHeader = 'Außerhalb des Veranstaltungsorts';
  exitedExplanation = 'Diese List repräsentiert alle Teilnehmer:innen des Events, die zum aktuellen Zeitpunkt außerhalb des Veranstaltungsorts sind und mittels Button & Drag\'n\'Drop in die Spalte "Im Veranstaltungsort" verschoben werden können.';
  exitedButton = 'Verschieben zu: Im Veranstaltungsort';

  exitedObservable: Observable<Attendee[]>;

  constructor() {
    // Intentionally empty
  }

  ngOnInit(): void {
    this.defaultObservable = new Observable<Attendee[]>(); // TODO - replace with actual call
    this.enteredObservable = new Observable<Attendee[]>(); // TODO - replace with actual call
    this.exitedObservable = new Observable<Attendee[]>(); // TODO - replace with actual call
  }
}
