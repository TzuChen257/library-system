package service.impl;

import dao.ReviewsDao;
import dao.impl.ReviewsDaoImpl;
import model.Reviews;
import service.ReviewsService;

import java.util.List;

public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsDao dao = new ReviewsDaoImpl();

    @Override
    public int submitReview(Reviews r) {
        // state: 0 = pending
        r.setState(0);
        r.setIs_public(false);
        return dao.create(r);
    }

    @Override
    public List<Reviews> myReviews(String reader_id) {
        return dao.findByReader(reader_id);
    }

    @Override
    public List<Reviews> pendingReviews() {
        return dao.findPending();
    }

    @Override
    public int updateReview(Reviews r) {
        return dao.update(r);
    }
}
