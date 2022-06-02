package com.viewfunction.docg;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Theme("docg-pac")
@PWA(name = "My Todo", shortName = "My Todo", offlineResources = {})
public class ApplicationLaunchpad extends SpringBootServletInitializer implements AppShellConfigurator{

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(ApplicationLaunchpad.class, args));
    }
}
