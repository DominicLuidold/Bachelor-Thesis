package at.fhv.dlu9576.vaadin.userstory2.presentation;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.time.LocalDateTime;

@CssImport("./styles/views/photo-manager/image-card.css")
public class ImageCard extends VerticalLayout {
    private static final long serialVersionUID = -8967169617393818826L;

    /**
     * Creates a custom image card that shows the title and the upload date of a given {@link Image}
     *
     * @param image      {@link Image} to display
     * @param fileName   Filename of the image
     * @param uploadedAt Upload date of image
     */
    public ImageCard(Image image, String fileName, LocalDateTime uploadedAt) {
        // Styling
        setClassName("image-card");

        // Components & component settings
        H4 fileNameHeading = new H4(fileName);
        fileNameHeading.setClassName("custom-h4");

        add(image, fileNameHeading, new Text(uploadedAt.toString()));
    }
}
