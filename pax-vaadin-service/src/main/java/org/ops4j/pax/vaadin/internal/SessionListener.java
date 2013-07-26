package org.ops4j.pax.vaadin.internal;

public interface SessionListener {
    void sessionDestroyed(String sessionId);
    void sessionInitialized(String sessionId);
}
