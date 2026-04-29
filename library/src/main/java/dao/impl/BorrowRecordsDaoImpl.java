package dao.impl;

import dao.BorrowRecordsDao;
import model.BorrowRecords;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordsDaoImpl implements BorrowRecordsDao {

    @Override
    public int create(BorrowRecords br) {
        String sql = "INSERT INTO borrow_records (reader_id,book_barcode,borrow_date,due_date,return_date,admin_note) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, br.getReader_id());
            ps.setString(2, br.getBook_barcode());
            ps.setString(3, br.getBorrow_date());
            ps.setString(4, br.getDue_date());
            ps.setString(5, br.getReturn_date());
            ps.setString(6, br.getAdmin_note());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("BorrowRecords create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int update(BorrowRecords br) {
        String sql = "UPDATE borrow_records SET reader_id=?,book_barcode=?,borrow_date=?,due_date=?,return_date=?,admin_note=? WHERE id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, br.getReader_id());
            ps.setString(2, br.getBook_barcode());
            ps.setString(3, br.getBorrow_date());
            ps.setString(4, br.getDue_date());
            ps.setString(5, br.getReturn_date());
            ps.setString(6, br.getAdmin_note());
            ps.setInt(7, br.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("BorrowRecords update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BorrowRecords> findBorrowingByReader(String reader_id) {
        String sql = "SELECT * FROM borrow_records WHERE reader_id=? AND return_date IS NULL ORDER BY borrow_date DESC";
        List<BorrowRecords> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("BorrowRecords findBorrowingByReader failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BorrowRecords> findAll() {
        String sql = "SELECT * FROM borrow_records ORDER BY id DESC";
        List<BorrowRecords> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("BorrowRecords findAll failed: " + e.getMessage(), e);
        }
    }
    //額外
    private BorrowRecords map(ResultSet rs) throws SQLException {
        BorrowRecords br = new BorrowRecords();
        br.setId(rs.getInt("id"));
        br.setReader_id(rs.getString("reader_id"));
        br.setBook_barcode(rs.getString("book_barcode"));
        br.setBorrow_date(rs.getString("borrow_date"));
        br.setDue_date(rs.getString("due_date"));
        br.setReturn_date(rs.getString("return_date"));
        br.setAdmin_note(rs.getString("admin_note"));
        return br;
    }
}
