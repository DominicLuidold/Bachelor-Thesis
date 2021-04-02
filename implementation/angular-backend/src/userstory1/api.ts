import { v4 as uuidv4 } from 'uuid';
import { Express } from 'express';
import { Attendee } from './model/attendee.interface';
import { Event } from './model/event.interface';
import { LogEntry } from './model/logentry.interface';
import { createEventTestData, populateAttendeeTestData } from './test-data';

// In-memory "database"
const attendees: Attendee[] = [];
const events: Event[] = [];
const logEntries: LogEntry[] = [];

// Generate test data
events.push(...createEventTestData());
attendees.push(...populateAttendeeTestData(events));

/**
 * Configures the server to listen to all specific routes for User Story 1.
 * Also containts the logic subsequently needed to serve those routes.
 */
export function configureServerForUserStory1(app: Express): void {
  // Get all events
  app.get('/events', (request, response) => {
    response.json(events);
  });

  // Get all attendees for a specific event
  app.get('/events/:eventId/attendees', (request, response) => {
    const eventId = request.params.eventId;
    const attendeesAtEvent: Attendee[] = [];

    attendees.forEach((attendee) => {
      if (attendee.events.some(({ id }) => id === eventId)) {
        attendeesAtEvent.push(attendee);
      }
    });

    response.json(attendeesAtEvent);
  });

  // Mark an attendee as entered/exited
  app.put('/attendees/:attendeeId/event/:eventId', (request, response) => {
    const attendeeId = request.params.attendeeId;
    const eventId = request.params.eventId;
    const updatedAttendee = request.body;

    const newLogEntry = {
      id: uuidv4(),
      attendee: findAttendeeById(attendeeId),
      event: findEventById(eventId),
      status: updatedAttendee.status,
      timestamp: new Date(),
    };
    logEntries.push(newLogEntry);

    response.json(newLogEntry);
  });
}

/**
 * Finds an event based on the provided id.
 *
 * @param {string} eventId - Id of the event to look for
 * @return {Event} An event
 */
function findEventById(eventId: string): Event {
  return events.find(event => event.id === eventId);
}

/**
 * Finds an attendee based on the provided id.
 *
 * @param {string} attendeeId - Id of the event to look for
 * @return {Attendee} An event
 */
function findAttendeeById(attendeeId: string): Attendee {
  return attendees.find(event => event.id === attendeeId);
}
