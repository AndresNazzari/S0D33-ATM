package infrastructure.repositories;

import core.application.mappings.DbUserToUser;
import core.domain.User;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static User getUserByDni(int dni) {
        DbConn dbConn = DbConn.getInstance();
        String sql = "SELECT u.id, u.first_name, u.last_name, u.dni, u.is_admin, u.password, a.id AS account_id, a.balance " +
                "FROM users u " +
                "LEFT JOIN account a ON u.id = a.user_id " +
                "WHERE u.dni = ?";

        try (Connection connection = dbConn.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, dni);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return DbUserToUser.mapUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting user by DNI: " + dni, e);
        }
        return null;
    }
}
