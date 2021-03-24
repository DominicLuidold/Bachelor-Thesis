package at.fhv.dlu9576.vaadin.userstory1.persistence.service;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.LogEntry;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.EventRepository;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.LogEntryRepository;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LogEntryService {
    private static final Logger LOG = Logger.getLogger(LogEntryService.class.getName());

    private final EventRepository eventRepository;
    private final LogEntryRepository logEntryRepository;

    public LogEntryService(EventRepository eventRepository, LogEntryRepository logEntryRepository) {
        this.eventRepository = eventRepository;
        this.logEntryRepository = logEntryRepository;
    }

    public List<LogEntry> findAllUniqueForEvent(UUID eventId) {
        return removeDuplicates(logEntryRepository.findAllByEventId(eventId));
    }

    public void markAttendeesAs(
        LogEntry.EntranceStatus status,
        UUID eventId,
        List<Attendee> attendees
    ) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            LOG.log(Level.SEVERE, "Could not find event with id [{}]", eventId);
            return;
        }

        List<LogEntry> logEntries = new LinkedList<>();
        attendees.forEach(attendee -> {
            LogEntry entry = new LogEntry();

            entry.setAttendee(attendee);
            entry.setEvent(eventOptional.get());
            entry.setStatus(status);
            entry.setTimestamp(LocalDateTime.now());

            logEntries.add(entry);
        });

        LOG.log(
            Level.INFO, "Marking [" + logEntries.size() + "] attendees as "
                + (status.equals(LogEntry.EntranceStatus.ENTERED) ? "[Entered]" : "[Exited]")
        );
        logEntryRepository.saveAll(logEntries);
    }

    private static List<LogEntry> removeDuplicates(List<LogEntry> logEntries) {
        return new LinkedList<>(logEntries.stream().collect(Collectors.toMap(
            LogEntry::getAttendee,
            Function.identity(),
            (entry1, entry2) -> entry2
        )).values());
    }
}
