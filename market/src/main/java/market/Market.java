package market;

import market.order.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Market {

    public static void main(String[] args) {

        int ID;
        Stock stock = new Stock();

        if (stock.getProducts().size() > 0) {
            for (Product item : stock.getProducts()) System.out.println(item.getName() + " " + item.getQuantity() + " " + item.getCost());
        } else System.out.println("Unable to initialize stock listings, please reboot Market.");

        try (
            // establishes connection to router
            Socket mSock = new Socket("localhost", 5001);
            PrintWriter out = new PrintWriter(mSock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(mSock.getInputStream()));
        ) {
            out.println("Market Connecting");
            String fromServer = in.readLine();
            ID = Integer.parseInt(fromServer);
            System.out.println("ID received: " + ID);

            // awaits messages from router
            while (true) {
                fromServer = in.readLine();

                if (fromServer != null) {
                    System.out.println("Received from router: " + fromServer);

                    // constructs order into readable format
                    Order order = new Order();
                    order.constructOrder(fromServer, ID);
                    String outcome =  order.doOrder(stock);

                    System.out.println("Sending to router: " + outcome);

                    // returns outcome of the order to router
                    out.println(outcome);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e);
        } catch (IOException e) {
            System.out.println("Router is not available for communication.");
            System.out.println("IOException: " + e);
        }
    }
}
