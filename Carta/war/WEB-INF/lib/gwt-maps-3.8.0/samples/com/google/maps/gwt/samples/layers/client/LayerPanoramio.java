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
package com.google.maps.gwt.samples.layers.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.panoramio.PanoramioLayer;
import com.google.maps.gwt.client.panoramio.PanoramioMouseEvent;
import com.google.maps.gwt.client.panoramio.PanoramioLayer.ClickHandler;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/layer-panoramio.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class LayerPanoramio implements EntryPoint {

  @Override
  public void onModuleLoad() {
    LatLng fremont = LatLng.create(47.651743, -122.349243);
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(16);
    mapOpts.setCenter(fremont);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    final GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"),
        mapOpts);

    PanoramioLayer panoramioLayer = PanoramioLayer.create();
    panoramioLayer.setMap(map);

    panoramioLayer.addClickListener(new ClickHandler() {
      @Override
      public void handle(PanoramioMouseEvent event) {
        Document document = Document.get();
        Element photoDiv = document.getElementById("photo_panel");
        Text attribution = document.createTextNode(event.getFeatureDetails().getTitle()
            + ": " + event.getFeatureDetails().getAuthor());
        Element br = document.createElement("br");
        Element link = document.createElement("a");
        link.setAttribute("href", event.getFeatureDetails().getUrl());
        link.appendChild(attribution);
        photoDiv.appendChild(br);
        photoDiv.appendChild(link);
      }
    });
  }
}
