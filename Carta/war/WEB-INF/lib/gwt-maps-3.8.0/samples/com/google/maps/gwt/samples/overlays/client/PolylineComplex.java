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
package com.google.maps.gwt.samples.overlays.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/polyline-complex.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class PolylineComplex implements EntryPoint {

  private static final LatLng CHICAGO = LatLng.create(41.879535, -87.624333);
  private GoogleMap map;
  private Polyline poly;

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(7);
    mapOpts.setCenter(CHICAGO);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    PolylineOptions polyOpts = PolylineOptions.create();
    polyOpts.setStrokeColor("#000000");
    polyOpts.setStrokeOpacity(1.0);
    polyOpts.setStrokeWeight(3);

    poly = Polyline.create(polyOpts);
    poly.setMap(map);

    map.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        addLatLng(event.getLatLng());
      }
    });
  }

  private void addLatLng(LatLng latLng) {
    double position = poly.getPath().push(latLng);

    MarkerOptions markerOpts = MarkerOptions.create();
    markerOpts.setPosition(latLng);
    markerOpts.setTitle("#" + position);
    markerOpts.setMap(map);
    Marker.create(markerOpts);
  }

}
