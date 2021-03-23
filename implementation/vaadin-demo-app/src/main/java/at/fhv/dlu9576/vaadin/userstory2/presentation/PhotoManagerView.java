package at.fhv.dlu9576.vaadin.userstory2.presentation;

import at.fhv.dlu9576.vaadin.general.presentation.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "photo-manager", layout = MainView.class)
@PageTitle("Fotoverwaltung")
@CssImport("./views/photo-manager/photo-manager-view.css")
public class PhotoManagerView extends Div {
    private static final long serialVersionUID = 5669148573856349726L;

    public PhotoManagerView() {
        // TODO
    }
}
