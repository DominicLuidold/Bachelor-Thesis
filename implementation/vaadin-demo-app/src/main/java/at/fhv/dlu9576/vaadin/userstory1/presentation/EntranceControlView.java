package at.fhv.dlu9576.vaadin.userstory1.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.LogEntry;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.AttendeeService;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.LogEntryService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Route(value = "entrance-control/:eventId", layout = MainView.class)
@PageTitle("Echtzeit Eingangskontrolle")
@CssImport("./views/entrance-control/entrance-control-view.css")
public class EntranceControlView extends HorizontalLayout implements BeforeEnterObserver {
    private static final long serialVersionUID = 6414586759012039620L;

    private final AttendeeService attendeeService;
    private final LogEntryService logEntryService;

    private UUID eventId;

    private final Grid<Attendee> attendeeGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> enteredAttendeesGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> exitedAttendeesGrid = new Grid<>(Attendee.class);

    private final List<Attendee> selectedFromAttendeeGrid = new LinkedList<>();
    private final List<Attendee> selectedFromEnteredGrid = new LinkedList<>();
    private final List<Attendee> selectedFromExitedGrid = new LinkedList<>();

    private final Button moveToEnteredBtn = new Button("Verschieben zu: Im Veranstaltungsort");
    private final Button moveToExitedBtn = new Button(
        "Verschieben zu: Außerhalb des Veranstaltungsorts"
    );
    private final Button moveToEnteredFromExitedBtn = new Button(
        "Verschieben zu: Im Veranstaltungsort"
    );

    public EntranceControlView(AttendeeService attendeeService, LogEntryService logEntryService) {
        this.attendeeService = attendeeService;
        this.logEntryService = logEntryService;

        setSizeFull();
        setClassName("responsiveGrids");
        configureDragAndDrop();

        add(
            prepareDefaultAttendeesColumn(),
            prepareEnteredAttendeesColumn(),
            prepareExitedAttendeesColumn()
        );

        prepareButtonFunctionality();
    }

    private VerticalLayout prepareDefaultAttendeesColumn() {
        return new AttendeesColumn(
            "Allgemeine Teilnehmer:innenliste",
            "Diese List repräsentiert alle Teilnehmer:innen des Events. " +
                "Diese können ausgewählt, und mittels Button & Drag'n'Drop in die jeweils " +
                "passende Spalte (\"Im Veranstaltungsort\" und \"Außerhalb des " +
                "Veranstaltungsorts\") verschoben werden.",
            attendeeGrid,
            "default-attendees-grid",
            moveToEnteredBtn
        );
    }

    private VerticalLayout prepareEnteredAttendeesColumn() {
        return new AttendeesColumn(
            "Im Veranstaltungsort",
            "Diese List repräsentiert alle Teilnehmer:innen des Events, " +
                "die zum aktuellen Zeitpunkt innerhalb des Veranstaltungsorts sind und mittels " +
                "Button & Drag'n'Drop in die Spalte \"Außerhalb des Veranstaltungsorts\" " +
                "verschoben werden können.",
            enteredAttendeesGrid,
            "entered-attendees-grid",
            moveToExitedBtn
        );
    }

    private VerticalLayout prepareExitedAttendeesColumn() {
        return new AttendeesColumn(
            "Außerhalb des Veranstaltungsorts",
            "Diese List repräsentiert alle Teilnehmer:innen des Events, " +
                "die zum aktuellen Zeitpunkt außerhalb des Veranstaltungsorts sind und mittels " +
                "Button & Drag'n'Drop in die Spalte \"Im Veranstaltungsort\" verschoben werden " +
                "können.",
            exitedAttendeesGrid,
            "exited-attendees-grid",
            moveToEnteredFromExitedBtn
        );
    }

    /**
     * Configures/enables drag'n'drop between the {@link Attendee} list, the list of
     * {@link LogEntry.EntranceStatus#ENTERED} attendees and the list of
     * {@link LogEntry.EntranceStatus#EXITED} attendees
     */
    private void configureDragAndDrop() {
        var ref = new Object() {
            List<Attendee> draggedItems;
            Grid<Attendee> dragSource;
        };

        ComponentEventListener<GridDragStartEvent<Attendee>> dragStartListener = event -> {
            ref.draggedItems = event.getDraggedItems();
            ref.dragSource = event.getSource();
            enteredAttendeesGrid.setDropMode(GridDropMode.BETWEEN);
            exitedAttendeesGrid.setDropMode(GridDropMode.BETWEEN);
        };

        ComponentEventListener<GridDragEndEvent<Attendee>> dragEndListener = event -> {
            ref.draggedItems = null;
            ref.dragSource = null;
            enteredAttendeesGrid.setDropMode(null);
            exitedAttendeesGrid.setDropMode(null);
        };

        ComponentEventListener<GridDropEvent<Attendee>> dropListener = event -> {
            Optional<Attendee> target = event.getDropTargetItem();
            if (target.isPresent() && ref.draggedItems.contains(target.get())) {
                return;
            }

            // Remove the items from the source grid
            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> sourceDataProvider =
                (ListDataProvider<Attendee>) ref.dragSource.getDataProvider();
            List<Attendee> sourceItems = new ArrayList<>(sourceDataProvider.getItems());
            sourceItems.removeAll(ref.draggedItems);
            ref.dragSource.setItems(sourceItems);

            // Add dragged items to the target Grid
            Grid<Attendee> targetGrid = event.getSource();
            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> targetDataProvider =
                (ListDataProvider<Attendee>) targetGrid.getDataProvider();
            List<Attendee> targetItems = new ArrayList<>(targetDataProvider.getItems());

            targetGrid.getId().ifPresent(id -> {
                if (id.equals("entered-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.ENTERED,
                        eventId,
                        ref.draggedItems
                    );
                } else if (id.equals("exited-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.EXITED,
                        eventId,
                        ref.draggedItems
                    );
                }
            });

            int index = target.map(attendee -> targetItems.indexOf(attendee)
                + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0)).orElse(0);
            targetItems.addAll(index, ref.draggedItems);
            targetGrid.setItems(targetItems);
        };

