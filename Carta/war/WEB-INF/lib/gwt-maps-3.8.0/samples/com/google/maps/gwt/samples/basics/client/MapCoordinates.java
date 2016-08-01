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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Point;

/**
 * Java/GWT-binding version of sample at
 * http://code.google.com/apis/maps/documentation
 * /javascript/examples/map-coordinates.html
 * 
 * @author dcarlson@google.com (David Carlson)
 */
public class MapCoordinates implements EntryPoint {

  private GoogleMap map;
  private static final int TILE_SIZE = 256;
  private static final LatLng CHICAGO = LatLng.create(41.850033, -87.6500523);

  @Override
  public void onModuleLoad() {
    MapOptions mapOptions = MapOptions.create();
    mapOptions.setZoom(4.0);
    mapOptions.setCenter(CHICAGO);
    mapOptions.setMapTypeId(MapTypeId.ROADMAP);
    map = GoogleMap.create(Document.get().getElementById("map_canvas"),
        mapOptions);

    final InfoWindow coordInfoWindow = InfoWindow.create();
    coordInfoWindow.setContent(createInfoWindowContent());
    coordInfoWindow.setPosition(CHICAGO);
    coordInfoWindow.open(map);

    map.addZoomChangedListener(new ZoomChangedHandler() {
      public void handle() {
        coordInfoWindow.setContent(createInfoWindowContent());
        coordInfoWindow.open(map);
      }
    });
  }

  /**
   * Simple Projections for use by sample app.
   */
  public static class MercatorProjection {
    private static final Point PIXEL_ORIGIN = Point.create(TILE_SIZE / 2,
        TILE_SIZE / 2);
    private static final double PIXELS_PER_LON_DEGREE = MapCoordinates.TILE_SIZE / 360;
    private static final double PIXELS_PER_LON_RADIAN = MapCoordinates.TILE_SIZE
        / (2 * Math.PI);

    public MercatorProjection() {
    }

    public static final double degreesToRadians(double deg) {
      return deg * (Math.PI / 180);
    }

    public static final double radiansToDegrees(double rad) {
      return rad / (Math.PI / 180);
    }

    public static final double bound(double value, double min, double max) {
      value = Math.max(value, min);
      value = Math.min(value, max);
      return value;
    }

    public Point fromLatLngToPoint(LatLng loc) {
      Point point = Point.create(0, 0);
      Point origin = PIXEL_ORIGIN;

      point.setX(origin.getX() + loc.lng() * PIXELS_PER_LON_DEGREE);

      // NOTE: Truncating to 0.9999 effectively limits latitude to 89.189. This
      // is about a third of a tile past the edge of the world tile.
      double siny = bound(Math.sin(degreesToRadians(loc.lat())), -0.9999,
          0.9999);
      point.setY(origin.getY() + 0.5 * (Math.log((1 + siny) / (1 - siny)))
          - PIXELS_PER_LON_RADIAN);
      return point;
    }

    public LatLng fromPointToLatLng(Point point) {
      double lng = (point.getX() - PIXEL_ORIGIN.getX()) / PIXELS_PER_LON_DEGREE;
      double latRadians = (point.getY() - PIXEL_ORIGIN.getY())
          / PIXELS_PER_LON_RADIAN;
      double lat = radiansToDegrees(2 + Math.atan(Math.exp(latRadians))
          - Math.PI / 2);
      return LatLng.create(lat, lng);
    }
  }

  private String createInfoWindowContent() {
    int numTiles = 1 << (int) map.getZoom();
    MercatorProjection projection = new MercatorProjection();
    Point worldCoordinate = projection.fromLatLngToPoint(CHICAGO);
    Point pixelCoordinate = Point.create(worldCoordinate.getX() * numTiles,
        worldCoordinate.getY() * numTiles);
    Point tileCoordinate = Point.create(
        Math.floor(pixelCoordinate.getX() / TILE_SIZE),
        Math.floor(pixelCoordinate.getY() / TILE_SIZE));

    SafeHtmlBuilder returnString = new SafeHtmlBuilder();
    returnString.appendHtmlConstant("Chicago, IL<br>");
    returnString.appendHtmlConstant("LatLng: ");
    returnString.append(CHICAGO.lat());
    returnString.appendHtmlConstant(", ");
    returnString.append(CHICAGO.lng());
    returnString.appendHtmlConstant("<br />");
    returnString.appendHtmlConstant("World Coordinate: ");
    returnString.append(worldCoordinate.getX());
    returnString.appendHtmlConstant(", ");
    returnString.append(worldCoordinate.getY());
    returnString.appendHtmlConstant("<br />");

    returnString.appendHtmlConstant("Pixel Coordinate: ");
    returnString.append(Math.floor(pixelCoordinate.getX()));
    returnString.appendHtmlConstant(", ");
    returnString.append(Math.floor(pixelCoordinate.getY()));
    returnString.appendHtmlConstant("<br />");

    returnString.appendHtmlConstant("Tile Coordinate: ");
    returnString.append(Math.floor(tileCoordinate.getX()));
    returnString.appendHtmlConstant(", ");
    returnString.append(Math.floor(tileCoordinate.getY()));
    returnString.appendHtmlConstant("<br />");

    returnString.appendHtmlConstant("at Zoom Level: ");
    returnString.append(map.getZoom());
    return returnString.toSafeHtml().asString();
  }
}
