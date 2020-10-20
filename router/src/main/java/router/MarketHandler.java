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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ExecutorService pool = Executors.newFixedThreadPool(1); // 3
        System.out.println("Market listening on port " + marketSocket.getLocalPort());
        System.out.println("Waiting for a market to connect...");

        try {
            while (true) {
                pool.execute(new Market(marketSocket.accept()));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
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

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String inputLine;
            int ID = -1;
            System.out.println("Market connected!");

            try {
                marketOut = new PrintWriter(socket.getOutputStream(), true);
                marketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    inputLine = marketIn.readLine();
    
                    if (inputLine.equals("Market Connecting")) {
                        ID = genny.genMarketID();
                        Router.addNewMarket(new router.Instance(ID, marketOut, marketIn));
                        marketOut.println(ID);
    
                        System.out.println("Added New Market: " + ID);
                    } else {
                        // Pass FIX order to broker via router
                        Router.messageBroker(inputLine);
                    }
                }
            } catch (IOException | NullPointerException e) {
                System.out.println("Error while implementing market functionality.");
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
