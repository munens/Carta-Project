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
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/aerial-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class AerialSimple implements EntryPoint {

  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    MapOptions mapOptions = MapOptions.create();
    mapOptions.setCenter(LatLng.create(-25.363882, 131.044922));
    mapOptions.setZoom(18.0);
    mapOptions.setMapTypeId(MapTypeId.SATELLITE);

    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOptions);
    map.setTilt(45);
  }
}
