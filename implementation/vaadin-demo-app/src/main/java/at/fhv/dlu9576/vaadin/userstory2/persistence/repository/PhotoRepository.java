package at.fhv.dlu9576.vaadin.userstory2.persistence.repository;

import at.fhv.dlu9576.vaadin.userstory2.persistence.entity.Photo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    // Intentionally empty - handled by Spring Boot
}