        // Grid of attendees
        attendeeGrid.addDragStartListener(dragStartListener);
        attendeeGrid.addDragEndListener(dragEndListener);
        attendeeGrid.setRowsDraggable(true);

        // Grid of entered attendees
        enteredAttendeesGrid.addDropListener(dropListener);
        enteredAttendeesGrid.addDragStartListener(dragStartListener);
        enteredAttendeesGrid.addDragEndListener(dragEndListener);
        enteredAttendeesGrid.setRowsDraggable(true);

        // Grid of exited attendees
        exitedAttendeesGrid.addDropListener(dropListener);
        exitedAttendeesGrid.addDragStartListener(dragStartListener);
        exitedAttendeesGrid.addDragEndListener(dragEndListener);
        exitedAttendeesGrid.setRowsDraggable(true);
    }

    private void prepareButtonFunctionality() {
        attendeeGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromAttendeeGrid.addAll(event.getAllSelectedItems())
        );

        enteredAttendeesGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromEnteredGrid.addAll(event.getAllSelectedItems())
        );

        exitedAttendeesGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromExitedGrid.addAll(event.getAllSelectedItems())
        );

        configureBtnListener(moveToEnteredBtn, attendeeGrid, enteredAttendeesGrid);
        configureBtnListener(moveToExitedBtn, enteredAttendeesGrid, exitedAttendeesGrid);
        configureBtnListener(moveToEnteredFromExitedBtn, exitedAttendeesGrid, enteredAttendeesGrid);
    }

    private void configureBtnListener(Button button, Grid<Attendee> source, Grid<Attendee> target) {
        List<Attendee> selectedItems = new LinkedList<>();
        if (source.getId().orElse("").equals("default-attendees-grid")) {
            selectedItems = selectedFromAttendeeGrid;
        } else if (source.getId().orElse("").equals("entered-attendees-grid")) {
            selectedItems = selectedFromEnteredGrid;
        } else if (source.getId().orElse("").equals("exited-attendees-grid")) {
            selectedItems = selectedFromExitedGrid;
        }

        List<Attendee> finalSelectedItems = selectedItems;
        button.addClickListener(event -> {
            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> sourceDataProvider =
                (ListDataProvider<Attendee>) source.getDataProvider();
            List<Attendee> shrunkAttendeeList = new LinkedList<>(sourceDataProvider.getItems());
            shrunkAttendeeList.removeAll(finalSelectedItems);
            source.setItems(shrunkAttendeeList);

            target.getId().ifPresent(id -> {
                if (id.equals("entered-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.ENTERED,
                        eventId,
                        finalSelectedItems
                    );
                } else if (id.equals("exited-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.EXITED,
                        eventId,
                        finalSelectedItems
                    );
                }
            });

            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> targetDataProvider =
                (ListDataProvider<Attendee>) target.getDataProvider();
            List<Attendee> enlargedAttendeeList = new LinkedList<>(targetDataProvider.getItems());
            enlargedAttendeeList.addAll(finalSelectedItems);
            target.setItems(enlargedAttendeeList);

            // Clean up
            selectedFromAttendeeGrid.clear();
            selectedFromEnteredGrid.clear();
            selectedFromExitedGrid.clear();

            attendeeGrid.asMultiSelect().deselectAll();
            enteredAttendeesGrid.asMultiSelect().deselectAll();
            exitedAttendeesGrid.asMultiSelect().deselectAll();
        });
    }

    private void updateGridData() {
        List<Attendee> entered = new LinkedList<>();
        List<Attendee> exited = new LinkedList<>();

        logEntryService.findAllUniqueForEvent(eventId).forEach(entry -> {
            if (entry.getStatus().equals(LogEntry.EntranceStatus.ENTERED)) {
                entered.add(entry.getAttendee());
            } else {
                exited.add(entry.getAttendee());
            }
        });

        List<Attendee> withoutStatus = attendeeService.findAll();
        withoutStatus.removeAll(entered);
        withoutStatus.removeAll(exited);

        attendeeGrid.setItems(withoutStatus);
        enteredAttendeesGrid.setItems(entered);
        exitedAttendeesGrid.setItems(exited);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("eventId").ifPresent(
            value -> eventId = UUID.fromString(value)
        );
        updateGridData();
    }
}
