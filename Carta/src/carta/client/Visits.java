package carta.client;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Index;

@SuppressWarnings("serial")
public class Visits implements Serializable {
	@Index Set<String> users = new HashSet<String>();
	
	@Index Integer numberOfVisits = 0;
	
	public boolean markAsVisited(String userId) {
		++numberOfVisits;
		
		return users.add(userId);
	}
	
	public void markAsUnvisited(String userId) {
		--numberOfVisits;
		
		users.remove(userId);
	}
	
	public boolean hasVisited(String userId) {
		return users.contains(userId);
	}
}
