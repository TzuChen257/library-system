package dao.impl;

import dao.ReviewsDao;
import model.Reviews;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewsDaoImpl implements ReviewsDao {

    @Override
    public int create(Reviews r) {
        String sql = "INSERT INTO reviews (reader_id,book_barcode,content,word_count,state,is_public) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReader_id());
            ps.setString(2, r.getBook_barcode());
            ps.setString(3, r.getContent());
            ps.setInt(4, r.getWord_count());
            ps.setInt(5, r.getState());
            ps.setBoolean(6, r.isIs_public());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Reviews create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int update(Reviews r) {
        String sql = "UPDATE reviews SET content=?,word_count=?,state=?,is_public=? WHERE id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getContent());
            ps.setInt(2, r.getWord_count());
            ps.setInt(3, r.getState());
            ps.setBoolean(4, r.isIs_public());
            ps.setInt(5, r.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Reviews update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reviews> findByReader(String reader_id) {
        String sql = "SELECT * FROM reviews WHERE reader_id=? ORDER BY id DESC";
        List<Reviews> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Reviews findByReader failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reviews> findPending() {
        String sql = "SELECT * FROM reviews WHERE state=0 ORDER BY id ASC";
        List<Reviews> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Reviews findPending failed: " + e.getMessage(), e);
        }
    }

    private Reviews map(ResultSet rs) throws SQLException {
        Reviews r = new Reviews();
        r.setId(rs.getInt("id"));
        r.setReader_id(rs.getString("reader_id"));
        r.setBook_barcode(rs.getString("book_barcode"));
        r.setContent(rs.getString("content"));
        r.setWord_count(rs.getInt("word_count"));
        r.setState(rs.getInt("state"));
        r.setIs_public(rs.getBoolean("is_public"));
        return r;
    }
}
