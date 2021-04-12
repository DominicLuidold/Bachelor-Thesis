export interface Photo {
  id: string;
  fileName: string;
  uploadedAt: Date;
}

export interface OfflinePhoto extends Photo {
  // The offline photo has an additional property to store the base64 encoded image and directly upload this to the backend.
  imageData: string;
}
