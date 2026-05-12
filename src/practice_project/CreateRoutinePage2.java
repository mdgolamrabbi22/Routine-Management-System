package practice_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CreateRoutinePage2 extends JFrame {
    private JTextField dayField, timeField, subjectField, teacherField, roomField;
    private JButton createButton, backButton;
    private PillTitle headerTitle;
    private JTable table;
    private DefaultTableModel model;
    private String department, semester, shift, group;

    public CreateRoutinePage2(String dept, String sem, String shft, String grp) {
        this.department = dept;
        this.semester   = sem;
        this.shift      = shft;
        this.group      = grp;

        setTitle("Create Routine Page 2");
        setSize(1366, 768);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // ===== Logo top-left =====
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(10, 10, 60, 60);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreateRoutinePage1();
                dispose();
            }
        });
        bg.add(logoLabel);

        // ===== Header =====
        headerTitle = new PillTitle("Create Routine Page");
        headerTitle.setFont(new Font("Poppins", Font.BOLD, 18));
        headerTitle.setBounds((1366 - 300) / 2, 20, 300, 40);
        bg.add(headerTitle);

        // ===== Form Panel (centered horizontally) =====
        int formWidth  = 550;
        int formHeight = 375;
        int formX      = (1366 - formWidth) / 2;
        int formY      = 100;

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(formX, formY, formWidth, formHeight);
        formPanel.setBackground(new Color(255, 255, 255, 200)); // হালকা সাদা ব্যাকগ্রাউন্ড
        formPanel.setOpaque(true);
        formPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        bg.add(formPanel);

        JLabel formTitle = new JLabel("   Create Routine");
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

        JLabel dayLabel = new JLabel("   Day:");
        dayLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dayLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(dayLabel);

        dayField = new JTextField();
        dayField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(dayField);
        dayField.addActionListener(e -> timeField.requestFocus());

        fieldY += verticalGap;
        JLabel timeLabel = new JLabel("   Time:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timeLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(timeField);
        timeField.addActionListener(e -> subjectField.requestFocus());

        fieldY += verticalGap;
        JLabel subjectLabel = new JLabel("   Subject Code:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subjectLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(subjectField);
        subjectField.addActionListener(e -> teacherField.requestFocus());

        fieldY += verticalGap;
        JLabel teacherLabel = new JLabel("   Teacher Name:");
        teacherLabel.setFont(new Font("Arial", Font.BOLD, 18));
        teacherLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(teacherLabel);

        teacherField = new JTextField();
        teacherField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(teacherField);
        teacherField.addActionListener(e -> roomField.requestFocus());

        fieldY += verticalGap;
        JLabel roomLabel = new JLabel("   Room No:");
        roomLabel.setFont(new Font("Arial", Font.BOLD, 18));
        roomLabel.setBounds(labelX, fieldY, 160, 30);
        formPanel.add(roomLabel);

        roomField = new JTextField();
        roomField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        formPanel.add(roomField);
        roomField.addActionListener(e -> insertData());

        int buttonWidth = 200;
        int buttonHeight = 40;
        int btnY = fieldY + fieldHeight + 20;

        backButton = createGradientButton("Back", new Color(255, 102, 204), new Color(255, 204, 51));
        backButton.setFont(new Font("Poppins", Font.BOLD, 16));
        backButton.setBounds(labelX, btnY, buttonWidth, buttonHeight);
        backButton.addActionListener(e -> {
            new CreateRoutinePage1();
            dispose();
        });
        formPanel.add(backButton);

        createButton = createGradientButton("Create", new Color(255, 102, 204), new Color(255, 204, 51));
        createButton.setFont(new Font("Poppins", Font.BOLD, 16));
        createButton.setBounds(labelX + buttonWidth + 20, btnY, buttonWidth, buttonHeight);
        createButton.addActionListener(e -> insertData());
        formPanel.add(createButton);

        // ===== Table Panel (below form) =====
        int tableY = formY + formHeight + 20;
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Day", "Time", "Subject", "Teacher", "Room"});

        table = new JTable(model) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(60, 120, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(new Dimension(1100, 400));

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBounds(100, tableY, 1200, 220);
        tablePanel.setBackground(new Color(255, 255, 255, 230));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        bg.add(tablePanel);

        setVisible(true);
    }

    private void insertData() {
        String day     = dayField.getText().trim();
        String time    = timeField.getText().trim();
        String subject = subjectField.getText().trim();
        String teacher = teacherField.getText().trim();
        String room    = roomField.getText().trim();

        if (day.isEmpty() || time.isEmpty() || subject.isEmpty() || teacher.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.addRow(new String[]{day, time, subject, teacher, room});

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/routine_manager", "root", "MdGolamRabbi743693@@##%$$%");
             PreparedStatement pst = conn.prepareStatement(
                     "INSERT INTO routines (department, semester, shift, grp, day, `time`, subject_code, teacher_name, room_number) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, department);
            pst.setString(2, semester);
            pst.setString(3, shift);
            pst.setString(4, group);
            pst.setString(5, day);
            pst.setString(6, time);
            pst.setString(7, subject);
            pst.setString(8, teacher);
            pst.setString(9, room);
            pst.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }

        dayField.setText("");
        timeField.setText("");
        subjectField.setText("");
        teacherField.setText("");
        roomField.setText("");
        dayField.requestFocus();
    }

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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = getModel().isRollover();
                GradientPaint gp = new GradientPaint(0, 0, hover ? c2 : c1, getWidth(), 0, hover ? c1 : c2);
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

    private static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        private static final Color EVEN_COLOR = new Color(224, 255, 255);
        private static final Color ODD_COLOR  = new Color(200, 255, 200);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                comp.setBackground((row % 2 == 0) ? EVEN_COLOR : ODD_COLOR);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return comp;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new CreateRoutinePage2("CST", "5th", "Morning", "A1")
        );
    }
}
