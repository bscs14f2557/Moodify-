package moodify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

// ============================================================
//  FeelScreen.java  —  Module 2: Mood Selection
//  "How you feeling now? Let's tackle together with Moodify"
//  Shows 9 mood cards in a grid
// ============================================================
public class FeelScreen extends JFrame {

    private Timer animTimer;
    private float animPhase = 0;

    // Card hover tracking
    private int hoveredCard = -1;
    private float[] cardGlow;

    // Mood data
    private List<MoodPlaylist> moods;
    private Rectangle[] cardBounds;

    // Card colors (one accent per mood)
    private static final Color[] CARD_ACCENTS = {
        new Color(255, 200, 60),   // happy - gold
        new Color(100, 130, 200),  // sad - blue-grey
        new Color(120, 200, 255),  // meditation - sky
        new Color(255, 140, 60),   // motivation - orange
        new Color(220, 80, 120),   // heartbroken - rose
        new Color(140, 100, 255),  // starry - purple
        new Color(255, 120, 60),   // punjabi - vibrant orange
        new Color(200, 160, 80),   // qawwali - warm gold
        new Color(100, 190, 220),  // rainy - rain blue
    };

    // Mood background image simulation colors (gradient fills for cards)
    private static final Color[] CARD_BG_TOP = {
        new Color(80, 60, 10),
        new Color(20, 30, 70),
        new Color(10, 50, 80),
        new Color(80, 40, 10),
        new Color(70, 15, 35),
        new Color(35, 20, 80),
        new Color(80, 30, 10),
        new Color(60, 45, 15),
        new Color(15, 45, 65),
    };
    private static final Color[] CARD_BG_BOT = {
        new Color(30, 20, 5),
        new Color(5, 10, 35),
        new Color(5, 20, 40),
        new Color(40, 20, 5),
        new Color(35, 5, 20),
        new Color(15, 8, 45),
        new Color(40, 15, 5),
        new Color(30, 20, 8),
        new Color(5, 20, 40),
    };

