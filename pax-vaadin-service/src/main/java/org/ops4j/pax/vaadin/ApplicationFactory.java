package org.ops4j.pax.vaadin;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.ui.UI;

public interface ApplicationFactory {
    
    @Deprecated
    UI createApplication(HttpServletRequest request) throws ServletException;
    
    Class<? extends UI> getUIClass() throws ClassNotFoundException;
    
    UI getUI();
    
    Map<String,String> getAdditionalHeaders();
    String getAdditionalHeadContent();
    List<ScriptTag> getAdditionalScripts();
    String getAdditionalBodyStartContent();
}
