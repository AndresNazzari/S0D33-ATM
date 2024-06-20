package infrastructure.repositories;

import core.domain.Atm;
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

        Connection conn = DbConn.getInstance().getConnection();
        String sql = "SELECT * FROM atm";
        PreparedStatement stmt = conn.prepareStatement(sql);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Atm atm = new Atm();
                atm.setAtmId(rs.getInt("id"));
                atm.setBalance(rs.getInt("balance"));

                atms.add(atm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atms;
    }
}
