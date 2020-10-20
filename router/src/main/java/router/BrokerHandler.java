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

    private static class Broker implements Runnable {

        private final Socket socket;
        private PrintWriter brokerOut;
        private BufferedReader brokerIn;
        private Generator genny = new Generator();
        private FixValidation fix = new FixValidation();

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
