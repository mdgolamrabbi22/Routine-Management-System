package practice_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class RoutineViewPage extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    // MySQL ডাটাবেস ক্রেডেনশিয়ালস (প্রয়োজনে আপনার মতো পরিবর্তন করবেন)
    private static final String URL  = "jdbc:mysql://localhost:3306/routine_manager";
    private static final String USER = "root";
    private static final String PASS = "MdGolamRabbi743693@@##%$$%";

    public RoutineViewPage(String dept, String sem, String shift, String group) {
        super("View Routine");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ১) পুরো JFrame-এ BackgroundPanel ব্যবহার
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(null);
        setContentPane(bg);

        // ২) উপরে বামে HomePage-এ যেভাবে ছিল সেই লোগো
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

        // ৩) উপরের শিরোনাম: “View Routine”
        PillTitle heading = new PillTitle("View Routine");
        heading.setBounds((1366 - 400) / 2, 30, 400, 40);
        heading.setFont(new Font("Poppins", Font.BOLD, 28));
        bg.add(heading);

        // ৪) JTable এবং ScrollPane-সহ একটি ট্রান্সপারেন্ট প্যানেল
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBounds(100, 120, 1166, 450);
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bg.add(tablePanel);

        // ৪.১) DefaultTableModel + JTable তৈরি (Pivot বাদ দিয়ে সরাসরি রেকর্ড)
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Day", "Time", "Subject Name", "Teacher’s Name", "Room"
        });

        table = new JTable(model) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));

        // কলাম খুব চওড়া হলে হরিজন্টাল স্ক্রল দেখানোর জন্য
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Header styling (নীল ব্যাকগ্রাউন্ড, সাদা লেখা)
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(60, 120, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setReorderingAllowed(false);

        // Alternating row color renderer
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // JScrollPane এ বসিয়ে দেওয়া
        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ৫) নিচে “Download PDF” বাটন (গ্রেডিয়েন্ট)
        JButton downloadBtn = createGradientButton("Download PDF",
                new Color(60, 120, 180), new Color(20, 80, 120));
        downloadBtn.setFont(new Font("Poppins", Font.BOLD, 18));
        downloadBtn.setBounds((1366 - 220) / 2, 600, 220, 50);
        downloadBtn.addActionListener(e -> createPDF());
        bg.add(downloadBtn);

        // ৬) ডাটাবেস থেকে সরাসরি রুটিন লোড করা
        loadRoutineDirectlyFromDatabase(dept, sem, shift, group);

        setVisible(true);
    }

    /** BackgroundPanel: পুরো উইন্ডোর ব্যাকগ্রাউন্ডে BackGround.png **/
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

    /** AlternatingRowRenderer: JTable-এ alternating row colors (light cyan & light green) **/
    private static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        private static final Color EVEN_COLOR = new Color(224, 255, 255);
        private static final Color ODD_COLOR  = new Color(200, 255, 200);

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column
        ) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
            );
            if (!isSelected) {
                comp.setBackground((row % 2 == 0) ? EVEN_COLOR : ODD_COLOR);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return comp;
        }
    }

    /**
     * গ্রেডিয়েন্ট বাটন তৈরির মেথড (HomePage-এ ব্যবহার করেছেন যেভাবে)
     * @param text বাটনের লেখা
     * @param c1 বাটনের প্রথম রঙ
     * @param c2 বাটনের হোভার/গ্রেডিয়েন্ট শেষে রঙ
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
     * ডাটাবেস থেকে সরাসরি রুটিন লোড করার মেথড:
     * আমরা এখানে Day, Time, Subject Name, Teacher’s Name, Room আকারে রেকর্ড লোড করবো
     */
    private void loadRoutineDirectlyFromDatabase(String dept, String sem, String shift, String group) {
        // মডেল থেকে আগের কোনো রেকর্ডে থাকলে ক্লিয়ার করে ফেলতে হবে
        model.setRowCount(0);

        String sql = "SELECT day, `time`, subject_code AS subjectName, teacher_name AS teacherName, room_number AS room " +
                "FROM `routines` " +
                "WHERE department = ? AND semester = ? AND shift = ? AND grp = ? " +
                "ORDER BY FIELD(day, 'Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'), " +
                "       STR_TO_DATE(`time`, '%h:%i %p');";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dept);
            pst.setString(2, sem);
            pst.setString(3, shift);
            pst.setString(4, group);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String day       = rs.getString("day");
                String time      = rs.getString("time");
                String subject   = rs.getString("subjectName");
                String teacher   = rs.getString("teacherName");
                String roomNo    = rs.getString("room");
                model.addRow(new Object[]{ day, time, subject, teacher, roomNo });
            }
            rs.close();

            // যদি রেকর্ড না পাওয়া যায়, তাহলে একটি পপআপ দেখিয়ে HomePage-এ ফিরিয়ে দিই
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No routines were found according to the selected criteria.\n",
                        "No Routine",
                        JOptionPane.WARNING_MESSAGE
                );
                new HomePage();
                dispose();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            new HomePage();
            dispose();
        }
    }

    /**
     * PDF তৈরি করার মেথড:
     * - iText ব্যবহার করে A4 landscape সাইজে ডকুমেন্ট তৈরি করবে
     * - শিরোনাম “Routine Schedule” সেন্টারে লিখবে
     * - JTable থেকে Column ও Row পড়ে PdfPTable-এ বসাবে (Header নীল ব্যাকগ্রাউন্ড)
     * - শেষমেশ $HOME/Downloads/routine.pdf ফাইলে সেভ করবে
     */
    private void createPDF() {
        try {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            String pdfPath = System.getProperty("user.home") + "/Downloads/routine.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Title Font
            com.itextpdf.text.Font titleFont =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titlePara = new Paragraph("Routine Schedule\n\n", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);

            // PdfPTable তৈরি করা
            int colCount = model.getColumnCount();
            PdfPTable pdfTable = new PdfPTable(colCount);
            pdfTable.setWidthPercentage(100);

            // Column width: প্রথম কলাম একটু সংকীর্ণ, বাকিগুলো বড়
            float[] widths = new float[colCount];
            widths[0] = 2f;
            for (int i = 1; i < colCount; i++) widths[i] = 3f;
            pdfTable.setWidths(widths);

            // Header Row (iText)
            com.itextpdf.text.Font headerFont =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            for (int c = 0; c < colCount; c++) {
                PdfPCell cell = new PdfPCell(
                        new Phrase(model.getColumnName(c), headerFont)
                );
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(60, 120, 180));
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }

            // Data Rows (iText)
            com.itextpdf.text.Font cellFont =
                    FontFactory.getFont(FontFactory.HELVETICA, 11);
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < colCount; c++) {
                    String text = Objects.toString(model.getValueAt(r, c), "");
                    PdfPCell cell = new PdfPCell(new Phrase(text, cellFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(6);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(
                    this,
                    "PDF Download Success :\n" + pdfPath,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "PDF Error while creating:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /** টেস্ট করার জন্য মেইন মেথড (ডেমো) **/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new RoutineViewPage("CST", "3rd", "Morning", "A1")
        );
    }
}
