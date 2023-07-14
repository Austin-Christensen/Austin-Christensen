package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@PreAuthorize("isAuthenticated")
public class AccountBalanceController {


    private AccountBalanceDao accountBalanceDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AccountBalanceController(AccountBalanceDao account, UserDao userDao, TransferDao transferDao) {
        this.accountBalanceDao = account;
        this.userDao = userDao;
        this.transferDao = transferDao;

    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int accountId){
        return accountBalanceDao.getAccountById(accountId);

    }


    //TODO look at the authorization here and double check its correct
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id){
        BigDecimal balance = accountBalanceDao.getBalanceById(id);
        if(balance == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return balance;
        }

    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/{id}/sender/{transferId}", method = RequestMethod.PUT)
    public Account updateSenderBalanceById(@Valid @RequestBody Account account, @PathVariable int id, @PathVariable int transferId){

        Transfer transfer = transferDao.getTransferById(transferId);
        try{

            Account updatedAccount = accountBalanceDao.updateSenderAccountById(account, transfer.getAmount());
            return  updatedAccount;
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/{id}/receiver/{transferId}", method = RequestMethod.PUT)
    public Account updateReceiverBalanceById(@Valid @RequestBody Account account, @PathVariable int id, @PathVariable int transferId){

        Transfer transfer = transferDao.getTransferById(transferId);
        try{

            Account updatedAccount = accountBalanceDao.updateReceiverAccountById(account, transfer.getAmount());
            return  updatedAccount;
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
        //TODO could get error if transfer is null
    }








}
