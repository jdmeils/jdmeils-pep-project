package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountService {

    public Account get(String username) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account get(int id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("SELECT * FROM account WHERE account_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account create(Account newUser) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            var statement = conn.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newUser.username);
            statement.setString(2, newUser.password);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Account(id, newUser.username, newUser.password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
