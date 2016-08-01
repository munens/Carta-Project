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
import com.google.maps.gwt.client.GroundOverlay;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/groundoverlay-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class GroundOverlaySimple implements EntryPoint {

  private static final LatLng NEWARK = LatLng.create(40.740, -74.18);
  private static final LatLngBounds IMAGE_BOUNDS = LatLngBounds.create(
      LatLng.create(40.716216, -74.213393),
      LatLng.create(40.765641, -74.139235));

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(13);
    mapOpts.setCenter(NEWARK);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    GroundOverlay oldmap = GroundOverlay.create(
        "http://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg",
        IMAGE_BOUNDS);
    oldmap.setMap(map);
  }
}
