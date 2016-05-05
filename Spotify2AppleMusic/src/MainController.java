import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by roopghosh on 4/23/16.
 */

public class MainController {
    final static String client_id = "b0e9cb56b4e446f7ad308a8dddff7e55";
    final static String client_Secret = "d9eb1dcc1f704d63a79503558a6e318e";
    final static String sample_URL = "https://accounts.spotify.com/authorize/?client_id=b0e9cb56b4e446f7ad308a8dddff7e55&response_type=code";
    final static String redirect_URI = "https://linkedin.com/in/roopghosh";
    final static String encoded64_key = Base64.getEncoder().encodeToString( (client_id + ":" +client_Secret).getBytes() );
    final static Map payLoad = new HashMap<String,String>(  );
    static String accessToken;

    public static void main(String[] args) throws IOException, InterruptedException {
        GetPostRequest getPostRequest = new GetPostRequest();
        String userName = "roopkumarghosh";
        /*System.out.println("Enter your userid for spotify : ");
        Scanner s = new Scanner( System.in );
        final String userid = s.nextLine();
        System.out.println("Enter your password : ");*/
        //Console console = System.console();
        //final String password = new String(console.readPassword());
        System.out.println("Enter the below url in site and copy the new url which gets created.\n\n\n\n");
        System.out.println(sample_URL+"&redirect_uri="+redirect_URI);
        Thread.sleep( 2000 );
        System.out.println("Have you copied the url to clipboard? Press enter when you are ready");
        new Scanner(  System.in ).nextLine();
        String access_code = getClipBoardCOntent();

        //get response from post request
        String target_URL = "https://accounts.spotify.com/api/token";
        payLoad.put( "grant_type","authorization_code" ); //access_code,encoded64_key,redirect_URI
        payLoad.put( "code",access_code );
        payLoad.put( "redirect_uri",redirect_URI);
        String urlParameters = createPayload(payLoad);
        payLoad.clear();
        JSONObject response = getPostRequest.postRequest(target_URL,urlParameters,encoded64_key);
        accessToken = response.getString( "access_token" );

        // from this json.. get each playlist.
        List<Playlist> playlists = getPlaylists(accessToken,userName,getPostRequest);
        Path path = Paths.get("");
        File filePath = new File(System.getProperty("user.dir")+File.pathSeparator+"ExtractedPlaylists.txt");;

        boolean firstTime = true;
        for(Playlist playlist : playlists){
            //remove hardcoding.
            target_URL = playlist.getUrl()+"/tracks/?fields=items(track(name,album(name),artists(name)))&limit=100";
            //for getting playlist Details
            JSONObject playlistDetail = getPostRequest.getRequest( target_URL,accessToken) ;

            //extractTracksToFiles(playlist);
            playlist.extractTracks( playlistDetail );
            System.out.println("PLAYLIST: "+playlist.getName()+"  Songs: "+playlist.getTracks().size());
            playlist.writePlaylistToFile(filePath,firstTime);  //second argument if its an append.
            firstTime= false;
        }
    }

    private static List<Playlist> getPlaylists(String accessToken,String username,GetPostRequest getPostRequest) {
        List<Playlist> playlists = new ArrayList<>(  );
        StringBuffer targetURL = new StringBuffer("https://api.spotify.com/v1/users/"+username+"/playlists");
        targetURL.append( "?limit=50" );
        JSONObject playlistJSON  = getPostRequest.getRequest(targetURL.toString(),accessToken);
        JSONArray jsonArray = playlistJSON.getJSONArray( "items" );

        for(int i=0;i<jsonArray.length();i++){
            JSONObject obj = (JSONObject) jsonArray.get( i );
            playlists.add( new Playlist
                    (obj.getString( "name" ),obj.getString("href" ),obj.getJSONObject( "owner" ).getString( "id" )) );
        }
        return playlists;
    }

    private static String createPayload(Map<String,String> payLoad) {
        StringBuffer payloadString = new StringBuffer(  );
        Iterator it = payLoad.entrySet().iterator();
        while(it.hasNext()){
            Entry entry = (Entry) it.next();
            payloadString.append( entry.getKey() );
            payloadString.append( "=" );
            payloadString.append( URLEncoder.encode(entry.getValue().toString() ));
            if(it.hasNext()){
                payloadString.append( "&" );
            }

        }
        return payloadString.toString();
    }

    private static String getClipBoardCOntent() {
        try {
            return java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getData( DataFlavor.stringFlavor)
                    .toString().split( "/?code=" )[1];
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
