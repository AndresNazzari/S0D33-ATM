package core.application.mappings;

import core.domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbCustomerToCustomer {
    public static Customer mapCustomer (ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setDni(rs.getInt("dni"));
        customer.setAccountId(rs.getInt("account_id"));
        customer.setBalance(rs.getInt("balance"));

        return customer;
    }
}
