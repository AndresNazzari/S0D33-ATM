package Presentation.views;

import core.domain.User;
import infrastructure.repositories.AccountRepository;
import infrastructure.repositories.UserRepository;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class NewCustomerView extends JFrame {
    private JTextField firstNameTxt;
    private JTextField dniTxt;
    private JPasswordField passwordTxt;
    private JTextField initialFoundsTxt;
    private JTextField lastNameTxt;
    private JButton createUserButton;
    private JCheckBox isAdministratorCheckBox;
    private JPanel customerPanel;
    private JPasswordField password2Txt;
    private JButton cancelBtn;

    public NewCustomerView() {
        setContentPane(customerPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        addListeners();
    }

    private void addListeners(){
        addWinListener();
        addCancelBtnListener();
        addCreateCustomerBtnListener();
    }

    private void addCreateCustomerBtnListener(){
        createUserButton.addActionListener(_ -> {
            try {
                String firstName = firstNameTxt.getText();
                String lastName = lastNameTxt.getText();
                String dni = dniTxt.getText();
                String password = new String(passwordTxt.getPassword());
                String password2 = new String(password2Txt.getPassword());
                int initialFounds = Integer.parseInt(initialFoundsTxt.getText());
                boolean isAdministrator = isAdministratorCheckBox.isSelected();

                String validationError = customerValidations(firstName, lastName, dni, password, password2, initialFounds, isAdministrator);
                if (!validationError.isEmpty()) {
                    JOptionPane.showMessageDialog(this, validationError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User userExists = UserRepository.getUserByDni(dni);
                if (userExists != null) {
                    JOptionPane.showMessageDialog(this, "User with DNI " + dni + " already exists.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newUserId = UserRepository.createUser(firstName, lastName, dni, password, isAdministrator);
                AccountRepository.createAccount(newUserId, initialFounds);

                JOptionPane.showMessageDialog(this, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                DashView dashView = new DashView();
                dashView.setVisible(true);
                dashView.pack();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addCancelBtnListener(){
        cancelBtn.addActionListener(_ -> {
            dispose();

            DashView dashView = new DashView();
            dashView.setVisible(true);
            dashView.pack();
        });
    }

    private String customerValidations(String firstName, String lastName, String dni, String password, String password2, int initialFounds, boolean isAdministrator){
        String errorMessage = "";

        if (!firstName.matches("[a-zA-Z0-9]{4,20}")) {
            errorMessage += "First name must be between 4 and 20 alphanumeric characters.\n";
        }

        if (!lastName.matches("[a-zA-Z0-9]{4,20}")) {
            errorMessage += "Last name must be between 4 and 20 alphanumeric characters.\n";
        }

        if (!dni.matches("\\d{1,10}")) {
            errorMessage += "DNI must be a number up to 10 digits long.\n";
        }

        if (!password.equals(password2)) {
            errorMessage += "Passwords do not match.\n";
        }
        if (!password.matches("[a-zA-Z0-9]{4,10}")) {
            errorMessage += "Password must be between 4 and 10 alphanumeric characters.\n";
        }

        if (initialFounds <= 0) {
            errorMessage += "Initial funds must be greater than 0.\n";
        }

        return errorMessage;
    }

    private void addWinListener(){
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
