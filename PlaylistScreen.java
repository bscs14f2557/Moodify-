package moodify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

// ============================================================
//  PlaylistScreen.java  —  Module 4: Song List + Player
//  Shows all songs; click a song → plays audio
// ============================================================
public class PlaylistScreen extends JFrame {

    private MoodPlaylist    mood;
    private List<Song>      songs;
    private JFrame          parent;
    private MusicPlayer     player;
    private Timer           animTimer;
    private float           animPhase = 0f;

    private int  selectedIdx  = -1;
    private int  hoveredIdx   = -1;
    private float[] rowGlow;

    // Scroll state
    private int scrollOffset = 0;
    private static final int ROW_H     = 56;
    private static final int LIST_TOP  = 220;
    private static final int LIST_BOT_MARGIN = 170;

    // Player bar state
    private boolean     isPlaying  = false;
    private String      nowTitle   = "";
    private String      nowArtist  = "";
    private float       nowProgress= 0f;
    private float       volume     = 0.85f;
    private int         hoveredCtrl= -1; // 0=prev, 1=play/pause, 2=next, 3=stop

    // Control areas (painted at bottom)
    private Rectangle ctrlPrev, ctrlPlay, ctrlNext, ctrlStop;
    private Rectangle volumeBar;
    private Rectangle scrollUp, scrollDown;

