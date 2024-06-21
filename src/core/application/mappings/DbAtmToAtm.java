package core.application.mappings;

import core.domain.Atm;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbAtmToAtm {
    public static Atm mapAtm(ResultSet rs) throws SQLException {
        Atm atm = new Atm();
        atm.setAtmId(rs.getInt("id"));
        atm.setBalance(rs.getInt("balance"));

        return atm;
    }
}
