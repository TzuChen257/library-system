package controller.reader;

import model.Books;
import model.Readers;
import model.ReviewDrafts;
import model.Reviews;
import service.ReviewsService;
import util.Tool;
import util.UITheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

public class PreviewUI extends JFrame {

    private final Readers me;
    private final ReviewDrafts draft;
    private final Books book;
    private final ReviewsService reviewsService;

    public PreviewUI(Readers me, ReviewDrafts draft, Books book, ReviewsService reviewsService) {
        this.me = me;
        this.draft = draft;
        this.book = book;
        this.reviewsService = reviewsService;

        setTitle("預覽 - " + draft.getTitle());
        setSize(860, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JLabel head = new JLabel("預覽：確認後可送出（送出後等待館員核可）");
        head.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        root.add(head, BorderLayout.NORTH);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        ta.setText(buildPreviewText());
        root.add(new JScrollPane(ta), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("重返編輯");
        btnBack.addActionListener(e -> dispose());
        JButton btnSubmit = new JButton("送出存檔");
        btnSubmit.addActionListener(e -> submit());
        bottom.add(btnBack);
        bottom.add(btnSubmit);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private String buildPreviewText() {
        StringBuilder sb = new StringBuilder();
        sb.append("【文章標題】").append(draft.getTitle()).append("\n");
        sb.append("【筆名】").append(draft.getPen_name() == null ? "" : draft.getPen_name()).append("\n");
        sb.append("【ISBN】").append(draft.getIsbn()).append("\n");
        if (book != null) {
            sb.append("【書名】").append(book.getTitle()).append("\n");
            sb.append("【作者】").append(book.getAuthor()).append("\n");
            sb.append("【出版社/年份】").append(book.getPublisher()).append(" / ").append(book.getPublish_year()).append("\n");
            sb.append("【分類/索書號】").append(book.getCategory()).append(" / ").append(book.getCall_number()).append("\n");
        }
        sb.append("\n");
        sb.append("【書籍簡介】\n").append(draft.getBook_intro() == null ? "" : draft.getBook_intro()).append("\n\n");
        sb.append("【內容擷取】\n").append(draft.getBook_excerpt() == null ? "" : draft.getBook_excerpt()).append("\n\n");
        sb.append("【心得】（字數：").append(draft.getWord_count()).append("）\n");
        sb.append(draft.getContent() == null ? "" : draft.getContent()).append("\n");
        return sb.toString();
    }

    private void submit() {
        try {
            // 找到一個同 ISBN 的館藏條碼：用 book_barcode 存（簡化）
            String barcode = null;
            if (book != null) barcode = book.getBook_barcode();
            if (barcode == null || barcode.trim().isEmpty()) barcode = "UNKNOWN";

            Reviews r = new Reviews();
            r.setReader_id(me.getReader_id());
            r.setBook_barcode(barcode);
            // content 以「題目/筆名/區塊」拼接，方便館員端審核與修改
            StringBuilder body = new StringBuilder();
            body.append("【標題】").append(draft.getTitle()).append("\n");
            body.append("【筆名】").append(draft.getPen_name() == null ? "" : draft.getPen_name()).append("\n");
            body.append("【ISBN】").append(draft.getIsbn()).append("\n\n");
            body.append("【書籍簡介】\n").append(draft.getBook_intro() == null ? "" : draft.getBook_intro()).append("\n\n");
            body.append("【內容擷取】\n").append(draft.getBook_excerpt() == null ? "" : draft.getBook_excerpt()).append("\n\n");
            body.append("【心得】\n").append(draft.getContent() == null ? "" : draft.getContent()).append("\n");
            r.setContent(body.toString());
            r.setWord_count(draft.getWord_count());

            reviewsService.submitReview(r);

            Tool.infoBox("已送出，等待館員核可。核可後將自動發放點數（若館員勾選核可）。");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            Tool.errorBox("送出失敗：" + e.getMessage());
        }
        UITheme.apply(this);
    }
}
