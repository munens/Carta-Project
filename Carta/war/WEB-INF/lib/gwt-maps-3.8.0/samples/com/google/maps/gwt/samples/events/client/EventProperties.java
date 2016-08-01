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
package com.google.maps.gwt.samples.events.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/event-properties.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class EventProperties implements EntryPoint {

  private GoogleMap map;

  private double zoomLevel;

  // No (good) way to set the size
  // private Size infoWindowSize = Size.create(50, 50);

  @Override
  public void onModuleLoad() {
    LatLng myLatLng = LatLng.create(-25.363882, 131.044922);
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(4.0);
    myOptions.setCenter(myLatLng);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    InfoWindowOptions myInfoWindowOpts = InfoWindowOptions.create();
    myInfoWindowOpts.setContent("Zoom Level Test");
    myInfoWindowOpts.setPosition(myLatLng);
    // myInfoWindowOpts.setSize(infoWindowSize); // want to size?
    final InfoWindow infowindow = InfoWindow.create(myInfoWindowOpts);
    infowindow.open(map);

    map.addZoomChangedListener(new ZoomChangedHandler() {
      public void handle() {
        zoomLevel = map.getZoom();
        infowindow.setContent("Zoom " + zoomLevel);
        if (zoomLevel == 0) {
          map.setZoom(10);
        }
      }
    });
  }
}
