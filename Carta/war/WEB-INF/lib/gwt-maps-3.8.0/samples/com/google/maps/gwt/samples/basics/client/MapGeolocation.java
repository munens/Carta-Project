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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/map-geolocation.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class MapGeolocation implements EntryPoint {

  private static final LatLng NEW_YORK = LatLng.create(40.69847032728747,
      -73.9514422416687);
  private static final LatLng SIBERIA = LatLng.create(60, 105);

  private Boolean browserSupportFlag;
  private LatLng initialLocation;
  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    final MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(14.0);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);

    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    // Try W3C Geolocation (Preferred)
    if (Geolocation.isSupported()) {
      browserSupportFlag = true;
      Geolocation.getIfSupported().getCurrentPosition(
          new Callback<Position, PositionError>() {

            @Override
            public void onSuccess(Position result) {
              Coordinates coords = result.getCoordinates();
              initialLocation = LatLng.create(coords.getLatitude(),
                  coords.getLongitude());
              map.setCenter(initialLocation);
            }

            @Override
            public void onFailure(PositionError reason) {
              MapGeolocation.this.handleNoGeolocation(browserSupportFlag);
            }
          });
    } else {
      browserSupportFlag = false;
      handleNoGeolocation(browserSupportFlag);
    }
  }

  private void handleNoGeolocation(boolean errorFlag) {
    if (errorFlag == true) {
      Window.alert("Geolocation service failed.");
      initialLocation = NEW_YORK;
    } else {
      Window.alert("Your browser doesn't support geolocation. We've placed you in Siberia.");
      initialLocation = SIBERIA;
    }
    map.setCenter(initialLocation);
  }
}
