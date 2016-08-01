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
package com.google.maps.gwt.samples.controls.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeControlOptions;
import com.google.maps.gwt.client.MapTypeControlStyle;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.PanControlOptions;
import com.google.maps.gwt.client.ScaleControlOptions;
import com.google.maps.gwt.client.StreetViewControlOptions;
import com.google.maps.gwt.client.ZoomControlOptions;
import com.google.maps.gwt.client.ZoomControlStyle;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/control-positioning.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class ControlPositioning implements EntryPoint {

  @Override
  public void onModuleLoad() {
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(12);
    myOptions.setCenter(LatLng.create(-28.643387, 153.612224));
    myOptions.setMapTypeId(MapTypeId.ROADMAP);

    myOptions.setMapTypeControl(true);
    MapTypeControlOptions mapTypeControlOptions = MapTypeControlOptions.create();
    mapTypeControlOptions.setStyle(MapTypeControlStyle.HORIZONTAL_BAR);
    mapTypeControlOptions.setPosition(ControlPosition.BOTTOM_CENTER);
    myOptions.setMapTypeControlOptions(mapTypeControlOptions);

    myOptions.setPanControl(true);
    PanControlOptions panControlOptions = PanControlOptions.create();
    panControlOptions.setPosition(ControlPosition.TOP_RIGHT);
    myOptions.setPanControlOptions(panControlOptions);

    myOptions.setZoomControl(true);
    ZoomControlOptions zoomControlOptions = ZoomControlOptions.create();
    zoomControlOptions.setStyle(ZoomControlStyle.LARGE);
    zoomControlOptions.setPosition(ControlPosition.LEFT_CENTER);
    myOptions.setZoomControlOptions(zoomControlOptions);

    myOptions.setScaleControl(true);
    ScaleControlOptions scaleControlOptions = ScaleControlOptions.create();
    scaleControlOptions.setPosition(ControlPosition.TOP_LEFT);
    myOptions.setScaleControlOptions(scaleControlOptions);

    myOptions.setStreetViewControl(true);
    StreetViewControlOptions streetViewControlOptions = StreetViewControlOptions.create();
    streetViewControlOptions.setPosition(ControlPosition.LEFT_TOP);
    myOptions.setStreetViewControlOptions(streetViewControlOptions);

    GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
  }

}
