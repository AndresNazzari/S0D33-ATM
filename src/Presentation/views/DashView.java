package Presentation.views;

import core.application.services.AtmService;
import core.application.services.CustomerService;
import core.application.services.TransactionServices;
import core.application.services.UserSession;
import core.domain.Atm;
import core.domain.Customer;
import core.domain.Transaction;
import core.domain.TransactionType;
import infrastructure.repositories.AtmRepository;
import infrastructure.repositories.CustomerRepository;
import infrastructure.repositories.TransactionRepository;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class DashView extends JFrame {
    private JPanel dashPanel;
    private JTable transactionsTable;
    private JTable atmTable;
    private JLabel lblUserName;
    private JButton addCustomerButton;
    private JButton addFoundsToAtmBtn;
    private JButton addAtmBtn;
    private JTable customersTable;
    private JButton logOutBtn;
    private JButton reloadBtn;
    private JButton depositBtn;
    private JButton withdrawalBtn;
    private JButton transferBtn;
    private JLabel balanceLabel;
    private final UserSession userSession = UserSession.getInstance();

    public DashView() throws SQLException {

        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(dashPanel);
        setLocationRelativeTo(null);
        pack();

        addListeners();
        initComponents();
        loadData();
    }

    private void initComponents() {
        lblUserName.setText("Welcome, " + userSession.getFullName());
        balanceLabel.setText("$" + userSession.getBalance());

        boolean isAdmin = userSession.isAdmin();

        addCustomerButton.setVisible(isAdmin);
        depositBtn.setVisible(!isAdmin);
        withdrawalBtn.setVisible(!isAdmin);
        transferBtn.setVisible(!isAdmin);
        addAtmBtn.setVisible(isAdmin);


        pack();
        setLocationRelativeTo(null);
    }

    private void addListeners(){
        addExitBtnListener();
        addReloadBtnListener();
        addDepositBtnListener();
        addWithdrawalBtnListener();
        addTransferBtnListener();
        addAddCustomerBtnListener();
        addAddFoundsToAtmListener();
        addAddAtmListener();
    }

    private void loadData(){
        try {
            loadTransactions();
            loadAtms();
            loadCustomers();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadTransactions() throws SQLException {
        List<Transaction> transactions = TransactionRepository.getTransactions(userSession);
        transactionsTable.setModel(TransactionServices.transactionsToTableModel(transactions));
    }

    private void loadAtms() throws SQLException {
        List<Atm> atms = AtmRepository.getAtms();
        addFoundsToAtmBtn.setVisible(!atms.isEmpty() && userSession.isAdmin());
        atmTable.setModel(AtmService.atmsToTableModel(atms));
    }

    private void loadCustomers() throws SQLException {
        List<Customer> customers = CustomerRepository.getCustomers(true);
        customersTable.setModel(CustomerService.customersToTableModel(customers));
    }

    private void addReloadBtnListener() {
        reloadBtn.addActionListener(_ -> {
            loadData();
        });
    }

    private void addAddFoundsToAtmListener() {
        addFoundsToAtmBtn.addActionListener(_ -> {
            dispose();

            TransactionView transactionView = new TransactionView(TransactionType.A);
            transactionView.setVisible(true);
            transactionView.pack();
        });
    }

    private void addAddCustomerBtnListener() {
        addCustomerButton.addActionListener(_ -> {
            dispose();

            NewCustomerView newCustomerView = new NewCustomerView();
            newCustomerView.setVisible(true);
            newCustomerView.pack();
        });
    }

    private void addDepositBtnListener() {
        depositBtn.addActionListener(_ -> {
            dispose();

            TransactionView transactionView = new TransactionView(TransactionType.D);
            transactionView.setVisible(true);
            transactionView.pack();
        });
    }

    private void addWithdrawalBtnListener() {
        withdrawalBtn.addActionListener(_ -> {
            dispose();

            TransactionView transactionView = new TransactionView(TransactionType.W);
            transactionView.setVisible(true);
            transactionView.pack();
        });
    }

    private void addTransferBtnListener() {
        transferBtn.addActionListener(_ -> {
            dispose();

            TransferView transferView = new TransferView();
            transferView.setVisible(true);
            transferView.pack();
        });
    }

    private void addExitBtnListener() {
        logOutBtn.addActionListener(_ -> {
            dispose();

            UserSession.getInstance().destroy();
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }

    private void addAddAtmListener() {
        addAtmBtn.addActionListener(_ -> {
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to create a new ATM?",
                    "Confirm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.OK_OPTION) {
                try {
                    AtmRepository.CreateNewAtm();
                    JOptionPane.showMessageDialog(this, "ATM created successfully with $ 10.000! You can add more founds to ATM.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAtms();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }
}
