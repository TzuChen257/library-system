package dao;

import model.BorrowRecords;
import java.util.List;

public interface BorrowRecordsDao {
    int create(BorrowRecords br);
    int update(BorrowRecords br);
    List<BorrowRecords> findBorrowingByReader(String reader_id); // return_date is null
    List<BorrowRecords> findAll();
}
