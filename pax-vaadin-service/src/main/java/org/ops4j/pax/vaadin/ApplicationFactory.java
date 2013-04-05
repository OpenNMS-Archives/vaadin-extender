package org.ops4j.pax.vaadin;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.UI;

public interface ApplicationFactory {

    /**
     * @deprecated The Application class was removed in Vaadin 7; 
     * use {@link #getUI()} instead.
     */
    UI createApplication(HttpServletRequest request) throws ServletException;

    Class<? extends UI> getUIClass() throws ClassNotFoundException;

    UI getUI();

    Map<String,String> getAdditionalHeaders();

    String getAdditionalHeadContent();

    /**
     * @deprecated Use the {@link JavaScript} annotation from Vaadin 7 instead.
     */
    List<ScriptTag> getAdditionalScripts();

    String getAdditionalBodyStartContent();
}
