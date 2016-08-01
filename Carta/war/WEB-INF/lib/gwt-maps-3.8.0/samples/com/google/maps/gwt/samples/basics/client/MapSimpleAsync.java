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
package com.google.maps.gwt.samples.basics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation/javascript/examples/map-simple-async.html
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class MapSimpleAsync implements EntryPoint {

  @Override
  public void onModuleLoad() {
    new JsonpRequestBuilder().send("http://maps.googleapis.com/maps/api/js?sensor=false",
      new AsyncCallback<Void>(){
        public void onFailure(Throwable caught) {
          Window.alert(caught.toString());
        }

        public void onSuccess(Void result) {
          renderMap();
        }
    });
  }

  public static void renderMap() {
    LatLng myLatLng = LatLng.create(-34.397, 150.644);
    MapOptions myOptions = MapOptions.create();
    myOptions.setZoom(8.0);
    myOptions.setCenter(myLatLng);
    myOptions.setMapTypeId(MapTypeId.ROADMAP);
    GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
  }

}
