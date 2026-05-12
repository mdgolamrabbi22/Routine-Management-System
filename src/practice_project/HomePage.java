package practice_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * HomePage (Updated):
 * - Criteria সিলেকশন চেক করবে (যেমন “Select …” থাকলে সাবমিট দিবে না)
 * - ডাটাবেসে চেক করে দেখবে ঐ Criteria অনুযায়ী রুটিন আছে কি না
 *   • যদি না থাকে: “নির্বাচিত Criteria অনুযায়ী কোনো রুটিন পাওয়া যায়নি।” বলে পপআপ দেখাবে
 *   • যদি থাকে: RoutineViewPage চালু করবে
 * - আপনার দেয়া UI স্ক্রিন অনুযায়ী ব্যাকগ্রাউন্ড, লোগো, গ্রেডিয়েন্ট বাটন, Poppins ফন্ট ইত্যাদি বজায় রাখা হয়েছে
 */
public class HomePage extends JFrame {
    private JComboBox<String> departmentBox, semesterBox, shiftBox, groupBox;
    private JLabel logoLabel;

    // MySQL ডাটাবেস ক্রেডেনশিয়ালস (প্রয়োজনে পাল্টিয়ে নেবেন)
    private static final String URL = "jdbc:mysql://localhost:3306/routine_manager";
    private static final String USER = "root";
    private static final String PASS = "MdGolamRabbi743693@@##%$$%";

