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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.PolygonOptions;
import com.google.maps.gwt.client.Polygon.ClickHandler;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/polygon-arrays.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class PolygonArrays implements EntryPoint {

  private static final LatLng CENTER = LatLng.create(24.886436490787712,
      -70.2685546875);
  private GoogleMap map;
  private Polygon bermudaTriangle;
  private static final MVCArray<MVCArray<LatLng>> triangleCoords = MVCArray.create();
  private InfoWindow infowindow;

  static {
    MVCArray<LatLng> path = MVCArray.create();
    path.push(LatLng.create(25.774252, -80.190262));
    path.push(LatLng.create(18.466465, -66.118292));
    path.push(LatLng.create(32.321384, -64.75737));
    triangleCoords.push(path);
  }

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(5);
    mapOpts.setCenter(CENTER);
    mapOpts.setMapTypeId(MapTypeId.TERRAIN);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    // Construct the polygon
    // Note that we do specify an array of arrays.
    PolygonOptions polyOpts = PolygonOptions.create();

    polyOpts.setPaths(triangleCoords);
    polyOpts.setStrokeColor("#FF0000");
    polyOpts.setStrokeOpacity(0.6);
    polyOpts.setStrokeWeight(3);
    polyOpts.setFillColor("#FF0000");
    polyOpts.setFillOpacity(0.35);

    bermudaTriangle = Polygon.create(polyOpts);

    bermudaTriangle.setMap(map);
    bermudaTriangle.addClickListener(new ClickHandler() {
      public void handle(MouseEvent event) {
        showArrays(event);
      }
    });
    infowindow = InfoWindow.create();
  }

  private void showArrays(MouseEvent event) {
    // Since this Polygon only has one path, we can call getPath()
    // to return the MVCArray of LatLngs
    MVCArray<LatLng> vertices = bermudaTriangle.getPath();

    SafeHtmlBuilder contentString = new SafeHtmlBuilder();
    contentString.appendHtmlConstant(
        "<b>Bermuda Triangle Polygon</b><br />");

    contentString.appendHtmlConstant("Clicked Location: <br />");
    contentString.append(event.getLatLng().lat());
    contentString.appendHtmlConstant(",");
    contentString.append(event.getLatLng().lng());
    contentString.appendHtmlConstant("<br />");

    // Iterate over the vertices.
    for (int i = 0; i < vertices.getLength(); i++) {
      LatLng xy = vertices.getAt(i);
      contentString.appendHtmlConstant("<br /> Coordinate: ");
      contentString.append(i);
      contentString.appendHtmlConstant("<br />");
      contentString.append(xy.lat());
      contentString.appendHtmlConstant(",");
      contentString.append(xy.lng());
    }

    // Replace our Info Window's content and position
    infowindow.setContent(contentString.toSafeHtml().asString());
    infowindow.setPosition(event.getLatLng());

    infowindow.open(map);
  }
}
