package dao.impl;

import dao.ReviewDraftsDao;
import model.ReviewDrafts;
import util.Tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewDraftsDaoImpl implements ReviewDraftsDao {

    private ReviewDrafts map(ResultSet rs) throws SQLException {
        ReviewDrafts d = new ReviewDrafts();
        d.setId(rs.getInt("id"));
        d.setReader_id(rs.getString("reader_id"));
        d.setSlot(rs.getInt("slot"));
        d.setTitle(rs.getString("title"));
        d.setIsbn(rs.getString("isbn"));
        d.setPen_name(rs.getString("pen_name"));
        d.setBook_intro(rs.getString("book_intro"));
        d.setBook_excerpt(rs.getString("book_excerpt"));
        d.setContent(rs.getString("content"));
        d.setWord_count(rs.getInt("word_count"));
        return d;
    }

    @Override
    public ReviewDrafts findByReaderAndSlot(String reader_id, int slot) {
        String sql = "SELECT * FROM review_drafts WHERE reader_id=? AND slot=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            ps.setInt(2, slot);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int upsert(ReviewDrafts d) {
        // MySQL: use ON DUPLICATE KEY UPDATE; unique(reader_id, slot)
        String sql = "INSERT INTO review_drafts(reader_id, slot, title, isbn, pen_name, book_intro, book_excerpt, content, word_count) " +
                "VALUES(?,?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE title=VALUES(title), isbn=VALUES(isbn), pen_name=VALUES(pen_name), " +
                "book_intro=VALUES(book_intro), book_excerpt=VALUES(book_excerpt), content=VALUES(content), word_count=VALUES(word_count)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getReader_id());
            ps.setInt(2, d.getSlot());
            ps.setString(3, d.getTitle());
            ps.setString(4, d.getIsbn());
            ps.setString(5, d.getPen_name());
            ps.setString(6, d.getBook_intro());
            ps.setString(7, d.getBook_excerpt());
            ps.setString(8, d.getContent());
            ps.setInt(9, d.getWord_count());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteByReaderAndSlot(String reader_id, int slot) {
        String sql = "DELETE FROM review_drafts WHERE reader_id=? AND slot=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            ps.setInt(2, slot);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
