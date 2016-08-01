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
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/polyline-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class PolylineSimple implements EntryPoint {

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(3);
    mapOpts.setCenter(LatLng.create(0, -180));
    mapOpts.setMapTypeId(MapTypeId.TERRAIN);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    MVCArray<LatLng> flightPathCoordinates = MVCArray.create();
    flightPathCoordinates.push(LatLng.create(37.772323, -122.214897));
    flightPathCoordinates.push(LatLng.create(21.291982, -157.821856));
    flightPathCoordinates.push(LatLng.create(-18.142599, 178.431));
    flightPathCoordinates.push(LatLng.create(-27.46758, 153.027892));

    PolylineOptions polyOpts = PolylineOptions.create();
    polyOpts.setPath(flightPathCoordinates);
    polyOpts.setStrokeColor("#FF0000");
    polyOpts.setStrokeOpacity(1.0);
    polyOpts.setStrokeWeight(2);

    Polyline flightPath = Polyline.create(polyOpts);
    flightPath.setMap(map);
  }
}
