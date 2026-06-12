package moodify;

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

// ============================================================
//  Theme.java  —  Starry Night colour palette + paint helpers
// ============================================================
public class Theme {

    // --- Colour Palette ---
    public static final Color BG_DEEP       = new Color(3,  6, 23);       // near-black navy
    public static final Color BG_MID        = new Color(7, 12, 45);       // dark indigo
    public static final Color BG_PANEL      = new Color(10, 17, 60);      // card background
    public static final Color ACCENT_GOLD   = new Color(255, 200, 87);    // golden starlight
    public static final Color ACCENT_CYAN   = new Color(100, 220, 255);   // cool blue glow
    public static final Color ACCENT_PURPLE = new Color(160, 100, 255);   // cosmic purple
    public static final Color TEXT_WHITE    = new Color(240, 240, 255);   // soft white
    public static final Color TEXT_DIM      = new Color(140, 145, 180);   // muted label
    public static final Color CARD_BORDER   = new Color(60, 80, 160);     // subtle border
    public static final Color HOVER_GLOW    = new Color(100, 140, 255, 60); // hover overlay

    // --- Fonts ---
    public static Font FONT_TITLE(float size) {
        return new Font("Georgia", Font.BOLD | Font.ITALIC, (int) size);
    }
    public static Font FONT_BODY(float size) {
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }
    public static Font FONT_BOLD(float size) {
        return new Font("SansSerif", Font.BOLD, (int) size);
    }

    // ---- Stars data (generated once) ----
    private static final int STAR_COUNT = 200;
    private static final float[] starX    = new float[STAR_COUNT];
    private static final float[] starY    = new float[STAR_COUNT];
    private static final float[] starSize = new float[STAR_COUNT];
    private static final float[] starAlpha= new float[STAR_COUNT];
    private static boolean starsReady = false;

    public static void initStars(int w, int h) {
        Random rng = new Random(42);
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i]     = rng.nextFloat() * w;
            starY[i]     = rng.nextFloat() * h;
            starSize[i]  = 0.5f + rng.nextFloat() * 2.5f;
            starAlpha[i] = 0.4f + rng.nextFloat() * 0.6f;
        }
        starsReady = true;
    }

    /** Paint the full starry night background onto any Graphics2D */
    public static void paintStarryBG(Graphics2D g2, int w, int h) {
        if (!starsReady) initStars(w, h);

        // Deep space gradient
        GradientPaint gp = new GradientPaint(0, 0, BG_DEEP, w, h, BG_MID);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        // Nebula blobs
        paintNebula(g2, w / 4, h / 3, 200, new Color(60, 0, 120, 30));
        paintNebula(g2, w * 3 / 4, h / 5, 160, new Color(0, 60, 140, 25));
        paintNebula(g2, w / 2, h * 2 / 3, 180, new Color(80, 20, 100, 20));

        // Stars
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < STAR_COUNT; i++) {
            float alpha = starAlpha[i];
            g2.setColor(new Color(1f, 1f, 1f, alpha));
            float sz = starSize[i];
            g2.fill(new Ellipse2D.Float(starX[i] - sz/2, starY[i] - sz/2, sz, sz));
        }
    }

    private static void paintNebula(Graphics2D g2, int cx, int cy, int r, Color c) {
        RadialGradientPaint rp = new RadialGradientPaint(
                new Point2D.Float(cx, cy), r,
                new float[]{0f, 1f},
                new Color[]{c, new Color(0, 0, 0, 0)});
        g2.setPaint(rp);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
    }

    /** Glowing text effect — draw same string multiple times with blur halo */
    public static void drawGlowText(Graphics2D g2, String text, int x, int y,
                                    Color glowColor, Color textColor, Font font) {
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // halo layers
        for (int r = 6; r >= 1; r--) {
            float a = 0.06f * (7 - r);
            g2.setColor(new Color(glowColor.getRed()/255f,
                                  glowColor.getGreen()/255f,
                                  glowColor.getBlue()/255f, a));
            g2.drawString(text, x - r, y);
            g2.drawString(text, x + r, y);
            g2.drawString(text, x, y - r);
            g2.drawString(text, x, y + r);
        }
        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    /** Rounded rectangle button shape */
    public static RoundRectangle2D buttonShape(int x, int y, int w, int h) {
        return new RoundRectangle2D.Float(x, y, w, h, 30, 30);
    }
}
