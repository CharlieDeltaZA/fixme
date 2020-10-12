package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Executor {

    private final ArrayList<Integer> brokers = new ArrayList();
    private final PrintWriter marketOut;
    private final BufferedReader marketIn;

    public Executor(BufferedReader marketIn, PrintWriter marketOut) {
        this.marketIn = marketIn;
        this.marketOut = marketOut;
    }

    // open server for brokers
    // new broker added to executor thread -> given an ID (hand shake happens)
    //                                     -> buy/sell msg sent to market
    //                                     -> sends outcome msg to broker
    public void openServer() {
        try (
            ServerSocket serverSocket = new ServerSocket(5000);
        ) {
            FixValidation fix = new FixValidation();

            while (true) {
                Socket broker = serverSocket.accept();
                Generator genny = new Generator();
                int ID = genny.genBrokerID(brokers);
                brokers.add(ID);



                //// new thread here: (add above id, brokerSocket, marketIn and marketOut into thread class) //////

                PrintWriter brokerOut = new PrintWriter(broker.getOutputStream(), true);
                BufferedReader brokerIn = new BufferedReader(new InputStreamReader(broker.getInputStream()));

                brokerOut.println(ID);
                System.out.println("Added New Broker: " + ID);

                while (true) {
                    String orderMsg = brokerIn.readLine();

                    if (orderMsg == null) break;  // dunno if this is the correct case

                    if (fix.validateFix(orderMsg)) {
                        marketOut.println(orderMsg);
                        String marketRet = marketIn.readLine();

                        brokerOut.println(marketRet);
                    }
                    else brokerOut.println("Formatting Error!");
                }
                broker.close();  // need to check if this is correct to do
                // stop thread or whatever you using here

                //// end of thread content. //////////////////////



            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
