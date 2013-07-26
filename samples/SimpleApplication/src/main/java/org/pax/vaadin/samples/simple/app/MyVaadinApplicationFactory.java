package org.pax.vaadin.samples.simple.app;

import com.vaadin.ui.UI;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;
import org.ops4j.pax.vaadin.OnmsServiceManager;
import org.osgi.framework.BundleContext;

public class MyVaadinApplicationFactory extends AbstractApplicationFactory {
    protected OnmsServiceManager serviceManager;

    @Override
    public UI getUI() {
        MyVaadinApplication application = new MyVaadinApplication();
        application.setServiceManager(serviceManager);
        return application;
    }

    @Override
    public Class<? extends UI> getUIClass() {
        return MyVaadinApplication.class;
    }

    public void setServiceManager(OnmsServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
