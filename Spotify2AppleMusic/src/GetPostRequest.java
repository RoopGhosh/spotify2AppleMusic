import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by roopghosh on 4/26/16.
 */
public class GetPostRequest {

    public JSONObject getRequest(String targetURL, String accessToken) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL( targetURL );
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( "GET" );
            connection.setRequestProperty( "Authorization","Bearer "+accessToken );
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            return new JSONObject(getResponseString( rd ) );

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }


    public JSONObject postRequest(String targetURL, String urlParameters,String encoded64_key) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL( targetURL );
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type",
                    "application/x-www-form-urlencoded" );
            connection.setRequestProperty( "Authorization","Basic "+encoded64_key );
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());


            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            return new JSONObject (getResponseString( rd ));

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }


    public static String getResponseString(BufferedReader rd) throws IOException {
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }
}
