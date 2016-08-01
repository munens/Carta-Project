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
package com.google.maps.gwt.samples.styling.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeControlOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MapTypeStyle;
import com.google.maps.gwt.client.MapTypeStyleFeatureType;
import com.google.maps.gwt.client.MapTypeStyler;
import com.google.maps.gwt.client.StyledMapType;
import com.google.maps.gwt.client.StyledMapTypeOptions;

/**
 * NOTE: This sample is currently broken.
 * 
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/pink-parks.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class PinkParks implements EntryPoint {

  @Override
  public void onModuleLoad() {
    MapTypeStyle styleAllDesaturated = MapTypeStyle.create();
    styleAllDesaturated.setFeatureType(MapTypeStyleFeatureType.ALL);
    styleAllDesaturated.setStylers(ArrayHelper.toJsArray(MapTypeStyler.saturation(-80)));

    MapTypeStyle pinkParkStyle = MapTypeStyle.create();
    pinkParkStyle.setFeatureType(MapTypeStyleFeatureType.POI_PARK);
    pinkParkStyle.setStylers(ArrayHelper.toJsArray(
        MapTypeStyler.hue("#ff0023"), MapTypeStyler.saturation(40)));

    StyledMapTypeOptions myStyledMapTypeOpts = StyledMapTypeOptions.create();
    myStyledMapTypeOpts.setName("Pink Parks");

    StyledMapType pinkMapType = StyledMapType.create(
        ArrayHelper.toJsArray(styleAllDesaturated, pinkParkStyle),
        myStyledMapTypeOpts);

    MapTypeControlOptions myMapTypeControlOpts = MapTypeControlOptions.create();
    myMapTypeControlOpts.setMapTypeIds(ArrayHelper.toJsArrayString(
        MapTypeId.ROADMAP.getValue(), MapTypeId.SATELLITE.getValue(),
        "pink_parks"));

    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(11.0);
    myOptions.setCenter(LatLng.create(55.6468, 37.581));
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    myOptions.setMapTypeControlOptions(myMapTypeControlOpts);

    GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
    map.getMapTypes().set("pink_parks", pinkMapType);
    map.setMapTypeId("pink_parks");
  }

}
