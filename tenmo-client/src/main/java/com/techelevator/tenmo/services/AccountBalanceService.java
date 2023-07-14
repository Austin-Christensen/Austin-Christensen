package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountBalanceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/accounts/";

    private String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public AccountBalanceService() {
    }

    public Account getAccountByAccountId(int accountId){
        Account account = new Account();
        String url = baseUrl + "/" + accountId;

        try{
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    public BigDecimal getBalanceById(int accountId) {
        BigDecimal balance = new BigDecimal("0.00");
        String url = baseUrl + accountId + "/balance";

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    public boolean updateSenderBalance(Account account, Transfer transfer) {
        HttpEntity<Account> entity = makeAccountEntity(account);
        boolean success = false;
        String url = baseUrl + account.getAccount_id() + "/sender/" + transfer.getTransferId();
        try {
            restTemplate.put(url, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean updateReceiverBalance(Account account, Transfer transfer) {
        HttpEntity<Account> entity = makeAccountEntity(account);
        boolean success = false;
        String url = baseUrl + account.getAccount_id() + "/receiver/" + transfer.getTransferId();
        try {
            restTemplate.put(url, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }





    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }


}
