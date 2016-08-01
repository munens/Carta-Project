package carta.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

import java.util.List;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.DirectionsPanel;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.reveregroup.gwt.facebook4gwt.Facebook;
import com.reveregroup.gwt.facebook4gwt.ShareButton;

import carta.client.SortableLabel;
import carta.server.ArtworkServiceImpl;


public class Carta implements EntryPoint {

	final int DEFAULT_NUMBER_OF_ARTWORKS_TO_SAVE = 60;
	final int DEFAULT_NUMBER_OF_ARTWORKS_TO_LOAD = 50;

	private final ArtworkServiceAsync artworkService = GWT.create(ArtworkService.class);

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access Carta.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Button skipLogin = new Button("Skip Login");
	final String LOCAL_BROWSER = "http://127.0.0.1:8888/Carta.html?gwt.codesvr=127.0.0.1:9997";
	int height = RootPanel.get().getOffsetHeight();
	final RootPanel rootPanel = RootPanel.get("artworkList").get();

	
	// starting point when web page is loaded
	public void onModuleLoad() {
		
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);

		// for debugging, redirect to local browser
		String hostPageUrl;
		if ( ! GWT.isProdMode() )
			hostPageUrl = LOCAL_BROWSER;
		else
			hostPageUrl = GWT.getHostPageBaseURL();

