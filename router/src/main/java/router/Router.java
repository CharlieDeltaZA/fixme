package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router {

    private static final int MARKET_PORT = 5001;
    private static final int BROKER_PORT = 5000;
    private static SomeName market = null;
    private static ArrayList<SomeName> brokers = new ArrayList<SomeName>();
    private static FixValidation fix = new FixValidation();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            ServerSocket marketSocket = new ServerSocket(MARKET_PORT);
            ServerSocket brokerSocket = new ServerSocket(BROKER_PORT);

            pool.execute(new MarketHandler(marketSocket));
            pool.execute(new BrokerHandler(brokerSocket));

            while (true) {
                // System.out.println("Henlo");
            }


        } catch (IOException | NullPointerException e) {
            //TODO: Handle
            System.out.println("Whoops, we encountered a problem!");
            e.printStackTrace();
        }
    }

    public static void addNewMarket(SomeName newMarket) {
        System.out.println("addNewMarket!");
        market = newMarket;
        System.out.println("My MarketID is: " + market.getID());
    }

    public static void messageBroker(String msg) {

        int brokerID = getBrokerID(msg);

        if (brokerID == -1) System.exit(-1); // Remove/refine me

        for (SomeName broker : brokers) {
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
        System.out.println("messageMarket! - "+msg);
        market.getOut().println(msg);
    }

	public static void addNewBroker(SomeName newBroker) {
        System.out.println("addNewBroker!");
        brokers.add(newBroker);
        System.out.println("There are " + brokers.size() + " brokers");
	}

	public static void removeBroker(int ID) {
        System.out.println("removeBroker!");
        ArrayList<SomeName> newBrokers = new ArrayList<SomeName>();
        for (SomeName broker : brokers) {
            if (broker.getID() != ID) {
                newBrokers.add(broker);
            }
        }
        brokers = newBrokers;
    }
    
    public static void removeMarket(int ID) {
        System.out.println("removeMarket!");
        // Enhance? This works for 1 market only
        market = null;
    }
}





// final ArrayList<Integer> markets = new ArrayList<>();
//         PrintWriter marketOut;
//         BufferedReader marketIn;

//         // open market server
//         try {
//             ServerSocket serverSocket = new ServerSocket(5001);
//             System.out.println("Waiting for a market to connect...");

//             Socket marketSocket = serverSocket.accept();

//             System.out.println("Market connected!");

//             marketOut = new PrintWriter(marketSocket.getOutputStream(), true);
//             marketIn = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));

//             String inputLine;

//             // will break when the executor class is done
//             while (true) {
//                 inputLine = marketIn.readLine();

//                 if (inputLine.equals("Market Connecting")) {
//                     Generator genny = new Generator();
//                     int ID = genny.genMarketID();
//                     markets.add(ID);
//                     marketOut.println(ID);

//                     System.out.println("Added New Market: " + ID);

//                     // construct executor class
//                     Executor brokerExec = new Executor(marketIn, marketOut);
//                     brokerExec.openServer();
//                     break;
//                 }
//             }
//         } catch (IOException e) {
//             System.out.println("IOException: " + e);
//         }