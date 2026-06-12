package moodify;

import java.util.ArrayList;
import java.util.List;

// ============================================================
//  Playlist.java  —  Abstract base class (Abstraction)
//  Demonstrates: abstract class, abstract methods, inheritance
// ============================================================
public abstract class Playlist {

    // --- Protected fields accessible by sub-classes ---
    protected String       name;
    protected List<Song>   songs;

    public Playlist(String name) {
        this.name  = name;
        this.songs = new ArrayList<>();
    }

    // --- Concrete shared methods ---
    public void addSong(Song s)    { songs.add(s); }
    public List<Song> getSongs()   { return songs; }
    public String     getName()    { return name;  }
    public int        size()       { return songs.size(); }

    // --- Abstract methods (sub-classes MUST implement) ---
    /** Returns a display description shown in the UI */
    public abstract String getDescription();

    /** Returns the emoji / icon for this playlist type */
    public abstract String getIcon();

    /** Returns true if this playlist supports language filtering */
    public abstract boolean supportsLanguageFilter();
}
