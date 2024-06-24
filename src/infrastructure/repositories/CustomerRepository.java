package infrastructure.repositories;

import core.application.mappings.DbCustomerToCustomer;
import core.domain.Customer;
import infrastructure.DbConn;
import core.application.services.UserSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public static List<Customer> getCustomers(boolean includeMe) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT u.id, u.first_name, u.last_name, u.dni , a.id AS account_id, a.balance " +
                "FROM users u " +
                "left JOIN account a ON u.id = a.user_id ";
        if (!includeMe){
            sql += "WHERE u.id != ?";
        }


        try {
            Connection conn = DbConn.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (!includeMe) {
                stmt.setInt(1, UserSession.getInstance().getUserId());
            }
            ResultSet rs = stmt.executeQuery(); // ResultSet también se incluye en try-with-resources

            while (rs.next()) {
                customers.add(DbCustomerToCustomer.mapCustomer(rs));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }
}
