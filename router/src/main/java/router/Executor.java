package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    private final ArrayList<Integer> brokers = new ArrayList<>();
    private final PrintWriter marketOut;
    private final BufferedReader marketIn;
    private ExecutorService pool;
    private final FixValidation fix;
    private final Generator genny;
    
    public Executor(BufferedReader marketIn, PrintWriter marketOut) {
        this.marketIn = marketIn;
        this.marketOut = marketOut;
        this.fix = new FixValidation();
        this.genny = new Generator();
    }
    
    private class BrokerHandler implements Runnable {
        private final Socket broker;
        
        public BrokerHandler(Socket socket) {
            this.broker = socket;
        }

        @Override
        public void run() {
            int ID = 0;
            System.out.println("Broker Connected!");

            try {
                ID = genny.genBrokerID();
                brokers.add(ID);
    
                PrintWriter brokerOut = new PrintWriter(broker.getOutputStream(), true);
                BufferedReader brokerIn = new BufferedReader(new InputStreamReader(broker.getInputStream()));
    
                brokerOut.println(ID);
                System.out.println("Added New Broker: " + ID);
    
                while (true) {
                    String orderMsg = brokerIn.readLine();
    
                    if (orderMsg == null) break;
    
                    if (fix.validateFix(orderMsg)) {
                        marketOut.println(orderMsg);
                        String marketRet = marketIn.readLine();

                        int result = fix.validateMarketFix(marketRet);

                        if (result == 1) brokerOut.println(marketRet);
                        else if (result == 0) brokerOut.println("Formatting Error - Market Message!");
                        else if (result == -1) brokerOut.println("The Market might be down, try restarting it!");
                    }
                    else brokerOut.println("Formatting Error - Order Message!");
                }

            } catch (SocketException e) {
                System.out.println("Error: " + broker);
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            }
            finally {
                try {
                    broker.close();
                    System.out.println("Broker Closed!");
                } catch (IOException | NullPointerException e) {
                    System.out.println("Error closing broker connection.");
                }
                // remove ID from arraylist
                brokers.remove(ID);
            }
        }

    }

    // open server for brokers
    // new broker added to executor thread -> given an ID (hand shake happens)
    //                                     -> buy/sell msg sent to market
    //                                     -> sends outcome msg to broker
    public void openServer() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Broker listening on port " + serverSocket.getLocalPort());
            pool = Executors.newFixedThreadPool(8);

            while (true) pool.execute(new BrokerHandler(serverSocket.accept()));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
