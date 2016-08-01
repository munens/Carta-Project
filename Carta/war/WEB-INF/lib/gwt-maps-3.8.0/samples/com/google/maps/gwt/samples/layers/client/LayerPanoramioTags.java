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
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.panoramio.PanoramioLayer;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/layer-panoramio-tags.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class LayerPanoramioTags implements EntryPoint {

  private PanoramioLayer panoramioLayer;

  @Override
  public void onModuleLoad() {
    LatLng nyHarbor = LatLng.create(40.693134, -74.031028);
    MapOptions mapOpts = MapOptions.create();
    mapOpts.setZoom(15);
    mapOpts.setCenter(nyHarbor);
    mapOpts.setMapTypeId(MapTypeId.ROADMAP);
    final GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"),
        mapOpts);

    panoramioLayer = PanoramioLayer.create();
    panoramioLayer.setMap(map);
    registerCallback();
  }

  public native void registerCallback() /*-{
    // localThis captures "this" for reference inside the closure function.
    var localThis = this;
    // getTaggedPhotos links the button click-handler to the method below.
    $wnd['getTaggedPhotos'] = function() {
      $entry(localThis.@com.google.maps.gwt.samples.layers.client.LayerPanoramioTags::getTaggedPhotos()());
    }
  }-*/;

  public void getTaggedPhotos() {
    InputElement tagElement = Document.get().getElementById("tag").cast();
    String tagFilter = tagElement.getValue();
    panoramioLayer.setTag(tagFilter);
  }
}
