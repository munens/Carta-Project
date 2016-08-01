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
import com.google.maps.gwt.client.Circle;
import com.google.maps.gwt.client.CircleOptions;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/circle-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class CircleSimple implements EntryPoint {

  static class City {
    LatLng center;
    Long population;

    City(LatLng center, Long population) {
      this.center = center;
      this.population = population;
    }
  }

  private static java.util.Map<String, City> citymap = new HashMap<String, City>();
  static {
    citymap.put("Chicago", new City(LatLng.create(41.878113, -87.629798),
        2842518L));
    citymap.put("New York", new City(LatLng.create(40.714352, -74.005973),
        8143197L));
    citymap.put("Los Angeles", new City(LatLng.create(34.052234, -118.243684),
        3844829L));
  }

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(4);
    mapOpts.setCenter(LatLng.create(37.09024, -95.712891));
    mapOpts.setMapTypeId(MapTypeId.TERRAIN);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    for (Entry<String, City> entry : citymap.entrySet()) {
      // Construct the circle for each value in citymap. Scale population by 20.
      CircleOptions populationOptions = CircleOptions.create();
      populationOptions.setStrokeColor("#ff0000");
      populationOptions.setStrokeOpacity(0.8);
      populationOptions.setStrokeWeight(2);
      populationOptions.setFillColor("#ff0000");
      populationOptions.setFillOpacity(0.35);
      populationOptions.setMap(map);
      populationOptions.setCenter(entry.getValue().center);
      populationOptions.setRadius(entry.getValue().population / 20);
      Circle.create(populationOptions);
    }
  }
}
