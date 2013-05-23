package org.ops4j.pax.vaadin.samples.internal;

import java.util.HashMap;
import java.util.Map;

import org.ops4j.pax.vaadin.AbstractApplicationFactory;

import com.vaadin.ui.UI;

public class ColorPickerApplicationFactory extends AbstractApplicationFactory {

    @Override
    public Class<? extends UI> getUIClass() {
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
    public String getAdditionalBodyStartContent() {
        return null;
    }

}
