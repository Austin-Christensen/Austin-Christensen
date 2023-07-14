package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

   // public List<String> getUsers();



    public List <Transfer> getMyTransferHistory(int id);


    public Transfer getTransferById(int id);


    public Transfer startTransfer(Transfer transfer);


    public Transfer requestTransfer(Transfer transfer);

    public Transfer acceptRequestById(int transferId);

    public Transfer denyRequestById(int transferId);

}
