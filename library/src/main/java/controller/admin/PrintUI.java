package controller.admin;

import util.ExcelTool;
import util.Tool;
import util.UITheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.time.LocalDate;

public class PrintUI extends JFrame {

    private final JComboBox<Integer> cbYear = new JComboBox<>();
    private final JSpinner spTopN = new JSpinner(new SpinnerNumberModel(20, 1, 200, 1));

    public PrintUI() {
        setTitle("報表印出（Excel）");
        setSize(365, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("返回首頁");
        btnBack.addActionListener(e -> dispose());
        top.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("選擇年度，輸出 Excel 報表");
        top.add(title, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        form.add(new JLabel("年度："));
        int now = LocalDate.now().getYear();
        for (int y = now; y >= now - 10; y--) cbYear.addItem(y);
        cbYear.setSelectedItem(now);
        form.add(cbYear);

        form.add(new JLabel("TopN（書籍排行）："));
        form.add(spTopN);

        root.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("輸出 Excel");
        btnExport.addActionListener(e -> export());
        bottom.add(btnExport);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private void export() {
        int year = (Integer) cbYear.getSelectedItem();
        int topN = (Integer) spTopN.getValue();

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("選擇輸出位置");
        fc.setSelectedFile(new File("library_report_" + year + ".xlsx"));
        int ok = fc.showSaveDialog(this);
        if (ok != JFileChooser.APPROVE_OPTION) return;

        File out = fc.getSelectedFile();
        try {
            File f = ExcelTool.exportReportWorkbook(year, topN, out);
            if (f != null) Tool.infoBox("輸出完成：" + f.getAbsolutePath());
            else Tool.errorBox("輸出失敗（請檢查 DB 連線與資料）");
        } catch (Exception e) {
            e.printStackTrace();
            Tool.errorBox("輸出失敗：" + e.getMessage());
        }
        UITheme.apply(this);
    }
}
