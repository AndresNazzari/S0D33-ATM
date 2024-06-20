package Presentation.Views;

import Core.Application.Services.AtmService;
import Core.Application.Services.CustomerService;
import Core.Application.Services.TransactionServices;
import Core.Application.Services.UserSession;
import Core.Domain.Atm;
import Core.Domain.Customer;
import Core.Domain.Transaction;
import Infrastructure.Repositories.AtmRepository;
import Infrastructure.Repositories.CustomerRepository;
import Infrastructure.Repositories.TransactionRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton addTransactionBtn;
    private JButton reloadBtn;
    private UserSession userSession = UserSession.getInstance();

    public DashView() throws SQLException {

        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(dashPanel);
        setLocationRelativeTo(null); // Centrar la ventana
        pack();

        initComponents();
        addExitBtnListener();
        addReloadBtnListener();

        loadTransactions();
        loadAtms();
        if (userSession.isAdmin()) {
            loadCustomers();
        }

    }

    private void initComponents() {

        lblUserName.setText("Welcome, " + userSession.getFullName());

        // Configuración de visibilidad de los componentes según si el usuario es admin o no
        boolean isAdmin = userSession.isAdmin();
        addCustomerButton.setVisible(isAdmin);
        addFoundsToAtmBtn.setVisible(isAdmin);
        addAtmBtn.setVisible(isAdmin);
        customersTable.setVisible(isAdmin);

        pack();
        setLocationRelativeTo(null);
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
        reloadBtn.addActionListener(e -> {
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
        logOutBtn.addActionListener(e -> {
            dispose();
            UserSession.getInstance().destroy();
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
