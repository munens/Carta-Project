/*
 * Copyright 2011 Google Inc.
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
package com.google.maps.gwt.samples.controls.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/control-custom-state.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class ControlCustomState implements EntryPoint {

  private static final LatLng CHICAGO = LatLng.create(41.850033, -87.6500523);
  private static final double initialZoom = 12;

  private GoogleMap map;

  class StatefulButtonPanel extends VerticalPanel {

    LatLng home = CHICAGO;
    double zoom = initialZoom;

    public StatefulButtonPanel() {
      Button homeBtn = new Button("Home", new ClickHandler() {
        public void onClick(ClickEvent event) {
          map.setCenter(home);
          map.setZoom(zoom);
        }
      });

      Button setBtn = new Button("Set Home", new ClickHandler() {
        public void onClick(ClickEvent event) {
          home = map.getCenter();
          zoom = map.getZoom();
        }
      });

      // Add button to the root panel. (register it on the GWT side)
      add(homeBtn);
      add(setBtn);
    }
  }

  @Override
  public void onModuleLoad() {
    Element mapDiv = Document.get().getElementById("map_canvas");
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(initialZoom);
    myOptions.setCenter(CHICAGO);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(mapDiv, myOptions);

    final Panel panel = new StatefulButtonPanel();

    RootPanel.get().add(panel);

    // Add button as a map control.
    map.getControls().get(
        new Double(ControlPosition.TOP_RIGHT.getValue()).intValue()).push(
        panel.getElement());
  }
}
