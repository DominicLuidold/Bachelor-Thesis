import { v4 as uuidv4 } from 'uuid';
import { Attendee } from './model/attendee.interface';
import { Event } from './model/event.interface';

/**
 * Creates two test events.
 *
 * @return {Event[]} Array of events
 */
export function populateEventTestData(): Event[] {
  const today = new Date();
  const todayPlus4Hours = new Date();
  todayPlus4Hours.setHours(today.getHours() + 4);

  return [
    {
      id: uuidv4(),
      name: 'Event #1',
      location: 'Eventhalle #1',
      description: 'Test Event #1',
      date: today,
      startTime: today,
      endTime: todayPlus4Hours,
    },
    {
      id: uuidv4(),
      name: 'Event #2',
      location: 'Eventhalle #2',
      description: 'Test Event #2',
      date: today,
      startTime: today,
      endTime: todayPlus4Hours,
    }
  ];
}

/**
 * Creates a list of attendees attending one of the test events randomly.
 *
 * @param {Event[]} testEvents - Previously created events that the attendees can attend
 * @return {Attendee[]} Array of attendees
 */
export function populateAttendeeTestData(testEvents: Event[]): Attendee[] {
  const randomNames: string[] = [
    'Reyes Robards', 'Robin Roan', 'Malinda Millington', 'Earnest Everton',
    'Maryam Murph', 'Venita Vigue', 'Calvin Collado', 'Gilbert Gagnon',
    'Gertrude Girouard', 'Nu Nghiem', 'Delilah Dorsey', 'Georgetta Goss',
    'Grover Gobin', 'Rolando Ruffin', 'Winifred Whitehurst', 'Lashawn Lennox',
    'Ray Roesner', 'Isabel Ingraham', 'Len Learned', 'Santa Salva', 'Kiara Koonce',
    'Maud Mehta', 'Jeanett Jara', 'Bettina Blunk', 'Dorthea Dobbin', 'Melva Mikus',
    'Darline Delisle', 'Connie Couturier', 'Lorinda Lumsden', 'Pandora Perrodin',
  ];

  return randomNames.map((name) => {
    // Create attendees based on random list of names
    const split: string[] = name.split(' ');

    const attendee: Attendee = {
      id: uuidv4(),
      firstName: split[0],
      lastName: split[1],
      email: split[0] + '.' + split[1] + '@example.com',
      events: [testEvents[Math.floor(Math.random() * testEvents.length)]]
    };

    return attendee;
  });
}
