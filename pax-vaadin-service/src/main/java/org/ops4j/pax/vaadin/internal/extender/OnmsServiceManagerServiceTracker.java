package org.ops4j.pax.vaadin.internal.extender;

import org.ops4j.pax.vaadin.OnmsServiceManager;
import org.ops4j.pax.vaadin.SessionRepository;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class OnmsServiceManagerServiceTracker extends ServiceTracker {
    private final SessionRepository sessionRepository;

    public OnmsServiceManagerServiceTracker(BundleContext context, SessionRepository sessionRepository) {
        super(context, OnmsServiceManager.class.getName(), null);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Object addingService(ServiceReference reference) {
        OnmsServiceManager manager = (OnmsServiceManager)super.addingService(reference);
        sessionRepository.addSessionListener(manager);
        return manager;
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        super.removedService(reference, service);
        sessionRepository.removeSessionListener(service);
    }
}
