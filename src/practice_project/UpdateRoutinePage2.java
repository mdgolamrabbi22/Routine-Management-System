package practice_project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateRoutinePage2 extends JFrame {
    // Database credentials
    private static final String URL      = "jdbc:mysql://localhost:3306/routine_manager";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MdGolamRabbi743693@@##%$$%";

    // Parameters passed from Page1
    private String department, semester, group, shift;

    // UI components
    private JTable routineTable;
    private DefaultTableModel tableModel;
    private JTextField dayField, timeField, subjectField, teacherField, roomField;
    private JButton updateButton, backButton;
    private PillTitle headerTitle;

    public UpdateRoutinePage2(String department, String semester, String group, String shift) {
        this.department = department;
        this.semester   = semester;
        this.group      = group;
        this.shift      = shift;

        setTitle("Update Routine - " + department + " " + semester + " " + group + " " + shift);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // Logo top-left
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(10, 10, 60, 60);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new HomePage();
                dispose();
            }
        });
        bg.add(logoLabel);

        // Header title (centered)
        headerTitle = new PillTitle("Update Routine Page");
        headerTitle.setFont(new Font("Poppins", Font.BOLD, 18));
        headerTitle.setBounds((1366 - 300) / 2, 20, 300, 40);
        bg.add(headerTitle);

        // ======== FORM PANEL ========
        int formWidth  = 550;
        int formHeight = 375;
        int formX = (1366 - formWidth) / 2;
        int formY = 100;

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(formX, formY, formWidth, formHeight);
        formPanel.setBackground(new Color(255, 255, 255, 200));
        formPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        bg.add(formPanel);

        JLabel formTitle = new JLabel("   Update Routine");
        formTitle.setFont(new Font("Poppins", Font.BOLD, 36));
        formTitle.setForeground(new Color(20, 20, 80));
        formTitle.setBounds(0, 0, 350, 50);
        formPanel.add(formTitle);

        JLabel underline = new JLabel("   ______________________");
        underline.setFont(new Font("Poppins", Font.PLAIN, 24));
        underline.setForeground(new Color(100, 100, 150));
        underline.setBounds(0, 40, 350, 30);
        formPanel.add(underline);

        int fieldY = 90;
        int labelX = 20;
        int fieldX = 180;
        int fieldWidth = 300;
        int fieldHeight = 30;
        int verticalGap = 45;

        // Day
        JLabel dayLabel = new JLabel("   Day:");
        dayLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dayLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(dayLabel);

        dayField = new JTextField();
        dayField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(dayField);
        dayField.addActionListener(e -> timeField.requestFocus());

        // Time
        fieldY += verticalGap;
        JLabel timeLabel = new JLabel("   Time:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timeLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(timeField);
        timeField.addActionListener(e -> subjectField.requestFocus());

        // Subject Code
        fieldY += verticalGap;
        JLabel subjectLabel = new JLabel("   Subject Code:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subjectLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(subjectField);
        subjectField.addActionListener(e -> teacherField.requestFocus());

        // Teacher Name
        fieldY += verticalGap;
        JLabel teacherLabel = new JLabel("   Teacher Name:");
        teacherLabel.setFont(new Font("Arial", Font.BOLD, 18));
        teacherLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(teacherLabel);

        teacherField = new JTextField();
        teacherField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(teacherField);
        teacherField.addActionListener(e -> roomField.requestFocus());

        // Room No.
        fieldY += verticalGap;
        JLabel roomLabel = new JLabel("   Room No:");
        roomLabel.setFont(new Font("Arial", Font.BOLD, 18));
        roomLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(roomLabel);

        roomField = new JTextField();
        roomField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(roomField);
        roomField.addActionListener(e -> updateRoutineInDatabase());

        // Buttons: Back and Update
        fieldY += (verticalGap + 10);
        int buttonWidth = 200;
        int buttonHeight = 40;

        backButton = createGradientButton("Back", new Color(255, 102, 204), new Color(255, 204, 51));
        backButton.setFont(new Font("Poppins", Font.BOLD, 16));
        backButton.setBounds(labelX, fieldY, buttonWidth, buttonHeight);
        backButton.addActionListener(e -> {
            new UpdateRoutinePage1();
            dispose();
        });
        formPanel.add(backButton);

        updateButton = createGradientButton("Update", new Color(255, 102, 204), new Color(255, 204, 51));
        updateButton.setFont(new Font("Poppins", Font.BOLD, 16));
        updateButton.setBounds(labelX + buttonWidth + 20, fieldY, buttonWidth, buttonHeight);
        updateButton.addActionListener(e -> updateRoutineInDatabase());
        formPanel.add(updateButton);

        // ======== ROUTINE TABLE PANEL (WITHOUT ID COLUMN) ========
        int tableWidth  = 1300;
        int tableHeight = 220;
        int tableX = (1366 - tableWidth) / 2;
        int tableY = formY + formHeight + 20;

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBounds(tableX, tableY, tableWidth, tableHeight);
        tablePanel.setBackground(new Color(255, 255, 255, 230));
        bg.add(tablePanel);

        // Table model without ID
        tableModel = new DefaultTableModel(
                new String[]{"Day", "Time", "Subject Code", "Teacher Name", "Room No"},
                0
        );
        routineTable = new JTable(tableModel);
        routineTable.setRowHeight(30);
        routineTable.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Header styling
        JTableHeader header = routineTable.getTableHeader();
        header.setBackground(new Color(60, 120, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Alternating row colors
        routineTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        routineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate form on row click
        routineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = routineTable.getSelectedRow();
                if (row >= 0) {
                    dayField.setText(tableModel.getValueAt(row, 0).toString());
                    timeField.setText(tableModel.getValueAt(row, 1).toString());
                    subjectField.setText(tableModel.getValueAt(row, 2).toString());
                    teacherField.setText(tableModel.getValueAt(row, 3).toString());
                    roomField.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(routineTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Load data without ID
        loadRoutineData();

        setVisible(true);
    }

    /**
     * Load routines without ID into JTable
     */
    private void loadRoutineData() {
        String query =
                "SELECT day, time, subject_code, teacher_name, room_number " +
                        "FROM `routines` " +
                        "WHERE `department` = ? AND `semester` = ? AND `grp` = ? AND `shift` = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, department);
            stmt.setString(2, semester);
            stmt.setString(3, group);
            stmt.setString(4, shift);

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("day"),
                        rs.getString("time"),
                        rs.getString("subject_code"),
                        rs.getString("teacher_name"),
                        rs.getString("room_number")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading routine:\n" + ex.getMessage(),
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Update selected routine in database
     */
    private void updateRoutineInDatabase() {
        int selectedRow = routineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a routine to update.");
            return;
        }

        String day  = dayField.getText().trim();
        String time = timeField.getText().trim();
        String subj = subjectField.getText().trim();
        String teach= teacherField.getText().trim();
        String room = roomField.getText().trim();

        if (day.isEmpty() || time.isEmpty() || subj.isEmpty() || teach.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        String updateSql =
                "UPDATE `routines` " +
                        "SET `day` = ?, `time` = ?, `subject_code` = ?, `teacher_name` = ?, `room_number` = ? " +
                        "WHERE `department`=? AND `semester`=? AND `grp`=? AND `shift`=? AND `day`=? AND `time`=?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, day);
            stmt.setString(2, time);
            stmt.setString(3, subj);
            stmt.setString(4, teach);
            stmt.setString(5, room);
            stmt.setString(6, department);
            stmt.setString(7, semester);
            stmt.setString(8, group);
            stmt.setString(9, shift);
            // original day & time used in WHERE can be passed or tracked separately
            stmt.setString(10, /* originalDay */ day);
            stmt.setString(11, /* originalTime */ time);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Routine updated successfully.");
                loadRoutineData();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error updating routine:\n" + ex.getMessage(),
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Creates a gradient-style JButton
     */
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

    /**
     * Alternating row colors renderer
     */
    private static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        private static final Color EVEN_COLOR = new Color(224, 255, 255);
        private static final Color ODD_COLOR  = new Color(200, 255, 200);

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? EVEN_COLOR : ODD_COLOR);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    /**
     * BackgroundPanel: full-screen background image
     */
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
        SwingUtilities.invokeLater(() ->
                new UpdateRoutinePage2("CST", "3rd", "A1", "Morning")
        );
    }
}
