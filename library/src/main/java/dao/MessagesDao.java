package dao;

import model.Messages;
import java.util.List;

public interface MessagesDao {
    int create(Messages m);
    int markRead(int id, boolean isRead);
    int delete(int id);
    List<Messages> findByReader(String reader_id);
    int countUnread(String reader_id);
}
