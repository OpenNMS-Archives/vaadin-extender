package org.ops4j.pax.vaadin;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.Application;

public interface ApplicationFactory {

    public Application createApplication(HttpServletRequest request) throws ServletException;
    public Class<? extends Application> getApplicationClass() throws ClassNotFoundException;
    
    public Map<String,String> getAdditionalHeaders();
    public String getAdditionalHeadContent();
    public List<ScriptTag> getAdditionalScripts();
    public String getAdditionalBodyStartContent();

}
