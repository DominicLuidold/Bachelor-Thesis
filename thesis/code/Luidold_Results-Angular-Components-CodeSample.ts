[...]

@Component({
  selector: 'app-entrance-control',
  templateUrl: './entrance-control.component.html',
  styleUrls: ['./entrance-control.component.css']
})
export class EntranceControlComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChildren(AttendeeColumnComponent) attendeeColumns;
  
  ngOnInit(): void {
    // Component & dependency initialization
  }
  
  [...]
}
