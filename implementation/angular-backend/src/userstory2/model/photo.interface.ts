export interface Photo {
  id: string;
  fileName: string;
  uploadedAt: Date;
}

export interface InternalPhoto extends Photo {
  imageData: string;
}
