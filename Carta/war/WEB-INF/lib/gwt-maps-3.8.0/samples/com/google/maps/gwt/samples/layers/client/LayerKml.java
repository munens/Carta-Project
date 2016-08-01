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
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.KmlLayer;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/layer-kml.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class LayerKml implements EntryPoint {

  @Override
  public void onModuleLoad() {
    LatLng location = LatLng.create(49.496675, -102.65625);
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(4);
    mapOpts.setCenter(location);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    final GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"),
        mapOpts);

    KmlLayer ctaLayer = KmlLayer.create("http://gmaps-samples.googlecode.com/svn/trunk/ggeoxml/cta.kml");
    ctaLayer.setMap(map);
  }
}
