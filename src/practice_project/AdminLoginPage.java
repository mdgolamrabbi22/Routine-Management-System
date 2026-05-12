package practice_project;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginPage extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(1366, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);



        // Full-window background
        BackgroundPanel bg = new BackgroundPanel("/resources/BackGround.png");
        bg.setLayout(null);
        setContentPane(bg);

        PillTitle pill = new PillTitle("Admin Login Page");
        pill.setBounds((1366-300)/2, 20, 300, 40);
        bg.add(pill);


        // Top-left logo
        JLabel logoLabel = createLogoLabel();
        bg.add(logoLabel);

        // Title
        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("Poppins", Font.BOLD, 36));
        title.setForeground(new Color(20, 20, 80));
        title.setBounds(100, 215, 300, 50);
        add(title);

        // Underline
        JLabel ul = new JLabel("______________________");
        ul.setFont(new Font("Poppins", Font.PLAIN, 24));
        ul.setForeground(new Color(100, 100, 150));
        ul.setBounds(100, 260, 300, 30);
        add(ul);
        // Username panel
        // Username label and field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        userLabel.setBounds(100, 330, 100, 30);
        add(userLabel);

        userField = new JTextField("Enter your username");
        userField.setFont(new Font("Poppins", Font.PLAIN, 15));
        userField.setForeground(Color.GRAY);
        userField.setBounds(210, 330, 290, 40);
        userField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (userField.getText().equals("Enter your username")) {
                    userField.setText("");
                    userField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (userField.getText().isEmpty()) {
                    userField.setText("Enter your username");
                    userField.setForeground(Color.GRAY);
                }
            }
        });
        add(userField);

// Password label and field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        passLabel.setBounds(100, 390, 100, 30);
        add(passLabel);

        passField = new JPasswordField("Enter your password");
        passField.setFont(new Font("Poppins", Font.PLAIN, 15 ));
        passField.setForeground(Color.GRAY);
        passField.setBounds(210, 390, 290, 40);
        passField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                String passText = new String(passField.getPassword());
                if (passText.equals("Enter your password")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                    passField.setEchoChar('●'); // bullet char for password
                }
            }
            public void focusLost(FocusEvent e) {
                String passText = new String(passField.getPassword());
                if (passText.isEmpty()) {
                    passField.setText("Enter your password");
                    passField.setForeground(Color.GRAY);
                    passField.setEchoChar((char) 0); // show plain text
                }
            }
        });
        passField.setEchoChar((char) 0); // initially show placeholder text
        add(passField);


        // Login button



        // Enter key behavior
        userField.addActionListener(e -> passField.requestFocus());
        passField.addActionListener(e -> attemptLogin());

        // Login button
        JButton loginBtn = createGradientButton("Login",
                new Color(255,102,204), new Color(255,204,51));
        loginBtn.setFont(new Font("Poppins", Font.BOLD, 24));
        loginBtn.setBounds(210, 470, 300, 50);
        loginBtn.addActionListener(e -> attemptLogin());
        bg.add(loginBtn);

        setVisible(true);
    }

    private void attemptLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        // Dummy check — replace with real DB lookup
        if ("admin".equals(user) && "1234".equals(pass)) {
            new AdminDashboard();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            userField.setText("");
            passField.setText("");
            userField.requestFocus();
        }
    }

    private JLabel createLogoLabel() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel lbl = new JLabel(new ImageIcon(img));
        lbl.setBounds(10, 10, 60, 60);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new HomePage();
                dispose();
            }
        });
        return lbl;
    }

    private JPanel createInputPanel(JTextComponent field, String iconPath) {
        JPanel p = new JPanel(null);
        p.setOpaque(false);
        JLabel icon = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource(iconPath))
                        .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        ));
        icon.setBounds(10, 10, 30, 30);
        field.setBounds(0, 0, 500, 50);
        p.add(icon);
        p.add(field);
        return p;
    }

    private JButton createGradientButton(String text, Color c1, Color c2) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = getModel().isRollover();
                GradientPaint gp = new GradientPaint(
                        0, 0, hover ? c2 : c1,
                        getWidth(), 0, hover ? c1 : c2
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                Rectangle r = fm.getStringBounds(getText(), g2).getBounds();
                int tx = (getWidth() - r.width) / 2;
                int ty = (getHeight() - r.height) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private static class BackgroundPanel extends JPanel {
        private final Image bg;
        BackgroundPanel(String path) {
            bg = new ImageIcon(getClass().getResource(path)).getImage();
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private static class ClearOnFocus implements FocusListener {
        private final String placeholder;
        ClearOnFocus(String placeholder) {
            this.placeholder = placeholder;
        }
        public void focusGained(FocusEvent e) {
            JTextComponent c = (JTextComponent)e.getComponent();
            if (c.getText().equals(placeholder)) {
                c.setText("");
            }
        }
        public void focusLost(FocusEvent e) {
            JTextComponent c = (JTextComponent)e.getComponent();
            if (c.getText().isEmpty()) {
                c.setText(placeholder);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginPage::new);
    }
}
