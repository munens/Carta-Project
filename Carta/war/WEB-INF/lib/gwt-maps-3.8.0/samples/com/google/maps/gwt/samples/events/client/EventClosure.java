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
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/event-closure.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class EventClosure implements EntryPoint {

  private GoogleMap map;

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

    // Add 5 markers to the map at random locations
    LatLng southWest = LatLng.create(-31.203405, 125.244141);
    LatLng northEast = LatLng.create(-25.363882, 131.044922);
    LatLngBounds bounds = LatLngBounds.create(southWest, northEast);
    map.fitBounds(bounds);

    double lngSpan = northEast.lng() - southWest.lng();
    double latSpan = northEast.lat() - southWest.lat();
    for (int i = 0; i < 5; i++) {
      LatLng location = LatLng.create(
          southWest.lat() + (latSpan * Math.random()), southWest.lng()
              + (lngSpan * Math.random()));
      MarkerOptions markerOpts = MarkerOptions.create();
      markerOpts.setPosition(location);
      markerOpts.setMap(map);
      Marker marker = Marker.create(markerOpts);
      int j = i + 1;
      marker.setTitle(Integer.toString(j));
      attachSecretMessage(marker, i);
    }

  }

  // The five markers show a secret message when clicked
  // but that message is not within the marker's instance data
  public void attachSecretMessage(final Marker marker, int number) {
    String[] message = new String[] {"This", "is", "the", "secret", "message"};
    InfoWindowOptions myInfoWindowOpts = InfoWindowOptions.create();
    myInfoWindowOpts.setContent(message[number]);
    // myInfoWindowOpts.setSize(infoWindowSize); // want to size?
    final InfoWindow infowindow = InfoWindow.create(myInfoWindowOpts);
    marker.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        infowindow.open(map, marker);
      }
    });
  }
}
