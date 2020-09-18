package com.fixme.market;

import java.util.ArrayList;

public class Market {

    public static void main(String[] args) {

        Initialize init = new Initialize();
        ArrayList<Product> products = init.initializeStock();

        if (products != null) {

            for (Product item: products) {
                System.out.println(item.getName() + " " + item.getQuantity() + " " + item.getCost());
            }

            // connects to router
            // awaits messages from router
            // does order of buy/sell
            // returns outcome of order to router
        } else {
            System.out.println("Unable to initialize stock listings, please reboot Market.");
        }
    }
}
