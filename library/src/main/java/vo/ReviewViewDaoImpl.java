package vo;

import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 依你的規格：ReviewViewDaoImpl.java 內含 interface ReviewViewDao.java
 */
interface ReviewViewDao {
    List<ReviewView> findLatestPublic(int limit);
    List<ReviewView> findAllPublic(int limit);
}


public class ReviewViewDaoImpl implements ReviewViewDao {

    @Override
    public List<ReviewView> findLatestPublic(int limit) {
        String sql = "SELECT * FROM review_view WHERE is_public=true ORDER BY review_id DESC LIMIT ?";
        List<ReviewView> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("ReviewView findLatestPublic failed: " + e.getMessage(), e);
        }
    }

    private ReviewView map(ResultSet rs) throws SQLException {
        ReviewView v = new ReviewView();
        v.setReview_id(rs.getInt("review_id"));
        v.setReader_id(rs.getString("reader_id"));
        v.setReader_name(rs.getString("reader_name"));
        v.setBook_barcode(rs.getString("book_barcode"));
        v.setIsbn(rs.getString("isbn"));
        v.setTitle(rs.getString("title"));
        v.setAuthor(rs.getString("author"));
        v.setPublisher(rs.getString("publisher"));
        v.setPublish_year(rs.getString("publish_year"));
        v.setCategory(rs.getString("category"));
        v.setCall_number(Tool.getStringSafe(rs, "call_number"));
        v.setWord_count(rs.getInt("word_count"));
        v.setReview_state(rs.getInt("review_state"));
        v.setIs_public(rs.getBoolean("is_public"));
        v.setContent(rs.getString("content"));
        return v;
    }

    @Override
    public List<ReviewView> findAllPublic(int limit) {
        // 以最新到最舊，最多 limit 筆（避免一次撈太大）
        return findLatestPublic(limit);
    }

}