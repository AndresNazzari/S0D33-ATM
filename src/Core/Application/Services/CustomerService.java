package Core.Application.Services;

import Core.Domain.Customer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class CustomerService {

    public static TableModel customersToTableModel(List<Customer> customers) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Customer ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("DNI");
        model.addColumn("Account ID");
        model.addColumn("Balance");

        for(Customer customer : customers) {
            model.addRow(new Object[]{
                    customer.getCustomerId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getDni(),
                    customer.getAccountId(),
                    customer.getBalance()
            });
        }
        return model;
    }
}
