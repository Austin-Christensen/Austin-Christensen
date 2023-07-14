package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    private AccountBalanceService accountBalanceSerivce = new AccountBalanceService();
    private UserService userservice = new UserService();
    TransferService transferService;



    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        //can fetch user token after this runs
        transferService = new TransferService(currentUser);
        transferService.setAuthToken(currentUser.getToken());
        accountBalanceSerivce.setAuthToken(currentUser.getToken());
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        BigDecimal balance = new BigDecimal("0.00");
        int id = currentUser.getUser().getId();
        balance = accountBalanceSerivce.getBalanceById(id);
        consoleService.printBalance(balance);
//        return balance;
    }


    private void viewTransferHistory() {

        String response = "";
        response = consoleService.promptForString("(1) Full transfer list\n" + "(2) Transfer by Id\n");

            if (response.equalsIgnoreCase("1")) {
                Transfer[] transfers = transferService.getMyTransferHistory();

                if (transfers.length > 0) {
                    consoleService.printMyTransferHistory(transfers, currentUser);
                } else {
                    System.out.println("No transfers in history.");
                }
            } else if (response.equalsIgnoreCase("2")) {


                try {
                    int transferId = Integer.parseInt(consoleService.promptForString("Enter transferID\n"));
                    consoleService.printTransferById(transferService.getTransferById(transferId), currentUser);
                } catch (Exception ex) {
                    System.out.println("We couldn't find your transfer.");
                }

            } else {
                System.out.println("Please enter a valid option.");
            }
        }




    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
//        System.out.println("Please select a user to send to.");
        User[] userArray = userservice.getUsers();
        String accountIdToSendTo = "";

        if (userArray != null) {
            consoleService.printUsers(userArray);
            System.out.println(" ");
            accountIdToSendTo= consoleService.promptForString("Enter user id: ");
        } else {
            consoleService.printErrorMessage();
        }

        try {
            if (Integer.parseInt(accountIdToSendTo) != currentUser.getUser().getId()) {

                BigDecimal amountToSend = new BigDecimal(consoleService.promptForString("How much do you want to send? \n"));

                Account accountToSendTo = accountBalanceSerivce.getAccountByAccountId(transferService.userToAcctId(Integer.parseInt(accountIdToSendTo)));
                Account accountSentFrom = accountBalanceSerivce.getAccountByAccountId(transferService.userToAcctId(currentUser.getUser().getId()));

                BigDecimal accountBal = new BigDecimal(String.valueOf(accountSentFrom.getBalance()));

                BigDecimal afterSendBal = accountBal.subtract(amountToSend);

                if(amountToSend.compareTo(new BigDecimal("0.00")) > 0){
                    if(afterSendBal.compareTo(new BigDecimal("0.00")) > 0){
                        try{
                            Transfer transfer = new Transfer(2, 2, accountSentFrom.getAccount_id(), accountToSendTo.getAccount_id(), amountToSend);
                            transferService.startTransfer(transfer);
                            transfer.setTransferId(transferService.getNewTransferId());

                            accountBalanceSerivce.updateSenderBalance(accountSentFrom, transfer);
                            accountBalanceSerivce.updateReceiverBalance(accountToSendTo, transfer);

                            System.out.println("");
                            System.out.println("Transfer was successful!");
                            System.out.println("$" + amountToSend + " was sent to user " + accountIdToSendTo);
                            System.out.print("Your remaining balance is: ");
                            viewCurrentBalance();
                        } catch (NullPointerException ex){
                            System.out.println("Account does not exist");
                        }

                    }else{
                        System.out.println("Insufficient funds for transfer.");
                    }
                }else{
                    System.out.println("Cannot send $0.00");
                }
            } else {
                System.out.println("cannot send money to yourself");
            }
        } catch (Exception ex) {
            System.out.println("something went wrong");
        }


    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
