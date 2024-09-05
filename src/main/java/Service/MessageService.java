package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessageText(int messageId, String newMessageText) {
        return messageDAO.updateMessageText(messageId, newMessageText);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessageByAccountId(accountId);
    }
}
