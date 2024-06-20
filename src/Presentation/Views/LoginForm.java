package Presentation.Views;

import Core.Application.Services.LoginServices;
import Core.Application.Services.PasswordService;
import Core.Application.Services.UserService;
import Core.Application.Services.UserSession;
import Core.Domain.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JPanel loginForm;
    private JTextField txtDni;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginForm);
        setLocationRelativeTo(null); // Centrar la ventana
        pack();

        btnListener();
    }

    private void btnListener(){
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dniString = txtDni.getText();
                String password = txtPassword.getText();

                String ErrorMessage = LoginServices.loginValidations(dniString,password);
                if (!ErrorMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(null, ErrorMessage);
                    return;
                }

                try {
                    int dni = Integer.parseInt(dniString);
                    User user = UserService.getUserByDni(dni);

                    if (user != null && PasswordService.checkPassword(password, user.password)) {
                        UserSession userSession = UserSession.getInstance();
                        userSession.setDni(dni);
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
            }
        });
    }


}
