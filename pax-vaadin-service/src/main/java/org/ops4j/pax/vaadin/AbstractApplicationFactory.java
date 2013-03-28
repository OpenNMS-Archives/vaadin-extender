package org.ops4j.pax.vaadin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.ui.UI;

public abstract class AbstractApplicationFactory implements ApplicationFactory {

    @Override
    public abstract UI createApplication(HttpServletRequest request) throws ServletException;

    @Override
    public abstract Class<? extends UI> getApplicationClass() throws ClassNotFoundException;

    @Override
    public abstract UI getUI();
    
    @Override
    public Map<String, String> getAdditionalHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public String getAdditionalHeadContent() {
        return null;
    }

    @Override
    public List<ScriptTag> getAdditionalScripts() {
        return Collections.emptyList();
    }

    @Override
    public String getAdditionalBodyStartContent() {
        return null;
    }

}
