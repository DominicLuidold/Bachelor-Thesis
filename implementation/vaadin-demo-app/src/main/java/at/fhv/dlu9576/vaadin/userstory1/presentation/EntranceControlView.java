package at.fhv.dlu9576.vaadin.userstory1.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.general.presentation.NotFoundView;
import at.fhv.dlu9576.vaadin.userstory1.Broadcaster;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.LogEntry;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.AttendeeService;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.EventService;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.LogEntryService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "entrance-control/:eventId", layout = MainView.class)
@PageTitle("Echtzeit Eingangskontrolle")
@CssImport("./styles/views/entrance-control/entrance-control-view.css")
public class EntranceControlView extends HorizontalLayout implements BeforeEnterObserver {
    private static final long serialVersionUID = 6414586759012039620L;
    private static final Logger LOG = LoggerFactory.getLogger(EntranceControlView.class);

    private final AttendeeService attendeeService;
    private final EventService eventService;
    private final LogEntryService logEntryService;

    private final Grid<Attendee> defaultGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> enteredGrid = new Grid<>(Attendee.class);
    private final Grid<Attendee> exitedGrid = new Grid<>(Attendee.class);

    private final Set<Attendee> selectedFromDefault = new HashSet<>();
    private final Set<Attendee> selectedFromEntered = new HashSet<>();
    private final Set<Attendee> selectedFromExited = new HashSet<>();

    private final Button moveToEnteredBtn = new Button(
        getTranslation("entrance-control.default-grid.button")
    );
    private final Button moveToExitedBtn = new Button(
        getTranslation("entrance-control.entered-grid.button")
    );
    private final Button moveToEnteredFromExitedBtn = new Button(
        getTranslation("entrance-control.exited-grid.button")
    );

    private UUID eventId;
    private UUID broadcastId;
    private Registration broadcasterRegistration;

    public EntranceControlView(
        AttendeeService attendeeService,
        EventService eventService,
        LogEntryService logEntryService
    ) {
        this.attendeeService = attendeeService;
        this.eventService = eventService;
        this.logEntryService = logEntryService;

        // Styling
        setSizeFull();
        setClassName("responsiveGrids");

        // Components & component configuration
        configureDragAndDrop();
        add(prepareDefaultColumn(), prepareEnteredColumn(), prepareExitedColumn());
        prepareButtonFunctionality();
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

    private VerticalLayout prepareEnteredColumn() {
        return new AttendeesColumn(
            getTranslation("entrance-control.entered-grid.heading"),
            getTranslation("entrance-control.entered-grid.explanation"),
            enteredGrid,
            "entered-attendees-grid",
            moveToExitedBtn
        );
    }

    private VerticalLayout prepareExitedColumn() {
        return new AttendeesColumn(
            getTranslation("entrance-control.exited-grid.heading"),
            getTranslation("entrance-control.exited-grid.explanation"),
            exitedGrid,
            "exited-attendees-grid",
            moveToEnteredFromExitedBtn
        );
    }

    /**
     * Configures/enables drag'n'drop between the three {@link Grid<Attendee>s.
     */
    private void configureDragAndDrop() {
        var ref = new Object() {
            List<Attendee> draggedItems;
            Grid<Attendee> dragSource;
        };

        ComponentEventListener<GridDragStartEvent<Attendee>> dragStartListener = event -> {
            ref.draggedItems = event.getDraggedItems();
            ref.dragSource = event.getSource();
            enteredGrid.setDropMode(GridDropMode.BETWEEN);
            exitedGrid.setDropMode(GridDropMode.BETWEEN);
        };

        ComponentEventListener<GridDragEndEvent<Attendee>> dragEndListener = event -> {
            ref.draggedItems = null;
            ref.dragSource = null;
            enteredGrid.setDropMode(null);
            exitedGrid.setDropMode(null);
        };

        ComponentEventListener<GridDropEvent<Attendee>> dropListener = event -> {
            LOG.debug("Registered drag'n'drop of [{}] attendee items", ref.draggedItems.size());

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

            // Notify LogEntryService about recent changes
            targetGrid.getId().ifPresent(id -> {
                if (id.equals("entered-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.ENTERED,
                        eventId,
                        new HashSet<>(ref.draggedItems)
                    );
                } else if (id.equals("exited-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.EXITED,
                        eventId,
                        new HashSet<>(ref.draggedItems)
                    );
                }
            });

            // Calculate exact drop location
            int index = target.map(attendee -> targetItems.indexOf(attendee)
                + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0)).orElse(0);
            targetItems.addAll(index, ref.draggedItems);
            targetGrid.setItems(targetItems);

            // Update other clients to reflect changes
            Broadcaster.broadcast(broadcastId);
        };

        // Grid of attendees
        defaultGrid.addDragStartListener(dragStartListener);
        defaultGrid.addDragEndListener(dragEndListener);
        defaultGrid.setRowsDraggable(true);

        // Grid of entered attendees
        enteredGrid.addDropListener(dropListener);
        enteredGrid.addDragStartListener(dragStartListener);
        enteredGrid.addDragEndListener(dragEndListener);
        enteredGrid.setRowsDraggable(true);

        // Grid of exited attendees
        exitedGrid.addDropListener(dropListener);
        exitedGrid.addDragStartListener(dragStartListener);
        exitedGrid.addDragEndListener(dragEndListener);
        exitedGrid.setRowsDraggable(true);
    }

