package org.ops4j.pax.vaadin;

import com.vaadin.ui.UI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractApplicationFactory implements ApplicationFactory {

    @Override
    @Deprecated
    public UI createApplication(HttpServletRequest request) throws ServletException {
        return getUI();
    }

    @Override
    public abstract UI getUI();
    
    @Override
    public abstract Class<? extends UI> getUIClass();
    
    @Override
    public Map<String, String> getAdditionalHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public String getAdditionalHeadContent() {
        return null;
    }

    @Override
    public final List<ScriptTag> getAdditionalScripts() {
        return Collections.emptyList();
    }

    @Override
    public String getAdditionalBodyStartContent() {
        return null;
    }

}
