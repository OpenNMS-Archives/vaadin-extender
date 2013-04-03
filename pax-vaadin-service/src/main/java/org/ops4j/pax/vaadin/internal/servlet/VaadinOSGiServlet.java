package org.ops4j.pax.vaadin.internal.servlet;

import org.ops4j.pax.vaadin.ApplicationFactory;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class VaadinOSGiServlet extends VaadinServlet {

    private ApplicationFactory m_appFactory;
    

    public VaadinOSGiServlet(ApplicationFactory factory) {
        m_appFactory = factory;
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) {
        
        final VaadinServletService service = super.createServletService(deploymentConfiguration);
        service.addSessionInitListener(new SessionInitListener() {

            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                event.getSession().addUIProvider(new OSGiUIProvider(m_appFactory));
                service.removeSessionInitListener(this);
            }
            
        });
        
        service.addSessionDestroyListener(new SessionDestroyListener() {

            @Override
            public void sessionDestroy(SessionDestroyEvent event) {
                event.getSession().close();
                
            }
            
        });
        
        
        
        //VaadinOSGiServletService servletService = new VaadinOSGiServletService(this, deploymentConfiguration, m_application);
        return service;
    }

}
