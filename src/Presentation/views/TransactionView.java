package Presentation.views;

import core.application.services.AtmService;
import core.application.services.UserSession;
import core.domain.Atm;
import core.domain.TransactionType;
import infrastructure.repositories.AccountRepository;
import infrastructure.repositories.AtmRepository;
import infrastructure.repositories.TransactionRepository;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class TransactionView extends JFrame{
    private JPanel transactionPanel;
    private JButton makeTransactionBtn;
    private JTable atmTable;
    private JTextField amountTxt;
    private final TransactionType transactionType;

    public TransactionView(TransactionType transactionType) {
        this.transactionType = transactionType;

        setTitle("Transaction - " + transactionType);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(transactionPanel);
        setLocationRelativeTo(null);
        pack();

        addWinListener();
        addMakeTransactionBtnListener();
        try {
            loadAtms();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMakeTransactionBtnListener() {
        makeTransactionBtn.addActionListener(_ -> {
            UserSession userSession = UserSession.getInstance();
            try {
                int amount = Integer.parseInt(amountTxt.getText());

                int selectedRow = atmTable.getSelectedRow();
                TableModel model = atmTable.getModel();
                int atmId = (Integer) model.getValueAt(selectedRow, 0);
                int atmBalance = (Integer) model.getValueAt(selectedRow, 1);

                Atm selectedAtm = new Atm();
                selectedAtm.setAtmId(atmId);
                selectedAtm.setBalance(atmBalance);

                String validationError = transactionValidations(selectedAtm, amount, transactionType, userSession.getBalance());
                if (!validationError.isEmpty()) {
                    JOptionPane.showMessageDialog(this, validationError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AtmRepository.updateBalance(selectedAtm.getAtmId(), amount, transactionType);
                AccountRepository.updateBalance(selectedAtm.getAtmId(), amount, userSession.getUserId(), userSession.getAccountId(), transactionType);
                TransactionRepository.createTransaction(selectedAtm.getAtmId(), amount, userSession.getAccountId(), transactionType);

                UserSession.updateBalance(amount, transactionType);
                JOptionPane.showMessageDialog(this, "Transaction successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Refresh ATM data

                DashView dashView = new DashView();
                dashView.setVisible(true);
                dashView.pack();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private String transactionValidations(Atm selectedAtm,int amount,TransactionType transactionType,int userBalance){
        String Message = "";
        if (amount <= 0) {
            return "Please enter a valid amount greater than 0.";
        }
        if (transactionType == TransactionType.W && amount > userBalance) {
            return "You dont have enough money to withdrawal.";
        }
        if (selectedAtm == null) {
            return "Please select an ATM. No ATM Selected";
        }
        if (selectedAtm.getBalance() < amount && transactionType == TransactionType.W) {
            return "Insufficient ATM balance for this transaction. Insufficient Balance";
        }
        return Message;
    }

    private void loadAtms() throws SQLException {
        List<Atm> atms = AtmRepository.getAtms();
        atmTable.setModel(AtmService.atmsToTableModel(atms));
    }

    private void addWinListener(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DashView dashView = new DashView();
                    dashView.setVisible(true);
                    dashView.pack();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
}
