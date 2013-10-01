package org.ops4j.pax.vaadin;

import com.vaadin.ui.UI;

import java.util.Map;

public interface ApplicationFactory {

    Class<? extends UI> getUIClass();

    UI createUI();

    Map<String,String> getAdditionalHeaders();
}
