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
package com.google.maps.gwt.samples.services.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Window;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.Geocoder.Callback;
import com.google.maps.gwt.client.GeocoderAddressComponent;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/geocoding-reverse.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class GeocodingReverse implements EntryPoint {

  InfoWindow infowindow = InfoWindow.create();
  private Geocoder geocoder;
  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    geocoder = Geocoder.create();

    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(8.0);
    myOptions.setCenter(LatLng.create(40.730885, -73.997383));
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
    registerCallback();
  }

  private final native void registerCallback() /*-{
    var localThis = this;
    $wnd['codeLatLng'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.services.client.GeocodingReverse::codeLatLng()());
    }
  }-*/;

  public void codeLatLng() {
    InputElement addressElement = Document.get().getElementById("latlng").cast();
    String latLngStr[] = addressElement.getValue().split(",", 2);
    double lat = Double.parseDouble(latLngStr[0]);
    double lng = Double.parseDouble(latLngStr[1]);
    final LatLng latLng = LatLng.create(lat, lng);
    GeocoderRequest request = GeocoderRequest.create();
    request.setLocation(latLng);
    geocoder.geocode(request, new Callback() {
      public void handle(JsArray<GeocoderResult> results, GeocoderStatus status) {
        if (status == GeocoderStatus.OK) {
          if (results.length() > 0) {
            map.setZoom(11);
            MarkerOptions markerOpts = MarkerOptions.create();
            markerOpts.setMap(map);
            markerOpts.setPosition(latLng);
            Marker marker = Marker.create(markerOpts);
            GeocoderResult geocoderResult = results.get(0);
            StringBuffer sb = new StringBuffer();

            JsArray<GeocoderAddressComponent> addressComponents = 
                geocoderResult.getAddressComponents();
            for (int i = 0; i < addressComponents.length(); i++) {
              if (i > 0) {
                sb.append(", ");
              }
              sb.append(addressComponents.get(i).getLongName());
            }
            infowindow.setContent(sb.toString());
            infowindow.open(map, marker);
          } else {
            Window.alert("No results found");
          }
        } else {
          Window.alert("Geocode failed due to: " + status);
        }
      }
    });
  }
}
