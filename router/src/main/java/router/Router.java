package router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Router {

    public static void main(String[] args) {
        final ArrayList<Integer> markets = new ArrayList<>();
        PrintWriter marketOut;
        BufferedReader marketIn;

        // open market server
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            Socket marketSocket = serverSocket.accept();

            marketOut = new PrintWriter(marketSocket.getOutputStream(), true);
            marketIn = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));

            String inputLine;

            // will break when the executor class is done
            while (true) {
                inputLine = marketIn.readLine();

                if (inputLine.equals("Market Connecting")) {
                    Generator genny = new Generator();
                    int ID = genny.genMarketID();
                    markets.add(ID);
                    marketOut.println(ID);

                    System.out.println("Added New Market: " + ID);

                    // construct executor class
                    Executor brokerExec = new Executor(marketIn, marketOut);
                    brokerExec.openServer();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
