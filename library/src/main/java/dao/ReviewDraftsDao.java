package dao;

import model.ReviewDrafts;

public interface ReviewDraftsDao {
    ReviewDrafts findByReaderAndSlot(String reader_id, int slot);
    int upsert(ReviewDrafts d); // insert or update by (reader_id, slot)
    int deleteByReaderAndSlot(String reader_id, int slot);
}
