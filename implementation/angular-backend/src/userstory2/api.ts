import { Express } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { InternalPhoto, Photo } from './model/photo.interface';
import { populatePhotoTestData } from './test-data';

// In-memory "database"
const photos: InternalPhoto[] = [];

// Generate test data
photos.push(...populatePhotoTestData());

/**
 * Configures the server to listen to all specific routes for User Story 2.
 * Also contains the logic subsequently needed to serve those routes.
 *
 * @param app An Express server
 */
export function configureServerForUserStory2(app: Express): void {
  // Get all images (only reference)
  app.get('/photos', (request, response) => {
    const photoDataForClient: Photo[] = [];

    photos.forEach(photo => {
      photoDataForClient.push({
        id: photo.id,
        fileName: photo.fileName,
        uploadedAt: photo.uploadedAt
      })
    });

    response.json(photoDataForClient);
  });

  // Get single image (actual file) by id
  app.get('/photos/:id', (request, response) => {
    const photoId = request.params.id;

    const internalPhoto = photos.find(photo => photo.id === photoId);
    if (undefined === internalPhoto) {
      response.status(404).end();
      return;
    }
    const image = Buffer.from(internalPhoto.imageData, 'base64');

    response.writeHead(200, {
      'Content-Type': `image/${ internalPhoto.fileName.split('.')[1].toLowerCase() }`,
      'Content-Length': image.length
    });
    response.end(image);
  });

  // Create/upload new image
  app.post('/photos', (request, response) => {
    const file = request['files'].userUpload;
    console.log(`Image [${ file.name }] has been uploaded..`);

    const newPhoto: InternalPhoto = {
      id: uuidv4(),
      imageData: file.data.toString('base64'),
      fileName: file.name,
      uploadedAt: new Date()
    };
    photos.push(newPhoto);

    response.status(200).end();
  });
}
