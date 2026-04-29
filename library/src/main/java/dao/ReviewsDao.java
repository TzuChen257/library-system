package dao;

import model.Reviews;
import java.util.List;

public interface ReviewsDao {
    int create(Reviews r);
    int update(Reviews r);
    List<Reviews> findByReader(String reader_id);
    List<Reviews> findPending(); // state=0
}
