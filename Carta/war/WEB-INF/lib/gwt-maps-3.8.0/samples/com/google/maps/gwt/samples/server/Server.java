// Copyright 2011 Google Inc.  All Rights Reserved

package com.google.maps.gwt.samples.server;

import com.google.gse.GoogleServletEngine;
import com.google.gwt.gserver.GwtResourceServlet;
import com.google.net.base.IpScanners;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletException;

/**
 * Serves the sample GWT-bindings for Maps v3 demos.
 *
 * @author dcarlson@google.com (David Carlson)
 */
public class Server {

  public static void main(String[] args)
      throws MalformedURLException, IOException, ServletException {
    new Server().runServer(args);
  }

  private void runServer(String[] args) throws IOException,
      MalformedURLException, ServletException {
    com.google.common.flags.Flags.parse(args);
    GoogleServletEngine gse = new GoogleServletEngine();
    gse.configure(); // Necessary if GoogleServletEngine is constructed with no arguments
    gse.setServerType("maps_gwt_samples");
    String gwtUiModuleName = System.getProperty("gwt.ui.module.name");

    GwtResourceServlet resourceServlet =
        new GwtResourceServlet(getClass().getClassLoader(), IpScanners.ALLOW_ALL,
            gwtUiModuleName);
    resourceServlet.setDefaultResource(gwtUiModuleName + ".html");
    gse.getPathDispatcher().addServlet("/*", resourceServlet);

    gse.run();
  }
}
