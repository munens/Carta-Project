package carta;

import carta.client.Artwork;
import carta.server.ArtworkKmlParser;
import carta.server.ArtworkUrlParser;

import com.google.gwt.junit.client.GWTTestCase;

// http://www.gwtproject.org/doc/latest/tutorial/JUnit.html
public class ParserTest extends GWTTestCase {
	
	//-- tests for URL parser --//
	
	public void testCorrectlyParsesArtworkUrl() {
		String url = 
				"https://app.vancouver.ca/PublicArt_Net/ArtworkDetails.aspx?ArtworkID=385&amp";
		
		ArtworkUrlParser aup = null;
		try {
			aup = new ArtworkUrlParser(url);
		}
		catch(Exception e) {

		}

		assertTrue(
				aup.getTitle().equals("Garland"));

		assertTrue(
				aup.getImgUrl().equals("https://app.vancouver.ca/publicart_net/Img/jpg/385.jpg"));

		assertTrue(
				aup.getArtist().equals("Douglas Senft"));

		assertTrue(
				aup.getDescription().equals("Garland is a green steel archway 16' high that spans the sidewalk between the community centre and the parking lot. The arch is adorned with a cluster of forged steel oak leaves each about 3' long. It contains lights that illuminate the path."));

		assertTrue(
				aup.getArtistStatement().equals("\"garland is intended as a lyrical work creating a memorable image of arrival, passage and place. The imagery for this gateway sculpture is derived from two sources. First is the image of the oak leaf, in reference to Oak Park. The image or form of the arch reflects the arc lines and shapes of the concrete forms that are part of the walkway itself.\""));
		
		assertTrue(
				aup.getLocation_Neighbourhood().equals("Marpole"));
		
		assertTrue(
				aup.getLocation_SiteName().equals("Marpole-Oakridge Community Centre (Oak Park)"));
		
		assertTrue(
				aup.getLocation_Address().equals("990 West 59th Avenue"));
		
		assertTrue(
				aup.getLocation_LocationOnSite().equals("NW corner of the park, off 59th Avenue"));
		
		assertTrue(
				aup.getSourceProgram().equals("Civic"));
		
		assertTrue(
				aup.getYearInstalled().equals("2002"));
		
		assertTrue(
				aup.getPrimaryMaterials().equals("6-inch rolled steel pipe; cut and forged steel leaves"));
		
		assertTrue(
				aup.getType().equals("Gateway"));
		
		assertTrue(
				aup.getStatus().equals("Existing"));
		
		assertTrue(
				aup.getOwnership().equals("City of Vancouver"));
		
		assertTrue(
				aup.getSponsoringOrganization().equals(""));
		
		assertTrue(
				aup.getLinkToProjectPage().equals(""));		
	}
	
	public void testHandlesParsingOfIncorrectArtworkUrl() {
		String url = "Incorrect URL";
		
		try {
			ArtworkUrlParser aup = new ArtworkUrlParser(url);
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	//-- --//
	
	
	//-- tests for KML parser --//
	
	public void testParseReturnsCorrectArtwork() {
		String filename = "kml_public_art.zip";
		String baseDirectory = System.getProperty("user.dir");
		String filepath = baseDirectory + "/test/carta/" + filename;
		
		Artwork artwork = null;
		
		try {
			ArtworkKmlParser akp = new ArtworkKmlParser(filepath, Integer.MAX_VALUE);

			int counter = 0;
			
			// testing particular artwork in KML file
			int countOfArtworkToTest = 3;
			
			while ((artwork = akp.getNextArtwork()) != null) {
				counter++;
				
				if (counter == countOfArtworkToTest)
					break;
			}			
		}
		catch(Exception e) {

		}
		
		assertTrue(
				artwork.title.equals("Garland"));
		
		int isEqual = 0;
		assertTrue(
				isEqual == Double.compare(artwork.coordinates.longitude, -123.128093));
		
		assertTrue(
				isEqual == Double.compare(artwork.coordinates.latitude, 49.217006));
		
		assertTrue(
				isEqual == Double.compare(artwork.coordinates.altitude, 0));

		assertTrue(
				artwork.imgUrl.equals("https://app.vancouver.ca/publicart_net/Img/jpg/385.jpg"));

		assertTrue(
				artwork.artist.equals("Douglas Senft"));

		assertTrue(
				artwork.description.equals("Garland is a green steel archway 16' high that spans the sidewalk between the community centre and the parking lot. The arch is adorned with a cluster of forged steel oak leaves each about 3' long. It contains lights that illuminate the path."));

		assertTrue(
				artwork.artistStatement.equals("\"garland is intended as a lyrical work creating a memorable image of arrival, passage and place. The imagery for this gateway sculpture is derived from two sources. First is the image of the oak leaf, in reference to Oak Park. The image or form of the arch reflects the arc lines and shapes of the concrete forms that are part of the walkway itself.\""));
		
		assertTrue(
				artwork.neighbourhood.equals("Marpole"));
		
		assertTrue(
				artwork.siteName.equals("Marpole-Oakridge Community Centre (Oak Park)"));
		
		assertTrue(
				artwork.address.equals("990 West 59th Avenue"));
		
		assertTrue(
				artwork.locationOnSite.equals("NW corner of the park, off 59th Avenue"));
		
		assertTrue(
				artwork.sourceProgram.equals("Civic"));
		
		assertTrue(
				artwork.yearInstalled.equals("2002"));
		
		assertTrue(
				artwork.primaryMaterials.equals("6-inch rolled steel pipe; cut and forged steel leaves"));
		
		assertTrue(
				artwork.type.equals("Gateway"));
		
		assertTrue(
				artwork.status.equals("Existing"));
		
		assertTrue(
				artwork.ownership.equals("City of Vancouver"));
		
		assertTrue(
				artwork.sponsoringOrganization.equals(""));
		
		assertTrue(
				artwork.linkToProjectPage.equals(""));	
	}
	
	public void testParsesAllArtworks() { 
		String filename = "kml_public_art.zip";
		String baseDirectory = System.getProperty("user.dir");
		String filepath = baseDirectory + "/test/carta/" + filename;
		
		int totalNumberOfArtworks = 331;

		try {
			ArtworkKmlParser akp = new ArtworkKmlParser(filepath, Integer.MAX_VALUE);

			assertTrue(akp.numberofAvailableArtworks == totalNumberOfArtworks);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}	
	
	public void testHandlesRequestOfFixedNumberOfArtworks() {
		
		int requestedNonzerNumberofArtworks = 5 ;
		int counter = 0;
		
		try {
			ArtworkKmlParser akp = new ArtworkKmlParser(requestedNonzerNumberofArtworks);

			while (akp.getNextArtwork() != null) {
				counter++;
			}
		}
		catch(Exception e) {

		}

		assertTrue(counter == requestedNonzerNumberofArtworks);
	}

	public void testHandlesRequestOfZeroArtworks() {

		int nbOfArtworks = 0 ;
		int counter = 0;

		try {
			ArtworkKmlParser akp = new ArtworkKmlParser(nbOfArtworks);

			while (akp.getNextArtwork() != null) {
				counter++;
			}
		}
		catch(Exception e) {

		}

		assertTrue(counter == nbOfArtworks);
	}
	
	public void testHandlesIncorrectArtworkKML() {

		String kmlPath = "Incorrect KML";
		
		try {
			ArtworkKmlParser akp = new ArtworkKmlParser(kmlPath, Integer.MAX_VALUE);
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	//-- --//

	
	@Override
	public String getModuleName() {
		return null;
	}
}