package carta.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import carta.client.NotLoggedInException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import carta.client.ArtworkService;
import carta.client.Artwork;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.QueryKeys;


@SuppressWarnings("serial")
public class ArtworkServiceImpl extends RemoteServiceServlet implements ArtworkService {

	ArtworkKmlParser akp = null;

	public Integer initializeParser(int maxNbOfArtworks) {

		int nbAvailableArtworks = 0;

		try {
			akp = new ArtworkKmlParser(maxNbOfArtworks);
		}
		catch(Exception e) {

		}

		Objectify ofy = ObjectifyService.ofy();
		ObjectifyService.register(Artwork.class);

		// delete existing Artworks in datastore
		Iterable<Key<Artwork>> allKeys = ofy.load().type(Artwork.class).keys();
		ofy.delete().keys(allKeys);
		
		return akp.numberofAvailableArtworks;
	}
	
	public String updateArtworks() {
		Objectify ofy = ObjectifyService.ofy();
		ObjectifyService.register(Artwork.class);
		ObjectifyService.register(DatePersistor.class);

		Artwork artwork = null;
		try {
			artwork = akp.getNextArtwork();
		}
		catch(Exception e) {

		}
		
		ofy.save().entity(artwork);
		
		return artwork.urlLink;
	}
	
	public Integer updateArtworks(long timeElapsedThresholdInSeconds) throws NotLoggedInException {

		checkLoggedIn();

		// delete all existing Artworks in datastore
		Objectify ofy = ObjectifyService.ofy();
		ObjectifyService.register(Artwork.class);

		Artwork artwork = null;
		int numberOfArtworksSaved = 0;

		// http://stackoverflow.com/questions/1770010/how-do-i-measure-time-elapsed-in-java
		long timeBefore = System.nanoTime();

		try {
			while ((artwork = akp.getNextArtwork()) != null) {
				ofy.save().entity(artwork);
				++numberOfArtworksSaved;

				long timeElapsedSeconds = (System.nanoTime() - timeBefore) / (long)Math.pow(10, 9);

				if (timeElapsedSeconds >= timeElapsedThresholdInSeconds)
					break;			
			}
		}
		catch(Exception e) {

		}

		//-- save date when updated --//
		// http://stackoverflow.com/questions/2010284/equivalent-of-cs-datetime-now-in-java
		Iterable<Key<DatePersistor>> datePersistorKeys = ofy.load().type(DatePersistor.class).keys();
		ofy.delete().keys(datePersistorKeys);

		DatePersistor dp = new DatePersistor();
		dp.dateTime = new Date();
		ofy.save().entity(dp);
		//-- --//

		return numberOfArtworksSaved;
	}

	Objectify ofy = ObjectifyService.ofy();

	public ArtworkServiceImpl() {
		// TODO
		ObjectifyService.register(Artwork.class);
		ObjectifyService.register(DatePersistor.class);
	}
	
	
	String lastUpdated = null;

	public String updateArtworks(int maximumNumberOfArtworks) throws NotLoggedInException {

		checkLoggedIn();
		

		String result = "";
		int numberOfArtworksSaved = 0;

		try {

		// delete all existing Artworks in datastore
		Objectify ofy = ObjectifyService.ofy();
		ObjectifyService.register(Artwork.class);
		ObjectifyService.register(DatePersistor.class);
		
		Iterable<Key<Artwork>> allKeys = ofy.load().type(Artwork.class).keys();
		ofy.delete().keys(allKeys);

		// get and save new artworks
		ArtworkKmlParser akp = new ArtworkKmlParser(maximumNumberOfArtworks);

		Artwork artwork = null;
		//int numberOfArtworksSaved = 0;
		while ((artwork = akp.getNextArtwork()) != null) {
			ofy.save().entity(artwork);
			++numberOfArtworksSaved;
		}

		//-- save date when updated --//
		// http://stackoverflow.com/questions/2010284/equivalent-of-cs-datetime-now-in-java
		Iterable<Key<DatePersistor>> datePersistorKeys = ofy.load().type(DatePersistor.class).keys();
		ofy.delete().keys(datePersistorKeys);

		DatePersistor dp = new DatePersistor();
		dp.dateTime = new Date();
		ofy.save().entity(dp);
		//-- --//
		
		}
		catch(Exception e) {
			result = e.getMessage() + '\n';
		}
		
		finally {
			result += numberOfArtworksSaved + " artworks saved." + '\n';
			return result;
		}
	}
	
	public void saveArtwork(Artwork artwork) throws NotLoggedInException { 
		
		synchronized(lock) {
			
			//ofy.clear();
			
			
			
			//Objectify ofy = ObjectifyService.ofy();
			//ObjectifyService.register(Artwork.class);
			ofy.save().entity(artwork).now();
		}
	}
	
