package com.techelevator.CustomerInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Customer {
    private double balanceAmount = 20;
    private double feedAmount;

    public Customer(double balanceAmount, double feedAmount){
        this.balanceAmount = balanceAmount;
        this.feedAmount = feedAmount;
    }

    public double getBalanceAmount(){
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount){
        this.balanceAmount = balanceAmount;
    }

    public double getFeedAmount(){
        return feedAmount;
    }

    public void setFeedAmount(double feedAmount){
        this.feedAmount = feedAmount;
    }

    public void feedMoney(double balanceAmount, double feedAmount){


        if(balanceAmount > 0){

            double updatedBalance = balanceAmount - 1;
            setBalanceAmount(updatedBalance);
            DecimalFormat numberFormat  = new DecimalFormat("0.00");
            System.out.println("Money left in Wallet: $" + numberFormat.format(getBalanceAmount()));

            double updatedFeedAmount = feedAmount + 1;
            setFeedAmount(updatedFeedAmount);
            System.out.println("Current money provided: $" + numberFormat.format(getFeedAmount()));

            LocalDateTime date = LocalDateTime.now();
            String dateFormatted = (DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(date));
            String dateReformatted = dateFormatted.replaceAll("-", "/");


            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
            String timeFormatted = timeFormat.format(new Date()).toString();


            File transactionLog = new File("Log.txt");
            try(PrintWriter feedMoneyOutput = new PrintWriter(new FileOutputStream(transactionLog, true))){
                feedMoneyOutput.println(dateReformatted + " " + timeFormatted + " FEED MONEY: $1.00 $" + numberFormat.format(getFeedAmount()));
            }catch(Exception ex){
                System.out.println("Cannot open file for writing");
            }


        }else{
            System.out.println("You are out of money!");
        }
    }
}
