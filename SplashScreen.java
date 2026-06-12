package moodify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;

// ============================================================
//  SplashScreen.java  —  Module 1: Landing Page
//  Shows animated starry title + beats + "Let's Dive" button
// ============================================================
public class SplashScreen extends JFrame {

    private Timer animTimer;
    private float[] beatScale;
    private float   beatPhase = 0;
    private int     beatCount = 14;

    // Floating note positions
    private float[] noteX, noteY, noteSpeed, noteAlpha;
    private String[] noteChars = {"♪","♫","♬","♩","♭","♮"};
    private String[] notesArr;
    private Random rng = new Random(99);

    public SplashScreen() {
        setTitle("Moodify");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        initNotes();
        initBeatScales();

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

        // Invisible click-through button area (we paint the button ourselves)
        JButton diveBtn = new JButton() {
            @Override protected void paintComponent(Graphics g) { /* painted in canvas */ }
            @Override protected void paintBorder(Graphics g) {}
        };
        diveBtn.setOpaque(false);
        diveBtn.setContentAreaFilled(false);
        diveBtn.setBorderPainted(false);
        diveBtn.setFocusPainted(false);
        diveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        diveBtn.setBounds(300, 490, 400, 58);
        diveBtn.addActionListener(e -> openMoodSelect());
        canvas.add(diveBtn);

        // Hover repaint
        diveBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { canvas.repaint(); }
            public void mouseExited(MouseEvent e)  { canvas.repaint(); }
        });

        add(canvas);

        // Animation timer — 40 fps
        animTimer = new Timer(25, e -> {
            beatPhase += 0.07f;
            updateNotes();
            canvas.repaint();
        });
        animTimer.start();
    }

    // ---- Drawing ---------------------------------------------------
    private void drawScene(Graphics2D g2, int w, int h) {
        Theme.paintStarryBG(g2, w, h);
        drawFloatingNotes(g2, w, h);
        drawBeatBars(g2, w, h);
        drawTitle(g2, w, h);
        drawTaglines(g2, w, h);
        drawDiveButton(g2, w, h);
    }

    private void drawTitle(Graphics2D g2, int w, int h) {
        // Big glowing "Moodify"
        String title = "Moodify";
        Font bigFont = new Font("Georgia", Font.BOLD | Font.ITALIC, 92);
        g2.setFont(bigFont);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(title)) / 2;
        int ty = 175;
        Theme.drawGlowText(g2, title, tx, ty,
                Theme.ACCENT_GOLD, Theme.ACCENT_GOLD, bigFont);

        // Subtitle in cyan
        String sub = "✦  Where every mood finds its melody  ✦";
        Font subFont = new Font("SansSerif", Font.ITALIC, 17);
        g2.setFont(subFont);
        FontMetrics sfm = g2.getFontMetrics();
        int sx = (w - sfm.stringWidth(sub)) / 2;
        Theme.drawGlowText(g2, sub, sx, ty + 38,
                Theme.ACCENT_CYAN, new Color(180, 230, 255), subFont);
    }

    private void drawTaglines(Graphics2D g2, int w, int h) {
        String[] lines = {
            "🎵  Feel it. Play it. Live it.",
            "Tune in to your soul — every vibe has a soundtrack."
        };
        Font f1 = new Font("SansSerif", Font.BOLD, 15);
        Font f2 = new Font("SansSerif", Font.ITALIC, 13);
        Font[] fonts = {f1, f2};
        Color[] colors = {new Color(200, 180, 255), Theme.TEXT_DIM};
        int baseY = 250;
        for (int i = 0; i < lines.length; i++) {
            g2.setFont(fonts[i]);
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(lines[i])) / 2;
            g2.setColor(colors[i]);
            g2.drawString(lines[i], x, baseY + i * 24);
        }
    }

    private void drawBeatBars(Graphics2D g2, int w, int h) {
        int totalW = beatCount * 22;
        int startX = (w - totalW) / 2;
        int centerY = 330;

        for (int i = 0; i < beatCount; i++) {
            float wave = (float) Math.sin(beatPhase + i * 0.55f);
            float scale = 0.35f + 0.65f * ((wave + 1f) / 2f);
            int barH = (int)(10 + 55 * scale);
            int bx   = startX + i * 22;
            int by   = centerY - barH / 2;

            // Gradient bar
            float t = (float) i / beatCount;
            Color topC   = interpolateColor(Theme.ACCENT_CYAN, Theme.ACCENT_PURPLE, t);
            Color botC   = interpolateColor(Theme.ACCENT_GOLD, Theme.ACCENT_CYAN, t);
            GradientPaint gp = new GradientPaint(bx, by, topC, bx, by + barH, botC);
            g2.setPaint(gp);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fill(new RoundRectangle2D.Float(bx, by, 14, barH, 7, 7));

            // Glow dot on top
            g2.setColor(new Color(topC.getRed(), topC.getGreen(), topC.getBlue(), 120));
            g2.fill(new Ellipse2D.Float(bx + 3, by - 3, 8, 8));
        }
    }

    private Color interpolateColor(Color a, Color b, float t) {
        int r = (int)(a.getRed()   + t * (b.getRed()   - a.getRed()));
        int g = (int)(a.getGreen() + t * (b.getGreen() - a.getGreen()));
        int bl= (int)(a.getBlue()  + t * (b.getBlue()  - a.getBlue()));
        return new Color(Math.min(255,r), Math.min(255,g), Math.min(255,bl));
    }

    private void drawDiveButton(Graphics2D g2, int w, int h) {
        int bw = 400, bh = 58;
        int bx = (w - bw) / 2, by = 490;

        // Background glow
        g2.setColor(new Color(80, 120, 255, 40));
        g2.fill(new RoundRectangle2D.Float(bx - 8, by - 8, bw + 16, bh + 16, 40, 40));

        // Gradient fill
        GradientPaint gp = new GradientPaint(bx, by,
                new Color(60, 80, 200), bx + bw, by + bh,
                new Color(120, 40, 180));
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(bx, by, bw, bh, 30, 30));

        // Border
        g2.setColor(new Color(160, 180, 255, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(bx, by, bw, bh, 30, 30));

        // Text
        String btnText = "✨  Let's Dive into the World of Melodies";
        Font btnFont = new Font("SansSerif", Font.BOLD, 16);
        g2.setFont(btnFont);
        FontMetrics fm = g2.getFontMetrics();
        int tx = bx + (bw - fm.stringWidth(btnText)) / 2;
        int ty = by + (bh + fm.getAscent()) / 2 - 4;
        g2.setColor(Color.WHITE);
        g2.drawString(btnText, tx, ty);
    }

    // ---- Floating musical notes ------------------------------------
    private void initNotes() {
        int count = 25;
        noteX     = new float[count];
        noteY     = new float[count];
        noteSpeed = new float[count];
        noteAlpha = new float[count];
        notesArr  = new String[count];
        for (int i = 0; i < count; i++) {
            noteX[i]    = rng.nextInt(980);
            noteY[i]    = rng.nextInt(660);
            noteSpeed[i]= 0.3f + rng.nextFloat() * 0.7f;
            noteAlpha[i]= 0.15f + rng.nextFloat() * 0.35f;
            notesArr[i] = noteChars[rng.nextInt(noteChars.length)];
        }
    }

    private void updateNotes() {
        for (int i = 0; i < noteY.length; i++) {
            noteY[i] -= noteSpeed[i];
            if (noteY[i] < -20) {
                noteY[i] = 680;
                noteX[i] = rng.nextInt(980);
            }
        }
    }

    private void drawFloatingNotes(Graphics2D g2, int w, int h) {
        Font nf = new Font("SansSerif", Font.PLAIN, 18);
        g2.setFont(nf);
        for (int i = 0; i < notesArr.length; i++) {
            Color c = (i % 3 == 0) ? Theme.ACCENT_GOLD
                    : (i % 3 == 1) ? Theme.ACCENT_CYAN
                    : Theme.ACCENT_PURPLE;
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(),
                    (int)(noteAlpha[i] * 255)));
            g2.drawString(notesArr[i], noteX[i], noteY[i]);
        }
    }

    private void initBeatScales() {
        beatScale = new float[beatCount];
        for (int i = 0; i < beatCount; i++) beatScale[i] = 0.5f;
    }

    // ---- Navigation ------------------------------------------------
    private void openMoodSelect() {
        animTimer.stop();
        dispose();
        SwingUtilities.invokeLater(() -> {
            FeelScreen feel = new FeelScreen();
            feel.setVisible(true);
        });
    }
}
