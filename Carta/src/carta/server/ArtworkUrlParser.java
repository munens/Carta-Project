package carta.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ArtworkUrlParser {

	final String dataIdentifier = "dvArtworkDetails_";

	Document document;

	public ArtworkUrlParser(String url) throws Exception {
		// read HTML file from URL into memory
		URL artworkURL = new URL(url);
		BufferedReader in = new BufferedReader( new InputStreamReader(artworkURL.openStream()) );

		// parse out lines containing artwork data to get HTML with just the relevant data lines
		// this preprocessing of the HTML speeds up the subsequent parsing done by Jsoup
		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains(dataIdentifier)) {	
				response.append(inputLine);
			}
		}
		in.close();
		String strippedHtml = response.toString();

		// we stripped out the relevant lines of the HTML file; 
		// so inform Jsoup of this by telling it the lines are fragments of the HTML body
		document = Jsoup.parseBodyFragment(strippedHtml);
	}
	
	/*
	 Some examples URLs to parse (For refereance):
		https://app.vancouver.ca/PublicArt_Net/ArtworkDetails.aspx?ArtworkID=514&Neighbourhood=&Ownership=&Program="
		https://app.vancouver.ca/PublicArt_Net/ArtworkDetails.aspx?ArtworkID=363&Neighbourhood=&Ownership=&Program="
	 */

	public String getTitle() {		
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblTitle");
	}

	public String getImgUrl() {
		Element e = document.select("img[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_imgArtwork").first();

		String fullURL = null;
		if (e != null) {
			String imageURL = e.attr("src");
			fullURL = "https://app.vancouver.ca" + imageURL;

		}
		else {
			fullURL = "";
		}

		return fullURL;

	}

	public String getPhotoCredit() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblPhotoCreditWeb");
	}
	
	public String getArtist() {
		return parseQuery("a[href*=ArtistDetails.aspx?");
	}

	public String getDescription() {		
		String result = parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblDescription");
		
		return result;
	}

	public String getArtistStatement() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblArtistStatement");
	}
	
	public String getLocation_Neighbourhood() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblNeighbourhood");
	}
	
	public String getLocation_SiteName() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblSiteName");
	}
	
	public String getLocation_Address() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblAddress");
	}
	
	public String getLocation_LocationOnSite() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblLocationOnSite");
	}
	
	public String getSourceProgram() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblSource");
	}
	
	public String getYearInstalled() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblInstalled");
	}
	
	public String getPrimaryMaterials() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblPrimaryMaterials");		
	}

	public String getType() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblType");
	}
	
	public String getStatus() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblStatus");
	}
	
	public String getOwnership() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblOwnership");
	}
	
	public String getSponsoringOrganization() {
		return parseQuery("span[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_lblSponsoringOrg");
	}
	
	public String getLinkToProjectPage() {
		String result = parseQuery("a[id*=CSGMasterPage_cphMasterContent_dvArtworkDetails_hlProjectPage");
		
		return result;
		
	}
	
	String parseQuery(String cssQuery) {
		Element e = document.select(cssQuery).last();
		
		return e != null ? e.text() : "";
	}
}