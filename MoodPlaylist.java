package moodify;

import java.util.ArrayList;
import java.util.List;

// ============================================================
//  MoodPlaylist.java  —  Concrete subclass (Inheritance)
//  Demonstrates: extends, method overriding (Polymorphism)
// ============================================================
public class MoodPlaylist extends Playlist {

    private String  icon;
    private String  description;
    private boolean mixedLanguage; // true = has both Eng + Urdu songs

    public MoodPlaylist(String name, String icon, String description) {
        super(name);           // call parent constructor
        this.icon        = icon;
        this.description = description;
        this.mixedLanguage = false;
    }

    /**
     * Add song with a language tag.
     * If we see both "English" and "Urdu/Hindi" tags → mixed = true.
     */
    public void addSong(String languageTag, Song s) {
        s = new Song(s.getTitle(), s.getArtist(), s.getAudioPath(),
                     s.getDurationSec(), languageTag);
        songs.add(s);
        // detect mixed playlists
        boolean hasEng  = songs.stream().anyMatch(x -> x.getLanguage().equals("English"));
        boolean hasUrdu = songs.stream().anyMatch(x -> x.getLanguage().equals("Urdu/Hindi"));
        mixedLanguage = hasEng && hasUrdu;
    }

    /** Filter songs by language tag */
    public List<Song> getSongsByLanguage(String lang) {
        List<Song> filtered = new ArrayList<>();
        for (Song s : songs) {
            if (s.getLanguage().equals(lang) || s.getLanguage().equals("All")) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    // --- Override abstract methods (Runtime Polymorphism) ---
    @Override public String  getDescription()          { return description; }
    @Override public String  getIcon()                  { return icon; }
    @Override public boolean supportsLanguageFilter()   { return mixedLanguage; }

    // Compile-time Polymorphism (Method Overloading) ---
    /** Get all songs */
    public List<Song> getSongs() { return songs; }

    /** Get songs filtered by language — overloaded version */
    public List<Song> getSongs(String language) { return getSongsByLanguage(language); }
}
