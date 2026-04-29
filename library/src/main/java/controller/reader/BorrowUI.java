package controller.reader;

import model.Books;
import model.BorrowRecords;
import model.Readers;
import service.BooksService;
import service.BorrowRecordsService;
import service.ReservationsService;
import service.impl.BooksServiceImpl;
import service.impl.BorrowRecordsServiceImpl;
import service.impl.ReservationsServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * BorrowUI：搜尋/選書/借閱/預約（依 isavalible/state 狀態決定按鈕可用性）
 * 0=可借閱；1=借出中；2=歸還待審核；3=毀損/遺失
 */
public class BorrowUI extends JFrame {

    private final Readers me;
    private final BooksService booksService = new BooksServiceImpl();
    private final BorrowRecordsService borrowService = new BorrowRecordsServiceImpl();
    private final ReservationsService reservationsService = new ReservationsServiceImpl();

    private final JTextField tfSearch = new JTextField();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"條碼","ISBN","書名","作者","分類","索書號","狀態"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable table = new JTable(model);

    private final JLabel lbSelected = new JLabel("尚未選擇書籍");
    private final JButton btnBorrow = new JButton("借閱", Tool.loadIcon("/bookicon/ic_book.png"));
    private final JButton btnReserve = new JButton("預約", Tool.loadIcon("/bookicon/ic_mail.png"));

    public BorrowUI(Readers me, String keyword) {
        this.me = me;

        setTitle("借閱/預約 - " + me.getReader_name());
        setSize(1060, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        // top: back + search
        JPanel top = new JPanel(new BorderLayout(10,10));
        JButton btnBack = new JButton("返回主頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JPanel search = new JPanel(new BorderLayout(8,8));
        tfSearch.setText(keyword == null ? "" : keyword);
        JButton btnSearch = new JButton("", Tool.loadIcon("/bookicon/ic_search.png"));
        btnSearch.setToolTipText("搜尋");
        btnSearch.addActionListener(e -> load());
        search.add(tfSearch, BorderLayout.CENTER);
        search.add(btnSearch, BorderLayout.EAST);
        top.add(search, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);

        // center table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this::onSelect);
        JScrollPane sp = new JScrollPane(table);
        root.add(sp, BorderLayout.CENTER);

        // right cart panel
        JPanel cart = new JPanel();
        cart.setLayout(new BoxLayout(cart, BoxLayout.Y_AXIS));
        cart.setBorder(BorderFactory.createTitledBorder("操作區"));

        lbSelected.setAlignmentX(Component.LEFT_ALIGNMENT);
        cart.add(lbSelected);
        cart.add(Box.createVerticalStrut(10));

        btnBorrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReserve.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnBorrow.addActionListener(e -> borrow());
        btnReserve.addActionListener(e -> reserve());

        cart.add(btnBorrow);
        cart.add(Box.createVerticalStrut(8));
        cart.add(btnReserve);

        root.add(cart, BorderLayout.EAST);

        btnBorrow.setEnabled(false);
        btnReserve.setEnabled(false);

        load();
        UITheme.apply(this);
    }

    private void load() {
        model.setRowCount(0);
        String kw = tfSearch.getText().trim();
        List<Books> list = booksService.search(kw);
        for (Books b : list) {
            if (b.getState() == 3) continue; // 毀損/遺失：讀者端不顯示
            model.addRow(new Object[]{
                    b.getBook_barcode(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                    b.getCategory(), b.getCall_number(), stateText(b.getState())
            });
        }
    }

    private void onSelect(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = table.getSelectedRow();
        if (row < 0) {
            lbSelected.setText("尚未選擇書籍");
            btnBorrow.setEnabled(false);
            btnReserve.setEnabled(false);
            return;
        }
        String bc = (String) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 2);
        String st = (String) model.getValueAt(row, 6);

        lbSelected.setText("<html><b>已選：</b>" + title + "<br/>條碼：" + bc + "<br/>狀態：" + st + "</html>");

        // 依 state 控制按鈕：0=只能借閱；1,2=只能預約；3=不可操作
        int state = parseStateText(st);
        btnBorrow.setEnabled(state == 0);
        btnReserve.setEnabled(state == 1 || state == 2);
    }

    private int parseStateText(String st) {
        if (st.startsWith("0")) return 0;
        if (st.startsWith("1")) return 1;
        if (st.startsWith("2")) return 2;
        if (st.startsWith("3")) return 3;
        return -1;
    }

    private String stateText(int s) {
        if (s == 0) return "0 可借閱";
        if (s == 1) return "1 借出中";
        if (s == 2) return "2 歸還待審核";
        return "3 毀損/遺失";
    }

    private String selectedBarcode() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return (String) model.getValueAt(row, 0);
    }

    private void borrow() {
        String bc = selectedBarcode();
        if (bc == null) { Tool.errorBox("請先選一本書"); return; }

        int ok = JOptionPane.showConfirmDialog(this,
                "確認借閱此書？\n條碼：" + bc,
                "確認借閱",
                JOptionPane.OK_CANCEL_OPTION);

        if (ok != JOptionPane.OK_OPTION) return;

        try {
            BorrowRecords br = borrowService.borrowBook(me.getReader_id(), bc);
            Tool.infoBox("借閱成功！\n借閱日：" + br.getBorrow_date() + "\n到期日：" + br.getDue_date());
            load();
        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
    }

    private void reserve() {
        String bc = selectedBarcode();
        if (bc == null) { Tool.errorBox("請先選一本書"); return; }

        String expected = JOptionPane.showInputDialog(this,
                "請輸入預計取書日（示範：可先填上一位到期日）",
                "2026-03-01");
        if (expected == null) return;

        try {
            reservationsService.reserve(me.getReader_id(), bc, expected);
            Tool.infoBox("預約成功！\n預計取書日：" + expected);
        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
    }
}
