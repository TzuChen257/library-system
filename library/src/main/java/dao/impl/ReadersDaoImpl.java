package dao.impl;

import dao.ReadersDao;
import model.Readers;
import util.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadersDaoImpl implements ReadersDao {

    @Override
    public int create(Readers r) {
        String sql = "INSERT INTO readers (reader_id,reader_name,national_id,birthday,address,phone,email,online_username,online_password,reward_points,reader_level) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReader_id());
            ps.setString(2, r.getReader_name());
            ps.setString(3, r.getNational_id());
            ps.setString(4, r.getBirthday());
            ps.setString(5, r.getAddress());
            ps.setString(6, r.getPhone());
            ps.setString(7, r.getEmail());
            ps.setString(8, r.getOnline_username());
            ps.setString(9, r.getOnline_password());
            ps.setInt(10, r.getReward_points());
            ps.setInt(11, r.getReader_level());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Readers create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int update(Readers r) {
        String sql = "UPDATE readers SET reader_name=?,national_id=?,birthday=?,address=?,phone=?,email=?,online_username=?,online_password=?,reward_points=?,reader_level=? WHERE reader_id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReader_name());
            ps.setString(2, r.getNational_id());
            ps.setString(3, r.getBirthday());
            ps.setString(4, r.getAddress());
            ps.setString(5, r.getPhone());
            ps.setString(6, r.getEmail());
            ps.setString(7, r.getOnline_username());
            ps.setString(8, r.getOnline_password());
            ps.setInt(9, r.getReward_points());
            ps.setInt(10, r.getReader_level());
            ps.setString(11, r.getReader_id());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Readers update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Readers findByReaderId(String reader_id) {
        String sql = "SELECT * FROM readers WHERE reader_id=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Readers findByReaderId failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Readers findByUsername(String online_username) {
        String sql = "SELECT * FROM readers WHERE online_username=?";
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, online_username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Readers findByUsername failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Readers> findAll() {
        String sql = "SELECT * FROM readers ORDER BY id ASC";
        List<Readers> list = new ArrayList<>();
        try (Connection conn = Tool.getDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Readers findAll failed: " + e.getMessage(), e);
        }
    }

    private Readers map(ResultSet rs) throws SQLException {
        Readers r = new Readers();
        r.setId(rs.getInt("id"));
        r.setReader_id(rs.getString("reader_id"));
        r.setReader_name(rs.getString("reader_name"));
        r.setNational_id(rs.getString("national_id"));
        r.setBirthday(rs.getString("birthday"));
        r.setAddress(rs.getString("address"));
        r.setPhone(rs.getString("phone"));
        r.setEmail(rs.getString("email"));
        r.setOnline_username(rs.getString("online_username"));
        r.setOnline_password(rs.getString("online_password"));
        r.setReward_points(rs.getInt("reward_points"));
        r.setReader_level(rs.getInt("reader_level"));
        return r;
    }
}
