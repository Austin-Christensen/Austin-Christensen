package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import javax.xml.transform.TransformerFactory;
import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {
    TransferService transferService = new TransferService();

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println(balance);
    }

    public void printUsers(User[] users) {
        System.out.println("--------------------------------------------");
        System.out.println("Users");
        System.out.println("--------------------------------------------");
        for (User user : users) {
            System.out.println(user.getId() + ": " + user.getUsername());
        }
    }

    public void printMyTransferHistory(Transfer[] transfers, AuthenticatedUser currentUser) {
        //TODO make the output format pretty
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("--------------------------------------------");
        for (Transfer transfer : transfers) {
            if (transfer.getAccountFrom() == transferService.userToAcctId(currentUser.getUser().getId())) {
                System.out.println("Sent");
                System.out.println("     " + transfer.toString());
            } else {
                System.out.println("Received");
                System.out.println("     " +transfer.toString());
            }
//
        }
    }

    public void printTransferById(Transfer transfer, AuthenticatedUser currentUser) {
        if (transfer.getAccountTo() == transferService.userToAcctId(currentUser.getUser().getId()) || transfer.getAccountFrom() == transferService.userToAcctId(currentUser.getUser().getId())) {

            System.out.println("--------------------------------------------");
            System.out.println("Transfer");
            System.out.println("--------------------------------------------");

            if (transfer.getAccountFrom() == transferService.userToAcctId(currentUser.getUser().getId())) {
                System.out.println("Sent");
                System.out.println("     " + transfer.toString());
            } else {
                System.out.println("Received");
                System.out.println("     " + transfer.toString());
            }

        } else {
            System.out.println("That transfer is not associated with this account.");
        }

    }
}

