package org.ops4j.pax.vaadin.internal.servlet;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public final class VaadinOSGiServletService extends VaadinServletService {
    private static final long serialVersionUID = -6452889839000802542L;
    private UI m_application;
    public VaadinOSGiServletService(final VaadinServlet servlet, final DeploymentConfiguration deploymentConfiguration, UI application) {
      super(servlet, deploymentConfiguration);
      m_application = application;
    }
    @Override
    protected VaadinSession createVaadinSession(final VaadinRequest request) throws ServiceException {
      VaadinOSGiSession session = new VaadinOSGiSession(this, m_application);
      return session;
    }
  }
