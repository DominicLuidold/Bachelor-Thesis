package at.fhv.dlu9576.vaadin.userstory1.persistence.service;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Event;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.AttendeeRepository;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.EventRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AttendeeService {
    private static final Logger LOG = LoggerFactory.getLogger(AttendeeService.class);
    private final AttendeeRepository attendeeRepository;
    private final EventRepository eventRepository;

    public AttendeeService(AttendeeRepository attendeeRepository, EventRepository eventRepository) {
        this.attendeeRepository = attendeeRepository;
        this.eventRepository = eventRepository;
    }

    public List<Attendee> findAllByEvent(UUID eventId) {
        return attendeeRepository.findAllByEvents_Id(eventId);
    }

    @PostConstruct
    public void populateTestData() {
        if (eventRepository.count() == 0) {
            LOG.debug("Populating [Event] test data..");

            eventRepository.saveAll(
                Stream.of("Event #1", "Event #2").map(name -> {
                    String[] split = name.split(" ");

                    Event event = new Event();
                    event.setName(name);
                    event.setLocation(split[0] + "halle " + split[1]);
                    event.setDescription("Test " + name);
                    event.setDate(LocalDate.now());
                    event.setStartTime(LocalDateTime.now());
                    event.setEndTime(LocalDateTime.now().plusHours(4));

                    return event;
                }).collect(Collectors.toList())
            );
        }

        if (attendeeRepository.count() == 0) {
            LOG.debug("Populating [Attendee] test data..");

            List<Event> events = eventRepository.findAll();
            attendeeRepository.saveAll(
                Stream.of(
                    "Reyes Robards", "Robin Roan", "Malinda Millington", "Earnest Everton",
                    "Maryam Murph", "Venita Vigue", "Calvin Collado", "Gilbert Gagnon",
                    "Gertrude Girouard", "Nu Nghiem", "Delilah Dorsey", "Georgetta Goss",
                    "Grover Gobin", "Rolando Ruffin", "Winifred Whitehurst", "Lashawn Lennox",
                    "Ray Roesner", "Isabel Ingraham", "Len Learned", "Santa Salva", "Kiara Koonce",
                    "Maud Mehta", "Jeanett Jara", "Bettina Blunk", "Dorthea Dobbin", "Melva Mikus",
                    "Darline Delisle", "Connie Couturier", "Lorinda Lumsden", "Pandora Perrodin"
                ).map(name -> {
                    String[] split = name.split(" ");

                    Attendee attendee = new Attendee();
                    attendee.setFirstName(split[0]);
                    attendee.setLastName(split[1]);
                    attendee.setEmail(attendee.getFirstName()
                        + "."
                        + attendee.getLastName()
                        + "@example.com"
                    );
                    attendee.addEvent(events.get(new Random().nextInt(events.size())));

                    return attendee;
                }).collect(Collectors.toList())
            );
        }
    }
}
