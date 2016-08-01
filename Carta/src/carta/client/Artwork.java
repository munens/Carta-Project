package carta.client;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

// Implements serializable for GWT RPC serialization: http://www.gwtproject.org/doc/latest/tutorial/RPC.html#serialize
// Used objectify-appengine to store data and perform queries: https://code.google.com/p/objectify-appengine/
// @Entity is for objectify to save the data of the class
// @Index is for objectify to index the particular field in the database, to enable faster queries on database reads made later

@SuppressWarnings("serial")
@Entity
public class Artwork implements Serializable {

	@Id public Long id;

	// Objectify
	@Index public String title;	
	public Coordinates coordinates;
	public String urlLink;

	public String imgUrl;
	public String photoCredit;

	@Index public String artist;
	public String description;
	public String artistStatement;

	// location
	public String neighbourhood;
	public String siteName;
	public String address;
	public String locationOnSite;

	public String sourceProgram;
	@Index public String yearInstalled;
	public String primaryMaterials;
	public String type;
	public String status;
	public String ownership;
	public String sponsoringOrganization;
	public String linkToProjectPage;

	@Index public Ratings ratings = new Ratings();
	@Index public Views views = new Views();
	@Index public Visits visits = new Visits();
	
	// need parameterless constructor for GWT RPC serialization
	// http://www.gwtproject.org/doc/latest/tutorial/RPC.html#serialize
	public Artwork() {

	}
}
