package model;

public class BorrowRecords {
    private int id;
    private String reader_id;
    private String book_barcode;
    private String borrow_date;
    private String due_date;
    private String return_date;
    private String admin_note;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public String getBook_barcode() { return book_barcode; }
    public void setBook_barcode(String book_barcode) { this.book_barcode = book_barcode; }

    public String getBorrow_date() { return borrow_date; }
    public void setBorrow_date(String borrow_date) { this.borrow_date = borrow_date; }

    public String getDue_date() { return due_date; }
    public void setDue_date(String due_date) { this.due_date = due_date; }

    public String getReturn_date() { return return_date; }
    public void setReturn_date(String return_date) { this.return_date = return_date; }

    public String getAdmin_note() { return admin_note; }
    public void setAdmin_note(String admin_note) { this.admin_note = admin_note; }
}
