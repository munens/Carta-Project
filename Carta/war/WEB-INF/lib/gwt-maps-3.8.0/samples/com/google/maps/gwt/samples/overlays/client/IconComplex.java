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
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MarkerShape;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/icon-complex.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class IconComplex implements EntryPoint {

  private static final String BEACH_FLAG_SHADOW = "images/beachflag_shadow.png";
  private static final String BEACH_FLAG = "images/beachflag.png";

  class Beach {
    public String name;
    public LatLng location;
    public double zIndex;

    public Beach(String name, LatLng location, int zIndex) {
      this.name = name;
      this.location = location;
      this.zIndex = zIndex;
    }
  }

  @Override
  public void onModuleLoad() {
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(10.0);
    myOptions.setCenter(LatLng.create(-33.9, 151.2));
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    /**
     * Data for the markers consisting of a name, a LatLng and a zIndex for the
     * order in which these markers should display on top of each other.
     */
    Beach[] beaches = new Beach[] {
        new Beach("Bondi Beach", LatLng.create(-33.890542, 151.274856), 4),
        new Beach("Coogee Beach", LatLng.create(-33.923036, 151.259052), 5),
        new Beach("Cronulla Beach", LatLng.create(-34.028249, 151.157507), 3),
        new Beach("Manly Beach", LatLng.create(-33.80010128657071,
            151.28747820854187), 2),
        new Beach("Maroubra Beach", LatLng.create(-33.950198, 151.259302), 1)};
    setMarkers(map, beaches);
  }

  private void setMarkers(GoogleMap map, Beach[] beaches) {
    // Add markers to the map

    // Marker sizes are expressed as a Size of X,Y
    // where the origin of the image (0,0) is located
    // in the top left of the image.

    // Origins, anchor positions and coordinates of the marker
    // increase in the X direction to the right and in
    // the Y direction down.

    // This marker is 20 pixels wide by 32 pixels tall.
    // The origin for this image is 0,0.
    // The anchor for this image is the base of the flagpole at 0,32.
    MarkerImage icon = MarkerImage.create(BEACH_FLAG, Size.create(20, 32),
        Point.create(0, 0), Point.create(0, 32));

    // The shadow image is larger in the horizontal dimension
    // while the position and offset are the same as for the main image.
    MarkerImage shadow = MarkerImage.create(BEACH_FLAG_SHADOW,
        Size.create(37, 32), Point.create(0, 0), Point.create(0, 32));

    // Shapes define the clickable region of the icon.
    // The type defines an HTML <area> element 'poly' which
    // traces out a polygon as a series of X,Y points. The final
    // coordinate closes the poly by connecting to the first
    // coordinate.
    MarkerShape shape = MarkerShape.create();
    shape.setCoords(ArrayHelper.toJsArrayNumber(1, 1, 1, 20, 18, 20, 18, 1));
    shape.setType("poly");

    for (Beach beach : beaches) {
      MarkerOptions markerOpts = MarkerOptions.create();
      markerOpts.setPosition(beach.location);
      markerOpts.setMap(map);
      markerOpts.setIcon(icon);
      markerOpts.setShadow(shadow);
      markerOpts.setShape(shape);
      markerOpts.setTitle(beach.name);
      markerOpts.setZindex(beach.zIndex);

      Marker.create(markerOpts);
    }

  }
}
