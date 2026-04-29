package controller.reader;

import model.BorrowRecords;
import model.Readers;
import service.BorrowRecordsService;
import service.ReservationsService;
import service.impl.BorrowRecordsServiceImpl;
import service.impl.ReservationsServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReaderUI extends JFrame {

    private final Readers me;
    private final BorrowRecordsService borrowService = new BorrowRecordsServiceImpl();
    private final ReservationsService reservationsService = new ReservationsServiceImpl();

    private final DefaultTableModel borrowingModel = new DefaultTableModel(new Object[]{"紀錄ID","條碼","借閱日","到期日"}, 0);

    public ReaderUI(Readers me) {
        this.me = me;
        setTitle("會員中心 - " + me.getReader_name());
        setSize(760, 480);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setContentPane(root);

        JLabel info = new JLabel("點數：" + me.getReward_points() + " / 等級：" + me.getReader_level());
        info.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        root.add(info, BorderLayout.NORTH);

        JTable tb = new JTable(borrowingModel);
        root.add(new JScrollPane(tb), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("刷新借閱中");
        btnRefresh.addActionListener(e -> loadBorrowing());
        JButton btnReturn = new JButton("勾選歸還（申請）");
        btnReturn.addActionListener(e -> {
            int row = tb.getSelectedRow();
            if (row < 0) { Tool.errorBox("請先選一筆借閱中"); return; }
            int id = (int) borrowingModel.getValueAt(row, 0);
            borrowService.returnBook(me.getReader_id(), id, "讀者申請歸還");
            Tool.infoBox("已送出歸還申請（館員端可核可並補充書況備註）");
            loadBorrowing();
        });
        JButton btnClose = new JButton("返回");
        btnClose.addActionListener(e -> dispose());

        btns.add(btnRefresh);
        btns.add(btnReturn);
        btns.add(btnClose);
        root.add(btns, BorderLayout.SOUTH);

        loadBorrowing();
    }

    private void loadBorrowing() {
        borrowingModel.setRowCount(0);
        List<BorrowRecords> list = borrowService.myBorrowing(me.getReader_id());
        for (BorrowRecords br : list) {
            borrowingModel.addRow(new Object[]{br.getId(), br.getBook_barcode(), br.getBorrow_date(), br.getDue_date()});
        }
        UITheme.apply(this);
    }
}
