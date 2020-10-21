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

    private final ServerSocket brokerSocket;

    BrokerHandler(ServerSocket brokerSocket) {
        this.brokerSocket = brokerSocket;
    }

    // Spawns a thread pool of 8, and waits for a broker to connect.
    // When a broker connects, it passes the socket off to a new class
    // that handles running the socket in the thread pool.
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        System.out.println("Broker listening on port " + brokerSocket.getLocalPort());
        System.out.println("Waiting for a broker to connect...");

        try {
            while (true) {
                pool.execute(new Broker(brokerSocket.accept()));
            }
        } catch (IOException e) {
            System.out.println("[BrokerHandler] IOException: " + e);
        }
    }

    private static class Broker implements Runnable {

        private final Socket socket;
        private PrintWriter brokerOut;
        private BufferedReader brokerIn;
        private Generator genny = new Generator();
        private FixValidation fix = new FixValidation();

        Broker(Socket sock) {
            this.socket = sock;
        }

        // Runnable that handles receiving messages from the Broker component, and sending messages
        // to the market via the Router.
        @Override
        public void run() {
            int ID = 0;
            System.out.println("Broker Connected!");

            try {
                ID = genny.genBrokerID();
                
                brokerOut = new PrintWriter(socket.getOutputStream(), true);
                brokerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Router.addNewBroker(new router.Instance(ID, brokerOut, brokerIn));
                
                brokerOut.println(ID);
                System.out.println("Added New Broker: " + ID);
    
                while (true) {
                    String orderMsg = brokerIn.readLine();
    
                    if (orderMsg == null) break;
    
                    if (fix.validateFix(orderMsg)) Router.messageMarket(orderMsg);
                    else brokerOut.println("Formatting Error - Order Message!");
                }

            } catch (SocketException e) {
                System.out.println("Error: " + socket);
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Error completing transaction process. Please restart server!");
                System.exit(1);
            } finally {
                try {
                    socket.close();
                    System.out.println("Broker Closed!");
                } catch (IOException | NullPointerException e) {
                    System.out.println("Error closing broker connection.");
                }
                // remove ID from routing table
                Router.removeBroker(ID);
            }
        }

    }
    
}
