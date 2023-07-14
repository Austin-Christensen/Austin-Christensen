package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal balance;

    
    public int getAccount_id() {
        return accountId;
    }

    public int getUser_id() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setAccount_id(int account_id) {
        this.accountId = account_id;
    }

    public void setUser_id(int user_id) {
        this.userId = user_id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {

        return "Account{" +
                "account_id=" + accountId +
                ", user_id=" + userId +
                ", balance=" + balance +
                '}';
    }
}
