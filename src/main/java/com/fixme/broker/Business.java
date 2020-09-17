package com.fixme.broker;

import com.fixme.fix.Fix;

import java.util.Scanner;

public class Business {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static int ID;
    private String fix;

    private void sendOrder(String order) {
        Fix fix = new Fix();

        String msg = fix.constructFix(ID, order);

        // sends constructed msg to router
        // prints results returned
    }

    public void takeOrders() {
        boolean terminate = false;
        Validation validate = new Validation();
        Scanner scan = new Scanner(System.in);

        while (!terminate) {

            printTakeOrder();
            String line = scan.nextLine();

            if (line.equals("Q")) {
                terminate = true;
            } else {
                while (!(validate.validateInput(line))) {
                    printTakeOrder();
                    line = scan.nextLine();
                }
                sendOrder(line);
            }
        }
    }

    public void doInitialization() {
        // connect to router - stores ID that is given

    }

    public boolean getInitialization() {
        Scanner scan = new Scanner(System.in);

        System.out.println(ANSI_GREEN + "Enter 'C' to connect to the market and 'Q' to quit:" + ANSI_RESET);
        String line = scan.nextLine();

        while (!line.equals("C") && !line.equals("Q")) {
            System.out.println(ANSI_RED + "Enter only 'C' or 'Q' as input!" + ANSI_RESET);
            line = scan.nextLine();
        }

        return line.equals("C");
    }

    private void printTakeOrder() {
        System.out.println(ANSI_CYAN + "You can provide a buy or sell order in the following format:" + ANSI_RESET);
        System.out.println("Buy - Object - Quantity - Funds Available");
        System.out.println("OR");
        System.out.println("Sell - Object - Quantity - Selling Price Per Object");
        System.out.println(ANSI_CYAN + "Enter 'Q' to quit session." + ANSI_RESET);
    }
}
