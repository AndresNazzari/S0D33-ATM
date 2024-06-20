package infrastructure.repositories;

import core.domain.User;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    public static User getUserByDni(int dni) {
        DbConn dbConn = DbConn.getInstance();
        Connection connection = dbConn.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement(
                    "SELECT u.id, u.first_name, u.last_name, u.dni , u.is_admin, u.password , a.id AS account_id, a.balance " +
                            "FROM users u " +
                            "LEFT JOIN account a ON u.id = a.user_id " +
                            "WHERE u.dni = ?");
            preparedStatement.setInt(1, dni);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
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
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error getting user", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
