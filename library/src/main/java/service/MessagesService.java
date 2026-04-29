package service;

import model.Messages;
import java.util.List;

public interface MessagesService {
    void send(String reader_id, int messageType, String content);
    int unreadCount(String reader_id);
    List<Messages> inbox(String reader_id);
    void markRead(int messageId, boolean isRead);
    void delete(int messageId);
}
