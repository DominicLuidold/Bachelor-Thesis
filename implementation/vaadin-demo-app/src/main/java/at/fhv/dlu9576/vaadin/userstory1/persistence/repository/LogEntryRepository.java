package at.fhv.dlu9576.vaadin.userstory1.persistence.repository;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.LogEntry;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, UUID> {

    List<LogEntry> findAllByEventId(UUID eventId);
}
