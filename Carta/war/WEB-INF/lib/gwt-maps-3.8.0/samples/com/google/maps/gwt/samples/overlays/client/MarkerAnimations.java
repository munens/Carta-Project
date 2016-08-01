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
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/marker_animations.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class MarkerAnimations implements EntryPoint {

  private static final LatLng PARLIAMENT = LatLng.create(59.32522, 18.07002);
  private static final LatLng STOCKHOLM = LatLng.create(59.327383, 18.06747);
  private Marker marker;

  @Override
  public void onModuleLoad() {
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(13.0);
    myOptions.setCenter(STOCKHOLM);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    MarkerOptions newMarkerOpts = MarkerOptions.create();
    newMarkerOpts.setMap(map);
    newMarkerOpts.setDraggable(true);
    newMarkerOpts.setAnimation(Animation.DROP);
    newMarkerOpts.setPosition(PARLIAMENT);
    marker = Marker.create(newMarkerOpts);

    marker.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        toggleBounce();
      }
    });
  }

  private void toggleBounce() {
    if (marker.getAnimation() != null) {
      marker.setAnimation(null);
    } else {
      marker.setAnimation(Animation.BOUNCE);
    }
  }
}
