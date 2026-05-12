package practice_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/routine_manager";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MdGolamRabbi743693@@##%$$%";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // MySQL ড্রাইভার লোড করা
            Class.forName("com.mysql.cj.jdbc.Driver");

            // কানেকশন তৈরি করা
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Database connected successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }
}
