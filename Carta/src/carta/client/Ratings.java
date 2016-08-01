package carta.client;

import java.io.Serializable;

import java.util.Map;


import com.googlecode.objectify.annotation.Index;

import java.util.HashMap;



// implements serializable for GWT RPC serialization
public class Ratings implements Serializable {
	
	@Index Map<String, Integer> userRatings = new HashMap<String, Integer>();
	int netSumOfVotes = 0;
	@Index public Integer totalNumberOfVotes = 0;
	@Index public Integer numberOfUpvotes = 0;
	@Index public Integer numberOfDownvotes = 0;
	
	
	public static final int UPVOTE = +1;
	public static final int DOWNVOTE = -1;
	
	
	/* sumOfUpvotes >= 0, sumOfDownvotes <= 0
	 * (1) 	sumOfUpVotes + sumOfDownVotes = netSumOfVotes
	 * (2)	sumofUpvotes + |sumOfDownVotes| = totalNumberOfVotes
	 * 		=> sumofUpvotes - sumOfDownVotes = totalNumberOfVotes
	 * 
	 * therefore:
	 * numberOfUpvotes = (totalNumberOfVotes + netSumOfVotes) / 2
	 * numberOfDownvotes = (totalNumberOfVotes - netSumOfVotes) / 2
	 * 
	 */
	void calculateNumberOfUpvotes() {
		numberOfUpvotes = (totalNumberOfVotes + netSumOfVotes) / 2;
	}
	
	void calculateNumberOfDownvotes() {
		numberOfDownvotes = (totalNumberOfVotes - netSumOfVotes) / 2;
	}	
	
	public void addUserUpvote(String userId) {
		
		addUserVote(userId, UPVOTE);
	}
	
	public void addUserDownvote(String userId) {		
		
		addUserVote(userId, DOWNVOTE);
	}
	
	void addUserVote(String userId, int vote) {
		String key = userId;
		
		Integer previousVote = userRatings.put(key, vote);
		
		if (previousVote != null)
			undoVote(previousVote);
		
		netSumOfVotes += vote;
		++totalNumberOfVotes;
		
		calculateNumberOfUpvotes();
		calculateNumberOfDownvotes();
	}
	
	public int getVote(String userId) {
		// debugging
		//int currentSize = userRatings.size();
		
		Integer result = userRatings.get(userId);
		
		// if user has not voted then return 0, indicating that they have not voted
		if (result == null)
			result = 0;
		
		return result;
	}
	
	public void removeUserVote(String userId) {
		String key = userId;
		
		Integer previousVote = userRatings.remove(key);
		
		if (previousVote != null)
			undoVote(previousVote);
		
		calculateNumberOfUpvotes();
		calculateNumberOfDownvotes();
	}
	
	void undoVote(int voteToUndo) {
		netSumOfVotes -= voteToUndo;
		--totalNumberOfVotes;
	}
}
