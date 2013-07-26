package org.ops4j.pax.vaadin;


import com.vaadin.server.*;
import com.vaadin.ui.UI;
import org.ops4j.pax.vaadin.internal.SessionListener;
import org.ops4j.pax.vaadin.internal.VaadinApplicationContextImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionRepository implements SessionInitListener, SessionDestroyListener {
    private final static Logger LOG = LoggerFactory.getLogger(SessionRepository.class);

    /**
     * The sessionId is cleared as soon as the session is marked as "invalid". So there is no way, that we
     * can get the Session Id from the VaadinSession on "sessionDestroy"-Event. Therefore we have to cache it
     * here. (Key: VaadinSession, Value: SessionId)
     */
    private final Map<VaadinSession, String> vaadinSessionIdMap = new HashMap<VaadinSession, String>();
    // key: SessionId
    private final Map<String, VaadinApplicationContext> vaadinApplicationContextMap = new HashMap<String, VaadinApplicationContext>();
    private List<SessionListener> sessionListeners = new ArrayList<SessionListener>();


    public static SessionRepository getRepository(BundleContext context) {
        ServiceReference reference = context.getServiceReference(SessionRepository.class.getName());
        return (SessionRepository)context.getService(reference);
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        String sessionId = vaadinSessionIdMap.get(event.getSession());
        if (sessionId == null) throw new IllegalArgumentException("Unknown session : " + event.getSession());
        for (SessionListener eachListener : sessionListeners) {
            eachListener.sessionDestroyed(sessionId);
        }
        vaadinSessionIdMap.remove(event.getSession());
        vaadinApplicationContextMap.remove(sessionId);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        vaadinSessionIdMap.put(event.getSession(), event.getSession().getSession().getId());
        for (SessionListener eachListener : sessionListeners) {
            eachListener.sessionInitialized(vaadinSessionIdMap.get(event.getSession()));
        }
    }

    public void addSessionListener(SessionListener sessionListener) {
        sessionListeners.add(sessionListener);
    }

    public void removeSessionListener(Object service) {
        sessionListeners.remove(service);
    }

    public VaadinApplicationContext getVaadinApplicationContext(String sessionId, int uiId) {
        if (uiId == -1) throw new IllegalArgumentException("uiId must be set.");
        if (sessionId == null) throw new IllegalArgumentException("session Id must not be null.");
        if (vaadinApplicationContextMap.get(sessionId) == null)
            vaadinApplicationContextMap.put(sessionId, createVaadinApplicationContext(sessionId, uiId));
        return vaadinApplicationContextMap.get(sessionId);
    }

    public VaadinApplicationContext createVaadinApplicationContext(String sessionId, int uiId) {
        VaadinApplicationContextImpl context = new VaadinApplicationContextImpl();
        context.setSessionId(sessionId);
        context.setUiId(uiId);
        return context;
    }
}
