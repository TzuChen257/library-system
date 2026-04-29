package service;

import model.BorrowRecords;
import java.util.List;

public interface BorrowRecordsService {
    BorrowRecords borrowBook(String reader_id, String book_barcode);
    BorrowRecords returnBook(String reader_id, int borrowRecordId, String adminNoteTemp);
    List<BorrowRecords> myBorrowing(String reader_id);
    java.util.List<BorrowRecords> pendingReturns();
    int approveReturn(int borrowRecordId, int bookState, String note);
}
