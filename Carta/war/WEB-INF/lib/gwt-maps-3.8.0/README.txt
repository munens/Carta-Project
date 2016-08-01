Google Maps V3 Bindings for GWT

This software is distributed under the Apache 2.0 license.
See the file COPYING for more details.

For more information on this project, see:

  http://code.google.com/p/gwt-google-apis

Contents:
  gwt-maps-<version>.jar  Add this library to your GWT project
  javadoc/                Javadoc generated from the API
  samples/                Short examples that show how to use the API.

For support, try the gwt-google-apis Google group.
If you think you have found a bug, see the online Issue tracker

==================================================================

Instructions for using Eclipse

These instructions require
  GWT 2.4 or higher          http://code.google.com/webtoolkit
  Google Plugin for Eclipse  http://code.google.com/eclipse
  Java JDK 1.5 or higher     http://java.sun.com/

---------- General Instructions  ---------------------------------

To work with the API in Eclipse, First create a 'Web Application' project
using the Google Plugin for Eclipse with Google Web Toolkit enabled.

This should create a new directory in your workspace for the project and
a war/WEB-INF/lib directory.  The gwt-maps.jar file need to be copied there
then refresh your project in Eclipse.

In Eclipse, add the gwt-maps.jar file to the build path, for example:
Project
  -> Properties
    -> Java Build Path
      -> Libraries
        -> Select gwt-maps.jar

Next, open to your entry point's .gwt.xml file and add the line:

  <inherits name="com.google.maps.gwt.GoogleMaps" />


Finally, you need to do one of two things to load the API on
application startup:

  1) Update your host file to include the following script tag:

  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=SENSOR_TRUE_OR_FALSE"></script>

  Where the sensor value is either "true" or "false" depending on if you
  are hooked up to a location sensing device.  See the Maps API documentation
  at http://developers.google.com/maps/documentation/javascript/tutorial


  2) Load the API through the AjaxLoader library.  See the sample
  "MapsSimpleAjaxLoader.java" for an example.  The AjaxLoader library is
  bundled with the gwt-maps.jar distribution.  You will need to add
  the following line to your gwt module definition:

  <inherits name="com.google.gwt.ajaxloader.AjaxLoader" />


---------- Running a Sample ----------------------------------------

These instructions describe how to take one of the distributed samples
and run them in Eclipse using GWT's DevMode.  This assumes you are already
configured Eclipse with the Google Plugin for Eclipse and have run
through the GWT tutorials before.


Start by create a new Web Application Project. Uncheck the checkbox box to
install template code so that you start with an empty project.

Recursively copy in the sample code into the 'src' directory of your new
project.  Your src tree should now have the directories
com/google/maps/gwt/samples/...

Add the gwt-maps.jar to war/WEB-INF/lib and add it to the Java build path
of your project as mentioned in "General Instructions" above.

Choose the sample you'd like to run.  We'll use MapGeolocation.java as an
example.

Find MapGeolocoation.java in the tree, then find the corresponding .html file
for it in one of the 'public' directories.  In this case its
'map_geolocation.html'.

Copy the .html file down into the 'war' directory.

Open up the .html file and look at the module name.  To be compatible
with the Google Plugin for Eclipse, you'll need to edit the src attribute
'map_geolocation.nocache.js' and prepend the module name:

      <script type="text/javascript" language="javascript"
       src="map_geolocation/map_geolocation.nocache.js"></script>

Next, you'll need to create a GWT module

  1) Create a new GWT module using:
   File
     --> New
       --> Other...
         --> Google Web Toolkit
           --> Module

  2) Choose the module name to match the name in the .html file. In our
     example, that would be map_geolocation.  The package should be one
     level up from the location of the .java file:

     Package: com.google.maps.gwt.samples.basics
     Module name: map_geolocation
     Inherited Modules: com.google.gwt.user.User
                        com.google.maps.gwt.GoogleMaps

  3) Open the new module and add make the following modifications:

     Add a 'rename-to' attribute to the module start tag

     <module rename-to="map_geolocation" />


     Add in the .java sample as an entry point

     <entry-point
      class="com.google.maps.gwt.samples.basics.client.MapGeolocation" />


Finally, you are ready to launch the sample!  Right click on the
new map_geolocation.gwt.xml module and run the sample:

  Run As
   --> Web Application

Choose the web page you copied down (map_gelocation.html)  if prompted.
