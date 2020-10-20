package router;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router {

    private static final int MARKET_PORT = 5001;
    private static final int BROKER_PORT = 5000;
    private static Instance market = null;
    private static ArrayList<Instance> brokers = new ArrayList<Instance>();
    private static FixValidation fix = new FixValidation();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            ServerSocket marketSocket = new ServerSocket(MARKET_PORT);
            ServerSocket brokerSocket = new ServerSocket(BROKER_PORT);

            pool.execute(new MarketHandler(marketSocket));
            pool.execute(new BrokerHandler(brokerSocket));

            while (true) {
                // Just leave me empty :)
            }


        } catch (IOException | NullPointerException e) {
            //TODO: Handle Me
            System.out.println("Whoops, we encountered a problem!");
            e.printStackTrace();
        }
    }

    public static void addNewMarket(Instance newMarket) {
        // Enhance? This works for 1 market only
        market = newMarket;
    }

    public static void messageBroker(String msg) {

        int brokerID = getBrokerID(msg);

        if (brokerID == -1) System.exit(-1); // Remove/refine me

        for (Instance broker : brokers) {
            if (brokerID == broker.getID()) {
                int result = fix.validateMarketFix(msg);

                if (result == 1) broker.getOut().println(msg);
                else if (result == 0) broker.getOut().println("Formatting Error - Market Message!");
                else if (result == -1) broker.getOut().println("The Market might be down, try restarting it!");
            }
        }
    }

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

    public static void messageMarket(String msg) {
        market.getOut().println(msg);
    }

	public static void addNewBroker(Instance newBroker) {
        brokers.add(newBroker);
        System.out.println("There are " + brokers.size() + " brokers");
	}

	public static void removeBroker(int ID) {
        ArrayList<Instance> newBrokers = new ArrayList<Instance>();
        for (Instance broker : brokers) {
            if (broker.getID() != ID) {
                newBrokers.add(broker);
            }
        }
        brokers = newBrokers;
    }
    
    public static void removeMarket(int ID) {
        // Enhance? This works for 1 market only
        market = null;
    }
}
