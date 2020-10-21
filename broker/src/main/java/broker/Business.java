package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Business {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private int ID;
    Socket bSock;
    PrintWriter out;
    BufferedReader in;

    // establishes connection to router
    // stores ID that is given
    public Business() {
        try {
            bSock = new Socket("localhost", 5000);
            out = new PrintWriter(bSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bSock.getInputStream()));

            String fromServer = in.readLine();
            this.ID = Integer.parseInt(fromServer);
        } catch (UnknownHostException e) {
            System.out.println(ANSI_RED + "Unknown host: " + e + ANSI_RESET);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Router is not available for communication." + ANSI_RESET);
            System.out.println("IOException: " + e);
            System.exit(1);
        }
    }

    // constructs a valid fix message from the order parameters
    // sends the message through to the router
    // prints results returned from the router
    private void sendOrder(String[] order) {
        Fix fix = new Fix(order);
        String msg = fix.constructFix(ID);

        if (msg != null) {
            System.out.println("\nOrder: " + msg);

            try {
                out.println(msg);

                String fromServer = in.readLine();

                if (fromServer != null) {
                    System.out.println("Result: " + fromServer);
                    if (fromServer.contains("Rejected")) System.out.println(ANSI_RED + "Rejected\n" + ANSI_RESET);
                    else if (fromServer.contains("Accepted")) System.out.println(ANSI_GREEN + "Accepted\n" + ANSI_RESET);
                }
                else System.out.println(ANSI_RED + "The result from the market was unidentifiable. Please try again later!\n" + ANSI_RESET);
            } catch (IOException e) {
                System.out.println(ANSI_RED + "\nUnable to communicate with the server. Please try again!\n" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "FIX message could not be created! Please try again." + ANSI_RESET);
        }
    }

    // reads in orders from the terminal input and validates them
    // once a valid order is entered it then calls 'sendOrder()'
    public void takeOrders() {
        Validation validate = new Validation();
        Scanner scan = new Scanner(System.in);

        while (true) {
            printTakeOrder();
            String line = scan.nextLine().trim();

            if (line.equalsIgnoreCase("Q")) {
                try {
                    bSock.close();
                } catch (IOException e) {
                    System.out.println(ANSI_RED + "Broker " + this.ID + "threw IOException." + ANSI_RESET);
                    e.printStackTrace();
                }
                break;
            } else {
                while (!(validate.validateInput(line))) {
                    printTakeOrder();
                    line = scan.nextLine().trim();
                }
                sendOrder(line.split("-"));
            }
        }
    }

    // provides a user interface where the user can connect to the router or quit
    public boolean getInitialization() {
        Scanner scan = new Scanner(System.in);

        System.out.println(ANSI_GREEN + "Enter 'C' to connect to the market and 'Q' to quit:" + ANSI_RESET);
        String line = scan.nextLine().trim();

        while (!line.equalsIgnoreCase("C") && !line.equalsIgnoreCase("Q")) {
            System.out.println(ANSI_RED + "Enter only 'C' or 'Q' as input!" + ANSI_RESET);
            line = scan.nextLine();
        }

        return line.equalsIgnoreCase("C");
    }

    // provides some instructions on how an order should be formatted
    // printed out after every order outcome and after every invalid order
    private void printTakeOrder() {
        System.out.println("You can provide a buy or sell order in the following format: (Enter 'Q' to quit session)");
        System.out.println(ANSI_CYAN + "Buy - Object - Quantity - Funds Available" + ANSI_RESET + " OR " + ANSI_CYAN + "Sell - Object - Quantity - Selling Price Per Object" + ANSI_RESET);
    }
}
