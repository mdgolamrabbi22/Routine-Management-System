package practice_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

/**
 * ContactUsPage:
 * - Background pattern image
 * - Top bar with logo (left) and title centered
 * - Rounded white translucent content panel with bullet list of contacts
 */
public class ContactPage extends JFrame {

    JLabel logoLabel;


    public ContactPage() {
        super("Contact Us - BPI Seat Management");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);



        // Background
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 80));
        topBar.setBackground(new Color(40, 50, 60));

        // Header Title
        PillTitle pill = new PillTitle("Contact Us Page");
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
        JLabel title = new JLabel("Contact Us");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(title, BorderLayout.CENTER);

        bg.add(topBar, BorderLayout.NORTH);

        // Content panel
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

// Checkboxes for contact info
        Font infoFont = new Font("SansSerif", Font.PLAIN, 18);

        JLabel addrLabel = new JLabel("• Address: BPI, Barishal, Bangladesh");
        addrLabel.setFont(infoFont);
        addrLabel.setForeground(Color.BLACK);
        content.add(addrLabel);
        content.add(Box.createVerticalStrut(20));

        JLabel phoneLabel = new JLabel("• Phone: +880 1234 567890");
        phoneLabel.setFont(infoFont);
        phoneLabel.setForeground(Color.BLACK);
        content.add(phoneLabel);
        content.add(Box.createVerticalStrut(20));

        JLabel emailLabel = new JLabel("• Email: info@bpi.ac.bd");
        emailLabel.setFont(infoFont);
        emailLabel.setForeground(Color.BLACK);
        content.add(emailLabel);


        setVisible(true);
    }

    /**
     * Panel with rounded corners and background color
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
        SwingUtilities.invokeLater(ContactPage::new);
    }
}
