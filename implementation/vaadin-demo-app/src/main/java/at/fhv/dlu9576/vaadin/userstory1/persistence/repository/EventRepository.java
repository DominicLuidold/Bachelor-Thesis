package at.fhv.dlu9576.vaadin.userstory1.persistence.repository;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, UUID> {
    // Intentionally empty - handled by Spring Boot
}
