package service.impl;

import dao.MessagesDao;
import dao.impl.MessagesDaoImpl;
import model.Messages;
import service.MessagesService;
import util.Tool;

import java.util.List;

public class MessagesServiceImpl implements MessagesService {

    private final MessagesDao dao = new MessagesDaoImpl();

    @Override
    public void send(String reader_id, int messageType, String content) {
        Messages m = new Messages();
        m.setReader_id(reader_id);
        m.setMessage_type(messageType);
        m.setContent(content);
        m.setIs_read(false);
        m.setCreated_at(Tool.today());
        dao.create(m);
    }

    @Override
    public int unreadCount(String reader_id) {
        return dao.countUnread(reader_id);
    }

    @Override
    public List<Messages> inbox(String reader_id) {
        return dao.findByReader(reader_id);
    }

    @Override
    public void markRead(int messageId, boolean isRead) {
        dao.markRead(messageId, isRead);
    }

    @Override
    public void delete(int messageId) {
        dao.delete(messageId);
    }
}
