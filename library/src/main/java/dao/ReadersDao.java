package dao;

import model.Readers;
import java.util.List;

public interface ReadersDao {
    int create(Readers r);
    int update(Readers r);
    Readers findByReaderId(String reader_id);
    Readers findByUsername(String online_username);
    List<Readers> findAll();
}
