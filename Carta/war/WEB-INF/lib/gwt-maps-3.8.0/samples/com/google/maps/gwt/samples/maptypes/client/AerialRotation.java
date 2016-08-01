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
package com.google.maps.gwt.samples.maptypes.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/aerial-rotation.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class AerialRotation implements EntryPoint {

  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    registerCallback();
    MapOptions mapOptions = MapOptions.create();
    mapOptions.setCenter(LatLng.create(45.518970, -122.672899));
    mapOptions.setZoom(18.0);
    mapOptions.setMapTypeId(MapTypeId.SATELLITE);

    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOptions);
    map.setTilt(45);
    map.setHeading(90);
  }

  public native void registerCallback() /*-{
    // localThis captures "this" for reference inside the closure function.
    var localThis = this;
    $wnd['autoRotate'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.maptypes.client.AerialRotation::autorotate()());
    }
  }-*/;

  public void autorotate() {
    if (map.getTilt() != 0) {
      map.setHeading(180);
      new Timer() {
        public void run() {
          map.setHeading(270);
        }
      }.schedule(3000);

      new Timer() {
        public void run() {
          map.setHeading(0);
        }
      }.schedule(6000);

      new Timer() {
        public void run() {
          map.setHeading(90);
        }
      }.schedule(9000);
    }
  }
}
