package service.impl;

import dao.BorrowRecordsDao;
import dao.impl.BooksDaoImpl;
import dao.impl.BorrowRecordsDaoImpl;
import dao.impl.ReadersDaoImpl;
import exception.AppException;
import model.BorrowRecords;
import model.Readers;
import service.BorrowRecordsService;
import util.Settings;
import util.Tool;

import java.util.List;

public class BorrowRecordsServiceImpl implements BorrowRecordsService {

    private final BorrowRecordsDao dao = new BorrowRecordsDaoImpl();
    private final ReadersDaoImpl readersDao = new ReadersDaoImpl();
    private final BooksDaoImpl booksDao = new BooksDaoImpl();

    @Override
    public BorrowRecords borrowBook(String reader_id, String book_barcode) {
        Readers r = readersDao.findByReaderId(reader_id);
        if (r == null) throw new AppException("讀者不存在");

        model.Books b = booksDao.findByBarcode(book_barcode);
        if (b == null) throw new AppException("書籍不存在");
        // 0=可借閱；1=借出中；2=歸還待審核；3=毀損/遺失
        if (b.getState() != 0) {
            throw new AppException("此書目前不可借閱（狀態=" + b.getState() + "），請改用預約");
        }

        int limit = Settings.borrowLimitByLevel(r.getReader_level());
        if (limit <= 0) throw new AppException("目前讀者等級無借閱權限（可能負分或失誤未處理）");

        List<BorrowRecords> borrowing = dao.findBorrowingByReader(reader_id);
        if (borrowing.size() >= limit) throw new AppException("已達借閱上限：" + limit);

        BorrowRecords br = new BorrowRecords();
        br.setReader_id(reader_id);
        br.setBook_barcode(book_barcode);
        br.setBorrow_date(Tool.today());
        br.setDue_date(Tool.plusDays(br.getBorrow_date(), Settings.BORROW_DAYS));
        br.setReturn_date(null);
        br.setAdmin_note("");
        dao.create(br);

        // 更新書籍狀態為借出中(1)
        b.setState(1);
        booksDao.update(b);

        // 點數：示範先做「借閱成功 +1」；更完整可在歸還時才加
        r.setReward_points(r.getReward_points() + Settings.POINT_BORROW_RETURN_OK);
        readersDao.update(r);

        return br;
    }

    @Override
    public BorrowRecords returnBook(String reader_id, int borrowRecordId, String adminNoteTemp) {
        // v1: 讀者端先發起「歸還申請」，館員端再核可。這裡先把 return_date 寫入並留 admin_note 讓館員改。
        List<BorrowRecords> all = dao.findAll();
        BorrowRecords target = null;
        for (BorrowRecords br : all) {
            if (br.getId() == borrowRecordId) { target = br; break; }
        }
        if (target == null) throw new AppException("借閱紀錄不存在");
        if (!reader_id.equals(target.getReader_id())) throw new AppException("非本人借閱，無法操作");
        if (target.getReturn_date() != null) throw new AppException("此筆已歸還");

        target.setReturn_date(Tool.today());
        String extra = (adminNoteTemp == null ? "" : adminNoteTemp.trim());
        String note = "讀者申請歸還" + (extra.isEmpty() ? "" : ":" + extra);
        target.setAdmin_note(note);
        dao.update(target);

        // 讀者送出歸還申請後，書籍狀態改為歸還待審核(2)
        try {
            model.Books b = booksDao.findByBarcode(target.getBook_barcode());
            if (b != null) {
                b.setState(2);
                booksDao.update(b);
            }
        } catch (Exception ignored) {}

        return target;
    }

    @Override
    public List<BorrowRecords> myBorrowing(String reader_id) {
        return dao.findBorrowingByReader(reader_id);
    }

    @Override
    public java.util.List<BorrowRecords> pendingReturns() {
        java.util.List<BorrowRecords> all = dao.findAll();
        java.util.List<BorrowRecords> list = new java.util.ArrayList<>();
        for (BorrowRecords br : all) {
            if (br.getReturn_date() != null) {
                String note = br.getAdmin_note() == null ? "" : br.getAdmin_note();
                if (note.startsWith("讀者申請歸還")) list.add(br);
            }
        }
        return list;
    }

    @Override
    public int approveReturn(int borrowRecordId, int bookState, String note) {
        java.util.List<BorrowRecords> all = dao.findAll();
        BorrowRecords target = null;
        for (BorrowRecords br : all) {
            if (br.getId() == borrowRecordId) { target = br; break; }
        }
        if (target == null) throw new AppException("借閱紀錄不存在");

        String status;
        if (bookState == 0) status = "完好";
        else if (bookState == 3) status = "毀損/遺失";
        else if (bookState == 2) status = "歸還待審核";
        else status = "未知";

        String merged = "已確認歸還:" + status + (note == null || note.trim().isEmpty() ? "" : ":" + note.trim());
        target.setAdmin_note(merged);
        int n = dao.update(target);

        // 更新讀者點數（完好才加分）
        if (bookState == 0) {
            Readers r = readersDao.findByReaderId(target.getReader_id());
            if (r != null) {
                r.setReward_points(r.getReward_points() + Settings.POINT_BORROW_RETURN_OK);
                // 等級簡化：點數 >= 20 => 2；>=50 => 3
                if (r.getReward_points() >= 50) r.setReader_level(3);
                else if (r.getReward_points() >= 20) r.setReader_level(2);
                readersDao.update(r);
            }
        }

        // 書況狀態（人為痕跡）寫回 books.state（以條碼更新）
        try {
            dao.impl.BooksDaoImpl bdao = new dao.impl.BooksDaoImpl();
            model.Books b = bdao.findByBarcode(target.getBook_barcode());
            if (b != null) {
                b.setState(bookState);
                bdao.update(b);
            }
        } catch (Exception e) {
            // ignore
        }

        return n;
    }
}
