package org.ops4j.pax.vaadin.samples.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ops4j.pax.vaadin.ApplicationFactory;
import org.ops4j.pax.vaadin.ScriptTag;

import com.vaadin.ui.UI;

public class ColorPickerApplicationFactory implements ApplicationFactory {

    @Override
    public UI createApplication(HttpServletRequest request) throws ServletException {
        return new ColorPickerUI();
    }

    @Override
    public Class<? extends UI> getApplicationClass() throws ClassNotFoundException {
        return ColorPickerUI.class;
    }

    @Override
    public UI getUI() {
        return new ColorPickerUI();
    }

    @Override
    public Map<String, String> getAdditionalHeaders() {
        final Map<String,String> headers = new HashMap<String,String>();
        headers.put("X-UA-Compatible", "chrome=1");
        return headers;

    }

    @Override
    public String getAdditionalHeadContent() {
        return null;
    }

    @Override
    public List<ScriptTag> getAdditionalScripts() {
        return null;
    }

    @Override
    public String getAdditionalBodyStartContent() {
        return null;
    }

}
