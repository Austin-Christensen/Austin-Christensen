package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private AuthenticatedUser currentUser;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/transfers/";

    private String authToken = null;

    private int newTransferId;

    public int getNewTransferId() {
        return newTransferId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService() {
    }

    public TransferService(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }

    public int userToAcctId (int userId){
        int acctId = 3;
        String url = baseUrl + "accounts/userToAcctId/" + userId;

        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), int.class);
            acctId = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return acctId;
    }

    public Transfer[] getMyTransferHistory() {
        Transfer[] transferList = null;
        int getAcctId = this.userToAcctId(currentUser.getUser().getId());
        String url = baseUrl + "account/" + getAcctId;

        try {

            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer[].class);

            transferList = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferList;
    }

    public Transfer getTransferById(int id){
        Transfer transfer = null;

        String url = baseUrl + "/" + id;

        try{
            ResponseEntity <Transfer> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Transfer startTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(baseUrl, entity, Transfer.class);
            newTransferId = returnedTransfer.getTransferId();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } catch (NullPointerException ex){
            System.out.println("returned null");
        }

        return returnedTransfer;
    }





    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