    public HomePage() {
        setTitle("Home Page");
        setSize(1366, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ১) পুরো ব্যাকগ্রাউন্ডে BackGround.png
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // ২) উপরে বাম লোগো (HomePage এ ক্লিক করলে নিজেকে রি-লোড করবে)
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(10, 10, 60, 60);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ইতিমধ্যে HomePage, তবে রিফ্রেশ হিসেবে আবার HomePage চালাতে পারেন
                new HomePage();
                dispose();
            }
        });
        bg.add(logoLabel);

        // ৩) হেডার টাইটেল (PillTitle)
        PillTitle pill = new PillTitle("Home Page");
        pill.setBounds((1366 - 400) / 2, 20, 300, 40);
        pill.setFont(new Font("Poppins", Font.BOLD, 18));
        bg.add(pill);

        // ৪) Admin Login, About Us, Contact Us বাটন (গ্রেডিয়েন্ট)
        JButton adminBtn = createGradientButton("Admin Login",
                new Color(255, 102, 204), new Color(102, 204, 255));
        adminBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        adminBtn.setBounds(1200, 20, 130, 40);
        adminBtn.addActionListener(e -> {
            new AdminLoginPage();
            dispose();
        });
        bg.add(adminBtn);

        JButton aboutBtn = createGradientButton("About Us",
                new Color(255, 102, 204), new Color(102, 204, 255));
        aboutBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        aboutBtn.setBounds(1060, 20, 130, 40);
        aboutBtn.addActionListener(e -> {
            new AboutPage();
            dispose();
        });
        bg.add(aboutBtn);

        JButton contactBtn = createGradientButton("Contact Us",
                new Color(255, 102, 204), new Color(102, 204, 255));
        contactBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        contactBtn.setBounds(920, 20, 130, 40);
        contactBtn.addActionListener(e -> {
            new ContactPage();
            dispose();
        });
        bg.add(contactBtn);

        // ৫) ফর্ম প্যানেল (ট্রান্সপারেন্ট)
        JPanel formPanel = new JPanel(null);
        formPanel.setOpaque(false);
        formPanel.setBounds(50, 120, 550, 600);
        bg.add(formPanel);

        // ৬) ফর্মের শিরোনাম
        JLabel title = new JLabel("View Routine");
        title.setFont(new Font("Poppins", Font.BOLD, 36));
        title.setForeground(new Color(20, 20, 80));
        title.setBounds(0, 0, 300, 50);
        formPanel.add(title);

        JLabel ul = new JLabel("______________________");
        ul.setFont(new Font("Poppins", Font.PLAIN, 24));
        ul.setForeground(new Color(100, 100, 150));
        ul.setBounds(0, 40, 300, 30);
        formPanel.add(ul);

        // ৭) ComboBox Definitions
        String[] departments = {
                "Select Department", "CST", "ET", "CT", "MT", "PT", "ENT", "EMT"
        };
        String[] semesters = {
                "Select Semester", "1st", "3rd", "5th", "7th"
        };
        String[] shifts = {
                "Select Shift", "Morning", "Day"
        };
        String[] groups = {
                "Select Group", "A1", "A2", "B1", "B2", "C1", "C2"
        };

        departmentBox = new JComboBox<>(departments);
        semesterBox   = new JComboBox<>(semesters);
        shiftBox      = new JComboBox<>(shifts);
        groupBox      = new JComboBox<>(groups);

        addCombo(formPanel, "Department  :", departmentBox, 0, 120);
        addCombo(formPanel, "Semester     :", semesterBox,   0, 190);
        addCombo(formPanel, "Shift              :", shiftBox,     0, 260);
        addCombo(formPanel, "Group           :", groupBox,       0, 330);

        // ৮) View Routine বাটন
        JButton viewBtn = createGradientButton("View Routine",
                new Color(255, 102, 204), new Color(255, 204, 51));
        viewBtn.setFont(new Font("Poppins", Font.BOLD, 18));
        viewBtn.setBounds(150, 420, 300, 50);
        viewBtn.addActionListener(e -> showRoutine());
        formPanel.add(viewBtn);

        setVisible(true);
    }

    /** BackGround.png দিয়ে ব্যাকগ্রাউন্ড প্যানেল **/
    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel() {
            bgImage = new ImageIcon(getClass().getResource("/resources/BackGround.png")).getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // ব্যাকগ্রাউন্ড ইমেজ পুরো জায়গায় স্ট্রেচ করে অঙ্কন
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /** গ্রেডিয়েন্ট + হ্যান্ড কার্সর বাটন ***/
    private JButton createGradientButton(String text, Color c1, Color c2) {
        return new JButton(text) {
            {
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = getModel().isRollover();
                GradientPaint gp = new GradientPaint(
                        0, 0,
                        hover ? c2 : c1,
                        getWidth(), 0,
                        hover ? c1 : c2
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                FontMetrics fm = g2.getFontMetrics();
                Rectangle r = fm.getStringBounds(getText(), g2).getBounds();
                int tx = (getWidth() - r.width) / 2;
                int ty = (getHeight() - r.height) / 2 + fm.getAscent();
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
    }

    /** JLabel + JComboBox হেল্পার **/
    private void addCombo(JPanel p, String labelText, JComboBox<String> combo, int x, int y) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setBounds(x, y, 140, 40);
        p.add(lbl);
        combo.setBounds(150, y, 300, 40);
        p.add(combo);
    }

    /** ShowRoutine: Criteria ভরা হয়েছে কিনা এবং ডাটাবেসে রুটিন আছে কিনা চেক করবে **/
    private void showRoutine() {
        String d  = (String) departmentBox.getSelectedItem();
        String s  = (String) semesterBox.getSelectedItem();
        String sh = (String) shiftBox.getSelectedItem();
        String g  = (String) groupBox.getSelectedItem();

        // ১) Validate: “Select …” থাকলে এগিয়ে যাবে না
        if (d.startsWith("Select") || s.startsWith("Select") ||
                sh.startsWith("Select") || g.startsWith("Select")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select all options!",
                    "Missing Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ২) Database check: যদি রুটিন না থাকে, পপআপ দেখাবে
        if (!routineExists(d, s, sh, g)) {
            JOptionPane.showMessageDialog(
                    this,
                    "নির্বাচিত Criteria অনুযায়ী কোনো রুটিন পাওয়া যায়নি।",
                    "No Routine",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ৩) সবকিছু ঠিক থাকলে RoutineViewPage চালু করবে
        new RoutineViewPage(d, s, sh, g);
        dispose();
    }

    /**
     * ডাটাবেসে চেক করবে যে নির্বাচিত Dept/Sem/Shift/Group অনুযায়ী
     * টেবিলে কোনো রুটিন রেকর্ড আছে কি না
     */
    private boolean routineExists(String dept, String sem, String shift, String group) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) AS cnt FROM routines " +
                "WHERE department = ? AND semester = ? AND shift = ? AND grp = ?;";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dept);
            pst.setString(2, sem);
            pst.setString(3, shift);
            pst.setString(4, group);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                exists = (count > 0);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "ডাটাবেস ত্রুটি:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exists;
    }

    // টেস্ট করার জন্য মেইন মেথড
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
