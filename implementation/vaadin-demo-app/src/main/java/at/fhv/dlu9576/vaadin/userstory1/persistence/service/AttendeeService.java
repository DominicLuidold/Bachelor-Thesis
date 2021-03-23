package at.fhv.dlu9576.vaadin.userstory1.persistence.service;

import at.fhv.dlu9576.vaadin.userstory1.persistence.entity.Attendee;
import at.fhv.dlu9576.vaadin.userstory1.persistence.repository.AttendeeRepository;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AttendeeService {
    private static final Logger LOG = Logger.getLogger(AttendeeService.class.getName());
    private final AttendeeRepository attendeeRepository;

    public AttendeeService(AttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    public List<Attendee> findAll() {
        return attendeeRepository.findAll();
    }

    public long count() {
        return attendeeRepository.count();
    }

    @PostConstruct
    public void populateTestData() {
        if (attendeeRepository.count() == 0) {
            LOG.info("Populating [Attendee] test data..");

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

                    return attendee;
                }).collect(Collectors.toList())
            );
        }
    }
}
