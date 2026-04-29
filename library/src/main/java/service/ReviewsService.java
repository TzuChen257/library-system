package service;

import model.Reviews;
import java.util.List;

public interface ReviewsService {
    int submitReview(Reviews r);
    List<Reviews> myReviews(String reader_id);
    List<Reviews> pendingReviews();
    int updateReview(Reviews r);
}
