package practice_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteRoutinePage extends JFrame {
    JComboBox<String> deptBox, semesterBox, groupBox, shiftBox;
    JButton nextButton, backButton;
    JLabel logoLabel;

    public DeleteRoutinePage() {
        setTitle("Delete Routine");
        setSize(1366, 768);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Background
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
        PillTitle pill = new PillTitle("Delete Routine Page");
        pill.setBounds((1366-300)/2, 20, 300, 40);
        pill.setFont(new Font("Poppins", Font.BOLD, 18));
        bg.add(pill);

        // Form Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setOpaque(false);
        formPanel.setBounds(50, 120, 550, 600);
        bg.add(formPanel);

        // Form Title
        JLabel title = new JLabel("Delete Routine");
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
        String[] departments = {"Select Department", "CST", "ET", "CT", "MT", "PT", "ENT", "EMT"};
        String[] semesters = {"Select Semester", "1st", "3rd", "5th", "7th"};
        String[] shifts = {"Select Shift", "Morning", "Day"};
        String[] groups = {"Select Group", "A1", "A2", "B1", "B2", "C1", "C2"};

        deptBox = new JComboBox<>(departments);
        semesterBox = new JComboBox<>(semesters);
        shiftBox = new JComboBox<>(shifts);
        groupBox = new JComboBox<>(groups);

        addCombo(formPanel, "Department  :", deptBox, 0, 120);
        addCombo(formPanel, "Semester     :", semesterBox, 0, 190);
        addCombo(formPanel, "Shift              :", shiftBox, 0, 260);
        addCombo(formPanel, "Group           :", groupBox, 0, 330);

        // Next Button
        nextButton = createGradientButton("Delete Routine", new Color(255, 102, 204), new Color(255, 204, 51));
        nextButton.setFont(new Font("Poppins", Font.BOLD, 18));
        nextButton.setBounds(300, 520, 200, 50);
        nextButton.addActionListener(e -> handleNext());
        add(nextButton);

        // Back Button
        backButton = createGradientButton("Previous", new Color(255, 102, 204), new Color(255, 204, 51));
        backButton.setFont(new Font("Poppins", Font.BOLD, 18));
        backButton.setBounds(50, 520, 200, 50);
        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    private void handleNext() {
        // 1) Validate selections
        if (deptBox.getSelectedIndex() == 0 || semesterBox.getSelectedIndex() == 0 ||
                shiftBox.getSelectedIndex() == 0 || groupBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select all options",
                    "Incomplete Form",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dept     = (String) deptBox.getSelectedItem();
        String semester = (String) semesterBox.getSelectedItem();
        String shift    = (String) shiftBox.getSelectedItem();
        String group    = (String) groupBox.getSelectedItem();

        // 2) Check if exists in DB
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                    "JDBC Driver not found: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exists = false;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/routine_manager?useSSL=false&serverTimezone=UTC", "root", "MdGolamRabbi743693@@##%$$%")) {
            String query = "SELECT * FROM routines WHERE department=? AND semester=? AND shift=? AND `grp`=?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, dept);
                pst.setString(2, semester);
                pst.setString(3, shift);
                pst.setString(4, group);
                try (ResultSet rs = pst.executeQuery()) {
                    exists = rs.next();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!exists) {
            JOptionPane.showMessageDialog(this,
                    "This routine is not in the database.", // Not found
                    "Not Found",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3) Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Do you want to delete this routine?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // 4) Perform delete
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/routine_manager?useSSL=false&serverTimezone=UTC", "root", "MdGolamRabbi743693@@##%$$%")) {
            String delQuery = "DELETE FROM routines WHERE department=? AND semester=? AND shift=? AND `grp`=?";
            try (PreparedStatement pstDel = con.prepareStatement(delQuery)) {
                pstDel.setString(1, dept);
                pstDel.setString(2, semester);
                pstDel.setString(3, shift);
                pstDel.setString(4, group);
                int rows = pstDel.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Routine Deleted Successfully!",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Something went wrong, deletion could not be done.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Background panel
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

    // Gradient Button
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

    // ComboBox helper
    private void addCombo(JPanel p, String labelText, JComboBox<String> combo, int x, int y) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setBounds(x, y, 140, 40);
        p.add(lbl);
        combo.setBounds(150, y, 300, 40);
        p.add(combo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeleteRoutinePage::new);
    }
}