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

import java.util.ArrayList;
import java.util.List;

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

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/overlay-remove.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class OverlayRemove implements EntryPoint {

  private static final LatLng HAIGHT_ASHBURY = LatLng.create(37.7699298,
      -122.4469157);
  private GoogleMap map;
  private List<Marker> markers = new ArrayList<Marker>();

  @Override
  public void onModuleLoad() {
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(14.0);
    myOptions.setCenter(HAIGHT_ASHBURY);
    myOptions.setMapTypeId(MapTypeId.TERRAIN);

    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
    map.addClickListener(new ClickHandler() {
      @Override
      public void handle(MouseEvent event) {
        addMarker(event.getLatLng());
      }
    });

    registerCallbacks();
  }

  public native void registerCallbacks() /*-{
    // localThis captures "this" for reference inside the closure function.
    var localThis = this;
    $wnd['clearOverlays'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.overlays.client.OverlayRemove::clearOverlays()());
    }
    $wnd['showOverlays'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.overlays.client.OverlayRemove::showOverlays()());
    }
    $wnd['deleteOverlays'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.overlays.client.OverlayRemove::deleteOverlays()());
    }
  }-*/;

  private void addMarker(LatLng location) {
    MarkerOptions newMarkerOpts = MarkerOptions.create();
    newMarkerOpts.setPosition(location);
    newMarkerOpts.setMap(map);
    Marker marker = Marker.create(newMarkerOpts);
    markers.add(marker);
  }

  // Removes the overlays from the map, but keeps them in the array
  private void clearOverlays() {
    if (markers != null) {
      for (Marker marker : markers) {
        marker.setMap((GoogleMap) null);
      }
    }
  }

  // shows the overlays from the map
  private void showOverlays() {
    if (markers != null) {
      for (Marker marker : markers) {
        marker.setMap(map);
      }
    }
  }

  // Deletes all markers in the array by removing references to them
  private void deleteOverlays() {
    if (markers != null) {
      for (Marker marker : markers) {
        marker.setMap((GoogleMap) null);
      }
      markers.clear();
    }
  }
}
