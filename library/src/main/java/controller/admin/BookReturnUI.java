package controller.admin;

import model.BorrowRecords;
import service.BorrowRecordsService;
import service.impl.BorrowRecordsServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class BookReturnUI extends JFrame {

    private final BorrowRecordsService service = new BorrowRecordsServiceImpl();

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"紀錄ID","讀者ID","條碼","借閱日","到期日","歸還日","備註"}, 0
    );
    private final JTable table = new JTable(model);

    private final JComboBox<String> cbState = new JComboBox<>(new String[]{"完好", "毀損", "遺失"});
    private final JTextField tfNote = new JTextField();

    public BookReturnUI() {
        setTitle("書籍歸還核可");
        setSize(980, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("返回首頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("已歸還待核可列表（讀者端送出後會出現在此）");
        top.add(title, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("刷新");
        btnRefresh.addActionListener(e -> load());
        top.add(btnRefresh, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8,8));
        JPanel form = new JPanel(new BorderLayout(8,8));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.add(new JLabel("書況："));
        left.add(cbState);
        form.add(left, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(6,6));
        center.add(new JLabel("備註："), BorderLayout.WEST);
        center.add(tfNote, BorderLayout.CENTER);
        form.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnApprove = new JButton("核可歸還");
        btnApprove.addActionListener(e -> approve());
        right.add(btnApprove);
        form.add(right, BorderLayout.EAST);

        bottom.add(form, BorderLayout.CENTER);

        JLabel hint = new JLabel("核可後：完好會加點數；毀損/遺失會把書況寫入 books.state，並在借閱紀錄備註中留下核可結果。");
        bottom.add(hint, BorderLayout.SOUTH);

        root.add(bottom, BorderLayout.SOUTH);

        load();

        UITheme.apply(this);
    }

    private void load() {
        model.setRowCount(0);
        List<BorrowRecords> list = service.pendingReturns();
        for (BorrowRecords br : list) {
            model.addRow(new Object[]{
                    br.getId(), br.getReader_id(), br.getBook_barcode(),
                    br.getBorrow_date(), br.getDue_date(), br.getReturn_date(),
                    br.getAdmin_note()
            });
        }
        if (list.isEmpty()) Tool.infoBox("目前沒有待核可歸還的紀錄。");
    }

    private void approve() {
        int row = table.getSelectedRow();
        if (row < 0) { Tool.errorBox("請先選一筆待核可紀錄"); return; }
        int id = (int) model.getValueAt(row, 0);

        int bookState;
        String s = (String) cbState.getSelectedItem();
        if ("完好".equals(s)) bookState = 0;
        else if ("毀損".equals(s)) bookState = 3;
        else bookState = 3;

        String note = tfNote.getText().trim();

        try {
            service.approveReturn(id, bookState, note);
            Tool.infoBox("已核可歸還");
            tfNote.setText("");
            load();

        UITheme.apply(this);
        } catch (Exception e) {
            e.printStackTrace();
            Tool.errorBox("核可失敗：" + e.getMessage());
        }
    }
}
