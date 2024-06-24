package infrastructure.repositories;

import core.application.mappings.DbUserToUser;
import core.application.services.PasswordService;
import core.domain.User;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static int createUser(String firstName, String lastName, String dni, String password, boolean isAdmin) {
        String sql = "INSERT INTO users (first_name, last_name, dni, password, is_admin) VALUES (?, ?, ?, ?, ?)";
        String hashedPassword = PasswordService.hashPassword(password);

        try (Connection connection = DbConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, dni);
            stmt.setString(4, hashedPassword);
            stmt.setBoolean(5, isAdmin);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user: " + firstName + " " + lastName, e);
        }
    }

    public static User getUserByDni(String dni) {
        DbConn dbConn = DbConn.getInstance();
        String sql = "SELECT u.id, u.first_name, u.last_name, u.dni, u.is_admin, u.password, a.id AS account_id, a.balance " +
                "FROM users u " +
                "LEFT JOIN account a ON u.id = a.user_id " +
                "WHERE u.dni = ?";

        try (Connection connection = dbConn.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, dni);

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
