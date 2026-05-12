package practice_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CreateRoutinePage1 extends JFrame {
    JComboBox<String> deptBox, semesterBox, groupBox, shiftBox;
    JButton nextButton, backButton;
    JLabel logoLabel;

    // Database credentials
    private static final String URL      = "jdbc:mysql://localhost:3306/routine_manager";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MdGolamRabbi743693@@##%$$%";

    public CreateRoutinePage1() {
        setTitle("Create Routine - Page 1");
        setSize(1366, 768);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Custom Background Panel
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // Logo top-left
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

        // Header Title
        PillTitle pill = new PillTitle("Create Routine Page");
        pill.setBounds((1366 - 300) / 2, 20, 300, 40);
        pill.setFont(new Font("Poppins", Font.BOLD, 18));
        bg.add(pill);

        // Form Panel (Transparent)
        JPanel formPanel = new JPanel(null);
        formPanel.setOpaque(false);
        formPanel.setBounds(50, 120, 550, 600);
        bg.add(formPanel);

        // Form Title
        JLabel title = new JLabel("Create Routine");
        title.setFont(new Font("Poppins", Font.BOLD, 36));
        title.setForeground(new Color(20, 20, 80));
        title.setBounds(0, 0, 300, 50);
        formPanel.add(title);

        JLabel ul = new JLabel("______________________");
        ul.setFont(new Font("Poppins", Font.PLAIN, 24));
        ul.setForeground(new Color(100, 100, 150));
        ul.setBounds(0, 40, 300, 30);
        formPanel.add(ul);

        // ComboBox Definitions
        String[] departments = { "Select Department", "CST", "ET", "CT", "MT", "PT", "ENT", "EMT" };
        String[] semesters   = { "Select Semester", "1st", "3rd", "5th", "7th" };
        String[] shifts      = { "Select Shift", "Morning", "Day" };
        String[] groups      = { "Select Group", "A1", "A2", "B1", "B2", "C1", "C2" };

        deptBox     = new JComboBox<>(departments);
        semesterBox = new JComboBox<>(semesters);
        shiftBox    = new JComboBox<>(shifts);
        groupBox    = new JComboBox<>(groups);

        addCombo(formPanel, "Department  :", deptBox, 0, 120);
        addCombo(formPanel, "Semester     :", semesterBox, 0, 190);
        addCombo(formPanel, "Shift              :", shiftBox, 0, 260);
        addCombo(formPanel, "Group           :", groupBox, 0, 330);

        // Next Button
        nextButton = createGradientButton("Next", new Color(255, 102, 204), new Color(255, 204, 51));
        nextButton.setFont(new Font("Poppins", Font.BOLD, 18));
        nextButton.setBounds(300, 520, 200, 50);
        nextButton.addActionListener(e -> {
            // Validate selections
            if (deptBox.getSelectedIndex() == 0 ||
                    semesterBox.getSelectedIndex() == 0 ||
                    shiftBox.getSelectedIndex() == 0 ||
                    groupBox.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(this,
                        "Please select all options",
                        "Incomplete Form",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String department = deptBox.getSelectedItem().toString();
            String semester   = semesterBox.getSelectedItem().toString();
            String shift      = shiftBox.getSelectedItem().toString();
            String group      = groupBox.getSelectedItem().toString();

            // Check database for existing routine
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement pst = conn.prepareStatement(
                         "SELECT 1 FROM routines WHERE department=? AND semester=? AND shift=? AND grp=?")) {

                pst.setString(1, department);
                pst.setString(2, semester);
                pst.setString(3, shift);
                pst.setString(4, group);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    // Found existing routine
                    JOptionPane.showMessageDialog(this,
                            "Routine already exists!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // No existing routine: move to Page2
                    new CreateRoutinePage2(department, semester, shift, group);
                    dispose();
                }
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database error occurred.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        add(nextButton);

        // Back Button
        backButton = createGradientButton("Back", new Color(255, 102, 204), new Color(255, 204, 51));
        backButton.setFont(new Font("Poppins", Font.BOLD, 18));
        backButton.setBounds(50, 520, 200, 50);
        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    /** JLabel + JComboBox Helper **/
    private void addCombo(JPanel p, String labelText, JComboBox<String> combo, int x, int y) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setBounds(x, y, 140, 40);
        p.add(lbl);

        combo.setBounds(150, y, 300, 40);
        p.add(combo);
    }

    /** Gradient-style JButton Helper **/
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

    /** Background panel with full-screen image **/
    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel() {
            bgImage = new ImageIcon(getClass().getResource("/resources/BackGround.png")).getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateRoutinePage1::new);
    }
}
