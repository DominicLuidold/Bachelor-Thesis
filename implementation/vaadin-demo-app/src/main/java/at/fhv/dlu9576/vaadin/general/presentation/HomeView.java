package at.fhv.dlu9576.vaadin.general.presentation;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "home", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Home")
@CssImport("./styles/views/home/home-view.css")
public class HomeView extends Div {
    private static final long serialVersionUID = 4902017337970489705L;

    public HomeView() {
        Accordion accordion = new Accordion();
        accordion.add(
            getTranslation("home.entrance-control.heading"),
            new Text(getTranslation("home.entrance-control.explanation"))
        ).addThemeVariants(DetailsVariant.FILLED);
        accordion.add(
            getTranslation("home.photo-manager.heading"),
            new Text(getTranslation("home.photo-manager.explanation"))
        ).addThemeVariants(DetailsVariant.FILLED);

        add(accordion);
    }
}
