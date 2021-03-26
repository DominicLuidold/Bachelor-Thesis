package at.fhv.dlu9576.vaadin.general.presentation;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "not-found", layout = MainView.class)
@CssImport("./styles/views/not-found/not-found-view.css")
public class NotFoundView extends VerticalLayout {
    private static final long serialVersionUID = 6638890539408794658L;

    public NotFoundView() {
        // Components & component settings
        H1 heading404 = new H1(getTranslation("general.not-found.heading"));
        H3 explanation = new H3(getTranslation("general.not-found.explanation"));
        add(heading404, explanation);

        // Styling
        setClassName("not-found-view");
        heading404.setClassName("heading-without-margin");
    }
}
