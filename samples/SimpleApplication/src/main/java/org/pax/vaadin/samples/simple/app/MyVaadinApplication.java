/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pax.vaadin.samples.simple.app;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.ops4j.pax.vaadin.OnmsServiceManager;
import org.ops4j.pax.vaadin.VaadinApplicationContext;
import org.osgi.framework.BundleContext;

import java.util.Properties;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Title("My Test Vaadin Application")
@PreserveOnRefresh
public class MyVaadinApplication extends UI
{
    private VerticalLayout window;
    private OnmsServiceManager serviceManager;
    private VaadinApplicationContext applicationContext;

    @Override
    public void init(final VaadinRequest request)
    {
        window = new VerticalLayout();
        setContent(window);
        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                window.addComponent(new Label("Thank you for clicking"));
            }
        });

        Button button2 = new Button("Logout");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                request.getWrappedSession().invalidate();
            }
        });
        window.addComponent(button);
        window.addComponent(button2);
        applicationContext = serviceManager.getApplicationContext(request.getWrappedSession().getId(), getUI().getUIId());

        registerService(new Item("Saft"), "sessionId=" + request.getWrappedSession().getId());
        registerService(new Item("Wurst"), "sessionId=" + request.getWrappedSession().getId()+",otherProperty=5");
        registerService(new Item("Käse"), null);
    }

    public void setServiceManager(OnmsServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    private void registerService(Item item, String options) {
        Properties props = new Properties();
        if (options != null && !options.isEmpty()) {
            if (!options.endsWith(",")) options = options + ",";
            String[] elements = options.split(",");
            for (String eachElement : elements) {
                String[] pair = eachElement.split("=");
                props.put(pair[0].trim(), pair[1].trim());
            }
        }
        serviceManager.registerAsService(item, props);
    }
}
