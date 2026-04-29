package dao;

import model.Reservations;
import java.util.List;

public interface ReservationsDao {
    int create(Reservations r);
    int updateState(int id, int newState);
    List<Reservations> findActiveByReader(String reader_id); // state=0/1
    List<Reservations> findByBook(String book_barcode);
}
