import { Express } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { Photo } from './model/photo.interface';
import { populatePhotoTestData } from './test-data';

// In-memory "database"
const photos: Photo[] = [];

// Generate test data
photos.push(...populatePhotoTestData());

/**
 * Configures the server to listen to all specific routes for User Story 2.
 * Also contains the logic subsequently needed to serve those routes.
 *
 * @param app An Express server
 */
export function configureServerForUserStory2(app: Express): void {
  // Get all images
  app.get('/photos', (request, response) => {
    response.json(photos);
  });

  // Create/upload new image
  app.post('/photos', (request, response) => {
    const file = request['files'].userUpload;
    console.log(`Image [${ file.name }] has been uploaded..`);

    const newPhoto: Photo = {
      id: uuidv4(),
      imageData: file.data.toString('base64'),
      fileName: file.name,
      uploadedAt: new Date()
    };
    photos.push(newPhoto);

    response.status(200).end();
  });
}
