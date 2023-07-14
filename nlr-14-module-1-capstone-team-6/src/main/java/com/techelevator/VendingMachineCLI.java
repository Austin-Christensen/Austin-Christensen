package com.techelevator;

import com.techelevator.CustomerInfo.Customer;
import com.techelevator.Machine.Item;
import com.techelevator.Machine.Purchase;
import com.techelevator.Machine.VendingMachine;
import com.techelevator.view.Menu;

import java.io.InputStream;

public class VendingMachineCLI {
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_SECRET_OPTION = "*Sales Report";

	/*private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";*/

	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_SECRET_OPTION };
	/*private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};*/




	private Menu menu;
	private static Purchase purchase = new Purchase();

	private static VendingMachine vendingMachine = new VendingMachine();


	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		vendingMachine.readFile(new Customer(20, 0));
		Customer customer = new Customer(20, 0);
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);


			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				vendingMachine.itemDisplay(customer);
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				purchase.onSelection(customer, vendingMachine);
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				break;
			} else if (choice.equals(MAIN_MENU_SECRET_OPTION)) {
				vendingMachine.salesReport();
				break;
			}

		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
