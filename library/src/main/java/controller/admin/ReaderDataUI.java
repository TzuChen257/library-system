package controller.admin;

import model.Readers;
import service.ReadersService;
import service.impl.ReadersServiceImpl;
import util.Tool;
import util.UITheme;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

/**
 * ReaderDataUI：讀者資料管理（註冊 / 修改 / 查詢）
 * 依需求：移除密碼加密，密碼欄位直接存入資料庫（示範用）。
 */
public class ReaderDataUI extends JFrame {

    private final ReadersService service = new ReadersServiceImpl();

    // register fields
    private final JTextField rName = new JTextField();
    private final JTextField rNationalId = new JTextField();
    // 需求：新增資料的生日用日曆選擇
    private final DatePicker rBirthday;
    private final JTextField rAddress = new JTextField();
    private final JTextField rPhone = new JTextField();
    private final JTextField rEmail = new JTextField();
    private final JTextField rUsername = new JTextField();
    private final JPasswordField rPassword = new JPasswordField();

    // modify fields
    private final JComboBox<String> cbReaders = new JComboBox<>();
    private List<Readers> cached = new ArrayList<>();

    private final JTextField mReaderId = new JTextField();
    private final JTextField mName = new JTextField();
    private final JTextField mNationalId = new JTextField();
    private final JTextField mBirthday = new JTextField();
    private final JTextField mAddress = new JTextField();
    private final JTextField mPhone = new JTextField();
    private final JTextField mEmail = new JTextField();
    private final JTextField mUsername = new JTextField();
    private final JPasswordField mNewPassword = new JPasswordField();
    private final JLabel mHint = new JLabel("密碼可留空（代表不更改）");

    // query tab
    private final DefaultTableModel qModel = new DefaultTableModel(
            new Object[]{"讀者編號","姓名","身分證/居留證","生日","地址","手機","Email","帳號","等級","點數"}, 0
    );
    private final JTable qTable = new JTable(qModel);
    private final JComboBox<String> qFilter = new JComboBox<>();

