package Infrastructure.Repositories;

import Core.Application.Services.UserSession;
import Core.Domain.Customer;
import Core.Domain.Transaction;
import Infrastructure.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public static List<Customer> getCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        Connection conn = DbConn.getInstance().getConnection();
        String sql = "SELECT u.id, u.first_name, u.last_name, u.dni , a.id AS account_id, a.balance " +
                "FROM users u " +
                "left JOIN account a ON u.id = a.user_id " +
                "WHERE u.is_admin = 0";

        PreparedStatement stmt = conn.prepareStatement(sql);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setDni(rs.getInt("dni"));
                customer.setAccountId(rs.getInt("account_id"));
                customer.setBalance(rs.getInt("balance"));

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
