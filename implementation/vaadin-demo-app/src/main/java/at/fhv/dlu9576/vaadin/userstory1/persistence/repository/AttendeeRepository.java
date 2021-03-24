package at.fhv.dlu9576.vaadin.userstory1.persistence.repository;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, UUID> {

    List<Attendee> findAllByEvents_Id(UUID eventId);
}