    public ReaderDataUI() {
        setTitle("讀者資料 - 後台");
        setSize(920, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // init birthday picker (register tab)
        DatePickerSettings ds = new DatePickerSettings();
        ds.setFormatForDatesCommonEra("yyyy-MM-dd");
        rBirthday = new DatePicker(ds);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("返回首頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("讀者資料管理");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(UITheme.H2);
        top.add(title, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);

        // 需求：jTabbedPane 標題在上方
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("註冊", buildRegister());
        tabs.addTab("修改", buildModify());
        tabs.addTab("查詢", buildQuery());
        root.add(tabs, BorderLayout.CENTER);

        reloadReaders();
        UITheme.apply(this);
    }

    private JPanel buildRegister() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(UITheme.cardBorder());

        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        form.add(new JLabel("姓名")); form.add(rName);
        form.add(new JLabel("身分證/居留證")); form.add(rNationalId);
        form.add(new JLabel("生日")); form.add(rBirthday);
        form.add(new JLabel("地址")); form.add(rAddress);
        form.add(new JLabel("電話")); form.add(rPhone);
        form.add(new JLabel("Email")); form.add(rEmail);
        form.add(new JLabel("線上帳號")); form.add(rUsername);
        form.add(new JLabel("線上密碼")); form.add(rPassword);

        p.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreate = new JButton("新增讀者");
        btnCreate.addActionListener(e -> doCreate());
        bottom.add(btnCreate);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildModify() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(UITheme.cardBorder());

        JPanel choose = new JPanel(new FlowLayout(FlowLayout.LEFT));
        choose.add(new JLabel("選擇讀者："));
        choose.add(cbReaders);

        JButton btnLoad = new JButton("帶入資料");
        btnLoad.addActionListener(e -> loadSelectedToModify());
        choose.add(btnLoad);

        p.add(choose, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        mReaderId.setEditable(false);
        form.add(new JLabel("讀者編號")); form.add(mReaderId);
        form.add(new JLabel("姓名")); form.add(mName);
        form.add(new JLabel("身分證/居留證")); form.add(mNationalId);
        form.add(new JLabel("生日(yyyy-MM-dd)")); form.add(mBirthday);
        form.add(new JLabel("地址")); form.add(mAddress);
        form.add(new JLabel("電話")); form.add(mPhone);
        form.add(new JLabel("Email")); form.add(mEmail);
        form.add(new JLabel("線上帳號")); form.add(mUsername);
        form.add(new JLabel("新密碼(可空)")); form.add(mNewPassword);

        p.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        mHint.setForeground(UITheme.MUTED);
        bottom.add(mHint, BorderLayout.WEST);

        JButton btnSave = new JButton("儲存修改");
        btnSave.addActionListener(e -> doUpdate());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(btnSave);
        bottom.add(right, BorderLayout.EAST);

        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildQuery() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(UITheme.cardBorder());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("查詢："));
        top.add(new JLabel("讀者"));
        top.add(qFilter);

        JButton btnAll = new JButton("查詢全部");
        btnAll.addActionListener(e -> loadQueryAll());
        JButton btnOne = new JButton("查詢所選");
        btnOne.addActionListener(e -> loadQueryOne());
        top.add(btnAll);
        top.add(btnOne);

        p.add(top, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(qTable);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void doCreate() {
        try {
            Readers r = new Readers();
            r.setReader_name(rName.getText().trim());
            r.setNational_id(rNationalId.getText().trim());
            r.setBirthday(rBirthday.getDate() == null ? "" : rBirthday.getDate().toString());
            r.setAddress(rAddress.getText().trim());
            r.setPhone(rPhone.getText().trim());
            r.setEmail(rEmail.getText().trim());
            r.setOnline_username(rUsername.getText().trim());

            String rawPwd = new String(rPassword.getPassword()).trim();
            service.createReader(r, rawPwd);

            Tool.infoBox("新增成功！");
            clearRegister();
            reloadReaders();
        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
    }

    private void doUpdate() {
        try {
            Readers r = new Readers();
            r.setReader_id(mReaderId.getText().trim());
            r.setReader_name(mName.getText().trim());
            r.setNational_id(mNationalId.getText().trim());
            r.setBirthday(mBirthday.getText().trim());
            r.setAddress(mAddress.getText().trim());
            r.setPhone(mPhone.getText().trim());
            r.setEmail(mEmail.getText().trim());
            r.setOnline_username(mUsername.getText().trim());

            String rawPwd = new String(mNewPassword.getPassword()).trim();
            if (rawPwd.isEmpty()) rawPwd = null;

            service.updateReader(r, rawPwd);
            Tool.infoBox("修改成功！");
            reloadReaders();
        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
    }

    private void clearRegister() {
        rName.setText("");
        rNationalId.setText("");
        rBirthday.clear();
        rAddress.setText("");
        rPhone.setText("");
        rEmail.setText("");
        rUsername.setText("");
        rPassword.setText("");
    }

    private void reloadReaders() {
        if (Beans.isDesignTime()) {
            cached = new ArrayList<>();
            Readers demo = new Readers();
            demo.setReader_id("R00000001");
            demo.setReader_name("設計模式示範");
            demo.setNational_id("A123456789");
            demo.setBirthday("2000-01-01");
            demo.setAddress("台北市...（示範）");
            demo.setPhone("0912-345-678");
            demo.setEmail("demo@example.com");
            demo.setOnline_username("demo");
            demo.setReader_level(1);
            demo.setReward_points(0);
            cached.add(demo);
        } else {
            cached = service.allReaders();
        }
        cbReaders.removeAllItems();
        qFilter.removeAllItems();
        for (Readers r : cached) {
            String item = r.getReader_id() + " - " + r.getReader_name();
            cbReaders.addItem(item);
            qFilter.addItem(item);
        }
        if (!cached.isEmpty()) {
            cbReaders.setSelectedIndex(0);
            qFilter.setSelectedIndex(0);
            loadSelectedToModify();
        }
        loadQueryAll();
    }

    private Readers selectedReader() {
        int idx = cbReaders.getSelectedIndex();
        if (idx < 0 || idx >= cached.size()) return null;
        return cached.get(idx);
    }

    private Readers selectedQueryReader() {
        int idx = qFilter.getSelectedIndex();
        if (idx < 0 || idx >= cached.size()) return null;
        return cached.get(idx);
    }

    private void loadSelectedToModify() {
        Readers r = selectedReader();
        if (r == null) return;
        mReaderId.setText(r.getReader_id());
        mName.setText(r.getReader_name());
        mNationalId.setText(r.getNational_id());
        mBirthday.setText(r.getBirthday());
        mAddress.setText(r.getAddress());
        mPhone.setText(r.getPhone());
        mEmail.setText(r.getEmail());
        mUsername.setText(r.getOnline_username());
        mNewPassword.setText("");
    }

    private void loadQueryAll() {
        qModel.setRowCount(0);
        for (Readers r : cached) {
            qModel.addRow(new Object[]{
                    r.getReader_id(), r.getReader_name(), r.getNational_id(), r.getBirthday(),
                    r.getAddress(), r.getPhone(), r.getEmail(), r.getOnline_username(),
                    r.getReader_level(), r.getReward_points()
            });
        }
    }

    private void loadQueryOne() {
        qModel.setRowCount(0);
        Readers r = selectedQueryReader();
        if (r == null) return;
        qModel.addRow(new Object[]{
                r.getReader_id(), r.getReader_name(), r.getNational_id(), r.getBirthday(),
                r.getAddress(), r.getPhone(), r.getEmail(), r.getOnline_username(),
                r.getReader_level(), r.getReward_points()
        });
    }
}
