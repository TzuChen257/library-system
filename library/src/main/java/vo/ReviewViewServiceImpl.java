package vo;

import java.util.List;

/**
 * 依你的規格：ReviewViewServiceImpl.java 內含 interface ReviewViewService.java
 */
interface ReviewViewService {
    List<ReviewView> homePublicReviews(int limit);
    List<ReviewView> allPublicReviews(int limit);
}


public class ReviewViewServiceImpl implements ReviewViewService {

    private final ReviewViewDaoImpl dao = new ReviewViewDaoImpl();

    @Override
    public List<ReviewView> homePublicReviews(int limit) {
        return dao.findLatestPublic(limit);
    }

    @Override
    public List<ReviewView> allPublicReviews(int limit) {
        return dao.findAllPublic(limit);
    }

}