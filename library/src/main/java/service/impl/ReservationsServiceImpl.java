package service.impl;

import dao.BooksDao;
import dao.ReservationsDao;
import dao.impl.BooksDaoImpl;
import dao.impl.ReservationsDaoImpl;
import model.Reservations;
import service.ReservationsService;
import util.Tool;

import java.util.List;

public class ReservationsServiceImpl implements ReservationsService {

    private final ReservationsDao dao = new ReservationsDaoImpl();
    private final BooksDao booksDao = new BooksDaoImpl();

    @Override
    public void reserve(String reader_id, String book_barcode, String expectedPickupDate) {
        model.Books b = booksDao.findByBarcode(book_barcode);
        if (b == null) throw new RuntimeException("書籍不存在");
        if (!(b.getState() == 1 || b.getState() == 2)) {
            throw new RuntimeException("此書目前不可預約（狀態=" + b.getState() + "），可借閱狀態請直接借閱");
        }

        Reservations r = new Reservations();
        r.setReader_id(reader_id);
        r.setBook_barcode(book_barcode);
        r.setReserved_at(Tool.today());
        r.setExpected_pickup_date(expectedPickupDate);
        r.setState(0);
        dao.create(r);
    }

    @Override
    public List<Reservations> myActiveReservations(String reader_id) {
        return dao.findActiveByReader(reader_id);
    }
}
