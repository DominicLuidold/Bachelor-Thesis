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
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogEntryService {
    private static final Logger LOG = LoggerFactory.getLogger(LogEntryService.class);

    private final EventRepository eventRepository;
    private final LogEntryRepository logEntryRepository;

    public LogEntryService(EventRepository eventRepository, LogEntryRepository logEntryRepository) {
        this.eventRepository = eventRepository;
        this.logEntryRepository = logEntryRepository;
    }

    public List<LogEntry> findAllUniqueForEvent(UUID eventId) {
        return removeDuplicates(logEntryRepository.findAllByEventId(eventId));
    }

    /**
     * Marks {@link Attendee}s as either {@link LogEntry.EntranceStatus#ENTERED} or
     * {@link LogEntry.EntranceStatus#EXITED} by creating a new {@link LogEntry}.
     *
     * @param status    The new status of the attendees
     * @param eventId   The event causing the status change
     * @param attendees List of attendees that recently changes their status
     */
    public void markAttendeesAs(
        LogEntry.EntranceStatus status,
        UUID eventId,
        List<Attendee> attendees
    ) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            LOG.error("Could not find event with id [{}]", eventId);
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

        LOG.info(
            "Marking [{}] attendees as [{}]",
            logEntries.size(),
            status.equals(LogEntry.EntranceStatus.ENTERED) ? "Entered" : "Exited"
        );
        logEntryRepository.saveAll(logEntries);
    }

    /**
     * Removes duplicate {@link LogEntry} entries and only keeps the most recent entry within the
     * list based on the {@link LogEntry#getAttendee()} and {@link LogEntry#getTimestamp()}.
     *
     * @param logEntries List of entries with duplicates
     *
     * @return List without any duplicates
     */
    private static List<LogEntry> removeDuplicates(List<LogEntry> logEntries) {
        return new LinkedList<>(logEntries.stream().collect(Collectors.toMap(
            LogEntry::getAttendee,
            Function.identity(),
            (entry1, entry2) -> entry2
        )).values());
    }
}
