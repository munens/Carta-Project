package carta.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("artwork")
public interface ArtworkService extends RemoteService {
  public String updateArtworks(int maximumNumberOfArtworks) throws NotLoggedInException;
  public List<Artwork> getArtworks(int maximumNumberOfArtworks) throws NotLoggedInException;
  public String lastUpdated() throws NotLoggedInException;
  public void saveArtwork(Artwork Artwork) throws NotLoggedInException;
  public List<Artwork> getArtworks(String oqs) throws NotLoggedInException;
  public Integer initializeParser(int maxNbOfArtworks) throws NotLoggedInException;
  public Integer updateArtworks(long maxTimeElapsedSeconds) throws NotLoggedInException;
}