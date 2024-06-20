package core.domain;

import java.util.List;

public class Account {
    public int accountId;
    public int userId;
    public double balance;
    public String createdAt    ;
    public List<Transaction> transactions;
}
