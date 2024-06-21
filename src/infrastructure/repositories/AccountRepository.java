package infrastructure.repositories;

import core.domain.TransactionType;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository {

    public static void updateBalance(int atmId, int amount, int userId,int accountId, TransactionType transactionType) throws SQLException {
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
}
