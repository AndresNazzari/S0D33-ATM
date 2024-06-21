package core.application.mappings;

import core.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUserToUser {

    public static User mapUser (ResultSet resultSet) throws SQLException {
        User user = new User();
        user.userId = resultSet.getInt("id");
        user.firstName = resultSet.getString("first_name");
        user.lastName = resultSet.getString("last_name");
        user.dni = resultSet.getString("dni");
        user.password = resultSet.getString("password");
        user.isAdmin = resultSet.getBoolean("is_admin");
        user.accountId = resultSet.getInt("account_id");
        user.balance = resultSet.getInt("balance");

        return user;
    }
}
