package at.fhv.dlu9576.vaadin.userstory1.persistence.service;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.EventRepository;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private static final Logger LOG = Logger.getLogger(EventService.class.getName());
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
