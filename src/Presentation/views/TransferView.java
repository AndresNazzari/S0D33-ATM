package Presentation.views;

import core.application.services.CustomerService;
import core.application.services.UserSession;
import core.domain.Customer;
import core.domain.TransactionType;
import infrastructure.repositories.AccountRepository;
import infrastructure.repositories.CustomerRepository;
import infrastructure.repositories.TransactionRepository;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class TransferView extends JFrame {
    private JButton makeTransferBtn;
    private JTextField amountTxt;
    private JTable customersTable;
    private JPanel transferPanel;
    private JLabel balanceLabel;

    public TransferView() {
        setTitle("Transfer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(transferPanel);
        setLocationRelativeTo(null);
        pack();

        balanceLabel.setText("$" + UserSession.getInstance().getBalance());
        addWinListener();
        addMakeTransferBtnListener();
        loadCustomers();
    }

    public void addMakeTransferBtnListener() {
        makeTransferBtn.addActionListener(_ -> {
            UserSession userSession = UserSession.getInstance();
            try {
                int amount = Integer.parseInt(amountTxt.getText());

                int selectedRow = customersTable.getSelectedRow();
                TableModel model = customersTable.getModel();

                Customer selectedCustomer = new Customer();
                selectedCustomer.setCustomerId((Integer) model.getValueAt(selectedRow, 0));
                selectedCustomer.setFirstName((String) model.getValueAt(selectedRow, 1));
                selectedCustomer.setLastName((String) model.getValueAt(selectedRow, 2));
                selectedCustomer.setDni((int) model.getValueAt(selectedRow, 3));
                selectedCustomer.setAccountId((Integer) model.getValueAt(selectedRow, 4));
                selectedCustomer.setBalance((Integer) model.getValueAt(selectedRow, 5));

                String validationError = transferValidations(selectedCustomer, amount, userSession.getBalance());
                if (!validationError.isEmpty()) {
                    JOptionPane.showMessageDialog(this, validationError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AccountRepository.updateMyBalance(amount, userSession.getUserId(), userSession.getAccountId(), TransactionType.T);
                AccountRepository.updateRecevierBalance(amount, selectedCustomer.getAccountId());
                TransactionRepository.createTransaction(0, amount, userSession.getAccountId(), TransactionType.T);
                UserSession.updateBalance(amount, TransactionType.T);

                JOptionPane.showMessageDialog(this, "Transaction successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                DashView dashView = new DashView();
                dashView.setVisible(true);
                dashView.pack();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String transferValidations(Customer selectedCustomer, int amount, int userBalance){
        String Message = "";
        if (amount <= 0) {
            return "Please enter a valid amount greater than 0.";
        }
        if ( amount > userBalance) {
            return "You dont have enough money to transfer.";
        }
        if (selectedCustomer == null) {
            return "Please select an Customer to transfer. No Customer Selected";
        }

        return Message;
    }

    private void loadCustomers() {
        List<Customer> customers = CustomerRepository.getCustomers(false);
        customersTable.setModel(CustomerService.customersToTableModel(customers));
    }

    private void addWinListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DashView dashView = new DashView();
                dashView.setVisible(true);
                dashView.pack();

            }
        });
    }
}
