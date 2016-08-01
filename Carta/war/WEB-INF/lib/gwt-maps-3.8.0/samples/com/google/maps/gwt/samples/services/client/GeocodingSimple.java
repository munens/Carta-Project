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
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.Geocoder.Callback;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/geocoding-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class GeocodingSimple implements EntryPoint {

  private Geocoder geocoder;
  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    geocoder = Geocoder.create();

    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(8.0);
    myOptions.setCenter(LatLng.create(-34.397, 150.644));
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
    registerCallback();
  }

  private final native void registerCallback() /*-{
    var localThis = this;
    $wnd['codeAddress'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.services.client.GeocodingSimple::codeAddress()());
    }
  }-*/;

  public void codeAddress() {
    InputElement addressElement = Document.get().getElementById("address").cast();
    GeocoderRequest request = GeocoderRequest.create();
    request.setAddress(addressElement.getValue());
    geocoder.geocode(request, new Callback() {
      public void handle(JsArray<GeocoderResult> results, GeocoderStatus status) {
        if (status == GeocoderStatus.OK) {
          GeocoderResult location = results.get(0);
          map.setCenter(location.getGeometry().getLocation());
          MarkerOptions markerOpts = MarkerOptions.create();
          markerOpts.setMap(map);
          markerOpts.setPosition(location.getGeometry().getLocation());
          Marker.create(markerOpts);
        } else {
          Window.alert("Geocode was not successful for the following reason: "
              + status);
        }
      }
    });
  }
}
