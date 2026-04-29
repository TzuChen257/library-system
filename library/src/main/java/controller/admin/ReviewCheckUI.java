package controller.admin;

import dao.impl.ReadersDaoImpl;
import model.Readers;
import model.Reviews;
import service.ReviewsService;
import service.impl.ReviewsServiceImpl;
import util.Settings;
import util.Tool;
import util.UITheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class ReviewCheckUI extends JFrame {

    private final ReviewsService service = new ReviewsServiceImpl();
    private final ReadersDaoImpl readersDao = new ReadersDaoImpl();

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"心得ID","讀者ID","字數","狀態","公開","內容(前 60 字)"}, 0
    );
    private final JTable table = new JTable(model);

    private final JTextArea ta = new JTextArea();
    private final JCheckBox ckApprove = new JCheckBox("核可（發點數）");
    private final JCheckBox ckPublic = new JCheckBox("發佈（首頁可見）");

    public ReviewCheckUI() {
        setTitle("讀書心得核可");
        setSize(1080, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("返回首頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("尚未核可的讀書心得（state=0）");
        top.add(title, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("刷新");
        btnRefresh.addActionListener(e -> load());
        top.add(btnRefresh, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10,10));
        // 需求：上方 table 不用太大，反而要讓下方內容欄位更大（且不用編輯內文）
        JScrollPane spTable = new JScrollPane(table);
        spTable.setBorder(BorderFactory.createTitledBorder("待核可清單"));

        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(BorderFactory.createTitledBorder("心得內容（僅檢視；勾選核可/發佈後送出）"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spTable, sp);
        split.setDividerLocation(220);
        center.add(split, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8,8));
        JPanel opts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opts.add(ckApprove);
        opts.add(ckPublic);
        bottom.add(opts, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnApply = new JButton("確認修改 / 送出");
        btnApply.addActionListener(e -> apply());
        btns.add(btnApply);
        bottom.add(btns, BorderLayout.EAST);

        root.add(bottom, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            loadSelectedToEditor();
        });

        load();

        UITheme.apply(this);
    }

    private void load() {
        model.setRowCount(0);
        List<Reviews> list = service.pendingReviews();
        for (Reviews r : list) {
            String s = r.getContent() == null ? "" : r.getContent().replaceAll("\\s+", " ");
            if (s.length() > 60) s = s.substring(0, 60) + "...";
            model.addRow(new Object[]{
                    r.getId(), r.getReader_id(), r.getWord_count(),
                    r.getState(), r.isIs_public(), s
            });
        }
        if (list.isEmpty()) {
            ta.setText("");
            Tool.infoBox("目前沒有待核可心得。");
        } else {
            table.setRowSelectionInterval(0,0);
        }
    }

    private Reviews selectedReview() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) model.getValueAt(row, 0);
        // pendingReviews() 量不大：直接再掃一次
        List<Reviews> list = service.pendingReviews();
        for (Reviews r : list) if (r.getId() == id) return r;
        return null;
    }

    private void loadSelectedToEditor() {
        Reviews r = selectedReview();
        if (r == null) return;
        ta.setText(r.getContent() == null ? "" : r.getContent());
        ckApprove.setSelected(true); // 預設多數情境會核可
        ckPublic.setSelected(r.isIs_public());
    }

    private void apply() {
        Reviews r = selectedReview();
        if (r == null) { Tool.errorBox("請先選一筆心得"); return; }

        // 需求：內文不用編輯，僅做核可/發佈
        String content = r.getContent() == null ? "" : r.getContent();
        r.setWord_count(content.replaceAll("\\s+", "").length());

        boolean approve = ckApprove.isSelected();
        boolean pub = ckPublic.isSelected();

        if (approve) {
            r.setState(1); // approved
        } else {
            r.setState(0); // keep pending
        }
        r.setIs_public(pub);

        try {
            service.updateReview(r);

            if (approve) {
                Readers reader = readersDao.findByReaderId(r.getReader_id());
                if (reader != null) {
                    reader.setReward_points(reader.getReward_points() + Settings.POINT_REVIEW_APPROVED);
                    if (reader.getReward_points() >= 50) reader.setReader_level(3);
                    else if (reader.getReward_points() >= 20) reader.setReader_level(2);
                    else reader.setReader_level(Math.max(reader.getReader_level(), 1));
                    readersDao.update(reader);
                }
            }

            Tool.infoBox("已更新。核可=" + approve + "，發佈=" + pub);
            load();

        UITheme.apply(this);
        } catch (Exception e) {
            e.printStackTrace();
            Tool.errorBox("更新失敗：" + e.getMessage());
        }
    }
}
