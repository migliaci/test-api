package com.migliaci.myretail.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Responsible for initializing the application and assigning
 * environment properties through profiles.
 *
 * @Author migliaci
 */
public class WebAppInit implements WebApplicationInitializer {
    private static final String SPRING_ROOT = "/api";

    @Override
    public void onStartup(ServletContext servletContext) {
        // Create the 'root' Spring application context.
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);
        rootContext.registerShutdownHook();
        rootContext.setServletContext(servletContext);

        //Allows Conditions to turn spring code on and off depending on environment
        rootContext.getEnvironment().addActiveProfile(System.getProperty("environment", "default"));
        rootContext.refresh();

        // Register and map the dispatcher servlet.
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(SPRING_ROOT + "/*");

    }

}
