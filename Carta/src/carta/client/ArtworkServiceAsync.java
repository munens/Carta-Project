package carta.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ArtworkServiceAsync {
  public void updateArtworks(int maximumNumberOfArtworks, AsyncCallback<String> async);
  public void getArtworks(int maximumNumberOfArtworks, AsyncCallback<List<Artwork>> async);
  public void lastUpdated(AsyncCallback<String> async);
  public void saveArtwork(Artwork artwork, AsyncCallback<Void> async);
  public void getArtworks(String oqs, AsyncCallback<List<Artwork>> async);
  public void initializeParser(int maxNbOfArtworks, AsyncCallback<Integer> async);
  public void updateArtworks(long maxTimeElapsedSeconds, AsyncCallback<Integer> async);
}