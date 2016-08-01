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
import com.google.gwt.user.client.Timer;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/marker-animations-iteration.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class MarkerAnimationsIteration implements EntryPoint {

  private static final LatLng BERLIN = LatLng.create(52.520816, 13.410186);

  private static final LatLng[] NEIGHBORHOODS = new LatLng[] {
      LatLng.create(52.511467, 13.447179), LatLng.create(52.549061, 13.422975),
      LatLng.create(52.497622, 13.396110), LatLng.create(52.517683, 13.394393)};

  private List<Marker> markers = new ArrayList<Marker>();

  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(12);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    mapOpts.setCenter(BERLIN);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);
    registerCallback();
  }

  public native void registerCallback() /*-{
    // localThis captures this for use within closure
    var localThis = this;
    $wnd['drop'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.overlays.client.MarkerAnimationsIteration::drop()());
    };
  }-*/;

  public void drop() {
    deleteOverlays();
    for (int i = 0; i < NEIGHBORHOODS.length; i++) {
      final int idx = i;
      new Timer() {
        public void run() {
          addMarker(idx);
        }
      }.schedule((i + 1) * 200);
    }
  }

  private void deleteOverlays() {
    if (markers != null) {
      for (Marker marker : markers) {
        marker.setMap((GoogleMap) null);
      }
      markers.clear();
    }
  }

  private void addMarker(int idx) {
    MarkerOptions markerOpts = MarkerOptions.create();
    markerOpts.setPosition(NEIGHBORHOODS[idx]);
    markerOpts.setMap(map);
    markerOpts.setDraggable(false);
    markerOpts.setAnimation(Animation.DROP);
    Marker marker = Marker.create(markerOpts);
    markers.add(marker);
  }
}
