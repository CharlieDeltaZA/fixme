package broker;

// import com.fixme.Fix;

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

    public Business() {
        // establishes connection to router - stores ID that is given
        try {
            bSock = new Socket("localhost", 5000);
            out = new PrintWriter(bSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bSock.getInputStream()));

            out.println("Broker Connecting");
            String fromServer = in.readLine();
            this.ID = Integer.parseInt(fromServer);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e);
        } catch (IOException e) {
            System.out.println("Router is not available for communication.");
            System.out.println("IOException: " + e);
        }
    }

    private void sendOrder(String[] order) {
        Fix fix = new Fix(order);
        String msg = fix.constructFix(ID);

        if (msg != null) {
            System.out.println(msg); // debugging

            try {
                out.println(msg);

                // FIX msg returned from market
                String fromServer = in.readLine();

                // prints results returned
                if (fromServer != null) System.out.println("Result: "+fromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("FIX message could not be created! Please try again.");
        }
    }

    public void takeOrders() {
        Validation validate = new Validation();
        Scanner scan = new Scanner(System.in);

        while (true) {
            printTakeOrder();
            String line = scan.nextLine();

            if (line.equals("Q")) break;
            else {
                while (!(validate.validateInput(line))) {
                    printTakeOrder();
                    line = scan.nextLine();
                }
                sendOrder(line.split("-"));
            }
        }
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
        System.out.println("You can provide a buy or sell order in the following format: (Enter 'Q' to quit session)");
        System.out.println(ANSI_CYAN + "Buy - Object - Quantity - Funds Available" + ANSI_RESET);
        System.out.println("OR");
        System.out.println(ANSI_CYAN + "Sell - Object - Quantity - Selling Price Per Object" + ANSI_RESET);
    }
}
