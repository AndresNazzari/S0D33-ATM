package core.application.services;


import core.domain.Transaction;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class TransactionServices {

    public static TableModel transactionsToTableModel(List<Transaction> transactions) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Transaction ID");
        model.addColumn("Amount");
        model.addColumn("Type");
        model.addColumn("ATM ID");
        model.addColumn("Created At");

        for (Transaction transaction : transactions) {
            model.addRow(new Object[]{
                    transaction.getTransactionId(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getAtmId(),
                    transaction.getCreatedAt()
            });
        }

        return model;
    }
}
