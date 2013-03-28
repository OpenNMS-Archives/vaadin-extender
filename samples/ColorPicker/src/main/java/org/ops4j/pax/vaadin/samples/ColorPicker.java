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

package org.ops4j.pax.vaadin.samples;

import org.ops4j.pax.vaadin.samples.client.mycomponent.MyComponentClientRpc;
import org.ops4j.pax.vaadin.samples.client.mycomponent.MyComponentServerRpc;
import org.ops4j.pax.vaadin.samples.client.mycomponent.MyComponentState;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
public class ColorPicker extends AbstractComponent {


    private MyComponentServerRpc m_rpc = new MyComponentServerRpc() {
        private int clickCount = 0;
        
        @Override
        public void clicked(MouseEventDetails mouseDetails) {
            // nag every 5:th click using RPC
            if (++clickCount % 5 == 0) {
                getRpcProxy(MyComponentClientRpc.class).alert(
                        "Ok, that's enough!");
            }
            // update shared state
            getState().text = "You have clicked " + clickCount + " times";
        }
    };
    
    public ColorPicker() {
        registerRpc(m_rpc);
    }

    @Override
    public MyComponentState getState() {
        return (MyComponentState) super.getState();
    }
}
