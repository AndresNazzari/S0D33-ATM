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
    private final UserSession userSession = UserSession.getInstance();

    public DashView() throws SQLException {

        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(dashPanel);
        setLocationRelativeTo(null);
        pack();

        initComponents();
        addListeners();

        loadTransactions();
        loadAtms();
        if (userSession.isAdmin()) {
            loadCustomers();
        }

    }

    private void initComponents() {
        lblUserName.setText("Welcome, " + userSession.getFullName());

        boolean isAdmin = userSession.isAdmin();
        addCustomerButton.setVisible(isAdmin);
        // depositBtn.setVisible(!isAdmin);
        // withdrawalBtn.setVisible(!isAdmin);
        addFoundsToAtmBtn.setVisible(isAdmin);
        addAtmBtn.setVisible(isAdmin);
        customersTable.setVisible(isAdmin);

        pack();
        setLocationRelativeTo(null);
    }

    private void addListeners(){
        addExitBtnListener();
        addReloadBtnListener();
        addDepositBtnListener();
        addWithdrawalBtnListener();
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
    private void loadTransactions() throws SQLException {
        List<Transaction> transactions = TransactionRepository.getTransactions(userSession);
        transactionsTable.setModel(TransactionServices.transactionsToTableModel(transactions));
    }

    private void loadAtms() throws SQLException {
        List<Atm> atms = AtmRepository.getAtms();
        atmTable.setModel(AtmService.atmsToTableModel(atms));
    }

    private void loadCustomers() throws SQLException {
        List<Customer> customers = CustomerRepository.getCustomers();
        customersTable.setModel(CustomerService.customersToTableModel(customers));
    }

    private void addReloadBtnListener() {
        reloadBtn.addActionListener(_ -> {
            try {
                loadTransactions();
                loadAtms();
                if (userSession.isAdmin()) {
                    loadCustomers();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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

}
