package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * 統一 UI 主題：nipponcolors - 柳鼠(Yanaginezumi)系
 * 目標：所有 UI 只要在建構子最後呼叫 UITheme.apply(this);
 */
public class UITheme {

    // Yanaginezumi palette (approx)
    public static final Color BG = new Color(244, 241, 234);          // 米色底
    public static final Color PANEL = new Color(250, 248, 244);       // 白米
    public static final Color PRIMARY = new Color(143, 138, 123);     // 柳鼠主色(灰褐)
    public static final Color PRIMARY_DARK = new Color(108, 104, 92); // 深柳鼠
    public static final Color ACCENT = new Color(207, 171, 123);      // 淡金點綴
    public static final Color TEXT = new Color(46, 43, 38);
    public static final Color MUTED = new Color(120, 116, 105);

    public static final Font H1   = new Font("Microsoft JhengHei", Font.BOLD, 22);
    public static final Font H2   = new Font("Microsoft JhengHei", Font.BOLD, 16);
    public static final Font BASE = new Font("Microsoft JhengHei", Font.PLAIN, 14);

    private static final Border CARD = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 216, 206)),
            BorderFactory.createEmptyBorder(10,10,10,10)
    );

    public static void apply(JFrame frame) {
        try {
            UIManager.put("Panel.background", BG);
            UIManager.put("OptionPane.background", BG);
            UIManager.put("OptionPane.messageForeground", TEXT);

            UIManager.put("Label.font", BASE);
            UIManager.put("Button.font", BASE);
            UIManager.put("TextField.font", BASE);
            UIManager.put("PasswordField.font", BASE);
            UIManager.put("TextArea.font", BASE);
            UIManager.put("Table.font", BASE);
            UIManager.put("TableHeader.font", H2);

            UIManager.put("Button.background", PRIMARY);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.select", PRIMARY_DARK);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("PasswordField.background", Color.WHITE);
            UIManager.put("TextArea.background", Color.WHITE);

            SwingUtilities.updateComponentTreeUI(frame);
            walk(frame.getContentPane());
        } catch (Exception ignored) {
        }
    }

    /** 套用到整棵 component tree：背景/字色/圓角感(用 border) */
    private static void walk(Component c) {
        if (c instanceof JPanel) {
            c.setBackground(BG);
            if (((JPanel) c).getBorder() != null) {
                // keep existing border
            }
        }
        if (c instanceof JLabel) {
            c.setForeground(TEXT);
            ((JLabel) c).setFont(((JLabel) c).getFont() == null ? BASE : ((JLabel) c).getFont());
        }
        if (c instanceof JButton) {
            JButton b = (JButton) c;
            b.setBackground(PRIMARY);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);

            // Buttons with icons：為了讓圖示清楚可見，改用淺底（不使用全域深色底）
            if (b.getIcon() != null) {
                b.setOpaque(true);
                b.setContentAreaFilled(true);
                b.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

                // Icon-only buttons：黑色線稿圖示 + 固定尺寸
                if (b.getText() == null || b.getText().trim().isEmpty()) {
                    b.setBackground(PANEL);
                    b.setForeground(TEXT);
                    b.setPreferredSize(new Dimension(44, 44));
                } else {
                    // Icon + text：用淺底避免圖示被底色吃掉
                    b.setBackground(PANEL);
                    b.setForeground(TEXT);
                    b.setIconTextGap(8);
                }
            }
        }

        if (c instanceof JTextField) {
            c.setBackground(Color.WHITE);
            c.setForeground(TEXT);
        }
        if (c instanceof JPasswordField) {
            c.setBackground(Color.WHITE);
            c.setForeground(TEXT);
        }
        if (c instanceof JTextArea) {
            c.setBackground(Color.WHITE);
            c.setForeground(TEXT);
        }
        if (c instanceof JTable) {
            JTable t = (JTable) c;
            t.setRowHeight(24);
            t.setFillsViewportHeight(true);
            JTableHeader th = t.getTableHeader();
            if (th != null) {
                th.setBackground(new Color(235, 231, 222));
                th.setForeground(TEXT);
            }
        }
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc.getBorder() != null && jc.getBorder().toString().contains("TitledBorder")) {
                // keep titled border
            } else if (jc instanceof JScrollPane) {
                // ok
            }
        }
        if (c instanceof Container) {
            for (Component ch : ((Container) c).getComponents()) walk(ch);
        }
    }

    public static Border cardBorder() { return CARD; }
}
