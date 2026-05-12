# 📅 Routine Management System
### Java Swing Desktop Application — Polytechnic Institute

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/GUI-Java_Swing-ED8B00?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-JDBC-4479A1?style=flat-square&logo=mysql)
![IDE](https://img.shields.io/badge/IDE-IntelliJ_IDEA-000000?style=flat-square&logo=intellijidea)

---

## 📌 প্রজেক্ট সম্পর্কে

**Routine Management System** একটি Java Swing-ভিত্তিক desktop GUI application যা পলিটেকনিক ইনস্টিটিউটের class routine manage করে। Admin login করে routine তৈরি, update ও delete করতে পারেন। Student বিভাগ, সেমিস্টার, শিফট ও গ্রুপ অনুযায়ী routine দেখতে পারে।

---

## ✨ মূল ফিচারসমূহ

### 🔐 Authentication
- Admin login (username + password)
- Session management — login ছাড়া management pages access হয় না

### 📋 Routine Operations (CRUD)
| অপারেশন | বিবরণ |
|----------|-------|
| **Create** | 2-step wizard দিয়ে নতুন routine তৈরি |
| **View** | Department / Semester / Shift / Group অনুযায়ী filter করে দেখা |
| **Update** | Existing routine সম্পাদনা করা |
| **Delete** | Routine মুছে ফেলা |

### 🖥️ UI Features
- **Background image** সহ custom UI design
- **Custom gradient buttons** (GradientButton class)
- **Alternating row colors** — table-এ পালাক্রমে রঙ
- **2-step wizard** — routine তৈরিতে ধাপে ধাপে input নেওয়া
- About ও Contact pages

### 🔍 Filter System
- Department অনুযায়ী filter
- Semester অনুযায়ী filter
- Shift (সকাল/বিকাল) অনুযায়ী filter
- Group (A/B) অনুযায়ী filter

---

## 🗂️ প্রজেক্ট স্ট্রাকচার

```
Routine Management-1/
├── src/
│   └── practice_project/
│       ├── AdminLoginPage.java      # Admin login window (1366x768)
│       ├── AdminDashboard.java      # Admin control panel
│       ├── HomePage.java            # Main home/welcome page
│       ├── CreateRoutinePage1.java  # Routine creation — Step 1
│       ├── CreateRoutinePage2.java  # Routine creation — Step 2 (save)
│       ├── UpdateRoutinePage1.java  # Routine update — Step 1 (select)
│       ├── UpdateRoutinePage2.java  # Routine update — Step 2 (edit)
│       ├── DeleteRoutinePage.java   # Routine deletion
│       ├── RoutineViewPage.java     # Routine view with filters
│       ├── DBConnection.java        # JDBC MySQL connection
│       ├── GradientButton.java      # Custom gradient button component
│       ├── AboutPage.java           # About page
│       └── ContactPage.java         # Contact page
│
└── out/
    └── production/                  # Compiled .class files
```

---

## ⚙️ ইনস্টলেশন

### Requirements
- JDK 17 বা তার উপরে
- MySQL 8.x
- IntelliJ IDEA (recommended) বা Eclipse
- MySQL Connector/J (JAR)

### ধাপ ১ — Database তৈরি করো
```sql
CREATE DATABASE routine_manager;
USE routine_manager;

-- Routine table (example structure):
CREATE TABLE routine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    shift VARCHAR(20) NOT NULL,
    group_name VARCHAR(10),
    day VARCHAR(15) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    teacher VARCHAR(100),
    start_time TIME,
    end_time TIME,
    room VARCHAR(20)
);
```

### ধাপ ২ — DB Connection আপডেট করো
`DBConnection.java` খুলে নিজের credentials দাও:
```java
private static final String URL = "jdbc:mysql://localhost:3306/routine_manager";
private static final String USERNAME = "root";
private static final String PASSWORD = "তোমার_password"; // এটা পরিবর্তন করো
```

### ধাপ ৩ — MySQL Connector JAR যোগ করো
```
IntelliJ IDEA:
File → Project Structure → Libraries → + → mysql-connector-j-x.x.x.jar
```

### ধাপ ৪ — প্রজেক্ট রান করো
```
src/practice_project/AdminLoginPage.java → Run করো
```

---

## 🔑 Default Admin Login

| Field | Value |
|-------|-------|
| Username | admin |
| Password | (database-এ set আছে যেটা) |

---

## 🧠 গুরুত্বপূর্ণ Java Concepts ব্যবহার

```java
// Custom Gradient Button (OOP — extends JButton)
public class GradientButton extends JButton {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(...);
        g2d.setPaint(gp);
        // Custom rendering
    }
}

// JDBC Connection (Singleton-style)
public static Connection getConnection() {
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(URL, USERNAME, PASSWORD);
}

// Inner class for custom background (OOP — inner class)
class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) { ... }
}
```

---

## 🛠️ Technologies Used

| Technology | কী কাজে লেগেছে |
|------------|----------------|
| Java 17+ | মূল programming language |
| Java Swing | GUI framework |
| JDBC | Database connectivity |
| MySQL | Data storage |
| IntelliJ IDEA | Development IDE |
| OOP Concepts | Inheritance, Inner classes, Custom components |

---

## 📐 Application Window Size

সকল windows: **1366 × 768 pixels** (Full HD laptop resolution-এর জন্য optimized)

---

## 🔮 উন্নয়নের সুযোগ

- JavaFX দিয়ে আধুনিক UI (Swing outdated)
- PDF routine export
- Student login (read-only view)
- Conflict detection (একই সময়ে একই room-এ দুটো class)
- Dark mode

---

## 👨‍💻 Developer Md Golam Rabbi

**Institution:** Barisal Polytechnic Institute  
**Project Type:** Desktop Application  
**Domain:** Java Development, OOP, JDBC  
**IDE:** IntelliJ IDEA
