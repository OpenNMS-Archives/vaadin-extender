package org.ops4j.pax.vaadin;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.ui.UI;

public interface ApplicationFactory {

    public UI createApplication(HttpServletRequest request) throws ServletException;
    public Class<? extends UI> getApplicationClass() throws ClassNotFoundException;
    
    public UI getUI();
    
    public Map<String,String> getAdditionalHeaders();
    public String getAdditionalHeadContent();
    public List<ScriptTag> getAdditionalScripts();
    public String getAdditionalBodyStartContent();

}
