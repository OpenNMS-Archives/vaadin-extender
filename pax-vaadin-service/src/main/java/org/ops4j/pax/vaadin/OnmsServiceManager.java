package org.ops4j.pax.vaadin;


import com.vaadin.server.VaadinSession;
import org.ops4j.pax.vaadin.internal.SessionListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OnmsServiceManager implements SessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(OnmsServiceManager.class);

    // key: Service
    private final Map<Object, ServiceRegistration> serviceRegistrations = Collections.synchronizedMap(new HashMap<Object, ServiceRegistration>());
    private final BundleContext bundleContext;

    public OnmsServiceManager(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void registerAsService(Object object) {
        registerAsService(object, null);
    }

    public void registerAsService(Object object, Properties properties) {
        if (object == null) return;
        ServiceRegistration serviceRegistration = bundleContext.registerService(object.getClass().getName(), object, properties);
        serviceRegistrations.put(object, serviceRegistration);
    }

    @Override
    public void sessionInitialized(String sessionId) {
        ; // we don't want to do anything
    }

    @Override
    public void sessionDestroyed(String sessionId) {
        final String sessionIdFilter = "(sessionId=%s)";
        try {
            ServiceReference[] allServiceReferences = bundleContext.getAllServiceReferences(null, String.format(sessionIdFilter, sessionId));
            if (allServiceReferences != null) {
                for (ServiceReference eachReference : allServiceReferences) {
                    Object service = bundleContext.getService(eachReference);
                    if (service == null) continue;
                    serviceRegistrations.get(service).unregister();
                    serviceRegistrations.remove(service);
                }
            }
        } catch (InvalidSyntaxException e) {
            LOG.error("Error retrieving ServiceReferences", e);
        }
    }

    public VaadinApplicationContext getApplicationContext(String sessionId, int uiId) {
        ServiceReference reference = bundleContext.getServiceReference(SessionRepository.class.getName());
        if (reference == null) throw new IllegalArgumentException("No SessionRepository registered.");
        Object service = bundleContext.getService(reference);
        if (service == null) throw new IllegalArgumentException("No service 'SessionRepository' found.");
        return ((SessionRepository)service).getVaadinApplicationContext(sessionId, uiId);
    }
}
