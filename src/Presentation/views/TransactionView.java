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
    private JLabel balanceLabel;
    private final TransactionType transactionType;

    public TransactionView(TransactionType transactionType) {
        this.transactionType = transactionType;
        setTitle("Transaction - " + setViewTitle(this.transactionType));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(transactionPanel);
        setLocationRelativeTo(null);
        pack();

        balanceLabel.setText("$" + UserSession.getInstance().getBalance());

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

                String validationError = transactionValidations(selectedAtm, amount, transactionType, userSession);
                if (!validationError.isEmpty()) {
                    JOptionPane.showMessageDialog(this, validationError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AtmRepository.updateBalance(selectedAtm.getAtmId(), amount, transactionType);

                if (transactionType == TransactionType.A) {
                    JOptionPane.showMessageDialog(this, "Founds Added to ATM successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    AccountRepository.updateMyBalance(amount, userSession.getUserId(), userSession.getAccountId(), TransactionType.D);
                    TransactionRepository.createTransaction(selectedAtm.getAtmId(), amount, userSession.getAccountId(), transactionType);
                    UserSession.updateBalance(amount, transactionType);

                    JOptionPane.showMessageDialog(this, "Transaction successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }

                dispose();
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

    private void loadAtms() throws SQLException {
        List<Atm> atms = AtmRepository.getAtms();
        atmTable.setModel(AtmService.atmsToTableModel(atms));
    }

    private String setViewTitle(TransactionType transactionType){
        return switch (transactionType) {
            case D -> "Deposit";
            case W -> "Withdrawal";
            case T -> "Transfer";
            case A -> "Add funds to ATM";
        };
    }

    private String transactionValidations(Atm selectedAtm,int amount,TransactionType transactionType,UserSession userSession){
        String Message = "";
        if (amount <= 0) {
            return "Please enter a valid amount greater than 0.";
        }
        if (transactionType == TransactionType.W && amount > userSession.getBalance()) {
            return "You dont have enough money to withdrawal.";
        }
        if (selectedAtm == null) {
            return "Please select an ATM. No ATM Selected";
        }
        if (transactionType != TransactionType.A && (transactionType == TransactionType.W && selectedAtm.getBalance() < amount)) {
            return "Insufficient ATM balance for this transaction. Insufficient Balance";
        }
        return Message;
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
