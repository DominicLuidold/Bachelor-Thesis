package at.fhv.dlu9576.vaadin.userstory2.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import at.fhv.dlu9576.vaadin.userstory2.persistence.entity.Photo;
import at.fhv.dlu9576.vaadin.userstory2.persistence.service.PhotoService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "photo-manager", layout = MainView.class)
@PageTitle("Fotoverwaltung")
@CssImport("./styles/views/photo-manager/photo-manager-view.css")
public class PhotoManagerView extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(PhotoManagerView.class);
    private static final long serialVersionUID = 5669148573856349726L;

    private final PhotoService photoService;

    private final Upload imageUpload = new Upload();
    private final HorizontalLayout imageGallery = new HorizontalLayout();

    public PhotoManagerView(PhotoService photoService) {
        this.photoService = photoService;

        // Styling
        imageGallery.setClassName("image-gallery");

        // Components & component settings
        configureImageUpload();
        add(new H3(getTranslation("photo-manager.heading")), imageUpload, new Hr(), imageGallery);
        updateData();
    }

    /**
     * Configures the image {@link Upload} to allow only file types with mime-type {@code image/*}
     * and a file size up to {@code 10485760 byte} ({@code 10 MB}).
     * <p>
     * Uploaded images will subsequently be added to the database and appear instantly on the
     * users interface.
     */
    private void configureImageUpload() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        imageUpload.setReceiver(buffer);
        imageUpload.setAcceptedFileTypes("image/*");
        imageUpload.setMaxFileSize(10485760); // 10MB
        imageUpload.setDropLabel(new Label(getTranslation("photo-manager.upload.label")));
        imageUpload.setWidth("50%");

        imageUpload.addSucceededListener(event -> {
            LocalDateTime uploadTime = LocalDateTime.now();
            Image image = createImage(
                event.getFileName(),
                buffer.getInputStream(event.getFileName()),
                uploadTime
            );
            imageGallery.add(new ImageCard(image, event.getFileName(), uploadTime));
        });
        imageUpload.addFileRejectedListener(
            event -> new Notification(event.getErrorMessage(), 5000).open()
        );
    }

    /**
     * Creates an {@link Image} based on provided file name and {@link InputStream}. Subsequently
     * saves the created {@link Image} to the database via
     * {@link PhotoService#persistImage(String, byte[], LocalDateTime)}.
     * <p>
     * Code based on https://vaadin.com/components/vaadin-upload/java-example
     *
     * @param fileName   The name of the uploaded file
     * @param stream     An input stream
     * @param uploadTime Time of upload
     *
     * @return An {@link Image}
     * @see <a href="https://vaadin.com/components/vaadin-upload/java-examples">
     * Java Examples | Upload | Components | Vaadin
     * </a>
     */
    private Image createImage(String fileName, InputStream stream, LocalDateTime uploadTime) {
        Image image = new Image();
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            image.getElement().setAttribute(
                "src",
                new StreamResource(fileName, () -> new ByteArrayInputStream(bytes))
            );

            // Save image to database
            photoService.persistImage(fileName, bytes, uploadTime);
        } catch (IOException e) {
            LOG.error("Error uploading image: ", e);
            new Notification(getTranslation("general.error"), 5000).open();
        }
        image.setMaxWidth("400px");
        return image;
    }

    /**
     * Fetches all {@link Photo}s from the database and creates {@link Image}s that get added to
     * the image gallery.
     */
    private void updateData() {
        photoService.findAll().forEach(photo -> {
            byte[] imageBytes = photo.getImageData();
            StreamResource resource = new StreamResource(
                photo.getFileName(),
                () -> new ByteArrayInputStream(imageBytes)
            );
            Image image = new Image(resource, photo.getFileName());
            image.setMaxWidth("400px");
            imageGallery.add(new ImageCard(image, photo.getFileName(), photo.getUploadedAt()));
        });
    }
}