	public List<Artwork> getArtworks() throws NotLoggedInException {

		return getArtworks(Integer.MAX_VALUE);
	}
	
	public List<Artwork> getArtworks(int maximumNumber) throws NotLoggedInException {

		//Objectify ofy = ObjectifyService.ofy();
		//ObjectifyService.register(Artwork.class);
		//ObjectifyService.register(DatePersistor.class);

		List<Artwork> artworksQuery = ofy.load().type(Artwork.class).list();

		// need to copy artworksQuery to ArrayList<T>, otherwise get RPC serialization errors
		// https://code.google.com/p/objectify-appengine/issues/detail?id=135#c20
		List<Artwork> artworks = new ArrayList<Artwork>();
		artworks.addAll(artworksQuery);

		return artworks;
	}
	
	public String lastUpdated() throws NotLoggedInException {
		
		// if not in our cache
		if (lastUpdated == null) {
			//Objectify ofy = ObjectifyService.ofy();
			//ObjectifyService.register(DatePersistor.class);
			
			Result<DatePersistor> result = ofy.load().type(DatePersistor.class).first();
			DatePersistor datePersistor = result.now();	
			
			if (datePersistor != null) {
				// http://stackoverflow.com/questions/1459656/how-to-get-the-current-time-in-yyyy-mm-dd-hhmisec-millisecond-format-in-java
				// https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, ''yy 'at' HH:mm");
				
				return sdf.format(datePersistor.dateTime);
			}
			else {
				return "Artwork has not been loaded";
			}
		}
		else {
			return lastUpdated;
		}
	}
	
	Object lock = new Object();
	

	public List<Artwork> getArtworks(String queries_scsv) {

		synchronized(lock) {

			//Objectify ofy = ObjectifyService.ofy();
			//ObjectifyService.register(Artwork.class);
			String[] queries = queries_scsv.split(";");

			com.googlecode.objectify.cmd.Query<Artwork> queryChain = ofy.load().type(Artwork.class);

			for (String query : queries) {

				if (query.contains("filter:")) {
					query = query.substring(query.indexOf(":")+1);
					String[] querySplit = query.split(",");

					String condition = querySplit[0];
					String value_string = querySplit[1];

					Object value = null;
					if (isInteger(value_string))
						value = Integer.parseInt(value_string);
					else if (value_string.contains("null")) {
						value = null;
					}
					else
						value = value_string;

					char lastChar = condition.charAt(-1+condition.length());

					// filter for objects NOT CONTAINED in container
					if (lastChar == '!') {
						condition = condition.substring(0, -1+condition.length());

						QueryKeys<Artwork> keys = queryChain.filter(condition, value).keys();
						// https://code.google.com/p/objectify-appengine/wiki/Queries
						for (Key<Artwork> key : keys) {
							queryChain = queryChain.filterKey("!=", key);
						}
					}
					// else filter for objects that meet condition
					else {
						queryChain = queryChain.filter(condition, value);
					}
				}
				else if (query.contains("order:")) {
					String queryValue = query.substring(query.indexOf(":")+1);

					queryChain = queryChain.order(queryValue);
				}
				else if (query.contains("limit:")) {
					String queryValue = query.substring(query.indexOf(":")+1);
					queryChain = queryChain.limit(Integer.parseInt(queryValue));				
				}
			}

			/*List<Artwork> artworks = new ArrayList<Artwork>();
		QueryKeys<Artwork> asfd = queryChain.
		for (Key<Artwork> key : asfd)
			artworks.add(key.*/

			//List<Artwork> artworksQuery = queryChain.list();
			List<Artwork> artworks = new ArrayList<Artwork>();
			
			QueryKeys<Artwork> keys = queryChain.keys();
			for (Key key: keys) {
				artworks.add( ofy.load().type(Artwork.class).id(key.getId()).now() );
			}

			//List<Artwork> artworksQuery2 = 
			//		ofy.load().type(Artwork.class).filter("ratings.userRatings."+getUser().getUserId(), 1).list();

			// need to copy artworksQuery to ArrayList<T>, otherwise get RPC serialization errors
			// https://code.google.com/p/objectify-appengine/issues/detail?id=135#c20
			//List<Artwork> artworks = new ArrayList<Artwork>();
			//artworks.addAll(artworksQuery);



			return artworks;

		}
	}


	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}
	
	private void checkIsAdmin() throws NotLoggedInException {
		
		boolean isAdmin = UserServiceFactory.getUserService().isUserAdmin();
		
		if (!isAdmin) {
			throw new NotLoggedInException("Admin is not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	static boolean isInteger(String str)  
	{  
		try {  
			Integer i = Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}