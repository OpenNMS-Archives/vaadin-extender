package org.ops4j.pax.vaadin.internal.servlet;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.UI;

public class VaadinOSGiServlet extends VaadinServlet {

    private UI m_application;
    
    public VaadinOSGiServlet(UI application) {
        m_application = application;
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) {
        
        final VaadinServletService service = super.createServletService(deploymentConfiguration);
        service.addSessionInitListener(new SessionInitListener() {

            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                event.getSession().addUIProvider(new OSGiUIProvider(m_application));
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
