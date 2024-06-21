package Presentation.views;

import core.application.services.LoginServices;
import core.application.services.PasswordService;
import core.application.services.UserSession;
import core.domain.User;
import infrastructure.repositories.UserRepository;

import javax.swing.*;

public class LoginForm extends JFrame {
    private JPanel loginForm;
    private JTextField txtDni;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginForm);
        setLocationRelativeTo(null);
        pack();

        btnListener();
    }

    private void btnListener(){
        btnLogin.addActionListener(_ -> {
            String dniString = txtDni.getText();
            String password = txtPassword.getText();

            String ErrorMessage = LoginServices.loginValidations(dniString,password);
            if (!ErrorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, ErrorMessage);
                return;
            }

            try {
                int dni = Integer.parseInt(dniString);
                User user = UserRepository.getUserByDni(dni);

                if (user != null && PasswordService.checkPassword(password, user.password)) {
                    UserSession userSession = UserSession.getInstance();
                    userSession.setDni(dni);
                    userSession.setUserId(user.userId);
                    userSession.setFirstName(user.firstName);
                    userSession.setLastName(user.lastName);
                    userSession.setAccountId(user.accountId);
                    userSession.setBalance(user.balance);
                    userSession.setAdmin(true);
                    dispose();

                    DashView dashView = new DashView();
                    dashView.setVisible(true);
                    dashView.pack();
                } else {
                    JOptionPane.showMessageDialog(null, "User does not exist or password is incorrect.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
            }
        });
    }


}
