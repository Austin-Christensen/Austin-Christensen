package com.techelevator.view;

import com.techelevator.CustomerInfo.Customer;
import com.techelevator.Machine.Chip;
import com.techelevator.Machine.Item;
import com.techelevator.Machine.VendingMachine;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VendingMachineTest {

    @Test
    public void test_calculateChange_so_machine_has_zero_money_left_from_customer(){
        //Arrange
        VendingMachine vendingMachine = new VendingMachine();

        //Act
        double result = vendingMachine.calculateChange(new Customer (5.00, 6.30));
        boolean isTrue = false;
        if(result == 0.0){
            isTrue = true;
        }
        //Assert

        Assert.assertEquals(isTrue, true);
    }

    @Test
    public void test_calculateChange_so_machine_returns_correct_change(){
        //Arrange
        VendingMachine vendingMachine = new VendingMachine();

        //Act
        vendingMachine.calculateChange(new Customer (5.00, 6.30));
        int resultQuarter = vendingMachine.getQuarterCount();
        int resultDime = vendingMachine.getDimeCounter();
        int resultNickel = vendingMachine.getNickelCounter();


        //Assert
        Assert.assertEquals(25, resultQuarter);
        Assert.assertEquals(0, resultDime);
        Assert.assertEquals(1, resultNickel);

    }

    @Test
    public void test_readFile_is_correct_and_map_is_being_created(){
        //Arrange

        VendingMachine vendingMachine = new VendingMachine();


        //Act
        vendingMachine.readFile(new Customer(5.00, 6.30));

        Map<String, Item> actualMap = new HashMap<>();
        actualMap = vendingMachine.getItemLocation();


        //Assert
        Assert.assertEquals("Potato Crisps", actualMap.get("A1").getName());
        Assert.assertEquals(3.05, actualMap.get("A1").getPrice(), 0.0);
        Assert.assertEquals(5, actualMap.get("A1").getQuantity());
    }

    @Test
    public void test_item_display_shows_quantity_of_items_at_start(){

        //Arrange

        Item item = new Item();

        //Act

        int maxQuantityOfProduct = item.getQuantity();

        //Assert
        Assert.assertEquals(5, maxQuantityOfProduct);


    }

   /* @Test
    public void test_item_selection_accurately_creating_child_classes(){
        //Arrange
        VendingMachine vendingMachine = new VendingMachine();

        //Act
        Map<String, Item> actualMap = new HashMap<>();
        actualMap = vendingMachine.getItemLocation();
        String expected = actualMap;

        //Assert
    }*/
}


