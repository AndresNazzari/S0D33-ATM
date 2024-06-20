package infrastructure.repositories;

import core.Application.services.UserSession;
import core.domain.Transaction;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public static List<Transaction> getTransactions(UserSession session) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        Connection conn = DbConn.getInstance().getConnection();
        String sql = "SELECT t.id, t.amount, t.`type`, t.created_at, a.id AS atm_id " +
                "FROM transactions t " +
                "INNER JOIN atm a ON a.id = t.atm_id ";

        if (!session.isAdmin()) {
            sql += "WHERE account_id = ?";
        }

        PreparedStatement stmt = conn.prepareStatement(sql);
        if (!session.isAdmin()) {
            stmt.setInt(1, session.getAccountId());
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("id"));
                transaction.setAmount(rs.getInt("amount"));
                transaction.setType(rs.getString("type"));
                transaction.setAtmId(rs.getInt("atm_id"));
                transaction.setCreatedAt(String.valueOf(rs.getDate("created_at")));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
