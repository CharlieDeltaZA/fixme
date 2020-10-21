package router;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router {

    private static final int MARKET_PORT = 5001;
    private static final int BROKER_PORT = 5000;

    // Currently, only 1 market is accepted. To deal with multiple markets, each time a broker connects, it would have to be prompted
    // with a market to use, if any are already connected and available. Else it would be forced to sit and wait for a market to connect
    // before any transactions would be able to be made.
    
    private static Instance market = null;
    // private static ArrayList<Instance> markets = new ArrayList<Instance>();
    private static ArrayList<Instance> brokers = new ArrayList<Instance>();
    private static final FixValidation fix = new FixValidation();

    // Creates server sockets for markets and brokers, then spawns 2 threads, for each of the
    // Market and Broker Handlers, passing the relevant serverSocket to the relevant handler.
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            ServerSocket marketSocket = new ServerSocket(MARKET_PORT);
            ServerSocket brokerSocket = new ServerSocket(BROKER_PORT);

            pool.execute(new MarketHandler(marketSocket));
            pool.execute(new BrokerHandler(brokerSocket));

        } catch (IOException | NullPointerException e) {
            System.out.println("Error encountered while running thread pool.");
        }
    }
    
    // A new market is added. For multi-market, a new market instance would be added to the list
    // of available connected markets.
    public static void addNewMarket(Instance newMarket) {
        // Enhance? This works for 1 market only
        market = newMarket;
    }

    // A new broker is added to the list of available connected brokers.
    public static void addNewBroker(Instance newBroker) {
        brokers.add(newBroker);
        System.out.println("There are " + brokers.size() + " brokers");
    }

    // Message from broker is routed to market.
    // This currently assumes only 1 market. For multi-market, the marketID would need to be extracted from the
    // message and the appropriate market assigned to a local variable and messaged.
    public static void messageMarket(String msg) {
        market.getOut().println(msg);
    }

    // Message from the market is routed to the appropriate broker, via brokerID obtained from FIX message.
    // FIX message validated before appropriate message is returned to broker.
    public static void messageBroker(String msg) {
        int brokerID = getBrokerID(msg);

        if (brokerID == -1) System.out.println("Unable to obtain broker ID from FIX.");
        else {
            for (Instance broker : brokers) {
                if (brokerID == broker.getID()) {
                    int result = fix.validateMarketFix(msg);

                    if (result == 1) broker.getOut().println(msg);
                    else if (result == 0) broker.getOut().println("Formatting Error - Market Message!");
                    else if (result == -1) broker.getOut().println("The Market might be down, try restarting it!");
                }
            }
        }
    }

    // Obtains broker ID from the market FIX message
    private static int getBrokerID(String msg) {
        int ID = -1;
        String[] split = msg.split("\\|");
        
        for (String temp : split) {
            if (temp.contains("22=")) {
                String[] arr = temp.split("=");
                ID = Integer.parseInt(arr[1]);
            }
        }
        return (ID);
    }

    // Builds a temp array of brokers that DO NOT match the ID of the broker to be removed.
    // Over writes existing array with temp array.
	public static void removeBroker(int ID) {
        ArrayList<Instance> newBrokers = new ArrayList<Instance>();
        for (Instance broker : brokers) {
            if (broker.getID() != ID) {
                newBrokers.add(broker);
            }
        }
        brokers = newBrokers;
    }
    
    // Single market - Simply nulls the market variable.
    // Multi-market - Would build up a new temp array of markets and then overwrite the existing one.
    public static void removeMarket(int ID) {
        // Enhance? This works for 1 market only
        market = null;
    }
}
