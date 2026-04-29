package controller.reader;

import model.Books;
import model.Readers;
import model.ReviewDrafts;
import model.Reviews;
import service.BooksService;
import service.ReviewDraftsService;
import service.ReviewsService;
import service.impl.BooksServiceImpl;
import service.impl.ReviewDraftsServiceImpl;
import service.impl.ReviewsServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class ReviewUI extends JFrame {

    private final Readers me;
    private final BooksService booksService = new BooksServiceImpl();
    private final ReviewDraftsService draftService = new ReviewDraftsServiceImpl();
    private final ReviewsService reviewsService = new ReviewsServiceImpl();

    private int currentSlot = 0;

    private final JTextField tfTitle = new JTextField();
    private final JTextField tfIsbn = new JTextField();
    private final JTextField tfPenName = new JTextField();
    private final JTextArea taIntro = new JTextArea(4, 20);
    private final JTextArea taExcerpt = new JTextArea(5, 20);
    private final JTextArea taContent = new JTextArea(12, 20);

    private final JLabel lbBookInfo = new JLabel("請輸入 ISBN 並按『帶入』", SwingConstants.LEFT);
    private final JLabel lbWord = new JLabel("字數：0（需 ≥ 500）");

    public ReviewUI(Readers me) {
        this.me = me;

        setTitle("讀書心得 - " + me.getReader_name());
        setSize(980, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        // top: slot buttons + back
        JPanel top = new JPanel(new BorderLayout(10,10));
        JButton btnBack = new JButton("返回主頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JPanel slots = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton s0 = new JButton("草稿 0");
        JButton s1 = new JButton("草稿 1");
        JButton s2 = new JButton("草稿 2");
        s0.addActionListener(e -> switchSlot(0));
        s1.addActionListener(e -> switchSlot(1));
        s2.addActionListener(e -> switchSlot(2));
        slots.add(new JLabel("草稿槽："));
        slots.add(s0);
        slots.add(s1);
        slots.add(s2);
        top.add(slots, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("儲存草稿");
        btnSave.addActionListener(e -> saveDraft());
        JButton btnPreview = new JButton("預覽 / 送出");
        btnPreview.addActionListener(e -> preview());
        JButton btnClear = new JButton("清空本草稿");
        btnClear.addActionListener(e -> clearDraft());
        topRight.add(btnClear);
        topRight.add(btnSave);
        topRight.add(btnPreview);
        top.add(topRight, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        // form center
        JPanel form = new JPanel(new BorderLayout(10,10));
        root.add(form, BorderLayout.CENTER);

        JPanel grid = new JPanel(new GridLayout(3, 2, 8, 8));

        grid.add(labelCell("文章標題"));
        grid.add(tfTitle);

        JPanel isbnRow = new JPanel(new BorderLayout(6,6));
        isbnRow.add(tfIsbn, BorderLayout.CENTER);
        JButton btnFetch = new JButton("帶入");
        btnFetch.addActionListener(e -> fetchBook());
        isbnRow.add(btnFetch, BorderLayout.EAST);
        grid.add(labelCell("ISBN（可帶入書籍資料）"));
        grid.add(isbnRow);

        grid.add(labelCell("筆名"));
        grid.add(tfPenName);

        form.add(grid, BorderLayout.NORTH);

        JPanel bookInfoPanel = new JPanel(new BorderLayout());
        lbBookInfo.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        bookInfoPanel.setBorder(BorderFactory.createTitledBorder("書籍基本資料"));
        bookInfoPanel.add(lbBookInfo, BorderLayout.CENTER);
        form.add(bookInfoPanel, BorderLayout.CENTER);

        JPanel textAreas = new JPanel(new GridLayout(1,3,8,8));
        textAreas.add(wrapArea("書籍簡介（最多 2000 字）", taIntro, 2000));
        textAreas.add(wrapArea("書籍內容擷取（最多 2000 字）", taExcerpt, 2000));
        textAreas.add(wrapArea("心得（需 ≥ 500 字）", taContent, 20000));
        form.add(textAreas, BorderLayout.SOUTH);

        // bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lbWord.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        lbWord.setForeground(new Color(50,50,50));
        bottom.add(lbWord);
        root.add(bottom, BorderLayout.SOUTH);

        // listeners
        taContent.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshWord(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshWord(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshWord(); }
        });

        addHint(taIntro, "最多 2000 字");
        addHint(taExcerpt, "最多 2000 字");
        addHint(taContent, "請輸入 500 字以上心得…");

        switchSlot(0);
    }

    private JPanel labelCell(String text) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        p.add(lb, BorderLayout.CENTER);
        return p;
    }

    private JPanel wrapArea(String title, JTextArea ta, int maxLen) {
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(ta);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(sp, BorderLayout.CENTER);

        // length limiter (soft)
        ta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void enforce() {
                String t = ta.getText();
                if (t != null && t.length() > maxLen) {
                    ta.setText(t.substring(0, maxLen));
                    Tool.infoBox("已限制最大字數：" + maxLen);
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { enforce(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });

        return p;
    }

    private void addHint(JTextArea ta, String hint) {
        ta.setForeground(Color.GRAY);
        ta.setText(hint);
        ta.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (ta.getText().equals(hint)) {
                    ta.setText("");
                    ta.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (ta.getText().trim().isEmpty()) {
                    ta.setForeground(Color.GRAY);
                    ta.setText(hint);
                }
            }
        });
    }

    private String normalizeArea(JTextArea ta) {
        String t = ta.getText();
        if (t == null) return "";
        // if still hint, treat as empty
        if (t.equals("最多 2000 字") || t.equals("請輸入 500 字以上心得…")) return "";
        return t.trim();
    }

    private void refreshWord() {
        String content = normalizeArea(taContent);
        int n = content.replaceAll("\\s+", "").length();
        lbWord.setText("字數：" + n + "（需 ≥ 500）");
    }

    private void switchSlot(int slot) {
        // save current draft silently before switch (optional)
        if (slot != currentSlot) {
            saveDraftSilent();
        }
        currentSlot = slot;
        setTitle("讀書心得（草稿 " + slot + "） - " + me.getReader_name());

        ReviewDrafts d = draftService.load(me.getReader_id(), slot);
        if (d == null) {
            tfTitle.setText("");
            tfIsbn.setText("");
            tfPenName.setText("");
            lbBookInfo.setText("請輸入 ISBN 並按『帶入』");
            taIntro.setForeground(Color.GRAY); taIntro.setText("最多 2000 字");
            taExcerpt.setForeground(Color.GRAY); taExcerpt.setText("最多 2000 字");
            taContent.setForeground(Color.GRAY); taContent.setText("請輸入 500 字以上心得…");
        } else {
            tfTitle.setText(nullToEmpty(d.getTitle()));
            tfIsbn.setText(nullToEmpty(d.getIsbn()));
            tfPenName.setText(nullToEmpty(d.getPen_name()));
            taIntro.setForeground(Color.BLACK); taIntro.setText(nullToEmpty(d.getBook_intro()));
            taExcerpt.setForeground(Color.BLACK); taExcerpt.setText(nullToEmpty(d.getBook_excerpt()));
            taContent.setForeground(Color.BLACK); taContent.setText(nullToEmpty(d.getContent()));
            refreshWord();
            if (d.getIsbn() != null && !d.getIsbn().trim().isEmpty()) fetchBookSilent();
        }
        refreshWord();
    }

    private String nullToEmpty(String s) { return s == null ? "" : s; }

    private void fetchBookSilent() {
        try {
            fetchBook();
        } catch (Exception ex) {
            // ignore in silent mode
        }
    }

    private void fetchBook() {
        String isbn = tfIsbn.getText().trim();
        if (isbn.isEmpty()) { Tool.errorBox("請先輸入 ISBN"); return; }
        Books b = booksService.findByIsbn(isbn);
        if (b == null) {
            lbBookInfo.setText("找不到該 ISBN 的書籍（可先儲存草稿，或請館員匯入書籍資料）");
            return;
        }
        lbBookInfo.setText("《" + b.getTitle() + "》 / " + b.getAuthor() + " / " + b.getPublisher() +
                " / " + b.getPublish_year() + " / 分類：" + b.getCategory() + " / 索書號：" + b.getCall_number());
    }

    private void saveDraftSilent() {
        try {
            ReviewDrafts d = buildDraft();
            draftService.save(d);
        } catch (Exception e) {
            // ignore
        }
    }

    private ReviewDrafts buildDraft() {
        ReviewDrafts d = new ReviewDrafts();
        d.setReader_id(me.getReader_id());
        d.setSlot(currentSlot);
        d.setTitle(tfTitle.getText().trim());
        d.setIsbn(tfIsbn.getText().trim());
        d.setPen_name(tfPenName.getText().trim());
        d.setBook_intro(normalizeArea(taIntro));
        d.setBook_excerpt(normalizeArea(taExcerpt));
        d.setContent(normalizeArea(taContent));
        d.setWord_count(d.getContent().replaceAll("\\s+", "").length());
        return d;
    }

    private void saveDraft() {
        ReviewDrafts d = buildDraft();
        draftService.save(d);
        Tool.infoBox("草稿已儲存（槽 " + currentSlot + "）");
    }

    private void clearDraft() {
        int ok = javax.swing.JOptionPane.showConfirmDialog(this, "確定要清空草稿槽 " + currentSlot + "？", "確認", javax.swing.JOptionPane.YES_NO_OPTION);
        if (ok != javax.swing.JOptionPane.YES_OPTION) return;
        draftService.clear(me.getReader_id(), currentSlot);
        switchSlot(currentSlot);
        Tool.infoBox("已清空");
    }

    private void preview() {
        ReviewDrafts d = buildDraft();
        // validate minimal
        if (d.getTitle().trim().isEmpty()) { Tool.errorBox("請輸入文章標題"); return; }
        if (d.getIsbn().trim().isEmpty()) { Tool.errorBox("請輸入 ISBN"); return; }
        if (d.getContent().replaceAll("\\s+", "").length() < 500) { Tool.errorBox("心得需至少 500 字"); return; }

        // save draft before preview
        draftService.save(d);

        // build preview payload
        Books b = booksService.findByIsbn(d.getIsbn());
        PreviewUI p = new PreviewUI(me, d, b, reviewsService);
        p.setVisible(true);
        UITheme.apply(this);
    }
}
