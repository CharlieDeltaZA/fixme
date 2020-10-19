package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrokerHandler implements Runnable {

    private ServerSocket brokerSocket;

    BrokerHandler(ServerSocket brokerSocket) {
        this.brokerSocket = brokerSocket;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ExecutorService pool = Executors.newFixedThreadPool(8);
        System.out.println("Broker listening on port " + brokerSocket.getLocalPort());

        try {
            while (true) {
                pool.execute(new Broker(brokerSocket.accept()));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    private class Broker implements Runnable {

        private final Socket socket;
        private PrintWriter brokerOut;
        private BufferedReader brokerIn;
        Generator genny = new Generator();
        FixValidation fix = new FixValidation();

        Broker(Socket sock) {
            this.socket = sock;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int ID = 0;
            System.out.println("Broker Connected!");

            try {
                ID = genny.genBrokerID();
                // brokers.add(ID);
                
                PrintWriter brokerOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader brokerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Router.addNewBroker(new router.SomeName(ID, brokerOut, brokerIn));
                
                brokerOut.println(ID);
                System.out.println("Added New Broker: " + ID);
    
                while (true) {
                    String orderMsg = brokerIn.readLine();
    
                    if (orderMsg == null) break;
    
                    if (fix.validateFix(orderMsg)) {
                        // marketOut.println(orderMsg);
                        Router.messageMarket(orderMsg);

                        // PROBLEM HERE - Code needs to pause until the router run stuff completes. :(

                        // String marketRet = marketIn.readLine(); //???????

                        // Send order to market, via router:
                        // THEN - let market do it's thing and call send2Broker
                        // Validate the marketRet msg in send2Broker, then send it off appropriately


                        // String marketRet = "InvalidFixMsg";

                        // int result = fix.validateMarketFix(marketRet);

                        // if (result == 1) brokerOut.println(marketRet);
                        // else if (result == 0) brokerOut.println("Formatting Error - Market Message!");
                        // else if (result == -1) brokerOut.println("The Market might be down, try restarting it!");
                    }
                    else brokerOut.println("Formatting Error - Order Message!");
                }

            } catch (SocketException e) {
                System.out.println("Error: " + socket);
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            }
            finally {
                try {
                    socket.close();
                    System.out.println("Broker Closed!");
                } catch (IOException | NullPointerException e) {
                    System.out.println("Error closing broker connection.");
                }
                // remove ID from arraylist
                // brokers.remove(ID);
                Router.removeBroker(ID);
            }
        }

    }
    
}
