package carta.server;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@ Entity
public class DatePersistor {
	@Id Long id;
	
	public Date dateTime;
}
