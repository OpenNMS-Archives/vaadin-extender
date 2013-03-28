package org.ops4j.pax.vaadin.internal.servlet;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class OSGiUIProvider extends UIProvider {
    
    private UI m_application;
    
    public OSGiUIProvider(UI application) {
        m_application = application;
    }
    
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return m_application.getClass();
    }
    
    @Override
    public UI createInstance(final UICreateEvent e) {
        return m_application;
    }

}
