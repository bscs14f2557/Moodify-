/*package moodify;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// ============================================================
//  MusicPlayer.java  —  Handles audio playback (Encapsulation)
//  Demonstrates: encapsulation, single responsibility
// ============================================================
public class MusicPlayer {

    private Clip          clip;
    private Song          currentSong;
    private boolean       playing;
    private FloatControl  volumeControl;

    public MusicPlayer() {
        playing = false;
    }

    /** Play a Song object. Looks for the file in ./audio/ folder *
    public boolean play(Song song) {
        stop(); // stop anything currently playing
        currentSong = song;

        // Try to find audio file
        String path = song.getAudioPath();
        File audioFile = new File(path);

        // Also try relative paths
        if (!audioFile.exists()) audioFile = new File("audio" + File.separator + new File(path).getName());
        if (!audioFile.exists()) audioFile = new File("." + File.separator + path);

        if (!audioFile.exists()) {
            System.out.println("[MusicPlayer] File not found: " + path);
            return false;
        }

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Volume control
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            clip.start();
            playing = true;
            return true;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("[MusicPlayer] Cannot play: " + e.getMessage());
            return false;
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        playing = false;
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            playing = false;
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            playing = true;
        }
    }

    public boolean isPlaying()      { return playing && clip != null && clip.isRunning(); }
    public Song    getCurrentSong() { return currentSong; }

    /** Set volume 0.0 (silent) to 1.0 (full) 
    public void setVolume(float vol) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * vol;
            volumeControl.setValue(gain);
        }
    }

    /** Returns playback progress as 0.0–1.0 *
    public float getProgress() {
        if (clip == null || clip.getMicrosecondLength() == 0) return 0f;
        return (float) clip.getMicrosecondPosition() / clip.getMicrosecondLength();
    }
}*/

package moodify;

import javax.sound.sampled.*;
import java.io.*;

// ============================================================
//  MusicPlayer.java  —  Handles audio playback (Encapsulation)
//  Demonstrates: encapsulation, single responsibility
// ============================================================
public class MusicPlayer {
    private Clip          clip;
    private Song          currentSong;
    private boolean       playing;
    private FloatControl  volumeControl;

    public MusicPlayer() {
        playing = false;
    }

    /** Play a Song object. Loads audio from classpath (works in JAR too) */
    public boolean play(Song song) {
        stop();
        currentSong = song;

        String path = song.getAudioPath(); // e.g. "audio/happy.wav"

        // Normalize path — remove leading "./" or "audio/" duplicates
        if (path.startsWith("./")) path = path.substring(2);
        if (!path.startsWith("audio/")) path = "audio/" + new File(path).getName();

        // 1st: try loading from classpath (inside JAR)
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);

        // 2nd: fallback to file system (for running from Eclipse directly)
        if (is == null) {
            File f = new File(path);
            if (f.exists()) {
                try { is = new FileInputStream(f); }
                catch (FileNotFoundException e) { is = null; }
            }
        }

        if (is == null) {
            System.out.println("[MusicPlayer] File not found: " + path);
            return false;
        }

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            clip.start();
            playing = true;
            return true;

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("[MusicPlayer] Cannot play: " + e.getMessage());
            return false;
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        playing = false;
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            playing = false;
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            playing = true;
        }
    }

    public boolean isPlaying()      { return playing && clip != null && clip.isRunning(); }
    public Song    getCurrentSong() { return currentSong; }

    public void setVolume(float vol) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue(min + (max - min) * vol);
        }
    }

    public float getProgress() {
        if (clip == null || clip.getMicrosecondLength() == 0) return 0f;
        return (float) clip.getMicrosecondPosition() / clip.getMicrosecondLength();
    }
}
