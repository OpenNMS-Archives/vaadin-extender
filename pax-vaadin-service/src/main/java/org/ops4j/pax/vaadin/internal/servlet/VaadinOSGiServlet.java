package org.ops4j.pax.vaadin.internal.servlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

import org.ops4j.pax.vaadin.ApplicationFactory;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

public class VaadinOSGiServlet extends VaadinServlet {
    private final OSGiUIProvider m_provider;
    private final Set<VaadinSession> m_sessions = Collections.synchronizedSet(new HashSet<VaadinSession>());

    public VaadinOSGiServlet(final ApplicationFactory factory) {
        m_provider = new OSGiUIProvider(factory);
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) {
        
        final VaadinServletService service = super.createServletService(deploymentConfiguration);
        service.addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                final VaadinSession session = event.getSession();
                m_sessions.add(session);
                if(session.getUIProviders().isEmpty() || !session.getUIProviders().contains(m_provider)) {
                    session.addUIProvider(m_provider);
                }
            }
        });
        
        
        
        service.addSessionDestroyListener(new SessionDestroyListener() {
            @Override
            public void sessionDestroy(SessionDestroyEvent event) {
                final VaadinSession session = event.getSession();
                m_sessions.remove(session);
                if (session.getUIProviders().contains(m_provider)) {
                    session.removeUIProvider(m_provider);
                }
            }
            
        });
        
        
        
        return service;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        System.out.println("servlet Initialized");
    }
    
    @Override
    public void destroy() {
        for (final VaadinSession vaadinSession : m_sessions) {
            vaadinSession.removeFromSession(vaadinSession.getService());
        }
        super.destroy();
    }

}
