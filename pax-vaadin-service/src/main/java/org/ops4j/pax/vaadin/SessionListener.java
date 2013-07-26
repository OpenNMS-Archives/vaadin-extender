package org.ops4j.pax.vaadin;

public interface SessionListener {
    void sessionDestroyed(String sessionId);
    void sessionInitialized(String sessionId);
}
