import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roopghosh on 4/24/16.
 */
public class Playlist {
    private String name;
    private String url;
    private String ownerId;
    private int noOfTracks;
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Playlist(String name, String url,String ownerId) {
        this.name = name;
        this.url = url;
        this.ownerId = ownerId;
    }

    public void extractTracks(JSONObject playlistDetail){
        JSONArray jsonArray = playlistDetail.getJSONArray( "items" );
        List<Track> tracks = new ArrayList<>(  );
        String name, album; 
        ArrayList<String> artist;
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject( i );
            name= jsonObject.getJSONObject("track").getString( "name" );
            album= jsonObject.getJSONObject("track").getJSONObject( "album" ).getString("name");
            artist = getArtists(jsonObject.getJSONObject( "track" ).getJSONArray( "artists" ));
            tracks.add( new Track( name, album,artist.toArray( new String[artist.size()] ) ) );
        }
        this.tracks = tracks;
    }

    private ArrayList<String> getArtists(JSONArray artists) throws JSONException {
        ArrayList<String> result = new ArrayList<>(  );
        for(int i=0;i<artists.length();i++){
            result.add( artists.getJSONObject( i ).getString( "name" ) );
        }
        return result;
    }

    public boolean writePlaylistToFile(File filePath, boolean firstWrite) {
        try {
            FileWriter wr = new FileWriter( filePath ,!firstWrite);
            StringBuffer buff = new StringBuffer(  );
            String nextLine = System.getProperty("line.separator");
            buff.append( "----"+name+"---"+ nextLine);
            for(Track track : tracks){
                buff.append(track.getName()+","+track.getAlbum()+","+getCommaSeperatedArtist(track.getArtist())+nextLine);
            }
            buff.append( "end of playlist"+nextLine );
            wr.write( buff.toString() );
            wr.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCommaSeperatedArtist(String[] artist) {
        StringBuffer buff = new StringBuffer(  );
        for(String string : artist){
            buff.append( string+"|" );
        }
        return buff.toString();
    }
}
