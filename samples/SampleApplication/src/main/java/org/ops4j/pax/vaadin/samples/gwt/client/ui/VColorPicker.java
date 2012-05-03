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

package org.ops4j.pax.vaadin.samples.gwt.client.ui;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VColorPicker extends GwtColorPicker implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "example-colorpicker";

    /** Component identifier in UIDL communications. */
    String uidlId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VColorPicker() {
        // The superclass has a lot of relevant initialization
        super();

        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    /**
     * This method must be implemented to update the client-side component from
     * UIDL data received from server.
     * 
     * This method is called when the page is loaded for the first time, and
     * every time UI changes in the component are received from the server.
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        // Get value received from server and actualize it in the GWT component
        setColor(uidl.getStringVariable("colorname"));
    }

    /** Override the method to communicate the new value to server. */
    @Override
    public void setColor(String newcolor) {
        // Ignore if no change
        if (newcolor.equals(currentcolor.getText())) {
            return;
        }

        // Let the original implementation to do whatever it needs to do
        super.setColor(newcolor);

        // Updating the state to the server can not be done before
        // the server connection is known, i.e., before updateFromUIDL()
        // has been called.
        if (uidlId == null || client == null) {
            return;
        }

        // Communicate the user interaction parameters to server. This call will
        // initiate an AJAX request to the server.
        client.updateVariable(uidlId, "colorname", newcolor, true);
    }
}
