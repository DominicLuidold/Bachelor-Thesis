package at.fhv.dlu9576.vaadin.userstory1.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.service.EventService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;

@Route(value = "entrance-control", layout = MainView.class)
@PageTitle("Event-Übersicht | Echtzeit Eingangskontrolle")
public class EventSelectionView extends VerticalLayout {
    private static final long serialVersionUID = -5647993022096824575L;

    private final EventService eventService;
    private final Grid<Event> eventGrid = new Grid<>(Event.class);

    public EventSelectionView(EventService eventService) {
        this.eventService = eventService;

        setSizeFull();
        configureGrid();
        prepareAdditionalInfo();
        add(eventGrid);
    }

    private void prepareAdditionalInfo() {
        H3 heading = new H3(getTranslation("event-selection.heading"));
        Text infoText = new Text(getTranslation("event-selection.explanation"));
        add(heading, infoText);
    }

    private void configureGrid() {
        eventGrid.setItems(eventService.findAll());
        eventGrid.setColumns("id", "name", "location", "startTime", "endTime", "description");
        eventGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Configure route redirect
        eventGrid.asSingleSelect().addValueChangeListener(event -> getUI().get().navigate(
            RouteConfiguration.forSessionScope().getUrl(
                EntranceControlView.class,
                new RouteParameters("eventId", event.getValue().getId().toString())
            )
        ));
    }
}
