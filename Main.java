package moodify;

import javax.swing.*;

// ============================================================
//  Main.java  —  Entry point
// ============================================================
public class Main {
    public static void main(String[] args) {
        // Use system look-and-feel (keeps custom painting clean on all OS)
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // All GUI work on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Theme.initStars(1000, 680); // pre-generate star positions
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
        });
    }
}
