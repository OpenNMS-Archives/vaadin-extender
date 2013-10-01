package org.ops4j.pax.vaadin.samples.internal;

import com.vaadin.ui.UI;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;

import java.util.HashMap;
import java.util.Map;

public class ColorPickerApplicationFactory extends AbstractApplicationFactory {

    @Override
    public Class<? extends UI> getUIClass() {
        return ColorPickerUI.class;
    }

    @Override
    public UI createUI() {
        return new ColorPickerUI();
    }

    @Override
    public Map<String, String> getAdditionalHeaders() {
        final Map<String,String> headers = new HashMap<String,String>();
        headers.put("X-UA-Compatible", "chrome=1");
        return headers;
    }
}
