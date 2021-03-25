package at.fhv.dlu9576.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

@Push
@SpringBootApplication
@Theme(value = "vaadindemoapp")
@PWA(
    name = "Vaadin Demo App",
    shortName = "Vaadin Demo App",
    offlineResources = {"images/logo.png"}
)
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {
    private static final long serialVersionUID = -1377577475853648519L;

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }
}
