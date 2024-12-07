package Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageService {
    
    public Message create(Message newMsg) {
        try  {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, newMsg.posted_by);
            statement.setString(2, newMsg.message_text);
            statement.setLong(3, newMsg.time_posted_epoch);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Message(id, newMsg.posted_by, newMsg.message_text, newMsg.time_posted_epoch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message getById(int id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("SELECT * FROM message WHERE message_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getByUser(int userId) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("SELECT * FROM message WHERE posted_by = ?");
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            var user = new ArrayList<Message>();
            while (rs.next()) {
                user.add(fromResultSet(rs));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Message>();
    }

    public List<Message> get() {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("SELECT * FROM message");
            ResultSet rs = statement.executeQuery();
            var all = new ArrayList<Message>();
            while (rs.next()) {
                all.add(fromResultSet(rs));
            }
            return all;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Message>();
    }

    public void delete(int id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("DELETE FROM message WHERE message_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int id, String newContent) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            statement.setString(1, newContent);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message fromResultSet(ResultSet rs) throws SQLException {
        return new Message(
            rs.getInt("message_id"),
            rs.getInt("posted_by"),
            rs.getString("message_text"),
            rs.getLong("time_posted_epoch")
        );
    }

}
