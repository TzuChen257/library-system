package dao;

import model.Books;
import java.util.List;

public interface BooksDao {
    int create(Books b);
    int update(Books b);
    Books findByBarcode(String book_barcode);
    Books findByIsbn(String isbn);
    List<Books> search(String keyword);
    List<Books> findLatest(int limit);
    List<Books> findLatestPage(int offset, int limit);
    int countAll();
}
