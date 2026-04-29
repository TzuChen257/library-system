package service;

import model.Books;
import java.util.List;

public interface BooksService {
    List<Books> search(String keyword);
    List<Books> latest(int limit);
    List<Books> latestPage(int page, int pageSize);
    int countAll();
    Books findByIsbn(String isbn);
}
