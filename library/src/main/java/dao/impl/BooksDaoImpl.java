package dao.impl;

import dao.BooksDao;
import model.Books;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDaoImpl implements BooksDao {

    @Override
    public int create(Books b) {
        String sql = "INSERT INTO books (book_barcode,isbn,title,author,publisher,publish_year,edition,category,call_number,price,state) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getBook_barcode());
            ps.setString(2, b.getIsbn());
            ps.setString(3, b.getTitle());
            ps.setString(4, b.getAuthor());
            ps.setString(5, b.getPublisher());
            ps.setString(6, b.getPublish_year());
            ps.setString(7, b.getEdition());
            ps.setString(8, b.getCategory());
            ps.setString(9, b.getCall_number());
            ps.setInt(10, b.getPrice());
            ps.setInt(11, b.getState());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Books create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int update(Books b) {
        String sql = "UPDATE books SET isbn=?,title=?,author=?,publisher=?,publish_year=?,edition=?,category=?,call_number=?,price=?,state=? WHERE book_barcode=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getIsbn());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getAuthor());
            ps.setString(4, b.getPublisher());
            ps.setString(5, b.getPublish_year());
            ps.setString(6, b.getEdition());
            ps.setString(7, b.getCategory());
            ps.setString(8, b.getCall_number());
            ps.setInt(9, b.getPrice());
            ps.setInt(10, b.getState());
            ps.setString(11, b.getBook_barcode());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Books update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Books findByBarcode(String book_barcode) {
        String sql = "SELECT * FROM books WHERE book_barcode=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book_barcode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Books findByBarcode failed: " + e.getMessage(), e);
        }
    }
@Override
public Books findByIsbn(String isbn) {
    String sql = "SELECT * FROM books WHERE isbn=? LIMIT 1";
    try (Connection conn = Tool.getDB();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, isbn);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    @Override
    public List<Books> search(String keyword) {
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ? OR isbn LIKE ? ORDER BY id DESC";
        List<Books> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            ps.setString(4, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Books search failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Books> findLatest(int limit) {
        String sql = "SELECT * FROM books WHERE state <> 3 ORDER BY id DESC LIMIT ?";
        List<Books> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Books findLatest failed: " + e.getMessage(), e);
        }
    }

    
    @Override
    public List<Books> findLatestPage(int offset, int limit) {
        String sql = "SELECT * FROM books WHERE state <> 3 ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Books> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Books findLatestPage failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) c FROM books WHERE state <> 3";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("c");
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Books countAll failed: " + e.getMessage(), e);
        }
    }

private Books map(ResultSet rs) throws SQLException {
        Books b = new Books();
        b.setId(rs.getInt("id"));
        b.setBook_barcode(rs.getString("book_barcode"));
        b.setIsbn(rs.getString("isbn"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setPublisher(rs.getString("publisher"));
        b.setPublish_year(rs.getString("publish_year"));
        b.setEdition(rs.getString("edition"));
        b.setCategory(rs.getString("category"));
        b.setCall_number(rs.getString("call_number"));
        b.setPrice(rs.getInt("price"));
        b.setState(rs.getInt("state"));
        return b;
    }
}
