package org.ops4j.pax.vaadin.samples.internal.client.mycomponent;

import com.google.gwt.user.client.ui.Label;

//TODO extend any GWT Widget
public class MyComponentWidget extends Label {

 public static final String CLASSNAME = "mycomponent";

 public MyComponentWidget() {

     // setText("MyComponent sets the text via MyComponentConnector using MyComponentState");
     setStyleName(CLASSNAME);

 }

}
