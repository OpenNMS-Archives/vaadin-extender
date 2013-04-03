package org.ops4j.pax.vaadin.internal.extender;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ops4j.pax.vaadin.ApplicationFactory;
import org.ops4j.pax.vaadin.internal.servlet.VaadinOSGiServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFactoryServiceTracker extends ServiceTracker {
    
    private Map<ApplicationFactory, ServiceRegistration> m_serviceRegistration = new HashMap<ApplicationFactory, ServiceRegistration>();
    private final Logger logger = LoggerFactory
            .getLogger(ApplicationFactoryServiceTracker.class.getName());
    
    public ApplicationFactoryServiceTracker(BundleContext context) {
        super(context, ApplicationFactory.class.getName(), null);
        
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object addingService(ServiceReference reference) {
        ApplicationFactory factory = (ApplicationFactory) super.addingService(reference);
        FactoryServlet servlet = new FactoryServlet(factory);
        Dictionary props = new Properties();
        
        for(String key : reference.getPropertyKeys()) {
            props.put(key, reference.getProperty(key));
        }
        
        if(props.get(PaxVaadinBundleTracker.ALIAS) == null) {
            logger.warn("You have not set the alias property for ApplicationFactory: " + factory);
        }
        m_serviceRegistration.put(factory, context.registerService(Servlet.class.getName(), servlet, props));
        
        return factory;
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
        //TODO: When does this get called
        super.modifiedService(reference, service);
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        
        ApplicationFactory factory = (ApplicationFactory) context.getService(reference);
        final ServiceRegistration servletRegistration = m_serviceRegistration.remove(factory);
        if (servletRegistration != null) {
            servletRegistration.unregister();
        }

        super.removedService(reference, service);
    }
    
    private class FactoryServlet extends VaadinOSGiServlet {
        private static final long serialVersionUID = 7458986273769030388L;

        private ApplicationFactory m_factory;

        public FactoryServlet(ApplicationFactory factory) {
            super(factory);
            m_factory = factory;
        }
        
//        @Override
//        protected Application getNewApplication(HttpServletRequest request) throws ServletException {
//            return m_factory.createApplication(request);
//        }
//
//        @Override
//        protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
//            return m_factory.getApplicationClass();
//        }

        @Override
        protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
            final Map<String,String> headers = m_factory.getAdditionalHeaders();
            if (headers.size() > 0) {
                for (final Map.Entry<String,String> entry : headers.entrySet()) {
                    response.addHeader(entry.getKey(), entry.getValue());
                }
            }
            super.service(request, response);
        }

//        @Override
//        protected void writeAjaxPageHtmlBodyStart(final BufferedWriter page, final HttpServletRequest request) throws IOException {
//            super.writeAjaxPageHtmlBodyStart(page, request);
//            final String content = m_factory.getAdditionalBodyStartContent();
//            if (content != null) {
//                page.write(content);
//            }
//        }
//
//        @Override
//        protected void writeAjaxPageHtmlHeadStart(final BufferedWriter page, final HttpServletRequest request) throws IOException {
//            super.writeAjaxPageHtmlHeadStart(page, request);
//            final String content = m_factory.getAdditionalHeadContent();
//            if (content != null) {
//                page.write(content);
//            }
//        }
//
//        @Override
//        protected void writeAjaxPageHtmlVaadinScripts(final Window window, final String themeName, final Application application, final BufferedWriter page, final String appUrl, final String themeUri, final String appId, final HttpServletRequest request) throws ServletException, IOException {
//            super.writeAjaxPageHtmlVaadinScripts(window, themeName, application, page, appUrl, themeUri, appId, request);
//            final List<ScriptTag> scripts = m_factory.getAdditionalScripts();
//            if (scripts != null && scripts.size() > 0) {
//                for (final ScriptTag tag : scripts) {
//                    page.write("<script");
//                    if (tag.getSource() != null) {
//                        page.write(" src=\"" + tag.getSource() + "\"");
//                    }
//                    if (tag.getType() != null) {
//                        page.write(" type=\"" + tag.getType() + "\"");
//                    }
//                    page.write(">");
//                    if (tag.getContents() != null) {
//                        page.write(tag.getContents());
//                    }
//                    page.write("</script>\n");
//                }
//            }
//        }
    }

}
