package Core.Application.Services;

import Core.Domain.Atm;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class AtmService {

    public static TableModel atmsToTableModel(List<Atm> atms) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ATM ID");
        model.addColumn("Balance");

        for (Atm atm : atms) {
            model.addRow(new Object[]{
                    atm.getAtmId(),
                    atm.getBalance()
            });
        }

        return model;
    }
}
