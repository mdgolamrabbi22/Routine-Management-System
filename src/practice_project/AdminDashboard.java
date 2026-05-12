package practice_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

// ১) গ্রেডিয়েন্ট বোতাম
class GradientButton extends JButton {
    private Color startColor, endColor;


    public GradientButton(String text, Color start, Color end) {
        super(text);
        this.startColor = start;
        this.endColor = end;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBorder(new EmptyBorder(8, 16, 8, 16));
        // হোভারের জন্য কার্সর অ্যাড
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // হোভার এ সামান্য লাইটেন
                startColor = startColor.brighter();
                endColor = endColor.brighter();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startColor = start;
                endColor = end;
                // ডিফল্ট কার্সরে ফেরত
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, startColor, w, 0, endColor);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 20, 20);
        super.paintComponent(g);
        g2.dispose();
    }
}

// ২) ড্যাশবোর্ড সাবটাইটেল (ড্যাশড আন্ডারলাইন)
//class UnderlinedLabel extends JLabel {
//    public UnderlinedLabel(String text) {
//        super(text);
//        setFont(new Font("SansSerif", Font.BOLD, 20));
//        setForeground(Color.DARK_GRAY);
//        setPreferredSize(new Dimension(200, 30));
//    }
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        // টেক্সটের নিচে ড্যাশড লাইন
//        Graphics2D g2 = (Graphics2D) g;
//        FontMetrics fm = g2.getFontMetrics();
//        int y = fm.getAscent() + 5;
//        int textWidth = fm.stringWidth(getText());
//        float[] dash = {4f, 4f};
//        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT,
//                BasicStroke.JOIN_MITER, 1.0f, dash, 0f));
//        g2.setColor(Color.GRAY);
//        g2.drawLine(0, y, textWidth, y);
//    }
//}

// ৩) পিল শৈলীর টাইটেল
class PillTitle extends JPanel {
    public PillTitle(String text) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 6));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        add(lbl);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 102, 178),
                w, 0, new Color(102, 178, 255));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 30, 30);
        super.paintComponent(g);
        g2.dispose();
    }
}

public class AdminDashboard extends JFrame {

    JLabel logoLabel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1366, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // ব্যাকগ্রাউন্ড প্যানেল
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // লোগো
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(10, 10, 60, 60);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new HomePage();
                dispose();
            }
        });
        bg.add(logoLabel);

        // পিল টাইটেল সেন্টারে
        PillTitle pill = new PillTitle("Admin Dashboard Page");
        pill.setBounds((1366-300)/2, 20, 300, 40);
        bg.add(pill);


        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Poppins", Font.BOLD, 28));
        title.setForeground(new Color(20, 20, 80));
        title.setBounds(200, 120, 200, 40);
        add(title);

        JLabel ul = new JLabel("_______________");
        ul.setFont(new Font("Poppins", Font.PLAIN, 24));
        ul.setForeground(new Color(100, 100, 150));
        ul.setBounds(200, 155, 300, 30);
        add(ul);

        // বোতামগুলো
        int bx = 200, bw = 220, bh = 50, gap = 70;
        GradientButton createBtn = new GradientButton("Create Routine",
                new Color(255, 51, 153), new Color(255, 204, 51));
        createBtn.setBounds(bx, 220, bw, bh);
        createBtn.addActionListener(e -> {
            new CreateRoutinePage1();
            dispose();
        });
        bg.add(createBtn);

        GradientButton updateBtn = new GradientButton("Update Routine",
                new Color(255, 51, 153), new Color(255, 204, 51));
        updateBtn.setBounds(bx, 220 + gap, bw, bh);
        updateBtn.addActionListener(e -> {
            new UpdateRoutinePage1();
            dispose();
        });
        bg.add(updateBtn);

        GradientButton deleteBtn = new GradientButton("Delete Routine",
                new Color(255, 51, 153), new Color(255, 204, 51));
        deleteBtn.setBounds(bx, 220 + 2*gap, bw, bh);
        deleteBtn.addActionListener(e -> {
            new DeleteRoutinePage();
            dispose();
        });
        bg.add(deleteBtn);

        GradientButton logoutBtn = new GradientButton("Logout",
                new Color(255, 51, 153), new Color(255, 204, 51));
        logoutBtn.setBounds(bx, 220 + 3*gap, bw, bh);
        logoutBtn.addActionListener(e -> {
            new HomePage();
            dispose();
        });
        bg.add(logoutBtn);

        setVisible(true);
    }

    // ব্যাকগ্রাউন্ড প্যানেল ক্লাস
    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel() {
            // getResource দিয়ে ইমেজ লোড (Classpath থেকে)
            java.net.URL imgUrl = getClass().getResource("/resources/BackGround.png");
            if (imgUrl != null) {
                bgImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("Background image not found at /resources/BackGround.png");
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
