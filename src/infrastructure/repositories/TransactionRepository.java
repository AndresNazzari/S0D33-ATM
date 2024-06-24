package infrastructure.repositories;

import core.application.mappings.DbTransactionToTransaction;
import core.application.services.UserSession;
import core.domain.Transaction;
import core.domain.TransactionType;
import infrastructure.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public static List<Transaction> getTransactions(UserSession session) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.id, t.amount, t.type, t.created_at, a.id AS atm_id " +
                "FROM transactions t " +
                "LEFT JOIN atm a ON a.id = t.atm_id ";
        String userSql = sql + (session.isAdmin() ? "" : " WHERE account_id = ?");

        try (Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(userSql)) {

            if (!session.isAdmin()) {
                stmt.setInt(1, session.getAccountId());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(DbTransactionToTransaction.mapTransaction(rs));
                }
            }
        }

        return transactions;
    }

    public static void createTransaction(int atmId, int amount, int accountId, TransactionType transactionType) throws SQLException {
        String transactionTypeValue = "";
        String sql = "INSERT INTO transactions (amount, atm_id, account_id, type) VALUES (?, ?, ?, ?)" ;

        try (Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);

            switch (transactionType) {
                case D:
                    transactionTypeValue = "D";
                    stmt.setInt(2, atmId);
                    break;
                case W:
                    transactionTypeValue = "W";
                    stmt.setInt(2, atmId);
                    break;
                case A:
                    transactionTypeValue = "A";
                    stmt.setInt(2, atmId);
                    break;
                case T:
                    transactionTypeValue = "T";
                    stmt.setNull(2, Types.NULL);
                    break;
            }

            stmt.setInt(3, accountId);
            stmt.setString(4, transactionTypeValue);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating ACCOUNT failed, no rows affected.");
            }
        }

    }
}
