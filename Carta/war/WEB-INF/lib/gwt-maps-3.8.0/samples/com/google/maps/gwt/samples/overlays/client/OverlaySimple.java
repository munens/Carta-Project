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
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.OverlayView;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/overlay-simple.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class OverlaySimple implements EntryPoint {

  /**
   * Extend map types in JS and wrap them in Java with overlay types.
   */
  static class USGSOverlay extends OverlayView {
    /**
     * Protected constructor for internal use only.
     */
    protected USGSOverlay() {
      /* Java constructor is protected, */
    }

    public static native USGSOverlay create(LatLngBounds bounds,
        String imageSrc, GoogleMap map)/*-{
      return new $wnd.USGSOverlay(bounds, imageSrc, map);
    }-*/;
  }

  @Override
  public void onModuleLoad() {
    LatLng myLatLng = LatLng.create(62.323907, -150.109291);
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(11.0);
    myOptions.setCenter(myLatLng);
    myOptions.setMapTypeId(MapTypeId.SATELLITE);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    LatLng swBound = LatLng.create(62.281819, -150.287132);
    LatLng neBound = LatLng.create(62.400471, -150.005608);
    LatLngBounds bounds = LatLngBounds.create(swBound, neBound);

    // Photograph courtesy of the U.S. Geological Survey
    String srcImage = "images/talkeetna.png";
    USGSOverlay.create(bounds, srcImage, map);
  }
}
