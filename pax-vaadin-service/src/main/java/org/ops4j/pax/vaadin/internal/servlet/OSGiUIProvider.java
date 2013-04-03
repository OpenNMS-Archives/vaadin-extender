package org.ops4j.pax.vaadin.internal.servlet;

import org.ops4j.pax.vaadin.ApplicationFactory;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class OSGiUIProvider extends UIProvider {
    
    private ApplicationFactory m_application;
    
    public OSGiUIProvider(ApplicationFactory appFactory) {
        m_application = appFactory;
    }
    
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return m_application.getUI().getClass();
    }
    
    @Override
    public UI createInstance(final UICreateEvent e) {
        return m_application.getUI();
    }

}
