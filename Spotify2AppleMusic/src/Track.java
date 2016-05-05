/**
 * Created by roopghosh on 4/26/16.
 */
public class Track {
    private String name;
    private String album;
    private String[] artist;

    public Track(String name, String album, String[] artist) {
        this.name = name;
        this.album = album;
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getArtist() {
        return artist;
    }

    public void setArtist(String[] artist) {
        this.artist = artist;
    }
}
