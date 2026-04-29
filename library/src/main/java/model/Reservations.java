package model;

public class Reservations {
    private int id;
    private String reader_id;
    private String book_barcode;
    private String reserved_at;
    private String expected_pickup_date;
    private int state;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public String getBook_barcode() { return book_barcode; }
    public void setBook_barcode(String book_barcode) { this.book_barcode = book_barcode; }

    public String getReserved_at() { return reserved_at; }
    public void setReserved_at(String reserved_at) { this.reserved_at = reserved_at; }

    public String getExpected_pickup_date() { return expected_pickup_date; }
    public void setExpected_pickup_date(String expected_pickup_date) { this.expected_pickup_date = expected_pickup_date; }

    public int getState() { return state; }
    public void setState(int state) { this.state = state; }
}