		// load login screen
		loginService.login(hostPageUrl, new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if (loginInfo.isLoggedIn()) {
					if (loginInfo.isAdmin) {
						loadAdminUserScreen();
					}
					else {
						loadNormalUserScreen();								
					}
				}
				// if not logged in, then load login screen
				else {
					loadLogin();
				}
			}
		});
		
		// launch Facebook API when in production mode
		if ( GWT.isProdMode() )
			Facebook.init("359835854178136");
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());

		// add handler to call when skip login is clicked
		skipLogin.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadNormalUserScreen();
			}
		});

		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		loginPanel.add(skipLogin);

		RootPanel.get("loginpanel").add(loginPanel);
	}


	
	//-- Admin user page --//
	final Button updateButton = new Button("Update Artwork Database");	
	final TextArea logger = new TextArea();
	Label lastUpdatedLabel = new Label("");
	int totalNumberParsedArtworks;
	
	void loadAdminUserScreen() {
		
		logger.setStylePrimaryName("logger");
		
		final NumberOfArtworksField requestField = new NumberOfArtworksField(DEFAULT_NUMBER_OF_ARTWORKS_TO_SAVE);
		
		// Make a new button that does something when you click it.
		updateButton.addClickHandler( new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// start updating artworks database
				updateButton.setEnabled(false);
				Utility.appendLineToTextArea(logger, "Updating...");				
				parseArtworks(requestField.getNumberOfArtworks());
			}
		});


		// load button for admin to see user page
		Button goToUserPageButton = new Button("Go To User Screen");
		goToUserPageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rootPanel.clear();
				loadNormalUserScreen();
			}
		});		

		// show time when database last updated
		updateLastUpdatedLabel();	

		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());		

		// layout
		VerticalPanel adminVerticalPanel = new VerticalPanel();
		adminVerticalPanel.add(requestField);
		adminVerticalPanel.add(updateButton);
		adminVerticalPanel.add(goToUserPageButton);
		adminVerticalPanel.add(logger);
		adminVerticalPanel.add(lastUpdatedLabel);
		adminVerticalPanel.add(signOutLink);

		// Associate the Main panel with the HTML host page.
		rootPanel.add(adminVerticalPanel);
	}
	
	void parseArtworks(int numberOfArtworks) {
		artworkService.initializeParser(numberOfArtworks, new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {

				handleError(error);
			}

			public void onSuccess(Integer numberOfAvailableArtworks) {

				totalNumberParsedArtworks = 0;
				
				long timeLimitForEachParsingCall_seconds = 5;
				parseArtworksWithTimeLimit(timeLimitForEachParsingCall_seconds);
			}
		});
	}
	
	// limits parsing to set amount of time b/c GWT App Engine shuts down RPC method call if 
	// longer than ~60 seconds
	// so make set of recursive calls to parse artworks
	void parseArtworksWithTimeLimit(final long timeLimitForEachParsingCall_seconds) {
		
		artworkService.updateArtworks(timeLimitForEachParsingCall_seconds, new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {

				handleError(error);
			}

			public void onSuccess(Integer numberOfParsedArtworks) {

				// if parsed some artworks, then call parser again to see if there are anymore
				// to be parsed
				if (numberOfParsedArtworks != 0) {
					String message = numberOfParsedArtworks + " artworks saved...";
					Utility.appendLineToTextArea(logger, message);
					totalNumberParsedArtworks += numberOfParsedArtworks;
					parseArtworksWithTimeLimit(timeLimitForEachParsingCall_seconds);
				// if no more artworks were parsed, then have parsed all artworks
				}
				else {
					String message = 
							"Finished. Total number of artworks saved: " + totalNumberParsedArtworks + ".";
					Utility.appendLineToTextArea(logger, message);

					updateLastUpdatedLabel();

					updateButton.setEnabled(true);
				}
			}
		});
	}

	void updateLastUpdatedLabel() {
		artworkService.lastUpdated(new AsyncCallback<String>() {
			public void onFailure(Throwable error) {

				handleError(error);
			}

			public void onSuccess(String dateTimeLastUpdated) {
				String notification = "Database last updated: " + dateTimeLastUpdated;

				lastUpdatedLabel.setText(notification);
			}
		});	
	}
	//-- --//

	
	
	//-- Normal user page --//
	private VerticalPanel leftScreenVerticalPanel = new VerticalPanel();
	MapWidget mainMap;
	private Grid siteGrid = new Grid(1, 2);
	final NumberOfArtworksField userRequestField = new NumberOfArtworksField(DEFAULT_NUMBER_OF_ARTWORKS_TO_LOAD);

	// tabs
	final int ARTWORKS_TAB = 0;
	final int FAVOURITES_TAB = 1;
	final int VISITED_TAB = 2;
	final int NUMBER_OF_TABS = +1 + VISITED_TAB;

	// table columns
	final int SETTINGS_COLUMN = 0;
	final int TITLE_COLUMN = 1;
	final int ARTIST_COLUMN = 2;
	final int YEAR_COLUMN = 4;
	final int UPVOTES_COLUMN = 5;
	final int DOWNVOTES_COLUMN = 6;
	final int VISITED_COLUMN = 7;

	// list box options
	final int DEFAULT = 0;
	final int MOST_LIKED = 1;
	final int MOST_VIEWED = 2;
	final int MOST_VISITED = 3;
	
	ArrayList<List<Artwork>> artworksCache;
	
	FlexTable[] tables = {
			new FlexTable(),
			new FlexTable(),
			new FlexTable()		
			};
	ListBox[] listOfThemes = {
			new ListBox(),
			new ListBox(),
			new ListBox()
			};
	TabPanel tp;
	
	ScrollPanel[] scrollPanels = new ScrollPanel[NUMBER_OF_TABS];
	List<Marker> listOfMarker = new ArrayList<Marker>();
	Object lock = new Object();
	
	int[] rowIndexOfPriorSelectedCell = new int[NUMBER_OF_TABS];
	int[] rowIndexOfMarkerSelected = new int[NUMBER_OF_TABS];
	String[] queryCache = new String [NUMBER_OF_TABS];
	
	
	private void loadNormalUserScreen() {

		// load maps with registered ID
		Maps.loadMapsApi("AIzaSyATLV2UUcy-aPRxRpFm6cubRi6ov-pxdY4", "2", false, new Runnable() {
			public void run() {
				// for debugging
				//String result = Maps.getVersion();

				loadMap();
				
				// for debugging
				/*Artwork artwork = new Artwork();
				final double startingLatitude = 49.258670;
				final double startingLongitude = -123.126783;
				Coordinates coor = new Coordinates(startingLongitude, startingLatitude, 0);
				artwork.coordinates = coor;
				loadDirections(artwork);*/
				
				loadTabPanel();
			}
		});
	}

	
	//-- Map portion of user page --//
	DirectionsPanel directionsPanel;
	DirectionResults directionResults = null;
	boolean isLoading = false;
	
	void loadMap() {

		// Open a map centered on Metrotown
		final double startingLat = 49.258670;
		final double startingLongitude = -123.126783;
		LatLng startingCoordinates = LatLng.newInstance(startingLat, startingLongitude);

		// adapted from "SimpleDirectionsDemo.java" of HelloMaps: https://github.com/emanoeltadeu/Hello
		
		//-- map layout --//
		Grid grid = new Grid(1, 2);
		grid.setWidth("100%");
		grid.getCellFormatter().setWidth(0, 0, "1000px");
		grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setWidth(0, 1, "400px");
		grid.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		mainMap = new MapWidget(startingCoordinates, 12);
		mainMap.setHeight("480px");
		grid.setWidget(0, 0, mainMap);
		directionsPanel = new DirectionsPanel();
		ScrollPanel vpDp = new ScrollPanel();
		vpDp.setHeight("480px");
		vpDp.add(directionsPanel);
		grid.setWidget(0, 1, vpDp);
		directionsPanel.setSize("100%", "100%");
		
		DecoratorPanel dp1 = new DecoratorPanel();
		dp1.add(grid);
		siteGrid.setWidth("100%");
		
		siteGrid.getCellFormatter().setVerticalAlignment(0,1,HasVerticalAlignment.ALIGN_TOP);
		siteGrid.setWidget(0, 1, dp1);
		RootPanel.get("hm-map").add(siteGrid);
		//-- --//
	}

	// fetches and displays directions from user's current location to the selected artwork
	void loadDirections(final Artwork artwork) {

		// get user's location
		com.google.gwt.geolocation.client.Geolocation geolocation = 
				com.google.gwt.geolocation.client.Geolocation.getIfSupported();

		if (geolocation != null) {
			geolocation.getCurrentPosition(new Callback<Position, PositionError>() {

				@Override
				public void onFailure(PositionError reason) {
					// TODO Auto-generated method stub

				}

				// once have user's location, get directions from it to the artwork
				@Override
				public void onSuccess(Position userLocation) {

					// user location
					LatLng userLatLng = 
							LatLng.newInstance(
									userLocation.getCoordinates().getLatitude(), 
									userLocation.getCoordinates().getLongitude());

					// artwork location
					final double startingLat = artwork.coordinates.latitude;
					final double startingLongitude = artwork.coordinates.longitude;
					final LatLng artworkLatLng = LatLng.newInstance(startingLat, startingLongitude);
					
					// create path from locations
					Waypoint[] waypoints = new Waypoint[2];
					waypoints[0] = new Waypoint(userLatLng);
					waypoints[1] = new Waypoint(artworkLatLng);
					
					// get directions for path
					DirectionQueryOptions opts = new DirectionQueryOptions(mainMap, directionsPanel);
					Directions.loadFromWaypoints(waypoints, opts, new DirectionsCallback() {

						public void onFailure(int statusCode) {
							Window.alert("Failed to load directions: Status "
									+ StatusCodes.getName(statusCode) + " " + statusCode);
						}

						public void onSuccess(DirectionResults result) {

							directionResults = result;

							GWT.log("Successfully loaded directions.", null);

							// set map focus to artwork location
							mainMap.setCenter(artworkLatLng);
							mainMap.panTo(artworkLatLng);
							mainMap.setZoomLevel(17);

							isLoading = false;
						}
					});

				}
			});
		}
	}

	// load tabs containing tables
	void loadTabPanel() {

		// initialize cache for each table
		artworksCache = new ArrayList<List<Artwork>>(NUMBER_OF_TABS);
		for (int i = 0; i < NUMBER_OF_TABS; i++)
			artworksCache.add(new ArrayList<Artwork>());

		// format map grid
		siteGrid.getCellFormatter().setWidth(0, 0, "480px");
		siteGrid.getCellFormatter().setVerticalAlignment(0,0,HasVerticalAlignment.ALIGN_TOP);
		final DecoratorPanel dp = new DecoratorPanel();
		dp.add(tp);
		
		
		// initialize tabs and tables
		tp = new TabPanel();
		tp.setWidth("500px");
		tp.setHeight("480px");
		
		FlexTable artworksTable = tables[ARTWORKS_TAB];
		FlexTable favouriteArtworksTable = tables[FAVOURITES_TAB];
		FlexTable visitedTable = tables[VISITED_TAB];

		artworksTable.setStyleName("artworksTable");
		favouriteArtworksTable.setStyleName("artworksTable");
		visitedTable.setStyleName("artworksTable");

		addTab(tp, ARTWORKS_TAB, artworksTable, "All artworks");
		addTab(tp, FAVOURITES_TAB, favouriteArtworksTable, "Favourites");
		addTab(tp, VISITED_TAB, visitedTable, "Visited");		

		tp.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				final int tabId = event.getSelectedItem();
				
				refreshTab(listOfThemes[tabId]);
			}
		});
		tp.selectTab(ARTWORKS_TAB);

		siteGrid.setWidget(0, 0, tp);
		leftScreenVerticalPanel.add(siteGrid);

		updateLastUpdatedLabel();
		
		// each time user selects a new tab, want the table for that tab to be updated
		userRequestField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				
				// http://stackoverflow.com/a/9397697
				int tabIndex = tp.getTabBar().getSelectedTab();

				refreshTab(listOfThemes[tabIndex]);
			}
		});		
		
		// add label showing when databaes last updated
		leftScreenVerticalPanel.add(lastUpdatedLabel);

		// if user is logged in, then show signout link
		// else a guest user is viewing the page, so can remove skip login button
		if (loginInfo.isLoggedIn()) {
			signOutLink.setHref(loginInfo.getLogoutUrl());
			leftScreenVerticalPanel.add(signOutLink);
		}
		else {
			loginPanel.remove(skipLogin);
		}

		// Associate the Main panel with the HTML host page.
		RootPanel.get("artworkList").add(leftScreenVerticalPanel);
	}
	
	// initializes selected tab with table
	void addTab(TabPanel tabPanel, final int tabIndex,  final FlexTable table, String title) {
		int ri = table.getRowCount();

		// make a list box of themes for the given tab
		final ListBox themes = listOfThemes[tabIndex];
		themes.setWidth("100px");
		themes.addItem("Default");
		themes.addItem("Most Liked");
		themes.addItem("Most Viewed");
		themes.addItem("Most Visited");

		// sets the number of items that are visible
		themes.setVisibleItemCount(0);

		//-- queries --//
		final String minNumberOfUpvotes = "filter:ratings.numberOfUpvotes >,0";
		final String minNumberOfViews = "filter:views.numberOfViews >,0";
		final String minNumberOfVisits = "filter:visits.numberOfVisits >,0";
		final String descendByUpvotes = "order:-ratings.numberOfUpvotes";
		final String descendByViews = "order:-views.numberOfViews";
		final String descendByVisits = "order:-visits.numberOfVisits";		

		final String baseDefaultQuery = "";
		final String baseMostLikedQuery = minNumberOfUpvotes + ";" + descendByUpvotes;
		final String baseMostViewedQuery = minNumberOfViews + ";" + descendByViews;
		final String baseMostVisitedQuery = minNumberOfVisits + ";" + descendByVisits;

		final String mainDefault = baseDefaultQuery;
		final String mainMostLikedQuery = baseMostLikedQuery;
		final String mainMostViewedQuery = baseMostViewedQuery;
		final String mainMostVisitedQuery = baseMostVisitedQuery;
		
		
		String userId = loginInfo.userId;
		
		final String usersFavourites = "filter:ratings.userRatings." + userId + ",1";  // http://stackoverflow.com/questions/25437341/gae-w-objectify-can-you-query-a-hashmap
		final String favouritesDefaultQuery = baseDefaultQuery+";"+usersFavourites;
		final String favouritesMostLikedQuery = baseMostLikedQuery+";"+usersFavourites;
		final String favouritesMostViewedQuery = baseMostViewedQuery+";"+usersFavourites;
		final String favouritesMostVisitedQuery = baseMostVisitedQuery+";"+usersFavourites;
		
		final String yesVisits = "filter:visits.users," + userId;
		final String visitsShuffle = baseDefaultQuery+";"+yesVisits;
		final String visitsMostLikedQuery = baseMostLikedQuery+";"+yesVisits;
		final String visitsMostViewedQuery = baseMostViewedQuery+";"+yesVisits;
		final String visitsMostVisitedQuery = baseMostVisitedQuery+";"+yesVisits;
		//-- --//
		
		//-- fetching queries for given tab based on theme selected --//
		switch (tabIndex) {

		case ARTWORKS_TAB:
			themes.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int themeIndex = themes.getSelectedIndex();

					String query = null;

					switch (themeIndex) {
					case DEFAULT: {
						query = mainDefault;
						break;
					}
					case MOST_LIKED: {
						query = mainMostLikedQuery;
						break;
					}
					case MOST_VIEWED: {
						query = mainMostViewedQuery;
						break;
					}
					case MOST_VISITED: {
						query = mainMostVisitedQuery;
						break;
					}
					}

					query += ";" + "limit:" + userRequestField.getNumberOfArtworks();
					queryCache[tabIndex] = query;
					getArtworks(table, query, tabIndex);

				}
			});
			break;

		case FAVOURITES_TAB:
			themes.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int themeIndex = themes.getSelectedIndex();

					String query = null;

					switch (themeIndex) {
					case DEFAULT: {
						query = favouritesDefaultQuery;
						break;
					}
					case MOST_LIKED: {
						query = favouritesMostLikedQuery;
						break;
					}
					case MOST_VIEWED: {
						query = favouritesMostViewedQuery;
						break;
					}
					case MOST_VISITED: {
						query = favouritesMostVisitedQuery;
						break;
					}
					}

					query += ";" + "limit:" + userRequestField.getNumberOfArtworks();
					queryCache[tabIndex] = query;
					getArtworks(table, query, tabIndex);  

				}
			});
			break;

		case VISITED_TAB:
			themes.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int themeIndex = themes.getSelectedIndex();

					String query = null;

					switch (themeIndex) {
					case DEFAULT: {
						query = visitsShuffle;
						break;
					}
					case MOST_LIKED: {
						query = visitsMostLikedQuery;
						break;
					}
					case MOST_VIEWED: {
						query = visitsMostViewedQuery;
						break;
					}
					case MOST_VISITED: {
						query = visitsMostVisitedQuery;
						break;
					}
					}

					query += ";" + "limit:" + userRequestField.getNumberOfArtworks();
					queryCache[tabIndex] = query;
					getArtworks(table, query, tabIndex);  
					//displayArtworks(table, artworksCache.get(tabIndex), tabIndex);
				}
			});
			break;
		}
		//-- --//


		// set list box to default to the first theme
		themes.setItemSelected(DEFAULT, true);

		//-- create sortable columns for table --//
		final SortableLabelGroup slf = new SortableLabelGroup();
		
		final SortableLabel titleLabel = slf.createLabel("Title", SortableLabel.SORT_ASCEND);
		titleLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(titleLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.title.compareTo(o2.title);
					}
				});
			}
		});

		
		final SortableLabel artistLabel = slf.createLabel("Artist", SortableLabel.SORT_ASCEND);
		artistLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(artistLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.artist.compareTo(o2.artist);
					}
				});
			}
		});
		
		
		final SortableLabel yearLabel = slf.createLabel("Year", SortableLabel.SORT_ASCEND);
		yearLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(yearLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.yearInstalled.compareTo(o2.yearInstalled);
					}
				});
			}
		});
		
		
		final SortableLabel upvotesLabel = slf.createLabel("Upvotes", SortableLabel.SORT_DESCEND);
		upvotesLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(upvotesLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.ratings.numberOfUpvotes - o2.ratings.numberOfUpvotes;
					}
				});
			}
		});
		
		
		final SortableLabel downvotesLabel = slf.createLabel("Downvotes", SortableLabel.SORT_DESCEND);
		downvotesLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(downvotesLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.ratings.numberOfDownvotes - o2.ratings.numberOfDownvotes;
					}
				});
			}
		});
		
		
		final SortableLabel visitedLabel = slf.createLabel("Visited", SortableLabel.SORT_DESCEND);
		visitedLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				addSortLogic(visitedLabel, tabIndex, table, new Comparator<Artwork>() {
					@Override
					public int compare(Artwork o1, Artwork o2) {
						return o1.visits.numberOfVisits - o2.visits.numberOfVisits;
					}
				});
			}
		});		
		// -- //		

		
		//-- layout table in given tab --//
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add(themes);

		FlexTable headingsTable = new FlexTable();		
		headingsTable.setWidget(ri, SETTINGS_COLUMN, vp2);
		headingsTable.setWidget(ri, TITLE_COLUMN, titleLabel);
		headingsTable.setWidget(ri, ARTIST_COLUMN, artistLabel);		
		headingsTable.setWidget(ri, YEAR_COLUMN, yearLabel);
		headingsTable.setText(ri, VISITED_COLUMN, "Visited");	
		headingsTable.getCellFormatter().setWidth(ri, TITLE_COLUMN, "127px");
		headingsTable.getCellFormatter().setWidth(ri, ARTIST_COLUMN, "83px");
		headingsTable.getCellFormatter().setWidth(ri, YEAR_COLUMN, "110px");

		scrollPanels[tabIndex] = new ScrollPanel();
		ScrollPanel verticalScrollPanel = scrollPanels[tabIndex];
		verticalScrollPanel.add(table);
		verticalScrollPanel.setHeight("429px");
		verticalScrollPanel.setWidth("500px");

		// combine headings and scroll panel to create table
		VerticalPanel vp = new VerticalPanel();
		vp.add(headingsTable);
		vp.add(verticalScrollPanel);

		tabPanel.add(vp, title);
		//-- --//
	}

	void refreshTab(ListBox themes) {
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), themes);

	}
	
	void addSortLogic (SortableLabel sl, int tabIndex, FlexTable table, Comparator<Artwork> cptr) {

		List<Artwork> list = artworksCache.get(tabIndex);

		if (!sl.isUsed()) {
			if (sl.toggle() == SortableLabel.SORT_ASCEND) {
				Collections.sort(list, cptr);			
			}
			else {
				Comparator<Artwork> cptr_reverse = Collections.reverseOrder(cptr);
				Collections.sort(list, cptr_reverse);
			}
		}
		else {
			sl.toggle();
			Collections.reverse(list);
		}
		
		displayArtworks(table, list, tabIndex);
	}

	
	// load artworks from database
	void getArtworks(final FlexTable table, final String query, final int tabIndex) {

		artworkService.getArtworks(query, new AsyncCallback<List<Artwork>>() {

			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(final List<Artwork> artworks) {
				
				// enable table to respond to clicks on it
				table.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						
						try {
							Cell cell = table.getCellForEvent(event);

							// if click event from user clicking on table
							if (cell != null) {

								int ri = cell.getRowIndex();

								// if clicked on artwork different from the most recent clicked artwork
								if (ri != rowIndexOfPriorSelectedCell[tabIndex]) {

									// highlight selected row
									HTMLTable.RowFormatter rf = table.getRowFormatter();
									String rowTableSelect = "rowSelect";
									rf.removeStyleName(rowIndexOfPriorSelectedCell[tabIndex], rowTableSelect);
									rf.addStyleName(ri, rowTableSelect);

									Artwork artwork = artworks.get(ri);
									
									// increment number of views for artwork
									artwork.views.addView(loginInfo.userId);
									updateArtworkInDatabase(artwork);
									
									// https://groups.google.com/forum/#!topic/gwt-google-apis/LBs4W7eX4vc
									// use lock to prevent simultaneous calls to google maps directions which can result in errors
									synchronized(lock) {
										if (isLoading) {
											// then skip
										} 
										else {
											isLoading = true;									
										}
									}

									// focus map on selected artwork
									final LatLng artLatLong = LatLng.newInstance(artwork.coordinates.latitude, artwork.coordinates.longitude);
									
									mainMap.setCenter(artLatLong);
									mainMap.panTo(artLatLong);
									mainMap.setZoomLevel(17);

									mainMap.getInfoWindow().close();
									mainMap.getInfoWindow().open(listOfMarker.get(ri), infoWindowDisplay(artwork));

									// remember the most recent row selected
									rowIndexOfPriorSelectedCell[tabIndex] = ri;
								}
							}
							// else click event has been generated programmatically from map marker
							else {
								
								// unhighlight previously selected row
								HTMLTable.RowFormatter rf = table.getRowFormatter();
								String rowTableSelect = "rowSelect";
								rf.removeStyleName(rowIndexOfPriorSelectedCell[tabIndex], rowTableSelect);

								// get index from map marker selected
								int ri = rowIndexOfMarkerSelected[tabIndex];
								
								// for each row, image height is 100, so can calculate rough
								// scroll position offset
								scrollPanels[tabIndex].setVerticalScrollPosition(50+100*ri);

								// set row to be highlighted
								rf.addStyleName(ri, rowTableSelect);
								
								// remember the most recent row selected
								rowIndexOfPriorSelectedCell[tabIndex] = ri;
							}
						}
						catch (Exception e) {
							System.out.println( e.getMessage() );
						}
					}
				});

				displayArtworks(table, artworks, tabIndex);
			}
		});
	}
	
	
	public InfoWindowContent infoWindowDisplay(final Artwork artwork) {
		final VerticalPanel verticalPanel = new VerticalPanel();
		final HorizontalPanel buttonsPanel = new HorizontalPanel();
		
		ShareButton fb = new ShareButton ("http://1-dot-carta-live.appspot.com/", "I just learned about " + artwork.title + " from Carta!", null, "Share " + artwork.title + " on Facebook!");
		verticalPanel.add(fb);		
		
		final Button more = new Button("More");
		final Button directions = new Button("Directions");

		verticalPanel.add(new HTML("<div style=\"line-height: normal; overflow: auto;\">" + "<p align= \"left\">" + "Name: " + artwork.title  + "<br />" + "Artist: " + artwork.artist  + "<br />" + "Year: " + artwork.yearInstalled  + /*"<br />" + artwork.description + "<br />" + */ "</p>" + "</div>"));
		buttonsPanel.add(more);
		buttonsPanel.add(directions);
		verticalPanel.add(buttonsPanel);
		
		more.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				popupDisplay(artwork);
				
			}
			
		});
		directions.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				if (directionResults != null)
					directionResults.clear();	
				
				loadDirections(artwork);	
			}
			
		});
		
		InfoWindowContent iwc = new InfoWindowContent(verticalPanel);
		
		return iwc;
	}
	
	public void popupDisplay(Artwork artwork){	
		final PopUp db = new PopUp();
		final VerticalPanel vp = new VerticalPanel();
		final ScrollPanel scroll = new ScrollPanel();
		final Button b = new Button("Close");
		final Image img = new Image(artwork.imgUrl);
		db.setGlassEnabled(true);
		vp.add(img);
		vp.add(new HTML("<p>" + "<b>" + "Name: " + "</b>" 
				+ artwork.title  + "<br />" + "<b>" + "Artist: " + "</b>" + artwork.artist  
				+ "<br />" + "<b>" + "Year: " + "</b>" + artwork.yearInstalled  + "</p>" + "<p>" + "<b>" + "Description: "  + "</b>" + artwork.description + "</p>" + "<p>" + "<b>" + "Artist's Statement: " + "</b>" + artwork.artistStatement + "</p>" + "<p>" + "<b>" + "Neighbourhood: "  + "</b>" + artwork.neighbourhood + "<br />" + "<b>" + "Site Name: " + "</b>" + artwork.siteName + "<br />" + "<b>" + "Address: " + "</b>" + artwork.address + "<br />" + "<b>" + "Location On Site: " + "</b>" + artwork.locationOnSite + "</p>" + "<p>" + "<b>" + "Source Program: " + "</b>" + artwork.sourceProgram + "</p>"+ "<p>" + "<b>" + "Year Installed: " + "</b>" + artwork.yearInstalled + "</p>" + "<p>" + "<b>" + "Primary Materials: " + "</b>" + artwork.primaryMaterials + "</p>" + "<p>" + "<b>" + "Type: "  + "</b>" + artwork.type + "</p>" + "<p>" + "<b>" + "Status: "  + "</b>" + artwork.status + "</p>" + "<p>" + "<b>" + "Ownership: "  + "</b>" + artwork.ownership + "</p>" + "<p>" + "<b>" + "Sponsoring Organization: "  + "</b>" + artwork.sponsoringOrganization + "</p>" + "<p>" + "<b>" + "Link to Project Page: "  + "</b>" + artwork.linkToProjectPage + "</p>" + "</div>"));
		vp.add(b);
		scroll.add(vp);
		scroll.setPixelSize(600, 400);
		db.add(scroll);
		db.setPixelSize(600, 400);
		//db.setPopupPosition(300, 100);
		db.center();
		db.show();
		b.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				db.hide();
				//mainMap.getInfoWindow().close();	
			}
		});		
	}


	private void displayArtworks(
			final FlexTable artworksTableInstance, final List<Artwork> artworks, final int tabIndex) {

		artworksCache.set(tabIndex, artworks);

		artworksTableInstance.removeAllRows();
		mainMap.clearOverlays();
		listOfMarker.clear();

		// http://stackoverflow.com/a/16947034/3781601
		// use GWT scheduler to break up loading of table into increments so that UI remains reasonably responsive
		final int size = artworks.size();
		Scheduler.get().scheduleIncremental(new RepeatingCommand() {
			int rowCounter  = 0;

			@Override
			public boolean execute() {

				if (rowCounter >= size)
					return false;

				// do some work
				final Artwork artwork = artworks.get(rowCounter);
				displayArtwork(artworksTableInstance, artwork, tabIndex);

				rowCounter++;
				
				return true;
			}
		});
	}


	private void displayArtwork(final FlexTable table, final Artwork artwork, final int tabIndex) {

		final int ri = table.getRowCount();
		int ci = -1;  // column index
		table.setWidget(ri, ++ci, createImage(artwork.imgUrl, 100, 100));
		
		table.getFlexCellFormatter().setHeight(ri, 0, "100px");
		table.getFlexCellFormatter().setWidth(ri, 0, "100px");

		table.setText(ri, ++ci, artwork.title);

		table.setText(ri, ++ci, artwork.artist);

		table.setText(ri, ++ci, artwork.yearInstalled);


		// format row colours
		// http://www.java2s.com/Code/Java/GWT/FlexTablewithrowstyle.htm
		HTMLTable.RowFormatter rf = table.getRowFormatter();
		// odd row
		if ((ri % 2) != 0) {
			rf.addStyleName(ri, "rowOdd");
		}
		// even row
		else {
			rf.addStyleName(ri, "rowEven");
		}

		final LatLng latLong = LatLng.newInstance(artwork.coordinates.latitude, artwork.coordinates.longitude);

		final Marker marker = new Marker(latLong);
		mainMap.addOverlay(marker);
		final InfoWindowContent iwc = infoWindowDisplay(artwork);
		listOfMarker.add(marker);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			
			@Override
			public void onClick(MarkerClickEvent event) {

				// open info window for selected marker
				mainMap.getInfoWindow().close();
				mainMap.getInfoWindow().open(marker, iwc);
				
				// save the row index of the marker selected
				rowIndexOfMarkerSelected[tabIndex] = ri;
				
				// fire event to select artwork from table that corresponds with selected marker				
				NativeEvent ne = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
				DomEvent.fireNativeEvent(ne, table);
			}
		});


		//-- if user is logged in, then add voting buttons --//
		if (loginInfo.isLoggedIn()) {
			// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/ToggleButton.html

			Image ua = createImage("images/up-arrow.png", 50, 50);
			Image uap = createImage("images/up-arrow-pressed.png", 50, 50);
			Image da = createImage("images/down-arrow.png", 50, 50);
			Image dap = createImage("images/down-arrow-pressed.png", 50, 50);

			final ToggleButton upvoteToggleButton = new ToggleButton(ua, uap);
			upvoteToggleButton.setStyleName("clearButton");
			final ToggleButton downvoteToggleButton = new ToggleButton(da, dap);
			downvoteToggleButton.setStyleName("clearButton");

			final int vote = artwork.ratings.getVote(loginInfo.userId);

			//-- add favourites button --//

			switch (vote) {
			case Ratings.UPVOTE:
				upvoteToggleButton.setDown(true);
				break;
			case Ratings.DOWNVOTE:
				downvoteToggleButton.setDown(true);
				break;
			default:
				// Do nothing
			}			

			final Label upvoteLabel = new Label();
			upvoteLabel.setStyleName("voteLabel");
			final Label downvoteLabel = new Label();
			downvoteLabel.setStyleName("voteLabel");

			// implement upvote button
			VerticalPanel upvoteVp = new VerticalPanel();
			upvoteLabel.setText(artwork.ratings.numberOfUpvotes.toString());
			upvoteVp.add(upvoteToggleButton);
			upvoteVp.add(upvoteLabel);
			table.setWidget(ri, ++ci, upvoteVp);

			upvoteToggleButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {

					Cell cell = table.getCellForEvent(event);
					
					if (cell != null) {

						
						if (upvoteToggleButton.isDown()) {
	
							artwork.ratings.addUserUpvote(loginInfo.userId);

	
							if (downvoteToggleButton.isDown()) {
								downvoteToggleButton.setDown(false);
								downvoteLabel.setText(artwork.ratings.numberOfDownvotes.toString());
							}
						}

						else {
							artwork.ratings.removeUserVote(loginInfo.userId);
							
				
						}

						updateArtworkInDatabase(artwork);

						upvoteLabel.setText(artwork.ratings.numberOfUpvotes.toString());

					}
				}
			});

			// implement downvote button
			VerticalPanel downvoteVp = new VerticalPanel();
			downvoteLabel.setText(artwork.ratings.numberOfDownvotes.toString());
			downvoteVp.add(downvoteToggleButton);
			downvoteVp.add(downvoteLabel);
			table.setWidget(ri, ++ci, downvoteVp);

			downvoteToggleButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					

					
					if (downvoteToggleButton.isDown()) {

						artwork.ratings.addUserDownvote(loginInfo.userId);

						if (upvoteToggleButton.isDown()) {
							upvoteToggleButton.setDown(false);
							upvoteLabel.setText(artwork.ratings.numberOfUpvotes.toString());
						}
					}
					else { // then downToggleButton is up
						artwork.ratings.removeUserVote(loginInfo.userId);
					}
					
					updateArtworkInDatabase(artwork);

					downvoteLabel.setText(artwork.ratings.numberOfDownvotes.toString());


				}
			});
			//-- --//
			
			//-- add visited checkbox --//
			final CheckBox checkBox = new CheckBox();
			boolean hasVisited = artwork.visits.hasVisited(loginInfo.userId);

			checkBox.setValue(hasVisited);


			final Label visitsLabel = new Label();
			VerticalPanel visitsVerticalPanel = new VerticalPanel();
			visitsVerticalPanel.add(checkBox);
			//visitsVerticalPanel.add(visitsLabel);
			table.setWidget(ri, ++ci, visitsVerticalPanel);
			visitsLabel.setText(artwork.visits.numberOfVisits.toString());

			// Hook up a handler to find out when it's clicked.
			checkBox.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					Cell cell = table.getCellForEvent(event);

					if (cell != null) {


						// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/CheckBox.html

						boolean isChecked = checkBox.getValue();
						
						if (isChecked) {
							
							artwork.visits.markAsVisited(loginInfo.userId);
						}
						// if is unchecked
						else {
							
							artwork.visits.markAsUnvisited(loginInfo.userId);
						}
						
						updateArtworkInDatabase(artwork);
						
						visitsLabel.setText(artwork.visits.numberOfVisits.toString());
					}
				}
			});
			//-- --//
		}
	}

	void updateArtworkInDatabase(Artwork artwork) {
		artworkService.saveArtwork(artwork, new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				// don't need to do anything

			}

			public void onFailure(Throwable error) {
				handleError(error);
			}
		});
	}

	Image createImage(String url, int width, int height){

		Image image = new Image();
		image.setUrl(url);
		image.setPixelSize(width, height);

		return image;
	}

	// -- --//

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}