private Image createImage(/* [...] */) {
    Image image = new Image();
    try {
        byte[] bytes = IOUtils.toByteArray(stream);

        // Save image to database
        photoService.persistImage(fileName, bytes, uploadTime);

        [...]
    }
    return image;
}
