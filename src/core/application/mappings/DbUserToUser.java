package core.application.mappings;

import core.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUserToUser {

    public static User mapUser (ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setDni(resultSet.getString("dni"));
        user.setPassword(resultSet.getString("password"));
        user.setAdmin(resultSet.getBoolean("is_admin"));
        user.setAccountId(resultSet.getInt("account_id"));
        user.setBalance(resultSet.getInt("balance"));

        return user;
    }
}
