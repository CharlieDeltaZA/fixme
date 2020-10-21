package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarketHandler implements Runnable {

    private final ServerSocket marketSocket;

    MarketHandler(ServerSocket marketSocket) {
        this.marketSocket = marketSocket;
    }

    // Spawns a thread pool of 1, and waits for a market to connect.
    // When a market connects, it passes the socket off to a new class
    // that handles running the socket in the thread pool.
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(1); // 3
        System.out.println("Market listening on port " + marketSocket.getLocalPort());
        System.out.println("Waiting for a market to connect...");

        try {
            while (true) {
                pool.execute(new Market(marketSocket.accept()));
            }
        } catch (IOException e) {
            System.out.println("[MarketHandler] IOException: " + e);
        }
    }

    private static class Market implements Runnable {

        private final Socket socket;
        private PrintWriter marketOut;
        private BufferedReader marketIn;
        private Generator genny = new Generator();

        Market(Socket sock) {
            this.socket = sock;
        }

        // Runnable that handles receiving messages from the Market component, and sending messages
        // to the relevant broker via the Router.
        @Override
        public void run() {
            String inputLine;
            int ID = -1;
            System.out.println("Market Connected!");

            try {
                marketOut = new PrintWriter(socket.getOutputStream(), true);
                marketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    inputLine = marketIn.readLine();
    
                    if (inputLine.equals("Market Connecting")) {
                        ID = genny.genMarketID();
                        Instance market = new Instance(ID, marketOut, marketIn);
                        Router.addNewMarket(market);
                        marketOut.println(ID);
    
                        System.out.println("Added New Market: " + ID);
                    } else {
                        // Pass FIX order to broker via router
                        Router.messageBroker(inputLine);
                    }
                }
            } catch (IOException | NullPointerException e) {
                System.out.println("Market closed unexpectedly! Please restart it.");
            } finally {
                try {
                    socket.close();
                    System.out.println("Market Closed!");
                } catch (IOException | NullPointerException e) {
                    System.out.println("Error closing market connection.");
                }
                // remove ID from routing table
                Router.removeMarket(ID);
            }
        }
    }
}
