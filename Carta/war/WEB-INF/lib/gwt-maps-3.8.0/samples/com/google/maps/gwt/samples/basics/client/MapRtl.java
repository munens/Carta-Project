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
package com.google.maps.gwt.samples.basics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/map-rtl.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class MapRtl implements EntryPoint {

  @Override
  public void onModuleLoad() {
    LatLng mapCenter = LatLng.create(30.064742, 31.249509);

    MapOptions mapOptions = MapOptions.create();
    mapOptions.setScaleControl(true);
    mapOptions.setCenter(mapCenter);
    mapOptions.setZoom(10.0);
    mapOptions.setMapTypeId(MapTypeId.ROADMAP);

    final GoogleMap map = GoogleMap.create(
        Document.get().getElementById("map_canvas"),
        mapOptions);

    MarkerOptions markerOpts = MarkerOptions.create();
    markerOpts.setPosition(mapCenter);
    markerOpts.setMap(map);
    final Marker marker = Marker.create(markerOpts);

    InfoWindowOptions infoWindowOpts = InfoWindowOptions.create();
    infoWindowOpts.setContent("<b>القاهرة</b>");

    final InfoWindow infoWindow = InfoWindow.create(infoWindowOpts);
    infoWindow.open(map);

    marker.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        infoWindow.open(map, marker);
      }
    });
  }
}
