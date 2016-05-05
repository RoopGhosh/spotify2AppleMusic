import org.sikuli.basics.Debug;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

class SikuliMain {
    public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
        System.out.println("Please enter the playlist location : ");
        String path = new Scanner( System.in ).nextLine();
        List<String> lines = Files.readAllLines( Paths.get(path), StandardCharsets.UTF_8);
        Debug.setDebugLevel( 3 );
        Screen screen = new Screen();
        App app = App.open( "iTunes.app" );
        app.focus();
        for(String line : lines){
            if(line.contains( "----" )){
                continue;
            }
            System.out.println("Adding : "+line);
            mainWorker(line,app,screen,0);
        }
        app.close();
    }

    public static void mainWorker(String line, App app, Screen s,int notFound){
        try {
            String songName = line.split( "," )[0];
            Thread.sleep( 5000 );
            TextRecognizer textRecognizer = TextRecognizer.getInstance();
            String path = System.getProperty( "user.dir" );
            System.out.println( path );
            ImagePath.setBundlePath( "imgs" );
            //s.find(s.userCapture().getFile()).highlight(2);
            //Pattern pattern = new Pattern( "itunes1.png" ).similar( .35f ).targetOffset( 0, -12 );
            Boolean isMac = Settings.isMac()? true:false;
            int key = isMac? KeyModifier.META: KeyModifier.CTRL;
            Pattern topResultsSel = new Pattern( "topResultsSel" ).similar( .75f );
            Pattern topResultsOff = new Pattern( "topResultsSel" ).similar( .75f ).targetOffset( -23, 58 );
            Pattern dotdot = new Pattern( "dotdot.png" );
            Pattern addPlaylist = new Pattern( "addPlaylist.png" );
            Pattern fromSpotify = new Pattern( "fromSpotify.png" );
            String str = textRecognizer.recognize( fromSpotify.getBImage() );
            s.type( "f",key );
            //s.find( pattern ).highlight( 1 ).click();
            s.type( songName );
            s.type( Key.ENTER );
            Thread.sleep( 3000 );
            s.find( topResultsSel ).highlight( 1 ).click();
            s.find( topResultsOff ).hover( topResultsOff );
            Thread.sleep( 2000 );
            s.find( dotdot ).highlight( 1 ).click();
            s.find( addPlaylist ).highlight( 1 ).click();
            Thread.sleep( 2000 );
            s.find( fromSpotify ).highlight( 1 ).click();
            Thread.sleep( 3000 );
            s.type( Key.ESC );
        }
        catch(Exception e){
            notFound++;
            e.printStackTrace();
        }
    }
}