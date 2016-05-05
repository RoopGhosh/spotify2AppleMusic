import org.sikuli.basics.Debug;
import org.sikuli.script.*;

class SikuliMain {
    public static void main(String[] args) throws FindFailed, InterruptedException {
        Debug.setDebugLevel(3);
        Screen s = new Screen();
        App app = App.open( "iTunes.app" );
        app.focus();
        Thread.sleep( 5000 );
        String path = System.getProperty( "user.dir" );
        System.out.println(path);
        ImagePath.setBundlePath( "imgs" );
        //s.find(s.userCapture().getFile()).highlight(2);
        Pattern pattern = new Pattern( "itunes1.png" ).similar( .25f ).targetOffset( 0,-12 );
        Pattern topResultsSel = new Pattern( "topResultsSel" ).similar( .75f );
        Pattern topResultsOff = new Pattern( "topResultsSel" ).similar( .75f ).targetOffset( -23,58 );
        Pattern dotdot = new Pattern( "dotdot.png" );
        Pattern addPlaylist = new Pattern( "addPlaylist.png" );
        Pattern fromSpotify = new Pattern( "fromSpotify.png" );
        //s.find( pattern ).highlight( 1 ).click();
        s.type( "f",KeyModifier.META );
        s.type("sweet nothing");
        s.type( Key.ENTER );
        Thread.sleep( 3000 );
        s.find( topResultsSel ).highlight( 1 ).click();
        s.find( topResultsOff ).hover(  topResultsOff);
        Thread.sleep( 2000 );
        s.find( dotdot ).highlight( 1 ).click();
        s.find( addPlaylist ).highlight( 1 ).click();
        Thread.sleep( 2000 );
        s.find( fromSpotify ).highlight( 1 ).click();
        app.close();
    }
}