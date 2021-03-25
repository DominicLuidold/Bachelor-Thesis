package at.fhv.dlu9576.vaadin.userstory1.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.EventService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "entrance-control", layout = MainView.class)
@PageTitle("Event-Übersicht | Echtzeit Eingangskontrolle")
public class EventSelectionView extends VerticalLayout {
    private static final long serialVersionUID = -5647993022096824575L;
    private static final Logger LOG = LoggerFactory.getLogger(EntranceControlView.class);

    private final EventService eventService;
    private final Grid<Event> eventGrid = new Grid<>(Event.class);

    public EventSelectionView(EventService eventService) {
        this.eventService = eventService;

        // Styling
        setSizeFull();

        // Components & component configuration
        configureGrid();
        prepareAdditionalInfo();
        add(eventGrid);
    }

    /**
     * Sets heading and explanation text.
     */
    private void prepareAdditionalInfo() {
        H3 heading = new H3(getTranslation("event-selection.heading"));
        Text infoText = new Text(getTranslation("event-selection.explanation"));
        add(heading, infoText);
    }

    /**
     * Configures {@link Grid<Event>} to allow clicking on an {@link Event} row which subsequently
     * redirects the user to the {@link EntranceControlView}.
     */
    private void configureGrid() {
        eventGrid.setItems(eventService.findAll());
        eventGrid.setColumns("id", "name", "location", "startTime", "endTime", "description");
        eventGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Configure route redirect
        eventGrid.asSingleSelect().addValueChangeListener(event -> {
            LOG.debug("Registered click on Event [{}]", event.getValue().getName());

            getUI().ifPresentOrElse(
                ui -> ui.navigate(
                    EntranceControlView.class,
                    new RouteParameters("eventId", event.getValue().getId().toString())
                ), () -> new Notification(getTranslation("general.error"), 5000).open()
            );
        });
    }
}
