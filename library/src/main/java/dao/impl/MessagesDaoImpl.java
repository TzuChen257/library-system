package dao.impl;

import dao.MessagesDao;
import model.Messages;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessagesDaoImpl implements MessagesDao {

    @Override
    public int create(Messages m) {
        String sql = "INSERT INTO messages (reader_id,message_type,content,is_read,created_at) VALUES (?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getReader_id());
            ps.setInt(2, m.getMessage_type());
            ps.setString(3, m.getContent());
            ps.setBoolean(4, m.isIs_read());
            ps.setString(5, m.getCreated_at());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Messages create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int markRead(int id, boolean isRead) {
        String sql = "UPDATE messages SET is_read=? WHERE id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isRead);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Messages markRead failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM messages WHERE id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Messages delete failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Messages> findByReader(String reader_id) {
        String sql = "SELECT * FROM messages WHERE reader_id=? ORDER BY id DESC";
        List<Messages> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Messages findByReader failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int countUnread(String reader_id) {
        String sql = "SELECT COUNT(*) AS c FROM messages WHERE reader_id=? AND is_read=false";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("c");
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Messages countUnread failed: " + e.getMessage(), e);
        }
    }

    private Messages map(ResultSet rs) throws SQLException {
        Messages m = new Messages();
        m.setId(rs.getInt("id"));
        m.setReader_id(rs.getString("reader_id"));
        m.setMessage_type(rs.getInt("message_type"));
        m.setContent(rs.getString("content"));
        m.setIs_read(rs.getBoolean("is_read"));
        m.setCreated_at(rs.getString("created_at"));
        return m;
    }
}
