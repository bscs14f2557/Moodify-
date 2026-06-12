package moodify;

// ============================================================
//  Song.java  —  Basic data holder (Encapsulation + Abstraction)
// ============================================================
public class Song {

    // --- Private fields (Encapsulation) ---
    private String title;
    private String artist;
    private String audioPath;   // relative path under /audio/
    private int    durationSec;
    private String language;    // "English" | "Urdu/Hindi" | "All"

    // --- Constructor ---
    public Song(String title, String artist, String audioPath,
                int durationSec, String language) {
        this.title       = title;
        this.artist      = artist;
        this.audioPath   = audioPath;
        this.durationSec = durationSec;
        this.language    = language;
    }

    // --- Getters (public interface) ---
    public String getTitle()       { return title; }
    public String getArtist()      { return artist; }
    public String getAudioPath()   { return audioPath; }
    public int    getDurationSec() { return durationSec; }
    public String getLanguage()    { return language; }

    /** Human-readable duration like "3:53" */
    public String getFormattedDuration() {
        int m = durationSec / 60;
        int s = durationSec % 60;
        return String.format("%d:%02d", m, s);
    }

    @Override
    public String toString() {
        return title + " — " + artist + " (" + getFormattedDuration() + ")";
    }
}
