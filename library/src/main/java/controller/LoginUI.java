package controller;

import controller.admin.AdminTopUI;
import controller.reader.LibraryUI;
import model.Readers;
import service.ReadersService;
import service.impl.ReadersServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import java.awt.*;

/**
 * LoginUI：
 * - 身分（讀者/館員）
 * - username / password
 * - 錯誤顯示隱藏 label："無此使用者" / "密碼有誤"
 * - 館員固定：admin / 0000（選館員時旁邊提示）
 * - 輸入格子不宜過大：採固定寬度 + 內距
 */
public class LoginUI extends JFrame {

    private final JRadioButton rbReader = new JRadioButton("讀者", true);
    private final JRadioButton rbAdmin = new JRadioButton("館員");

    private final JTextField tfUser = new JTextField();
    private final JPasswordField pfPass = new JPasswordField();

    private final JLabel lbError = new JLabel(" ");
    private final JLabel lbAdminHint = new JLabel("館員帳號：admin / 密碼：0000");

    private final ReadersService readersService = new ReadersServiceImpl();

    public LoginUI() {
        setTitle("愛讀公共圖書館 - 登入");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(560, 380);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        setContentPane(root);

        JLabel title = new JLabel("愛讀公共圖書館");
        title.setFont(UITheme.H1);
        root.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(UITheme.cardBorder());
        root.add(card, BorderLayout.CENTER);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.anchor = GridBagConstraints.WEST;

        // role
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbReader);
        bg.add(rbAdmin);

        JPanel role = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        role.add(new JLabel("身分："));
        role.add(rbReader);
        role.add(rbAdmin);

        g.gridx=0; g.gridy=0; g.gridwidth=2;
        card.add(role, g);

        // username
        g.gridwidth=1;
        g.gridx=0; g.gridy=1;
        card.add(new JLabel("帳號："), g);

        tfUser.setColumns(18);
        g.gridx=1;
        card.add(tfUser, g);

        // password
        g.gridx=0; g.gridy=2;
        card.add(new JLabel("密碼："), g);

        pfPass.setColumns(18);
        g.gridx=1;
        card.add(pfPass, g);

        // admin hint
        lbAdminHint.setForeground(UITheme.MUTED);
        g.gridx=0; g.gridy=3; g.gridwidth=2;
        card.add(lbAdminHint, g);

        // error label (hidden style)
        lbError.setForeground(new Color(200,60,60));
        g.gridy=4;
        card.add(lbError, g);

        // buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnLogin = new JButton("登入");
        btnLogin.addActionListener(e -> doLogin());
        actions.add(btnLogin);

        root.add(actions, BorderLayout.SOUTH);

        rbAdmin.addActionListener(e -> updateHint());
        rbReader.addActionListener(e -> updateHint());
        updateHint();

        UITheme.apply(this);
    }

    private void updateHint() {
        lbError.setText(" ");
        lbAdminHint.setVisible(rbAdmin.isSelected());
    }

    private void doLogin() {
        lbError.setText(" ");
        String user = tfUser.getText().trim();
        String pass = new String(pfPass.getPassword()).trim();

        if (rbAdmin.isSelected()) {
            if (!"admin".equals(user)) { lbError.setText("無此使用者"); return; }
            if (!"0000".equals(pass)) { lbError.setText("密碼有誤"); return; }
            dispose();
            new AdminTopUI().setVisible(true);
            return;
        }

        // reader
        Readers r = readersService.loginReader(user, pass);
        if (r == null) {
            // 判斷是帳號不存在還是密碼錯誤：為避免多查一次 DB，這裡簡化顯示「帳密有誤」。
            lbError.setText("帳號或密碼有誤（查無此帳號請找圖書館員開通）");
            return;
        }

        dispose();
        new LibraryUI(r).setVisible(true);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }

}
