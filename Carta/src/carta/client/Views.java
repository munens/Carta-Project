package carta.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.objectify.annotation.Index;

public class Views implements Serializable {	
	@Index Map<String, Integer> userViews = new HashMap<String, Integer>();

	@Index int numberOfViews = 0;
	
	public Views() {
	}
	
	public void addView(String userId) {
		Integer userNumberOfViews = userViews.get(userId);
		
		if (userNumberOfViews == null)
			userNumberOfViews = 1;
		else
			++userNumberOfViews;
		
		userViews.put(userId, userNumberOfViews);
		++numberOfViews;
		int x = 2;
	}	
}
