package moodify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

// ============================================================
//  LanguageScreen.java  —  Module 3: Language Chooser
//  "What is your swag?" → English or Urdu/Hindi
// ============================================================
public class LanguageScreen extends JFrame {

    private MoodPlaylist mood;
    private JFrame parent;
    private Timer animTimer;
    private float animPhase = 0f;
    private int hoveredBtn = -1; // 0=English, 1=Urdu/Hindi
    private float[] btnGlow = {0f, 0f};

    // Button areas
    private Rectangle btnEng  = new Rectangle(180, 370, 320, 130);
    private Rectangle btnUrdu = new Rectangle(600, 370, 320, 130);

    public LanguageScreen(MoodPlaylist mood, JFrame parent) {
        this.mood   = mood;
        this.parent = parent;

        setTitle("Moodify — Choose Your Vibe");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setResizable(false);

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

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int prev = hoveredBtn;
                if (btnEng.contains(e.getPoint()))       hoveredBtn = 0;
                else if (btnUrdu.contains(e.getPoint())) hoveredBtn = 1;
                else                                      hoveredBtn = -1;
                if (hoveredBtn != prev) canvas.repaint();
                canvas.setCursor(hoveredBtn >= 0
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (btnEng.contains(e.getPoint()))       openPlaylist("English");
                else if (btnUrdu.contains(e.getPoint())) openPlaylist("Urdu/Hindi");
            }
        });

        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        backBtn.setForeground(Theme.TEXT_DIM);
        backBtn.setBackground(new Color(20, 25, 60));
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(20, 15, 100, 32);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            animTimer.stop();
            dispose();
            parent.setVisible(true);
        });
        canvas.setLayout(null);
        canvas.add(backBtn);

        add(canvas);

        animTimer = new Timer(30, e -> {
            animPhase += 0.05f;
            btnGlow[0] += ((hoveredBtn == 0 ? 1f : 0f) - btnGlow[0]) * 0.12f;
            btnGlow[1] += ((hoveredBtn == 1 ? 1f : 0f) - btnGlow[1]) * 0.12f;
            canvas.repaint();
        });
        animTimer.start();

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                animTimer.stop();
                parent.setVisible(true);
            }
        });
    }

    private void drawScene(Graphics2D g2, int w, int h) {
        Theme.paintStarryBG(g2, w, h);
        drawHeader(g2, w);
        drawLanguageButtons(g2, w);
        drawMoodBadge(g2, w, h);
    }

    private void drawHeader(Graphics2D g2, int w) {
        // "What is your swag?" 
        Font f = new Font("Georgia", Font.BOLD | Font.ITALIC, 54);
        String q = "What is your  swag?";
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(q)) / 2;
        Theme.drawGlowText(g2, q, x, 155,
                Theme.ACCENT_PURPLE, Theme.TEXT_WHITE, f);

        // Sub
        Font sf = new Font("SansSerif", Font.ITALIC, 17);
        String sub = "Pick the language that hits different for you ✨";
        g2.setFont(sf);
        FontMetrics sfm = g2.getFontMetrics();
        g2.setColor(new Color(180, 200, 255, 210));
        g2.drawString(sub, (w - sfm.stringWidth(sub)) / 2, 198);

        // Mood name
        Font mf = new Font("Georgia", Font.BOLD, 20);
        String mn = mood.getIcon() + "  " + mood.getName();
        g2.setFont(mf);
        FontMetrics mfm = g2.getFontMetrics();
        g2.setColor(Theme.ACCENT_GOLD);
        g2.drawString(mn, (w - mfm.stringWidth(mn)) / 2, 252);
    }

    private void drawLanguageButtons(Graphics2D g2, int w) {
        drawLangBtn(g2, btnEng, btnGlow[0],
                "English",
                "Western vibes & global hits",
                "🎸",
                new Color(60, 100, 220),
                new Color(30, 60, 160));

        drawLangBtn(g2, btnUrdu, btnGlow[1],
                "Urdu / Hindi",
                "Desi melodies jo dil ko chhu lain",
                "🎵",
                new Color(180, 80, 60),
                new Color(120, 40, 30));
    }

    private void drawLangBtn(Graphics2D g2, Rectangle r, float glow,
                             String title, String sub, String icon,
                             Color topC, Color botC) {
        int x = r.x, y = r.y, w = r.width, h = r.height;

        // Outer glow
        if (glow > 0.01f) {
            int hw = (int)(glow * 16);
            g2.setColor(new Color(topC.getRed(), topC.getGreen(),
                                  topC.getBlue(), (int)(glow * 60)));
            g2.fill(new RoundRectangle2D.Float(x - hw, y - hw,
                    w + hw * 2, h + hw * 2, 35, 35));
        }

        // Body
        GradientPaint gp = new GradientPaint(x, y, topC, x, y + h, botC);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, 20, 20));

        // Border
        g2.setColor(new Color(255, 255, 255, (int)(50 + glow * 100)));
        g2.setStroke(new BasicStroke(1.8f));
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, 20, 20));

        // Emoji
        Font ef = new Font("Segoe UI Emoji", Font.PLAIN, 42);
        g2.setFont(ef);
        g2.setColor(Color.WHITE);
        g2.drawString(icon, x + w / 2 - 22, y + 58);

        // Title
        Font tf = new Font("Georgia", Font.BOLD, 22);
        g2.setFont(tf);
        g2.setColor(Color.WHITE);
        FontMetrics tfm = g2.getFontMetrics();
        g2.drawString(title, x + (w - tfm.stringWidth(title)) / 2, y + 88);

        // Sub
        Font sf = new Font("SansSerif", Font.ITALIC, 12);
        g2.setFont(sf);
        g2.setColor(new Color(255, 255, 255, 180));
        FontMetrics sfm = g2.getFontMetrics();
        g2.drawString(sub, x + (w - sfm.stringWidth(sub)) / 2, y + 108);

        // "Select →" hint on hover
        if (glow > 0.4f) {
            Font hf = new Font("SansSerif", Font.BOLD, 12);
            g2.setFont(hf);
            String hint = "Select  →";
            FontMetrics hfm = g2.getFontMetrics();
            g2.setColor(new Color(255, 255, 255, (int)(glow * 220)));
            g2.drawString(hint, x + (w - hfm.stringWidth(hint)) / 2, y + h - 12);
        }
    }

    private void drawMoodBadge(Graphics2D g2, int w, int h) {
        Font f = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(f);
        g2.setColor(new Color(100, 110, 150, 140));
        g2.drawString("✦ Moodify  |  " + mood.getName() + "  |  "
                + mood.size() + " songs", 32, h - 18);
    }

    private void openPlaylist(String language) {
        animTimer.stop();
        List<Song> songs = mood.getSongs(language);
        SwingUtilities.invokeLater(() -> {
            PlaylistScreen ps = new PlaylistScreen(mood, songs, parent);
            ps.setVisible(true);
        });
        dispose();
    }
}
