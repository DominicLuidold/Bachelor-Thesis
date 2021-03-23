package at.fhv.dlu9576.vaadin.userstory1.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.EntranceControl;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.AttendeeService;
import com.vaadin.flow.component.ComponentEventListener;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "entrance-control", layout = MainView.class)
@PageTitle("Echtzeit Eingangskontrolle")
@CssImport("./views/entrance-control/entrance-control-view.css")
public class EntranceControlView extends HorizontalLayout {
    private static final long serialVersionUID = 6414586759012039620L;

    private final AttendeeService attendeeService;
    private final Grid<Attendee> attendeeGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> enteredAttendeesGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> exitedAttendeesGrid = new Grid<>(Attendee.class);

    public EntranceControlView(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;

        setSizeFull();
        configureDragAndDrop();
        add(
            prepareDefaultAttendeesColumn(),
            prepareEnteredAttendeesColumn(),
            prepareExitedAttendeesColumn()
        );
    }

    private VerticalLayout prepareDefaultAttendeesColumn() {
        return new AttendeesColumn(
            "Allgemeine Teilnehmer:innenliste",
            "Diese List repräsentiert alle Teilnehmer:innen des Events. " +
                "Diese können ausgewählt, und mittels Drag'n'Drop in die jeweils passende Spalte " +
                "(\"Im Veranstaltungsort\" und \"Außerhalb des Veranstaltungsorts\") verschoben " +
                "werden.",
            attendeeGrid
        );
    }

    private VerticalLayout prepareEnteredAttendeesColumn() {
        return new AttendeesColumn(
            "Im Veranstaltungsort",
            "Diese List repräsentiert alle Teilnehmer:innen des Events, " +
                "die zum aktuellen Zeitpunkt innerhalb des Veranstaltungsorts sind und mittels " +
                "Drag'n'Drop in die Spalte \"Außerhalb des Veranstaltungsorts\" verschoben werden" +
                " können.",
            enteredAttendeesGrid
        );
    }

    private VerticalLayout prepareExitedAttendeesColumn() {
        return new AttendeesColumn(
            "Außerhalb des Veranstaltungsorts",
            "Diese List repräsentiert alle Teilnehmer:innen des Events, " +
                "die zum aktuellen Zeitpunkt außerhalb des Veranstaltungsorts sind und mittels " +
                "Drag'n'Drop in die Spalte \"Im Veranstaltungsort\" verschoben werden können.",
            exitedAttendeesGrid
        );
    }

    /**
     * Configures/enables drag'n'drop between the {@link Attendee} list, the list of
     * {@link EntranceControl.EntranceStatus#ENTERED} attendees and the list of
     * {@link EntranceControl.EntranceStatus#EXITED} attendees
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
}
