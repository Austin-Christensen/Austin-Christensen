package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountBalanceDao {


    public Account getAccountById(int accountId);

    public int userToAcctId(int userId);

    public BigDecimal getBalanceById(int id);

    public Account updateSenderAccountById(Account account, BigDecimal amount);

    public Account updateReceiverAccountById(Account account, BigDecimal amount);









}
