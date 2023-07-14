package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Account {

    private int user_id;
    private int account_id;
    @Positive
    private BigDecimal balance;

    public Account(){ }

    public Account(int user_id, int account_id, BigDecimal balance) {
        this.user_id = user_id;
        this.account_id = account_id;
        this.balance = balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setAccount_id(int account_id){
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "user_id=" + user_id +
                ", account_id=" + account_id +
                ", balance=" + balance +
                '}';
    }
}
