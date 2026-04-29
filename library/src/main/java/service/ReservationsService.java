package service;

import model.Reservations;
import java.util.List;

public interface ReservationsService {
    void reserve(String reader_id, String book_barcode, String expectedPickupDate);
    List<Reservations> myActiveReservations(String reader_id);
}
