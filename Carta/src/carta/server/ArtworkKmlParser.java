package carta.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import carta.client.Artwork;
import carta.client.Coordinates;

public class ArtworkKmlParser {

	// City Of Vancouver's Public art data package
	static final String urlPath = "http://data.vancouver.ca/download/kml/kml_public_art.zip";
	// files to be read from zip file
	static final String KMZfilename = "public_art_individual_locations.kmz";
	static final String KMLfilename = "public_art_individual_locations.kml";

	public int numberofAvailableArtworks = 0;
	int pi = -1; // parsing index
	List<Element> listOfExtendedData;
	List<Element> listOfCoordinates;
	
	public ArtworkKmlParser(int maximumNumberOfArtworksToParse) throws Exception {
		
		// load remote file into memory for reading
		URL url = new URL(urlPath);
		InputStream uis = url.openStream();

		parseInputStream(uis, maximumNumberOfArtworksToParse);
	}
	
	public ArtworkKmlParser(String filepath, int maximumNumberOfArtworksToParse) throws Exception {
		
		// load local file into memory for reading
		FileInputStream fis = new FileInputStream(filepath);

		parseInputStream(fis, maximumNumberOfArtworksToParse);
	}

	// parse and return at most the requested number of artworks
	void parseInputStream(InputStream uis, int maximumNumberOfArtworksToParse) {

		try {
			// load remote file as ZIP file
			ZipInputStream zis_ZIP = new ZipInputStream(uis);
			// find position of KMZ file in ZIP file
			ZipInputStream zis_KMZ = childZipInputStreamFromZipInputStream(
					KMZfilename, zis_ZIP);
			// load KMZ file as ZIP file
			zis_KMZ = new ZipInputStream(zis_KMZ);
			// find position of KML file in KMZ file
			ZipInputStream zis_KML = childZipInputStreamFromZipInputStream(
					KMLfilename, zis_KMZ);

			/*
			 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); byte[]
			 * buffer = new byte[4096]; int bytesRead = 0; while ((bytesRead =
			 * zis_KML.read(buffer)) > 0) { baos.write(buffer, 0, bytesRead); }
			 * String asdf = baos.toString(); InputStream stream = new
			 * ByteArrayInputStream( asdf.getBytes(StandardCharsets.UTF_8));
			 * 
			 * // parse KML file as an XML file org.jsoup.nodes.Document doc =
			 * Jsoup.parse(stream, null, "", Parser.xmlParser());
			 */

			// parse KML file as an XML file
			org.jsoup.nodes.Document doc = Jsoup.parse(zis_KML, null, "",
					Parser.xmlParser());

			// parse specific tags containing artwork information
			listOfExtendedData = doc.select("ExtendedData");
			listOfCoordinates = doc.select("coordinates");

			// should be 1:1 relationship, otherwise there is an error related
			// to parsing
			if (listOfExtendedData.size() != listOfCoordinates.size())
				throw new Exception(
						"listOfExtendedData.size() != listOfCoordinates.size()");

			numberofAvailableArtworks = Math.min(listOfExtendedData.size(),
					maximumNumberOfArtworksToParse);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Artwork getNextArtwork() throws Exception {

		++pi;
		if (pi >= numberofAvailableArtworks)
			return null;

		// get url tag
		Element extendedDataElement = listOfExtendedData.get(pi);
		List<Element> listOfSimpleData = extendedDataElement
				.select("SimpleData");
		Element urlElement = listOfSimpleData.get(2);

		// remove URL tags
		String urlString = stringInBetweenTwoStrings(urlElement.text(), "'",
				"'");

		// parse artwork URL
		ArtworkUrlParser artworkUrlParser = new ArtworkUrlParser(urlString);

		// get coordinate tag
		Element coordinateElement = listOfCoordinates.get(pi);

		// removes coordinates tags
		String coordinates_string = coordinateElement.text();

		// get coordinate values
		String[] coordinates_stringArray = coordinates_string.split(",");
		double longitude = Double.parseDouble(coordinates_stringArray[0]);
		double latitude = Double.parseDouble(coordinates_stringArray[1]);
		double altitude = Double.parseDouble(coordinates_stringArray[2]);

		// -- create Artwork from URL --//
		Artwork artwork = new Artwork();

		artwork.title = artworkUrlParser.getTitle();
		artwork.coordinates = new Coordinates(longitude, latitude, altitude);
		artwork.urlLink = urlString;

		artwork.imgUrl = artworkUrlParser.getImgUrl();
		artwork.photoCredit = artworkUrlParser.getPhotoCredit();

		artwork.artist = artworkUrlParser.getArtist();
		artwork.description = artworkUrlParser.getDescription();
		artwork.artistStatement = artworkUrlParser.getArtistStatement();

		artwork.neighbourhood = artworkUrlParser.getLocation_Neighbourhood();
		artwork.siteName = artworkUrlParser.getLocation_SiteName();
		artwork.address = artworkUrlParser.getLocation_Address();
		artwork.locationOnSite = artworkUrlParser.getLocation_LocationOnSite();

		artwork.sourceProgram = artworkUrlParser.getSourceProgram();
		artwork.yearInstalled = artworkUrlParser.getYearInstalled();
		artwork.primaryMaterials = artworkUrlParser.getPrimaryMaterials();
		artwork.type = artworkUrlParser.getType();
		artwork.status = artworkUrlParser.getStatus();
		artwork.ownership = artworkUrlParser.getOwnership();
		artwork.sponsoringOrganization = artworkUrlParser
				.getSponsoringOrganization();
		artwork.linkToProjectPage = artworkUrlParser.getLinkToProjectPage();
		// -- //

		return artwork;
	}

	public static ZipInputStream childZipInputStreamFromZipInputStream(
			String filename, ZipInputStream zis) throws java.io.IOException {

		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			String name = entry.getName();

			if (name.equals(filename)) {
				return zis;
			}
		}

		return null;
	}

	public static String stringInBetweenTwoStrings(String inputString,
			String first, String last) {
		String s = inputString;
		s = s.substring(s.indexOf(first) + 1);
		s = s.substring(0, s.indexOf(last));

		return s;
	}
}