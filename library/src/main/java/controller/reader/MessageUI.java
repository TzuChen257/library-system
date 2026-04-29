package controller.reader;

import model.Messages;
import model.Readers;
import service.MessagesService;
import service.impl.MessagesServiceImpl;
import util.Tool;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

public class MessageUI extends JFrame {

    private final Readers me;
    private Integer openMessageId = null;
    private final MessagesService service = new MessagesServiceImpl();

    // 需求：不顯示 id，但要有編號；最新在最上面、編號最後
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"編號","日期","已讀","摘要"}, 0);
    private final JTable table = new JTable(model);

    private final JTextArea taContent = new JTextArea();

    private List<Messages> cachedList = new ArrayList<>();

    public MessageUI(Readers me) {
        this(me, null);
    }

    public MessageUI(Readers me, Integer openMessageId) {
        this.me = me;
        this.openMessageId = openMessageId;
        setTitle("個人信箱 - " + me.getReader_name());
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setContentPane(root);

        // 左：列表 / 右：內容
        JScrollPane spTable = new JScrollPane(table);

        taContent.setEditable(false);
        taContent.setLineWrap(true);
        taContent.setWrapStyleWord(true);
        JScrollPane spContent = new JScrollPane(taContent);
        spContent.setBorder(BorderFactory.createTitledBorder("信件內容"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spTable, spContent);
        split.setDividerLocation(520);
        root.add(split, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRead = new JButton("切換已讀");
        btnRead.addActionListener(e -> toggleRead());
        JButton btnDelete = new JButton("刪除");
        btnDelete.addActionListener(e -> delete());
        JButton btnClose = new JButton("返回");
        btnClose.addActionListener(e -> dispose());

        btns.add(btnRead);
        btns.add(btnDelete);
        btns.add(btnClose);
        root.add(btns, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            showSelected();
        });

        load();

        UITheme.apply(this);
    }

    private void load() {
        model.setRowCount(0);
        List<Messages> list;
        if (Beans.isDesignTime()) {
            list = new ArrayList<>();
            Messages m1 = new Messages();
            m1.setId(1);
            m1.setCreated_at("2026-02-25 12:00:00");
            m1.setIs_read(false);
            m1.setContent("（設計模式示範）這裡會顯示信件內容...\n可調整視窗大小與排版。\n");
            list.add(m1);
        } else {
            list = service.inbox(me.getReader_id());
        }

        cachedList = list;

        // list 假設已按時間由新到舊（service 內若未排序，也會在 DB 端排序）
        int n = list.size();
        for (int i = 0; i < n; i++) {
            Messages m = list.get(i);
            int serial = n - i; // 最新(i=0) → 編號 n；最舊(i=n-1) → 1
            String content = m.getContent() == null ? "" : m.getContent();
            String summary = content.replaceAll("\\s+", " ");
            if (summary.length() > 40) summary = summary.substring(0, 40) + "...";
            model.addRow(new Object[]{serial, m.getCreated_at(), m.isIs_read(), summary});
        }

        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        } else {
            taContent.setText("");
        }
    }

    private void toggleRead() {
        int row = table.getSelectedRow();
        if (row < 0) { Tool.errorBox("請選一筆訊息"); return; }
        Messages m = selectedMessage();
        if (m == null) return;
        service.markRead(m.getId(), !m.isIs_read());
        load();

        UITheme.apply(this);
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row < 0) { Tool.errorBox("請選一筆訊息"); return; }
        Messages m = selectedMessage();
        if (m == null) return;
        service.delete(m.getId());
        load();

        UITheme.apply(this);
    }

    private Messages selectedMessage() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        List<Messages> list = cachedList;
        if (list == null || list.isEmpty()) return null;
        int n = list.size();
        // row 0 是最新 → list index 0
        int idx = row;
        if (idx < 0 || idx >= n) return null;
        return list.get(idx);
    }

    private void showSelected() {
        Messages m = selectedMessage();
        if (m == null) return;
        taContent.setText(m.getContent() == null ? "" : m.getContent());
        taContent.setCaretPosition(0);
        // 需求：只要點擊過就算已讀
        if (!Beans.isDesignTime() && !m.isIs_read()) {
            service.markRead(m.getId(), true);
            load();
        }
    }


    private void selectOpenMessage() {
        if (openMessageId == null) return;
        // 目前 UI 不顯示 id，openMessageId 改為：嘗試定位到該 id 所在列
        if (Beans.isDesignTime()) return;
        List<Messages> list = service.inbox(me.getReader_id());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == openMessageId.intValue()) {
                if (i < model.getRowCount()) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                }
                break;
            }
        }
    }

}
