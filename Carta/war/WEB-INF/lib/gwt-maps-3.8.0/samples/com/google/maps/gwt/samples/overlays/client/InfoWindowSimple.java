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
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Marker.ClickHandler;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/infowindow-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class InfoWindowSimple implements EntryPoint {

  private String contentString = "<div id=\"content\">"
      + "<div id=\"siteNotice\">"
      + "</div>"
      + "<h1 id=\"firstHeading\" class=\"firstHeading\">Uluru</h1>"
      + "<div id=\"bodyContent\">"
      + "<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large "
      + "sandstone rock formation in the southern part of the "
      + "Northern Territory, central Australia. It lies 335 km (208 mi) "
      + "south west of the nearest large town, Alice Springs; 450 km "
      + "(280 mi) by road. Kata Tjuta and Uluru are the two major "
      + "features of the Uluru - Kata Tjuta National Park. Uluru is "
      + "sacred to the Pitjantjatjara and Yankunytjatjara, the "
      + "Aboriginal people of the area. It has many springs, waterholes, "
      + "rock caves and ancient paintings. Uluru is listed as a World "
      + "Heritage Site.</p>"
      + "<p>Attribution: Uluru, <a href=\"http://en.wikipedia.org/w/index.php?"
      + "title=Uluru&oldid=297882194\">"
      + "http://en.wikipedia.org/w/index.php?title=Uluru</a> (last visited June "
      + "22, 2009).</p>"
      + "</div>"
      + "</div>";

  @Override
  public void onModuleLoad() {
    LatLng location = LatLng.create(-25.363882, 131.044922);

    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(4);
    mapOpts.setCenter(location);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    final GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"),
        mapOpts);

    InfoWindowOptions infowindowOpts = InfoWindowOptions.create();
    infowindowOpts.setContent(contentString);
    final InfoWindow infowindow = InfoWindow.create(infowindowOpts);

    MarkerOptions markerOpts = MarkerOptions.create();
    markerOpts.setPosition(location);
    markerOpts.setMap(map);
    markerOpts.setTitle("Uluru (Ayers Rock)");
    final Marker marker = Marker.create(markerOpts);
    marker.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        infowindow.open(map, marker);
      }
    });
  }
}
