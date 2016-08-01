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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/control-custom.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class ControlCustom implements EntryPoint {

  private static final LatLng CHICAGO = LatLng.create(41.850033, -87.6500523);

  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    Element mapDiv = Document.get().getElementById("map_canvas");
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(12);
    myOptions.setCenter(CHICAGO);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(mapDiv, myOptions);

    Button btn = new Button("Home", new ClickHandler() {
      public void onClick(ClickEvent event) {
        map.setCenter(CHICAGO);
        map.setZoom(12);
      }
    });

    // Add button to the root panel. (register it on the GWT side)
    RootPanel.get().add(btn);

    // Add button as a map control.
    map.getControls().get(
        new Double(ControlPosition.TOP_RIGHT.getValue()).intValue()).push(
        btn.getElement());
  }
}
