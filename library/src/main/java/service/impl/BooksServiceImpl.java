package service.impl;

import dao.BooksDao;
import dao.impl.BooksDaoImpl;
import model.Books;
import service.BooksService;

import java.util.List;

public class BooksServiceImpl implements BooksService {

    private final BooksDao booksDao = new BooksDaoImpl();

    @Override
    public List<Books> search(String keyword) {
        return booksDao.search(keyword);
    }

    @Override
    public List<Books> latest(int limit) {
        return booksDao.findLatest(limit);
    }

	@Override
	public Books findByIsbn(String isbn) {
		return booksDao.findByIsbn(isbn);
	}

	@Override
	public List<Books> latestPage(int page, int pageSize) {
		int offset = Math.max(0, (page - 1) * pageSize);
		return booksDao.findLatestPage(offset, pageSize);
	}

	@Override
	public int countAll() {
		return booksDao.countAll();
	}
}
