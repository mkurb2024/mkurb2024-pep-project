package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values(?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
