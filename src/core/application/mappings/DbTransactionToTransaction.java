package core.application.mappings;

import core.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbTransactionToTransaction {
    public static Transaction mapTransaction (ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("id"));
        transaction.setAmount(resultSet.getInt("amount"));
        transaction.setType(resultSet.getString("type"));
        transaction.setAtmId(resultSet.getInt("atm_id"));
        transaction.setCreatedAt(String.valueOf(resultSet.getDate("created_at")));

        return transaction;
    }
}
