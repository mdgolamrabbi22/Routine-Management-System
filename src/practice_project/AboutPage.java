package practice_project;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * AboutUsPage:
 * - Patterned background image
 * - Top bar with logo (left) and centered title
 * - Rounded translucent content panel with about text
 */
public class AboutPage extends JFrame {

    JLabel logoLabel;

    public AboutPage() {
        super("About Us - BPI Seat Management");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background panel
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 80));
        topBar.setBackground(new Color(40, 50, 60));

        // Header Title
        PillTitle pill = new PillTitle("About Us Page");
        pill.setBounds((1366-300)/2, 20, 300, 40);
        pill.setFont(new Font("Poppins", Font.BOLD, 18));
        bg.add(pill);

        // Logo left
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

        // Title center
        JLabel title = new JLabel("About Us");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(title, BorderLayout.CENTER);

        bg.add(topBar, BorderLayout.NORTH);

        // Content panel
        RoundedPanel content = new RoundedPanel(20, new Color(255, 255, 255, 200));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(40, 40, 40, 40));
        content.setOpaque(false);
        int margin = 100;
        content.setBounds(margin, 140, getWidth() - 2 * margin, 500);
        bg.setLayout(null);
        bg.add(topBar);
        bg.add(content);

        // About text
        Font textFont = new Font("SansSerif", Font.PLAIN, 18);

// Line 1: অ্যাপ্লিকেশন পরিচিতি
        JLabel line1 = new JLabel("Welcome to BPI Routine Management.");
        line1.setFont(textFont);
        line1.setForeground(Color.BLACK);
        content.add(line1);
        content.add(Box.createVerticalStrut(20));

// Line 2: মূল ফিচার
        JLabel line2 = new JLabel("Easily view and download your class schedules.");
        line2.setFont(textFont);
        line2.setForeground(Color.BLACK);
        content.add(line2);
        content.add(Box.createVerticalStrut(10));

// Line 3: ইউজার বেনিফিট
        JLabel line3 = new JLabel("Stay organized with up-to-date routines in PDF.");
        line3.setFont(textFont);
        line3.setForeground(Color.BLACK);
        content.add(line3);

        setVisible(true);
    }

    /**
     * Panel with rounded corners
     */
    static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    /**
     * BackgroundPanel draws tiled pattern image
     */
    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel() {
            bgImage = new ImageIcon(getClass().getResource("/resources/BackGround.png")).getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int iw = bgImage.getWidth(this);
            int ih = bgImage.getHeight(this);
            for (int x = 0; x < getWidth(); x += iw) {
                for (int y = 0; y < getHeight(); y += ih) {
                    g.drawImage(bgImage, x, y, this);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AboutPage::new);
    }
}