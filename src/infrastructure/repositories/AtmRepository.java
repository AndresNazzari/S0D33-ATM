package infrastructure.repositories;

import core.application.mappings.DbAtmToAtm;
import core.domain.Atm;
import core.domain.TransactionType;
import infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtmRepository {

    public static List<Atm> getAtms() throws SQLException {
        List<Atm> atms = new ArrayList<>();
        String sql = "SELECT * FROM atm";
        try (Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                atms.add(DbAtmToAtm.mapAtm(rs));
            }

        }

        return atms;
    }

    public static void updateBalance(int atmId, int amount, TransactionType transactionType) throws SQLException {
        String transactionTypeString = (transactionType == TransactionType.D || transactionType == TransactionType.A) ? " + " : " - ";
        String sql = "UPDATE atm SET balance = balance" +
                transactionTypeString + "? WHERE id = ?";

        try (Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, atmId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating ATM failed, no rows affected.");
            }
        }
    }

    public static void CreateNewAtm() throws SQLException {
        String sql = "INSERT INTO atm (balance) VALUES (?)";

        try (Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 10000);
            stmt.executeUpdate();

        }
    }

}
