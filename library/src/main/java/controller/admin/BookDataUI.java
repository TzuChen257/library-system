package controller.admin;

import model.Books;
import service.BooksService;
import service.impl.BooksServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * BookDataUI：
 * - 查詢全部書籍
 * - 查詢非閒置中(state != 0)書籍
 */
public class BookDataUI extends JFrame {

    private final BooksService service = new BooksServiceImpl();

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"條碼","ISBN","書名","作者","分類","索書號","狀態"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public BookDataUI() {
        setTitle("書籍資料 - 後台");
        setSize(1100, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout(10,10));
        JButton btnBack = new JButton("返回首頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("書籍資料查詢");
        title.setFont(UITheme.H2);
        top.add(title, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnAll = new JButton("查詢全部");
        JButton btnNonIdle = new JButton("查詢非閒置中");
        btnAll.addActionListener(e -> loadAll());
        btnNonIdle.addActionListener(e -> loadNonIdle());
        actions.add(btnAll);
        actions.add(btnNonIdle);
        top.add(actions, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(130);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(160);
        table.getColumnModel().getColumn(6).setPreferredWidth(140);

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        loadAll();
        UITheme.apply(this);
    }

    private String st(int s) {
        if (s == 0) return "0 可借閱";
        if (s == 1) return "1 借出中";
        if (s == 2) return "2 歸還待審核";
        return "3 毀損/遺失";
    }

    private void loadAll() {
        model.setRowCount(0);
        // 簡化：用 search("") 當全部
        List<Books> list = service.search("");
        for (Books b : list) {
            model.addRow(new Object[]{b.getBook_barcode(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                    b.getCategory(), b.getCall_number(), st(b.getState())});
        }
        if (list.isEmpty()) Tool.infoBox("查無資料");
    }

    private void loadNonIdle() {
        model.setRowCount(0);
        List<Books> list = service.search("");
        int count = 0;
        for (Books b : list) {
            if (b.getState() != 0) {
                count++;
                model.addRow(new Object[]{b.getBook_barcode(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                        b.getCategory(), b.getCall_number(), st(b.getState())});
            }
        }
        if (count == 0) Tool.infoBox("目前沒有非閒置中的書籍");
    }
}
