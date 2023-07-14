package com.techelevator.Machine;

import com.techelevator.CustomerInfo.Customer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;

public class VendingMachine {

    private Map<String, Item> itemLocation = new TreeMap<>();
    private double machineMoney;
    private final double NICKEL = 5;
    private final double DIME = 10;
    private final double QUARTER = 25;

    private int quarterCount = 0;
    private int dimeCounter = 0;
    private int nickelCounter = 0;

    public int getQuarterCount() {
        return quarterCount;
    }

    public int getDimeCounter() {
        return dimeCounter;
    }

    public int getNickelCounter() {
        return nickelCounter;
    }

    public Map getItemLocation(){
        return  itemLocation;
    }

    LocalDateTime date = LocalDateTime.now();
    String dateFormatted = (DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(date));
    String dateReformatted = dateFormatted.replaceAll("-", "/");


    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    String timeFormatted = timeFormat.format(new Date()).toString();
    DecimalFormat numberFormat = new DecimalFormat("0.00");

    File transactionLog = new File("Log.txt");

    public String getDateFormatted(){
        return dateReformatted;
    }

    public String getTimeFormatted(){
        return timeFormatted;
    }





    public double calculateChange(Customer customer){
        machineMoney = (int)(customer.getFeedAmount() * 100);
        while ( machineMoney  >= QUARTER){
           machineMoney  = machineMoney - QUARTER;
            quarterCount++;
        }
        while(machineMoney >= DIME){
            machineMoney = machineMoney - DIME;
            dimeCounter++;
        }
        while(machineMoney >= NICKEL){
            machineMoney = machineMoney - NICKEL;
            nickelCounter++;
        }
        if(quarterCount + dimeCounter + nickelCounter == 0){
            System.out.println("You have no change");
        }
        else {
            System.out.println("Your change is: " + quarterCount + " quarter(s), " + dimeCounter + " dime(s), " + nickelCounter + " nickel(s).");
        }
        String message = dateReformatted + " " + timeFormatted + " GIVE CHANGE: $" + numberFormat.format(customer.getFeedAmount()) + " $" + numberFormat.format(machineMoney);
        try(PrintWriter transactionOutput = new PrintWriter(new FileOutputStream(transactionLog, true)) ) {
            transactionOutput.println(message);
        }
        catch (Exception ex){
            System.out.println("Cannot open file for writing");
        }

        customer.setBalanceAmount(customer.getBalanceAmount() + customer.getFeedAmount());
        customer.setFeedAmount(machineMoney);
        return customer.getFeedAmount();

    }


    public void readFile(Customer customer) {
        File itemList = new File("vendingmachine.csv");
        try (Scanner scanner = new Scanner(itemList);) {

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\\|");

                double amount = Double.parseDouble(line[2]);
                Item item = null;
                int quantity = 5;
                if (line[3].equalsIgnoreCase("Chip")) {
                    item = new Chip(line[1], amount, quantity);
                } else if (line[3].equalsIgnoreCase("Candy")) {
                    item = new Candy(line[1], amount, quantity);
                } else if (line[3].equalsIgnoreCase("Drink")) {
                    item = new Drink(line[1], amount, quantity);
                } else if (line[3].equalsIgnoreCase("Gum")) {
                    item = new Gum(line[1], amount, quantity);

                }
                itemLocation.put(line[0], item);
            }

        } catch (Exception ex) {
            System.err.println("Something went wrong");
        }

    }

    public void itemDisplay(Customer customer) {
        for(Map.Entry<String, Item> entry : itemLocation.entrySet()){
            String key = entry.getKey();
            Item value = entry.getValue();
            if(value.getQuantity() > 0) {
                System.out.println(key + "|$" +value.getPrice() + "|" +value.getName() + "|Quantity: " + value.getQuantity());
            } else{
                System.out.println(value.getName() + "| SOLD OUT");
            }
        }
    }

    public void itemSelection(Customer customer) {

        for(Map.Entry<String, Item> entry : itemLocation.entrySet()){
            String key = entry.getKey();
            Item value = entry.getValue();
            System.out.println(key + "|" + value.getName());
        }

        System.out.println("Please select an item");
        Scanner itemChoice = new Scanner(System.in);
        String choice = itemChoice.nextLine().toUpperCase();

        if(!itemLocation.containsKey(choice)){
            System.out.println("Invalid selection");
            Purchase purchase = new Purchase();
            purchase.onSelection(customer, this);
        }

        if(itemLocation.get(choice).getQuantity() == 0){
            System.out.println("Sorry! Sold out!");
        } else if(customer.getFeedAmount() < itemLocation.get(choice).getPrice()){
            System.out.println("Sorry! Not enough funds.");
        }else {
            System.out.println("You have selected " + itemLocation.get(choice).getName());
            System.out.println(itemLocation.get(choice).getSound());
            }

            System.out.println("Money before purchase: $" + numberFormat.format(customer.getFeedAmount()));
            double currentBalance = customer.getFeedAmount();
            double newBalance = currentBalance - itemLocation.get(choice).getPrice();
            System.out.println("Cost of item purchased: $"+ numberFormat.format(itemLocation.get(choice).getPrice()));
            customer.setFeedAmount(newBalance);
            System.out.println("Money left to spend: $" + numberFormat.format(customer.getFeedAmount()));


            int updatedQuantity = itemLocation.get(choice).getQuantity() - 1;
            itemLocation.get(choice).setQuantity(updatedQuantity);




            String printName = itemLocation.get(choice).getName() +" " + choice;
            String printAmountSpent = (numberFormat.format(itemLocation.get(choice).getPrice()));
            String printRemainingBalance = (numberFormat.format(customer.getFeedAmount()));



            try(PrintWriter transactionOutput = new PrintWriter(new FileOutputStream(transactionLog, true)) ) {
                transactionOutput.println(dateReformatted + " " + timeFormatted + " " + printName + " $" +  printAmountSpent + " $" + printRemainingBalance);
            }
            catch (Exception ex){
                System.err.println("Cannot open file for writing");
            }


        }

       // printDate + " " + printName




    public void salesReport() {
        String salesDate = dateReformatted.replaceAll("/", "");
        String salesTime = timeFormatted.replaceAll(":", "");
        String fileName = salesDate + "_" + salesTime + "_Sales.txt";
        File salesOutput = new File(fileName);
        double totalSales = 0;



        try (PrintWriter salesPrint = new PrintWriter(salesOutput)) {
            for(Map.Entry<String, Item> entry : itemLocation.entrySet()){

                Item value = entry.getValue();
                if(value.getQuantity() < 5){


                    int numberSold = 5 - value.getQuantity();

                    salesPrint.println(value.getName() + "|" + numberSold);
                    totalSales = totalSales + (value.getPrice() * numberSold);
                }

            }

            salesPrint.println("");
            salesPrint.println("**TOTAL SALES** $" + numberFormat.format(totalSales));

        } catch (Exception ex) {
            System.err.println("Something went wrong");

        }
    }




    /*public void transactionLog(){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        File transactionLog = new File("Log.txt");

        try(PrintWriter transactionOutput = new PrintWriter(new FileOutputStream(transactionLog, true)) ) {
            transactionOutput.print(date + " " + time + " " + itemLocation.get(choice));
        }
        catch (Exception exception){
            System.out.println("Cannot open file for writing");
        }



    }*/


}
