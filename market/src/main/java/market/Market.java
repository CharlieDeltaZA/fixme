package market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Market {

    public static void main(String[] args) {

        int ID;
        Initialize init = new Initialize();
        ArrayList<Product> products = init.initializeStock();

        if (products != null) {
            for (Product item : products) {
                System.out.println(item.getName() + " " + item.getQuantity() + " " + item.getCost());
            }
        } else {
            System.out.println("Unable to initialize stock listings, please reboot Market.");
        }

        // establishes connection to router
        try (
            Socket mSock = new Socket("localhost", 5001);
            PrintWriter out = new PrintWriter(mSock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(mSock.getInputStream()));
        ) {
            out.println("Market Connecting");
            String fromServer = in.readLine();
            ID = Integer.parseInt(fromServer);

            // awaits messages from router
            while (true) {
                fromServer = in.readLine();
                System.out.println("Received from router: " + fromServer);

                // does order of buy/sell
                //make function to parse input

                // returns outcome of order to router
                out.println();
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e);
        } catch (IOException e) {
            System.out.println("Router is not available for communication.");
            System.out.println("IOException: " + e);
        }
    }
}
