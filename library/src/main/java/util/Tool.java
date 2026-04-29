package util;

import javax.swing.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

/**
 * Tool：存放與 model 無關的單一性 static method
 */
public class Tool {

    //private static final Properties PROPS = new Properties();
    private static final Random RND = new Random();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
  // get connection (優先讀取 src/main/resources/application.properties)
    public static Connection getDB() {
        try {
            Properties p = new Properties();
            try (InputStream in = Tool.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in != null) {
                    p.load(in);
                }
            } catch (IOException ignored) {}

            String url = p.getProperty("db.url", "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=Asia/Taipei&useUnicode=true&characterEncoding=utf8");
            String user = p.getProperty("db.user", "root");
            String pass = p.getProperty("db.password", "1234");

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            throw new RuntimeException("資料庫連線失敗：" + e.getMessage(), e);
        }
    }
    
    /** 書籍條碼：B + 8碼 */
    public static String newBookBarcode() {
        return "B" + String.format("%08d", 10000000 + RND.nextInt(90000000));
    }

    /** 讀者編號：R + 8碼 */
    public static String newReaderId() {
        return "R" + String.format("%08d", 10000000 + RND.nextInt(90000000));
    }
    public static String today() {
        return LocalDate.now().format(DF);
    }

    public static String plusDays(String yyyyMMdd, int days) {
        LocalDate d = LocalDate.parse(yyyyMMdd, DF);
        return d.plusDays(days).format(DF);
    }

    /** jar 打包後仍可讀取資源圖示 */
    public static ImageIcon loadIcon(String classpath) {
        try (InputStream in = Tool.class.getResourceAsStream(classpath)) {

            if (in == null) {
                System.err.println("找不到圖示資源：" + classpath);
                return null;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            return new ImageIcon(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // ===== ResultSet safe getters (避免資料庫 view / 欄位差異造成整個 UI 崩潰) =====
    public static boolean hasColumn(ResultSet rs, String col) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            for (int i = 1; i <= n; i++) {
                String label = md.getColumnLabel(i);
                String name = md.getColumnName(i);
                if (col.equalsIgnoreCase(label) || col.equalsIgnoreCase(name)) return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static String getStringSafe(ResultSet rs, String col) throws SQLException {
        if (!hasColumn(rs, col)) return null;
        return rs.getString(col);
    }

    public static Integer getIntSafe(ResultSet rs, String col) throws SQLException {
        if (!hasColumn(rs, col)) return null;
        int v = rs.getInt(col);
        return rs.wasNull() ? null : v;
    }

    public static void infoBox(String msg) {
        JOptionPane.showMessageDialog(null, msg, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void errorBox(String msg) {
        JOptionPane.showMessageDialog(null, msg, "錯誤", JOptionPane.ERROR_MESSAGE);
    }
}
