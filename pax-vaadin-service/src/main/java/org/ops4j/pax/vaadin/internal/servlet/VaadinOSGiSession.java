package org.ops4j.pax.vaadin.internal.servlet;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public final class VaadinOSGiSession extends VaadinSession {
    
    private final List<UIProvider> uiProviders;
    public VaadinOSGiSession(VaadinService service, final UI application) {
      super(service);
      
      uiProviders = new ArrayList<UIProvider>() {
        {
          //add(new OSGiUIProvider(application));
        }
      };
    }
    @Override
    public List<UIProvider> getUIProviders() {
      return uiProviders;
    }
  }
