package controller.admin;

import controller.LoginUI;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import java.awt.*;

/**
 * AdminTopUI：後台首頁
 * 依需求：可透過按鈕選擇分頁（各頁面都有返回首頁按鈕）
 */
public class AdminTopUI extends JFrame {

    public AdminTopUI() {
        setTitle("館員後台 - 愛讀公共圖書館");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JLabel title = new JLabel("館員後台系統");
        title.setFont(UITheme.H1);
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(0,2,12,12));

        JButton btn0 = new JButton("匯入書籍");
        btn0.addActionListener(e -> new ImportUI().setVisible(true));

        JButton btn1 = new JButton("書籍資料");
        btn1.addActionListener(e -> new BookDataUI().setVisible(true));

        JButton btn2 = new JButton("讀者資料（註冊/修改/查詢）");
        btn2.addActionListener(e -> new ReaderDataUI().setVisible(true));

        JButton btn3 = new JButton("書籍歸還（待核可）");
        btn3.addActionListener(e -> new BookReturnUI().setVisible(true));

        JButton btn4 = new JButton("讀書心得（核可/發佈）");
        btn4.addActionListener(e -> new ReviewCheckUI().setVisible(true));

        JButton btn5 = new JButton("報表印出（Excel）");
        btn5.addActionListener(e -> new PrintUI().setVisible(true));

        center.add(btn0);
        center.add(btn1);
        center.add(btn2);
        center.add(btn3);
        center.add(btn4);
        center.add(btn5);

        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnHome = new JButton("返回登入");
        btnHome.addActionListener(e -> { dispose(); new LoginUI().setVisible(true); });
        JButton btnExit = new JButton("結束");
        btnExit.addActionListener(e -> System.exit(0));
        bottom.add(btnHome);
        bottom.add(btnExit);
        root.add(bottom, BorderLayout.SOUTH);

        UITheme.apply(this);
    }
}
