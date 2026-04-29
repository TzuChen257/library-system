package service;

import model.ReviewDrafts;

public interface ReviewDraftsService {
    ReviewDrafts load(String reader_id, int slot);
    void save(ReviewDrafts d);
    void clear(String reader_id, int slot);
}
