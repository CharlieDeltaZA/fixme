package com.fixme.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Router {

    public static void main(String[] args) {
        final ArrayList markets = new ArrayList();
        final ArrayList brokers = new ArrayList();
        PrintWriter out;
        BufferedReader in;

        // open market server
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            Socket marketSocket = serverSocket.accept();

            out = new PrintWriter(marketSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("Market Connecting")) {
                    Generator genny = new Generator(markets, brokers);
                    int ID = genny.genMarketID();
                    markets.add(ID);
                    out.println(ID);
                    System.out.println("Added New Market: " + ID);

                    // construct executor class
                    Executor brokerExec = new Executor(brokers, markets, in, out);
                    brokerExec.openServer();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