    public FeelScreen() {
        setTitle("Moodify — How Do You Feel?");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setResizable(false);

        moods = DataManager.getInstance().getAllMoods();
        cardGlow = new float[moods.size()];
        layoutCards();

        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                drawScene(g2, getWidth(), getHeight());
            }
        };
        canvas.setBackground(Theme.BG_DEEP);
        canvas.setLayout(null);

        // Mouse listeners for hover + click
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                hoveredCard = getCardAt(e.getX(), e.getY());
                canvas.repaint();
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int idx = getCardAt(e.getX(), e.getY());
                if (idx >= 0) openPlaylistScreen(idx);
            }
            @Override public void mouseEntered(MouseEvent e) {
                canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        add(canvas);

        animTimer = new Timer(30, e -> {
            animPhase += 0.04f;
            // Smooth glow animation
            for (int i = 0; i < cardGlow.length; i++) {
                float target = (i == hoveredCard) ? 1f : 0f;
                cardGlow[i] += (target - cardGlow[i]) * 0.15f;
            }
            canvas.repaint();
        });
        animTimer.start();
    }

    private void layoutCards() {
        int cols = 3, rows = 3;
        int cardW = 300, cardH = 165;
        int hGap = 30, vGap = 22;
        int totalW = cols * cardW + (cols - 1) * hGap;
        int startX = (1100 - totalW) / 2;
        int startY = 230;
        cardBounds = new Rectangle[moods.size()];
        int idx = 0;
        for (int r = 0; r < rows && idx < moods.size(); r++) {
            for (int c = 0; c < cols && idx < moods.size(); c++) {
                int cx = startX + c * (cardW + hGap);
                int cy = startY + r * (cardH + vGap);
                cardBounds[idx] = new Rectangle(cx, cy, cardW, cardH);
                idx++;
            }
        }
    }

    private void drawScene(Graphics2D g2, int w, int h) {
        Theme.paintStarryBG(g2, w, h);
        drawHeader(g2, w);
        drawCards(g2);
        drawBackHint(g2, w, h);
    }

    private void drawHeader(Graphics2D g2, int w) {
        // App name small at top
        Font tiny = new Font("Georgia", Font.BOLD | Font.ITALIC, 22);
        g2.setFont(tiny);
        g2.setColor(new Color(Theme.ACCENT_GOLD.getRed(),
                              Theme.ACCENT_GOLD.getGreen(),
                              Theme.ACCENT_GOLD.getBlue(), 180));
        g2.drawString("✦ Moodify", 32, 42);

        // Main question
        Font qFont = new Font("Georgia", Font.BOLD | Font.ITALIC, 46);
        String q = "How you feeling now?";
        g2.setFont(qFont);
        FontMetrics fm = g2.getFontMetrics();
        int qx = (w - fm.stringWidth(q)) / 2;
        Theme.drawGlowText(g2, q, qx, 95,
                Theme.ACCENT_CYAN, Theme.TEXT_WHITE, qFont);

        // Sub
        Font sf = new Font("SansSerif", Font.ITALIC, 16);
        String sub = "Let's tackle it together with  Moodify  ✨";
        g2.setFont(sf);
        FontMetrics sfm = g2.getFontMetrics();
        int sx = (w - sfm.stringWidth(sub)) / 2;
        g2.setColor(new Color(180, 200, 255, 200));
        g2.drawString(sub, sx, 128);

        // Pick prompt
        Font pf = new Font("SansSerif", Font.BOLD, 13);
        String pick = "Pick your mood and let the music do the rest →";
        g2.setFont(pf);
        FontMetrics pfm = g2.getFontMetrics();
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(pick, (w - pfm.stringWidth(pick)) / 2, 165);
    }

    private void drawCards(Graphics2D g2) {
        for (int i = 0; i < moods.size(); i++) {
            drawCard(g2, i, cardBounds[i], CARD_ACCENTS[i % CARD_ACCENTS.length],
                    CARD_BG_TOP[i % CARD_BG_TOP.length],
                    CARD_BG_BOT[i % CARD_BG_BOT.length]);
        }
    }

    private void drawCard(Graphics2D g2, int idx, Rectangle r,
                          Color accent, Color bgTop, Color bgBot) {
        float glow = cardGlow[idx];
        int x = r.x, y = r.y, w = r.width, h = r.height;

        // Glow halo on hover
        if (glow > 0.01f) {
            int hw = (int)(glow * 18);
            g2.setColor(new Color(accent.getRed(), accent.getGreen(),
                                  accent.getBlue(), (int)(glow * 55)));
            g2.fill(new RoundRectangle2D.Float(x - hw, y - hw,
                    w + hw * 2, h + hw * 2, 30, 30));
        }

        // Card body gradient
        GradientPaint gp = new GradientPaint(x, y, bgTop, x, y + h, bgBot);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, 18, 18));

        // Colour band on left edge
        g2.setColor(accent);
        g2.fill(new RoundRectangle2D.Float(x, y, 5, h, 4, 4));

        // Border
        float borderAlpha = 0.3f + glow * 0.5f;
        g2.setColor(new Color(accent.getRed(), accent.getGreen(),
                              accent.getBlue(), (int)(borderAlpha * 255)));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, 18, 18));

        MoodPlaylist mood = moods.get(idx);

        // Icon (emoji) — large
        Font iconFont = new Font("Segoe UI Emoji", Font.PLAIN, 36);
        g2.setFont(iconFont);
        g2.setColor(Color.WHITE);
        g2.drawString(mood.getIcon(), x + 16, y + 52);

        // Mood name
        Font nameFont = new Font("Georgia", Font.BOLD | Font.ITALIC, 18);
        g2.setFont(nameFont);
        g2.setColor(glow > 0.5f ? accent : Theme.TEXT_WHITE);
        g2.drawString(mood.getName(), x + 64, y + 44);

        // Description
        Font descFont = new Font("SansSerif", Font.ITALIC, 12);
        g2.setFont(descFont);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(mood.getDescription(), x + 64, y + 63);

        // Song count badge
        int songs = mood.size();
        String badge = songs + " songs";
        Font badgeFont = new Font("SansSerif", Font.BOLD, 11);
        g2.setFont(badgeFont);
        FontMetrics bfm = g2.getFontMetrics();
        int bw2 = bfm.stringWidth(badge) + 14;
        int bh2 = 20;
        int bx2 = x + w - bw2 - 12;
        int by2 = y + h - bh2 - 10;
        g2.setColor(new Color(accent.getRed(), accent.getGreen(),
                              accent.getBlue(), 50));
        g2.fill(new RoundRectangle2D.Float(bx2, by2, bw2, bh2, 10, 10));
        g2.setColor(accent);
        g2.drawString(badge, bx2 + 7, by2 + 14);

        // Language badge
        boolean mixed = mood.supportsLanguageFilter();
        String langBadge = mixed ? "EN  |  اردو" : "🎵 One Vibe";
        Font lbFont = new Font("SansSerif", Font.PLAIN, 11);
        g2.setFont(lbFont);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(langBadge, x + 16, y + h - 14);

        // Hover "tap to play" hint
        if (glow > 0.3f) {
            Font hintFont = new Font("SansSerif", Font.ITALIC, 11);
            g2.setFont(hintFont);
            g2.setColor(new Color(255, 255, 255, (int)(glow * 180)));
            String hint = "→ Tap to explore";
            g2.drawString(hint, x + w - g2.getFontMetrics().stringWidth(hint) - 14,
                          y + h - 14);
        }
    }

    private void drawBackHint(Graphics2D g2, int w, int h) {
        Font f = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(f);
        g2.setColor(new Color(100, 110, 150, 150));
        g2.drawString("✦ Moodify  |  All moods. All melodies.", 32, h - 18);
    }

    private int getCardAt(int mx, int my) {
        if (cardBounds == null) return -1;
        for (int i = 0; i < cardBounds.length; i++) {
            if (cardBounds[i] != null && cardBounds[i].contains(mx, my)) return i;
        }
        return -1;
    }

    private void openPlaylistScreen(int moodIdx) {
        MoodPlaylist mood = moods.get(moodIdx);
        animTimer.stop();

        if (mood.supportsLanguageFilter()) {
            // Show language chooser first
            SwingUtilities.invokeLater(() -> {
                LanguageScreen lang = new LanguageScreen(mood, this);
                lang.setVisible(true);
            });
        } else {
            // Go directly to playlist
            SwingUtilities.invokeLater(() -> {
                PlaylistScreen ps = new PlaylistScreen(mood, mood.getSongs(), this);
                ps.setVisible(true);
            });
        }
        setVisible(false);
    }
}
