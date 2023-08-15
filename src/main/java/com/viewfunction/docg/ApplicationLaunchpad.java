package com.viewfunction.docg;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@Theme(value="docg-pac")
@PWA(name = "DOCG Platform System Administrator Console", shortName = "DOCG-SAC", offlineResources = {})
@Push
public class ApplicationLaunchpad extends SpringBootServletInitializer implements AppShellConfigurator, VaadinServiceInitListener {

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(ApplicationLaunchpad.class, args));
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            LoadingIndicatorConfiguration conf = uiInitEvent.getUI().getLoadingIndicatorConfiguration();
            // disable default theme -> loading indicator isn't shown
            conf.setApplyDefaultTheme(false);
            /*
             * Delay for showing the indicator and setting the 'first' class name.
             */
            conf.setFirstDelay(300); // 300ms is the default
            /* Delay for setting the 'second' class name */
            conf.setSecondDelay(1500); // 1500ms is the default
            /* Delay for setting the 'third' class name */
            conf.setThirdDelay(5000); // 5000ms is the default
        });
    }
}
