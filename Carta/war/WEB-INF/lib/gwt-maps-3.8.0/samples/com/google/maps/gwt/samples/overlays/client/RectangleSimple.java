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
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Rectangle;
import com.google.maps.gwt.client.RectangleOptions;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/rectangle-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class RectangleSimple implements EntryPoint {

  private static final LatLng COACHELLA = LatLng.create(33.6803003, -116.173894);

  private Rectangle rectangle;

  private GoogleMap map;

  @Override
  public void onModuleLoad() {
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(11);
    mapOpts.setCenter(COACHELLA);
    mapOpts.setMapTypeId(MapTypeId.TERRAIN);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"), mapOpts);

    rectangle = Rectangle.create();
    map.addZoomChangedListener(new ZoomChangedHandler() {
      public void handle() {
        updateRectangle();
      }
    });
  }

  public void updateRectangle() {
    // Get the current bounds, which reflect the bounds before the zoom.
    RectangleOptions rectOpts = RectangleOptions.create();
    rectOpts.setStrokeColor("#FF0000");
    rectOpts.setStrokeOpacity(0.8);
    rectOpts.setStrokeWeight(2);
    rectOpts.setFillColor("#FF0000");
    rectOpts.setFillOpacity(0.35);
    rectOpts.setMap(map);
    rectOpts.setBounds(map.getBounds());
    rectangle.setOptions(rectOpts);
  }
}
