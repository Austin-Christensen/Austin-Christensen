package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountBalanceDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@PreAuthorize("isAuthenticated")
public class TransferController {


    private TransferDao transferDao;
    private UserDao userDao;
    private AccountBalanceDao accountBalanceDao;


    public TransferController(TransferDao transferDao, UserDao userDao, AccountBalanceDao accountBalanceDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountBalanceDao = accountBalanceDao;

    }

    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer startTransfer(@Valid @RequestBody Transfer transfer) {

        return transferDao.startTransfer(transfer);

    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "accounts/userToAcctId/{userId}", method = RequestMethod.GET)
    public int userToAcctId(@PathVariable int userId) {

        return accountBalanceDao.userToAcctId(userId);

    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/account/{acctId}", method = RequestMethod.GET)
    public List<Transfer> getMyTransferHistory(@PathVariable int acctId) {

        List<Transfer> myTransferHistory = transferDao.getMyTransferHistory(acctId);
        if (myTransferHistory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer history not found.");
        } else {
            return myTransferHistory;
        }
    }


    @PreAuthorize("permitAll")
    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfer;
        }
    }

    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/requests", method = RequestMethod.POST)
    public Transfer requestTransfer(@Valid @RequestBody Transfer transfer) {

        return transferDao.requestTransfer(transfer);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/requests/accept/{id}", method = RequestMethod.PUT)
    public Transfer acceptRequestById(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        return transferDao.acceptRequestById(id);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/requests/deny/{id}", method = RequestMethod.PUT)
    public Transfer denyRequestById(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        return transferDao.denyRequestById(id);
    }


}
