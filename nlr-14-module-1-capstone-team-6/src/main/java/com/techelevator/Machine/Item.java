package com.techelevator.Machine;

import com.techelevator.CustomerInfo.Customer;
import com.techelevator.view.Menu;

import javax.print.DocFlavor;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Item {

    private static final String SLOT_IDENTIFIER = "A1";
    private static final String PRODUCT_NAME = "Potato Crisp";
    private static final String PRODUCT_PRICE = "3.05";
    private static final String[] ITEM_MENU_OPTIONS = {SLOT_IDENTIFIER, PRODUCT_NAME, PRODUCT_PRICE};

    private String name;
    private double price;
    private int quantity = 5;
    private String sound;

    public Item(String name, double price, int quantity, String sound) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sound = sound;

    }

    public Item() {

    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.name = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSound(){
        return sound;
    }






}