    public PlaylistScreen(MoodPlaylist mood, List<Song> songs, JFrame parent) {
        this.mood   = mood;
        this.songs  = songs;
        this.parent = parent;
        this.player = new MusicPlayer();
        rowGlow = new float[songs.size()];

        setTitle("Moodify — " + mood.getName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 760);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pre-compute control bounds (window-relative)
        int cx = 450; // centre of 900px
        ctrlPrev = new Rectangle(cx - 130, 610, 48, 48);
        ctrlPlay = new Rectangle(cx - 28,  605, 56, 56);
        ctrlNext = new Rectangle(cx + 82,  610, 48, 48);
        ctrlStop = new Rectangle(cx + 148, 615, 40, 40);
        volumeBar= new Rectangle(680, 627, 160, 14);
        scrollUp  = new Rectangle(860, LIST_TOP, 30, 32);
        scrollDown= new Rectangle(860, 560, 30, 32);

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

        // Back button
        JButton backBtn = makeBackBtn();
        canvas.add(backBtn);
        add(canvas);

        // ---- Mouse interactions ----
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                hoveredIdx  = rowAt(e.getX(), e.getY());
                hoveredCtrl = ctrlAt(e.getX(), e.getY());
                canvas.setCursor(
                    (hoveredIdx >= 0 || hoveredCtrl >= 0)
                    ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                    : Cursor.getDefaultCursor());
                canvas.repaint();
            }
            @Override public void mouseDragged(MouseEvent e) {
                if (volumeBar.contains(e.getX(), e.getY())) {
                    volume = Math.max(0f, Math.min(1f,
                            (float)(e.getX() - volumeBar.x) / volumeBar.width));
                    player.setVolume(volume);
                    canvas.repaint();
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = rowAt(e.getX(), e.getY());
                if (row >= 0) {
                    playSong(row);
                    return;
                }
                int ctrl = ctrlAt(e.getX(), e.getY());
                handleCtrl(ctrl);

                // Volume click
                if (volumeBar.contains(e.getX(), e.getY())) {
                    volume = Math.max(0f, Math.min(1f,
                            (float)(e.getX() - volumeBar.x) / volumeBar.width));
                    player.setVolume(volume);
                    canvas.repaint();
                }
                // Scroll
                if (scrollUp.contains(e.getPoint()))   scroll(-1);
                if (scrollDown.contains(e.getPoint())) scroll(1);
            }
        });

        // Mouse wheel scroll
        canvas.addMouseWheelListener(e -> {
            scroll(e.getWheelRotation() > 0 ? 1 : -1);
        });

        // Animation + progress update
        animTimer = new Timer(40, e -> {
            animPhase += 0.05f;
            if (player.isPlaying()) nowProgress = player.getProgress();
            for (int i = 0; i < rowGlow.length; i++) {
                float t = (i == hoveredIdx || i == selectedIdx) ? 1f : 0f;
                rowGlow[i] += (t - rowGlow[i]) * 0.15f;
            }
            canvas.repaint();
        });
        animTimer.start();

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                animTimer.stop();
                player.stop();
                parent.setVisible(true);
            }
        });
    }

    // ---- Drawing ---------------------------------------------------
    private void drawScene(Graphics2D g2, int w, int h) {
        Theme.paintStarryBG(g2, w, h);
        drawHeader(g2, w);
        drawSongList(g2, w);
        drawPlayerBar(g2, w, h);
        drawScrollButtons(g2);
    }

    private void drawHeader(Graphics2D g2, int w) {
        // Mood icon + name
        Font iconF = new Font("Segoe UI Emoji", Font.PLAIN, 38);
        g2.setFont(iconF);
        g2.setColor(Color.WHITE);
        g2.drawString(mood.getIcon(), 36, 68);

        Font nameF = new Font("Georgia", Font.BOLD | Font.ITALIC, 32);
        Theme.drawGlowText(g2, mood.getName(), 88, 65,
                Theme.ACCENT_GOLD, Theme.ACCENT_GOLD, nameF);

        Font descF = new Font("SansSerif", Font.ITALIC, 13);
        g2.setFont(descF);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(mood.getDescription(), 88, 85);

        // Song count
        Font cntF = new Font("SansSerif", Font.BOLD, 12);
        g2.setFont(cntF);
        g2.setColor(Theme.ACCENT_CYAN);
        g2.drawString(songs.size() + " songs", w - 100, 55);

        // Divider
        GradientPaint div = new GradientPaint(36, 0, Theme.ACCENT_PURPLE, w - 36, 0,
                new Color(0,0,0,0));
        g2.setPaint(div);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(36, 100, w - 36, 100);

        // Column headers
        Font colF = new Font("SansSerif", Font.BOLD, 11);
        g2.setFont(colF);
        g2.setColor(new Color(140, 150, 190));
        g2.drawString("#",  52, 126);
        g2.drawString("TITLE", 82, 126);
        g2.drawString("ARTIST", 400, 126);
        g2.drawString("DURATION", 720, 126);

        g2.setPaint(new Color(50, 60, 110));
        g2.setStroke(new BasicStroke(0.5f));
        g2.drawLine(36, 132, w - 36, 132);
    }

    private void drawSongList(Graphics2D g2, int w) {
        int visibleH = getHeight() - LIST_TOP - LIST_BOT_MARGIN;
        int visibleRows = visibleH / ROW_H;
        int maxScroll = Math.max(0, songs.size() - visibleRows);
        scrollOffset = Math.min(scrollOffset, maxScroll);

        // Clip to list area
        g2.setClip(30, LIST_TOP, w - 60, visibleH);

        for (int i = 0; i < songs.size(); i++) {
            int displayIdx = i - scrollOffset;
            if (displayIdx < 0) continue;
            int ry = LIST_TOP + displayIdx * ROW_H;
            if (ry >= LIST_TOP + visibleH) break;

            drawSongRow(g2, i, ry, w);
        }

        g2.setClip(null);
    }

    private void drawSongRow(Graphics2D g2, int idx, int ry, int w) {
        Song s = songs.get(idx);
        float glow = rowGlow[idx];
        boolean sel = (idx == selectedIdx);

        // Row background
        if (sel) {
            GradientPaint gp = new GradientPaint(36, ry,
                    new Color(40, 60, 140, 180), w - 36, ry,
                    new Color(60, 30, 120, 180));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Float(36, ry + 2, w - 72, ROW_H - 4, 10, 10));
        } else if (glow > 0.05f) {
            g2.setColor(new Color(255, 255, 255, (int)(glow * 20)));
            g2.fill(new RoundRectangle2D.Float(36, ry + 2, w - 72, ROW_H - 4, 10, 10));
        }

        // Alternating subtle stripe
        if (!sel && idx % 2 == 0) {
            g2.setColor(new Color(255, 255, 255, 6));
            g2.fill(new Rectangle(36, ry, w - 72, ROW_H));
        }

        // Row number / playing icon
        Font numF = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(numF);
        if (sel && player.isPlaying()) {
            // Animated pulse dots
            g2.setColor(Theme.ACCENT_CYAN);
            for (int d = 0; d < 3; d++) {
                float h2 = 4f + (float)(Math.sin(animPhase + d * 1.2f) + 1) * 5f;
                g2.fill(new RoundRectangle2D.Float(
                        46 + d * 5, ry + ROW_H/2 - h2/2, 3, h2, 2, 2));
            }
        } else {
            g2.setColor(sel ? Theme.ACCENT_CYAN : Theme.TEXT_DIM);
            g2.drawString(String.valueOf(idx + 1), 50, ry + 33);
        }

        // Title
        Font titleF = new Font("SansSerif", sel ? Font.BOLD : Font.PLAIN, 14);
        g2.setFont(titleF);
        g2.setColor(sel ? Theme.ACCENT_GOLD : Theme.TEXT_WHITE);
        String title = truncate(s.getTitle(), 30);
        g2.drawString(title, 80, ry + 33);

        // Artist
        Font artF = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(artF);
        g2.setColor(sel ? new Color(200, 200, 255) : Theme.TEXT_DIM);
        String artist = truncate(s.getArtist(), 28);
        g2.drawString(artist, 390, ry + 33);

        // Duration
        Font durF = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(durF);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(s.getFormattedDuration(), 720, ry + 33);

        // Separator
        if (idx % 1 == 0) {
            g2.setColor(new Color(50, 55, 100, 80));
            g2.setStroke(new BasicStroke(0.5f));
            g2.drawLine(36, ry + ROW_H - 1, w - 36, ry + ROW_H - 1);
        }
    }

    private void drawPlayerBar(Graphics2D g2, int w, int h) {
        int barY = h - 170;

        // Player bar background
        GradientPaint gp = new GradientPaint(0, barY,
                new Color(5, 8, 35, 240), 0, h, new Color(2, 4, 20, 255));
        g2.setPaint(gp);
        g2.fillRect(0, barY, w, h - barY);

        // Top border glow
        g2.setColor(new Color(80, 100, 200, 100));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(0, barY, w, barY);

        // Progress bar
        int pbX = 36, pbY = barY + 14, pbW = w - 72, pbH = 4;
        g2.setColor(new Color(50, 60, 120));
        g2.fill(new RoundRectangle2D.Float(pbX, pbY, pbW, pbH, 3, 3));
        int filled = (int)(nowProgress * pbW);
        GradientPaint pg = new GradientPaint(pbX, pbY,
                Theme.ACCENT_CYAN, pbX + filled, pbY, Theme.ACCENT_PURPLE);
        g2.setPaint(pg);
        g2.fill(new RoundRectangle2D.Float(pbX, pbY, filled, pbH, 3, 3));
        // Thumb
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(pbX + filled - 6, pbY - 4, 12, 12));

        // Now playing info
        Font nf = new Font("SansSerif", Font.BOLD, 14);
        g2.setFont(nf);
        g2.setColor(Theme.TEXT_WHITE);
        String ntx = nowTitle.isEmpty() ? "Select a song to play" : nowTitle;
        g2.drawString(ntx, 36, barY + 50);

        Font af = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(af);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString(nowArtist, 36, barY + 68);

        // Controls
        drawControl(g2, ctrlPrev, "⏮", 0);
        drawControl(g2, ctrlPlay, isPlaying ? "⏸" : "▶", 1);
        drawControl(g2, ctrlNext, "⏭", 2);
        drawControl(g2, ctrlStop, "⏹", 3);

        // Volume
        Font vf = new Font("SansSerif", Font.PLAIN, 11);
        g2.setFont(vf);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString("🔊", volumeBar.x - 28, volumeBar.y + 11);
        g2.setColor(new Color(50, 60, 120));
        g2.fill(new RoundRectangle2D.Float(volumeBar.x, volumeBar.y,
                volumeBar.width, volumeBar.height, 7, 7));
        GradientPaint vp = new GradientPaint(volumeBar.x, volumeBar.y,
                Theme.ACCENT_CYAN, volumeBar.x + (int)(volume * volumeBar.width),
                volumeBar.y, Theme.ACCENT_PURPLE);
        g2.setPaint(vp);
        g2.fill(new RoundRectangle2D.Float(volumeBar.x, volumeBar.y,
                (int)(volume * volumeBar.width), volumeBar.height, 7, 7));
    }

    private void drawControl(Graphics2D g2, Rectangle r, String icon, int idx) {
        boolean hover = (hoveredCtrl == idx);
        boolean active= (idx == 1 && isPlaying);

        // Circle button
        if (active || hover) {
            GradientPaint gp = new GradientPaint(r.x, r.y,
                    new Color(60, 80, 200), r.x + r.width, r.y + r.height,
                    new Color(100, 40, 180));
            g2.setPaint(gp);
        } else {
            g2.setColor(new Color(30, 35, 80));
        }
        g2.fill(new Ellipse2D.Float(r.x, r.y, r.width, r.height));
        g2.setColor(new Color(80, 100, 200, 150));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new Ellipse2D.Float(r.x, r.y, r.width, r.height));

        Font icF = new Font("Segoe UI Emoji", Font.PLAIN, idx == 1 ? 22 : 18);
        g2.setFont(icF);
        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width  - fm.stringWidth(icon)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;
        g2.setColor(hover || active ? Color.WHITE : new Color(180, 190, 220));
        g2.drawString(icon, tx, ty);
    }

    private void drawScrollButtons(Graphics2D g2) {
        for (Rectangle sr : new Rectangle[]{scrollUp, scrollDown}) {
            g2.setColor(new Color(30, 40, 90, 180));
            g2.fill(new RoundRectangle2D.Float(sr.x, sr.y, sr.width, sr.height, 8, 8));
            g2.setColor(Theme.TEXT_DIM);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            String a = (sr == scrollUp) ? "▲" : "▼";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(a, sr.x + (sr.width - fm.stringWidth(a)) / 2,
                    sr.y + (sr.height + fm.getAscent()) / 2 - 4);
        }
    }

    // ---- Interaction helpers ---------------------------------------
    private int rowAt(int mx, int my) {
        int visibleH = getHeight() - LIST_TOP - LIST_BOT_MARGIN;
        if (mx < 36 || mx > getWidth() - 36) return -1;
        if (my < LIST_TOP || my > LIST_TOP + visibleH) return -1;
        int relative = my - LIST_TOP;
        int row = scrollOffset + relative / ROW_H;
        return (row >= 0 && row < songs.size()) ? row : -1;
    }

    private int ctrlAt(int mx, int my) {
        Rectangle[] ctrls = {ctrlPrev, ctrlPlay, ctrlNext, ctrlStop};
        for (int i = 0; i < ctrls.length; i++) {
            if (ctrls[i].contains(mx, my)) return i;
        }
        return -1;
    }

    private void handleCtrl(int ctrl) {
        switch (ctrl) {
            case 0: // prev
                if (selectedIdx > 0) playSong(selectedIdx - 1); break;
            case 1: // play/pause
                if (!isPlaying && selectedIdx < 0 && !songs.isEmpty()) playSong(0);
                else if (isPlaying)  { player.pause();  isPlaying = false; }
                else                 { player.resume(); isPlaying = true;  }
                break;
            case 2: // next
                if (selectedIdx < songs.size() - 1) playSong(selectedIdx + 1); break;
            case 3: // stop
                player.stop(); isPlaying = false; nowProgress = 0f; break;
        }
    }

    private void playSong(int idx) {
        selectedIdx = idx;
        Song s = songs.get(idx);
        boolean ok = player.play(s);
        isPlaying = ok;
        nowTitle  = s.getTitle();
        nowArtist = s.getArtist();
        nowProgress = 0f;
        player.setVolume(volume);
        // Scroll into view
        int visibleRows = (getHeight() - LIST_TOP - LIST_BOT_MARGIN) / ROW_H;
        if (idx < scrollOffset) scrollOffset = idx;
        if (idx >= scrollOffset + visibleRows) scrollOffset = idx - visibleRows + 1;
    }

    private void scroll(int delta) {
        int visibleRows = (getHeight() - LIST_TOP - LIST_BOT_MARGIN) / ROW_H;
        int max = Math.max(0, songs.size() - visibleRows);
        scrollOffset = Math.max(0, Math.min(max, scrollOffset + delta));
    }

    private JButton makeBackBtn() {
        JButton b = new JButton("← Back");
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(Theme.TEXT_DIM);
        b.setBackground(new Color(18, 22, 55));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBounds(20, 14, 100, 30);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            animTimer.stop();
            player.stop();
            dispose();
            parent.setVisible(true);
        });
        return b;
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
