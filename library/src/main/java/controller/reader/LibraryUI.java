package controller.reader;

import controller.LoginUI;
import model.Books;
import model.Readers;
import service.BooksService;
import service.MessagesService;
import service.impl.BooksServiceImpl;
import service.impl.MessagesServiceImpl;
import util.Tool;
import util.UITheme;
import vo.ReviewViewServiceImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * LibraryUI：圖書館主頁
 * - menu：左上登出，右側依序：讀書心得、個人信箱(有未讀則改成警示圖示)、會員中心
 * - 中間：搜尋區（白框），可輸入關鍵字；點選下表書籍可帶入搜尋欄；按放大鏡進 BorrowUI
 * - 下表：所有書籍最新到最舊，一頁10筆，可上一頁/下一頁
 * - 最下方：進入公開心得牆 ReviewPublicUI
 */
public class LibraryUI extends JFrame {

    private final Readers me;

    private final BooksService booksService = new BooksServiceImpl();
    private final MessagesService messagesService = new MessagesServiceImpl();
    private final ReviewViewServiceImpl reviewService = new ReviewViewServiceImpl(); // 保留：避免你其他地方有用到

    private int page = 1;
    private static final int PAGE_SIZE = 10;
    private int total = 0;

    private final JTextField tfSearch = new JTextField();
    private JButton btnInbox;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"條碼","書名","作者","分類","狀態"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final JLabel lbPage = new JLabel(" ");

    public LibraryUI(Readers me) {
        this.me = me;

        setTitle("圖書館主頁 - " + me.getReader_name());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1020, 680);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        // ===== Top menu =====
        JPanel top = new JPanel(new BorderLayout(10,10));

        JButton btnLogout = new JButton("", Tool.loadIcon("/bookicon/ic_logout.png"));
        btnLogout.setToolTipText("登出");
        btnLogout.addActionListener(e -> { dispose(); new LoginUI().setVisible(true); });
        top.add(btnLogout, BorderLayout.WEST);

        // logo area (米色區塊)
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoArea.setBackground(UITheme.PANEL);
        JLabel logo = new JLabel("愛讀公共圖書館  |  READ • LEARN • SHARE");
        logo.setFont(UITheme.H2);
        logoArea.add(logo);
        top.add(logoArea, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        JButton btnReview = new JButton("", Tool.loadIcon("/bookicon/ic_review.png"));
        btnReview.setToolTipText("讀書心得");
        btnReview.addActionListener(e -> new ReviewUI(me).setVisible(true));

        // 信箱：預設 mail 圖示；若有未讀，refreshUnread() 會改成 alert 圖示
        btnInbox = new JButton("", Tool.loadIcon("/bookicon/ic_mail.png"));
        btnInbox.setToolTipText("個人信箱");
        btnInbox.addActionListener(e -> new MessageUI(me).setVisible(true));

        JButton btnMe = new JButton("", Tool.loadIcon("/bookicon/ic_user.png"));
        btnMe.setToolTipText("會員中心");
        btnMe.addActionListener(e -> new ReaderUI(me).setVisible(true));

        right.add(btnReview);
        right.add(btnInbox);
        right.add(btnMe);

        top.add(right, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        // ===== Center =====
        JPanel center = new JPanel(new BorderLayout(10,10));
        root.add(center, BorderLayout.CENTER);

        // Search card
        JPanel searchCard = new JPanel(new BorderLayout(8,8));
        searchCard.setBorder(BorderFactory.createTitledBorder("搜尋書籍（可輸入書名 / 作者 / ISBN / 分類）"));
        searchCard.setBackground(UITheme.PANEL);

        JButton btnSearch = new JButton("", Tool.loadIcon("/bookicon/ic_search.png"));
        btnSearch.setToolTipText("前往借閱/預約");
        btnSearch.addActionListener(e -> openBorrow());

        searchCard.add(tfSearch, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        center.add(searchCard, BorderLayout.NORTH);

        // Table + paging
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this::onTableSelect);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 欄寬才不會被自動縮放覆蓋
        table.getColumnModel().getColumn(0).setPreferredWidth(130);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("館藏列表（最新 → 最舊）"));
        center.add(sp, BorderLayout.CENTER);

        JPanel paging = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton btnPrev = new JButton("上一頁");
        JButton btnNext = new JButton("下一頁");
        btnPrev.addActionListener(e -> { if (page > 1) { page--; loadPage(); }});
        btnNext.addActionListener(e -> { if (page < maxPage()) { page++; loadPage(); }});

        left.add(btnPrev);
        left.add(btnNext);
        left.add(lbPage);
        paging.add(left, BorderLayout.WEST);

        center.add(paging, BorderLayout.SOUTH);

        // ===== Bottom =====
        JPanel bottom = new JPanel(new BorderLayout(10,10));

        JButton btnPublic = new JButton("進入書籍推薦 / 公開心得牆");
        btnPublic.addActionListener(e -> new ReviewPublicUI(me).setVisible(true));
        bottom.add(btnPublic, BorderLayout.SOUTH);

        root.add(bottom, BorderLayout.SOUTH);

        // 初次載入
        refreshUnread();
        loadPage();

        UITheme.apply(this);
    }

    /**
     * 不使用額外 badge
     * 有未讀：btnInbox icon 改成 ic_alert.png
     * 無未讀：btnInbox icon 改回 ic_mail.png
     */
    private void refreshUnread() {
        int c = messagesService.unreadCount(me.getReader_id());
        if (btnInbox != null) {
            btnInbox.setIcon(Tool.loadIcon(
                    c > 0 ? "/bookicon/ic_alert.png" : "/bookicon/ic_mail.png"
            ));
        }
    }

    private void onTableSelect(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = table.getSelectedRow();
        if (row < 0) return;
        String title = (String) tableModel.getValueAt(row, 1);
        tfSearch.setText(title);
    }

    private void openBorrow() {
        String kw = tfSearch.getText().trim();
        new BorrowUI(me, kw).setVisible(true);
    }

    private String stateText(int s) {
        if (s == 0) return "0 可借閱";
        if (s == 1) return "1 借出中";
        if (s == 2) return "2 歸還待審核";
        return "3 毀損/遺失";
    }

    private void loadPage() {
        total = booksService.countAll();
        tableModel.setRowCount(0);
        List<Books> list = booksService.latestPage(page, PAGE_SIZE);
        for (Books b : list) {
            if (b.getState() == 3) continue; // 毀損/遺失：讀者端不顯示
            tableModel.addRow(new Object[]{
                    b.getBook_barcode(), b.getTitle(), b.getAuthor(), b.getCategory(), stateText(b.getState())
            });
        }
        lbPage.setText("第 " + page + " / " + maxPage() + " 頁（共 " + total + " 本）");
    }

    private int maxPage() {
        if (total <= 0) return 1;
        int m = total / PAGE_SIZE;
        if (total % PAGE_SIZE != 0) m++;
        return Math.max(m, 1);
    }
}
