package at.fhv.dlu9576.vaadin.userstory1.persistence.service;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.EventRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private static final Logger LOG = Logger.getLogger(EventService.class.getName());
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    public void populateTestData() {
        if (eventRepository.count() == 0) {
            LOG.info("Populating [Event] test data..");

            Event event = new Event();
            event.setName("Event #1");
            event.setLocation("Eventhalle 1");
            event.setDescription("Test Event #1");
            event.setDate(LocalDate.now());
            event.setStartTime(LocalDateTime.now());
            event.setEndTime(LocalDateTime.now().plusHours(4));

            eventRepository.save(event);
        }
    }
}
