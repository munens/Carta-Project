package carta.client;

import java.io.Serializable;


public class Coordinates implements Serializable {
	public double longitude;
	public double latitude;
	public double altitude;
	
	public Coordinates (double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}
	
	// need parameterless constructor for GWT RPC serialization
	// http://www.gwtproject.org/doc/latest/tutorial/RPC.html#serialize
	public Coordinates() {
		
	}
}