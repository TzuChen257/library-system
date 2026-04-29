package dao.impl;

import dao.ReservationsDao;
import model.Reservations;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationsDaoImpl implements ReservationsDao {

    @Override
    public int create(Reservations r) {
        String sql = "INSERT INTO reservations (reader_id,book_barcode,reserved_at,expected_pickup_date,state) VALUES (?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReader_id());
            ps.setString(2, r.getBook_barcode());
            ps.setString(3, r.getReserved_at());
            ps.setString(4, r.getExpected_pickup_date());
            ps.setInt(5, r.getState());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Reservations create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int updateState(int id, int newState) {
        String sql = "UPDATE reservations SET state=? WHERE id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newState);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Reservations updateState failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservations> findActiveByReader(String reader_id) {
        String sql = "SELECT * FROM reservations WHERE reader_id=? AND state IN (0,1) ORDER BY id DESC";
        List<Reservations> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Reservations findActiveByReader failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservations> findByBook(String book_barcode) {
        String sql = "SELECT * FROM reservations WHERE book_barcode=? ORDER BY id DESC";
        List<Reservations> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book_barcode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Reservations findByBook failed: " + e.getMessage(), e);
        }
    }

    private Reservations map(ResultSet rs) throws SQLException {
        Reservations r = new Reservations();
        r.setId(rs.getInt("id"));
        r.setReader_id(rs.getString("reader_id"));
        r.setBook_barcode(rs.getString("book_barcode"));
        r.setReserved_at(rs.getString("reserved_at"));
        r.setExpected_pickup_date(rs.getString("expected_pickup_date"));
        r.setState(rs.getInt("state"));
        return r;
    }
}
