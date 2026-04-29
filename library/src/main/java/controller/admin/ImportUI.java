package controller.admin;

import dao.impl.BooksDaoImpl;
import model.Books;
import util.ExcelTool;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ImportUI extends JFrame {

    private final JTextArea taLog = new JTextArea();
    private final BooksDaoImpl booksDao = new BooksDaoImpl();

    // manual fields
    private final JTextField fIsbn = new JTextField();
    private final JTextField fTitle = new JTextField();
    private final JTextField fAuthor = new JTextField();
    private final JTextField fPublisher = new JTextField();
    private final JTextField fPublishYear = new JTextField();
    private final JTextField fEdition = new JTextField();
    private final JTextField fCategory = new JTextField();
    private final JTextField fCallNumber = new JTextField();
    private final JTextField fPrice = new JTextField("0");
    private final JComboBox<Integer> cbState = new JComboBox<>(new Integer[]{0,1,2,3});

    public ImportUI() {
        setTitle("匯入書籍（Excel/單筆）");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setContentPane(root);

        // 需求：讓使用者選擇「單筆輸入」或「批次上傳」
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("批次上傳", buildBatch());
        tabs.addTab("手動輸入", buildManual());
        root.add(tabs, BorderLayout.CENTER);

        JButton btnClose = new JButton("返回");
        btnClose.addActionListener(e -> dispose());
        root.add(btnClose, BorderLayout.SOUTH);
    }

    private JPanel buildBatch() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTemplate = new JButton("下載Excel範本");
        btnTemplate.addActionListener(e -> exportTemplate());
        JButton btnImport = new JButton("選擇Excel並匯入");
        btnImport.addActionListener(e -> importExcel());
        top.add(btnTemplate);
        top.add(btnImport);
        p.add(top, BorderLayout.NORTH);

        taLog.setEditable(false);
        p.add(new JScrollPane(taLog), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildManual() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(0,2,10,10));

        form.add(new JLabel("ISBN")); form.add(fIsbn);
        form.add(new JLabel("書名")); form.add(fTitle);
        form.add(new JLabel("作者")); form.add(fAuthor);
        form.add(new JLabel("出版社")); form.add(fPublisher);
        form.add(new JLabel("出版年份")); form.add(fPublishYear);
        form.add(new JLabel("版次")); form.add(fEdition);
        form.add(new JLabel("類別")); form.add(fCategory);
        form.add(new JLabel("索書號")); form.add(fCallNumber);
        form.add(new JLabel("價格")); form.add(fPrice);
        form.add(new JLabel("狀態(state)")); form.add(cbState);

        p.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("新增到資料庫");
        btnAdd.addActionListener(e -> addOne());
        bottom.add(btnAdd);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private void exportTemplate() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("books_import_template.xlsx"));
        int r = fc.showSaveDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;

        File out = fc.getSelectedFile();
        ExcelTool.exportBooksImportTemplate(out);
        Tool.infoBox("已輸出範本：" + out.getAbsolutePath());
    }

    private void importExcel() {
        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;

        File f = fc.getSelectedFile();
        try {
            List<Books> list = ExcelTool.readBooksFromExcel(f);
            int ok = 0;
            int row = 1;
            for (Books b : list) {
                try {
                    booksDao.create(b);
                    ok++;
                } catch (Exception ex) {
                    taLog.append("第" + row + "行匯入失敗：" + ex.getMessage() + "\n");
                }
                row++;
            }
            taLog.append("匯入完成：成功 " + ok + " 筆 / 來源 " + list.size() + " 筆\n");
        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
        UITheme.apply(this);
    }

    private void addOne() {
        try {
            Books b = new Books();
            b.setBook_barcode(Tool.newBookBarcode());
            b.setIsbn(fIsbn.getText().trim());
            b.setTitle(fTitle.getText().trim());
            b.setAuthor(fAuthor.getText().trim());
            b.setPublisher(fPublisher.getText().trim());
            b.setPublish_year(fPublishYear.getText().trim());
            b.setEdition(fEdition.getText().trim());
            b.setCategory(fCategory.getText().trim());
            b.setCall_number(fCallNumber.getText().trim());

            int price = 0;
            try { price = Integer.parseInt(fPrice.getText().trim()); } catch (Exception ignore) {}
            b.setPrice(price);
            b.setState((Integer) cbState.getSelectedItem());

            if (b.getTitle() == null || b.getTitle().trim().isEmpty()) {
                Tool.errorBox("書名不可空白");
                return;
            }
            booksDao.create(b);
            Tool.infoBox("新增成功！條碼=" + b.getBook_barcode());

            // clear minimal fields
            fIsbn.setText("");
            fTitle.setText("");
            fAuthor.setText("");
            fPublisher.setText("");
            fPublishYear.setText("");
            fEdition.setText("");
            fCategory.setText("");
            fCallNumber.setText("");
            fPrice.setText("0");
            cbState.setSelectedItem(0);

        } catch (Exception ex) {
            Tool.errorBox(ex.getMessage());
        }
        UITheme.apply(this);
    }
}
