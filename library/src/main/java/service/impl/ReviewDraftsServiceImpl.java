package service.impl;

import dao.ReviewDraftsDao;
import dao.impl.ReviewDraftsDaoImpl;
import model.ReviewDrafts;
import service.ReviewDraftsService;

public class ReviewDraftsServiceImpl implements ReviewDraftsService {

    private final ReviewDraftsDao dao = new ReviewDraftsDaoImpl();

    @Override
    public ReviewDrafts load(String reader_id, int slot) {
        return dao.findByReaderAndSlot(reader_id, slot);
    }

    @Override
    public void save(ReviewDrafts d) {
        dao.upsert(d);
    }

    @Override
    public void clear(String reader_id, int slot) {
        dao.deleteByReaderAndSlot(reader_id, slot);
    }
}
