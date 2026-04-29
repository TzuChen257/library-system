package controller.reader;

import model.Readers;
import util.UITheme;
import vo.ReviewView;
import vo.ReviewViewServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.beans.Beans;
import java.util.List;

/**
 * ReviewPublicUI：館員核可且勾選發佈的心得牆（示範）
 */
public class ReviewPublicUI extends JFrame {

    private final Readers me;
    private final ReviewViewServiceImpl service = new ReviewViewServiceImpl();

    private List<ReviewView> cached;
    private int index = 0; // 0 = 最新
    private final JTextArea ta = new JTextArea();
    private final JLabel lbPage = new JLabel(" ");
    private final JButton btnPrev = new JButton("上一頁");
    private final JButton btnNext = new JButton("下一頁");

    public ReviewPublicUI(Readers me) {
        this.me = me;

        setTitle("書籍推薦 / 公開心得 - " + me.getReader_name());
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("返回主頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("公開讀書心得（館員核可且勾選發佈）");
        title.setFont(UITheme.H2);
        top.add(title, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        root.add(new JScrollPane(ta), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrev.addActionListener(e -> prev());
        btnNext.addActionListener(e -> next());
        nav.add(btnPrev);
        nav.add(lbPage);
        nav.add(btnNext);
        bottom.add(nav, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        load();

        UITheme.apply(this);
    }

    private void load() {
        if (Beans.isDesignTime()) {
            ta.setText("（設計模式示範）\n這裡會一次顯示一篇心得，並可用上一頁/下一頁切換。\n");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            lbPage.setText("1 / 1");
            return;
        }

        cached = service.allPublicReviews(200);
        index = 0;
        render();
    }

    private void render() {
        if (cached == null || cached.isEmpty()) {
            ta.setText("目前尚無公開心得。");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            lbPage.setText("0 / 0");
            return;
        }
        if (index < 0) index = 0;
        if (index >= cached.size()) index = cached.size() - 1;

        ReviewView v = cached.get(index);
        StringBuilder sb = new StringBuilder();
        sb.append("《").append(v.getTitle()).append("》\n")
          .append("ISBN：").append(v.getIsbn()).append("\n")
          .append("作者：").append(v.getAuthor()).append("\n")
          .append("讀者：").append(v.getReader_name()).append(" / 字數：").append(v.getWord_count()).append("\n")
          .append("出版：").append(v.getPublisher()).append(" ").append(v.getPublish_year()).append("\n")
          .append("分類：").append(v.getCategory()).append(" / 索書號：").append(v.getCall_number()).append("\n\n")
          .append(v.getContent() == null ? "" : v.getContent());

        ta.setText(sb.toString());
        ta.setCaretPosition(0);

        int total = cached.size();
        lbPage.setText((index + 1) + " / " + total);
        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < total - 1);
    }

    private void prev() {
        if (cached == null || cached.isEmpty()) return;
        if (index > 0) index--;
        render();
    }

    private void next() {
        if (cached == null || cached.isEmpty()) return;
        if (index < cached.size() - 1) index++;
        render();
    }
}
