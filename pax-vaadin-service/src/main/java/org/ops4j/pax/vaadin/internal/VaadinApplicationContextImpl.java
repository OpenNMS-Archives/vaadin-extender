package org.ops4j.pax.vaadin.internal;

import org.ops4j.pax.vaadin.VaadinApplicationContext;

public class VaadinApplicationContextImpl implements VaadinApplicationContext {
    private String sessionId;
    private int uiId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public void setUiId(int uiId) {
        this.uiId = uiId;
    }

    @Override
    public int getUiId() {
        return uiId;
    }
}