    /**
     * Configures all {@link Grid<Attendee>}s to add selected items to a List and subsequently
     * adds a {@link Button#addClickListener(ComponentEventListener)} the three {@link Button}s.
     */
    private void prepareButtonFunctionality() {
        defaultGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromDefault.addAll(event.getAllSelectedItems())
        );

        enteredGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromEntered.addAll(event.getAllSelectedItems())
        );

        exitedGrid.asMultiSelect().addSelectionListener(
            event -> selectedFromExited.addAll(event.getAllSelectedItems())
        );

        configureBtnListener(moveToEnteredBtn, defaultGrid, enteredGrid);
        configureBtnListener(moveToExitedBtn, enteredGrid, exitedGrid);
        configureBtnListener(moveToEnteredFromExitedBtn, exitedGrid, enteredGrid);
    }

    /**
     * Configures the {@link Button} to move selected {@link Attendee}s from source
     * {@link Grid<Attendee>} to target {@link Grid<Attendee>}. Causes a database update.
     *
     * @param button The button to configure
     * @param source Source Grid from which the attendees get removed
     * @param target Target Grid to which the attendees get added
     */
    private void configureBtnListener(Button button, Grid<Attendee> source, Grid<Attendee> target) {
        Set<Attendee> items = new HashSet<>();
        if (source.getId().orElse("").equals("default-attendees-grid")) {
            items = selectedFromDefault;
        } else if (source.getId().orElse("").equals("entered-attendees-grid")) {
            items = selectedFromEntered;
        } else if (source.getId().orElse("").equals("exited-attendees-grid")) {
            items = selectedFromExited;
        }

        final Set<Attendee> selectedItems = items;
        button.addClickListener(event -> {
            LOG.debug(
                "[{}] button has been pressed, moving [{}] attendees",
                button.getText(),
                selectedItems.size()
            );

            // Remove items from source grid
            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> sourceDataProvider =
                (ListDataProvider<Attendee>) source.getDataProvider();
            Set<Attendee> shrunkAttendeeList = new HashSet<>(sourceDataProvider.getItems());
            shrunkAttendeeList.removeAll(selectedItems);
            source.setItems(shrunkAttendeeList);

            // Update items from target grid
            @SuppressWarnings("unchecked")
            ListDataProvider<Attendee> targetDataProvider =
                (ListDataProvider<Attendee>) target.getDataProvider();
            Set<Attendee> enlargedAttendeeList = new HashSet<>(targetDataProvider.getItems());
            enlargedAttendeeList.addAll(selectedItems);
            target.setItems(enlargedAttendeeList);

            // Notify LogEntryService about recent changes
            target.getId().ifPresent(id -> {
                if (id.equals("entered-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.ENTERED,
                        eventId,
                        selectedItems
                    );
                } else if (id.equals("exited-attendees-grid")) {
                    logEntryService.markAttendeesAs(
                        LogEntry.EntranceStatus.EXITED,
                        eventId,
                        selectedItems
                    );
                }
            });

            // Clean up
            selectedFromDefault.clear();
            selectedFromEntered.clear();
            selectedFromExited.clear();

            defaultGrid.asMultiSelect().deselectAll();
            enteredGrid.asMultiSelect().deselectAll();
            exitedGrid.asMultiSelect().deselectAll();

            // Update other clients to reflect changes
            Broadcaster.broadcast(broadcastId);
        });
    }

    /**
     * Updates the {@link Grid<Attendee>}s by fetching the current database state
     */
    private void updateGridData() {
        LOG.debug("Updating data of Grids [default, entered, exited]");

        Set<Attendee> entered = new HashSet<>();
        Set<Attendee> exited = new HashSet<>();
        logEntryService.findAllUniqueForEvent(eventId).forEach(entry -> {
            if (entry.getStatus().equals(LogEntry.EntranceStatus.ENTERED)) {
                entered.add(entry.getAttendee());
            } else {
                exited.add(entry.getAttendee());
            }
        });

        // TODO - Replace findAll() with find AttendeeService#findAllByEvent()
        List<Attendee> withoutStatus = attendeeService.findAll();
        withoutStatus.removeAll(entered);
        withoutStatus.removeAll(exited);

        defaultGrid.setItems(withoutStatus);
        enteredGrid.setItems(entered);
        exitedGrid.setItems(exited);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionally checks if the supplied {@code eventId} is valid and subsequently updates
     * the {@link Grid<Attendee>}s since {@code eventId} is now available and valid.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("eventId").ifPresent(
            value -> eventId = UUID.fromString(value)
        );

        if (!eventService.exists(eventId)) {
            LOG.warn("Could not find event [{}], redirecting to 404 page", eventId);
            event.rerouteTo(NotFoundView.class);
        }

        updateGridData();
    }

    /**
     * Registers with the {@link Broadcaster} to handle incoming broadcasts. These broadcasts
     * are getting pushed when one of the {@link Grid<Attendee>}s is getting updated.
     *
     * @param attachEvent {@link AttachEvent}
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        // Generates a new UUID for each client to prevent receiving notification
        broadcastId = UUID.randomUUID();

        broadcasterRegistration = Broadcaster.register(broadcastId, Void -> ui.access(() -> {
            updateGridData();
            new Notification(
                getTranslation("entrance-control.remote-update.notification"),
                5000
            ).open();
        }));
    }

    /**
     * Removes registration from {@link Broadcaster}.
     *
     * @param detachEvent {@link DetachEvent}
     */
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }
}
