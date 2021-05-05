[...]

@Route(value = "entrance-control/:eventId", layout = MainView.class)
@PageTitle("Echtzeit Eingangskontrolle")
@CssImport("./styles/views/entrance-control/entrance-control-view.css")
public class EntranceControlView extends HorizontalLayout implements BeforeEnterObserver {

private final Grid<Attendee> defaultGrid = new Grid<>(Attendee.class);

private final Button moveToEnteredBtn = new Button(/* Translation */);

public EntranceControlView([...]) {
    [...]

    // Components & component configuration
    add(prepareDefaultColumn(), prepareEnteredColumn(), prepareExitedColumn());
}

private VerticalLayout prepareDefaultColumn() {
    return new AttendeesColumn(
        getTranslation("entrance-control.default-grid.heading"),
        getTranslation("entrance-control.default-grid.explanation"),
        defaultGrid,
        "default-attendees-grid",
        moveToEnteredBtn
    );
}

[...]
