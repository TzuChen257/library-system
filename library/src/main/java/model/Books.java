package model;

public class Books {
    private int id;
    private String book_barcode;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publish_year;
    private String edition;
    private String category;
    private String call_number;
    private int price;
    private int state;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBook_barcode() { return book_barcode; }
    public void setBook_barcode(String book_barcode) { this.book_barcode = book_barcode; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublish_year() { return publish_year; }
    public void setPublish_year(String publish_year) { this.publish_year = publish_year; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCall_number() { return call_number; }
    public void setCall_number(String call_number) { this.call_number = call_number; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getState() { return state; }
    public void setState(int state) { this.state = state; }

    @Override
    public String toString() {
        return "Books{" +
                "book_barcode='" + book_barcode + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", call_number='" + call_number + '\'' +
                '}';
    }
}
