package infrastructure.repositories;

import core.domain.TransactionType;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository {

    public static void createAccount(int userId, int initialFounds) throws SQLException {
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";

        try (Connection conn = DbConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, initialFounds);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ACCOUNT failed, no rows affected.");
            }
        }
    }

    public static void updateMyBalance( int amount, int userId, int accountId, TransactionType transactionType) throws SQLException {
        String transactionTypeString = transactionType == TransactionType.D ? " + " : " - ";
        String sql = "UPDATE account SET balance = balance" +
                transactionTypeString + "? WHERE user_id = ? AND id = ?";

        try (Connection conn = DbConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, userId);
            stmt.setInt(3, accountId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating ACCOUNT failed, no rows affected.");
            }
        }
    }
    public static void updateRecevierBalance(int amount, int receiverAccountId) throws SQLException {

        String sql = "UPDATE account SET balance = balance + ? WHERE id = ?";

        try (Connection conn = DbConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, receiverAccountId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating ACCOUNT failed, no rows affected.");
            }
        }
    }
}
