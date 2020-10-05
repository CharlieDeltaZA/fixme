package com.fixme.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Router {

    public static void main(String[] args) {

        // open market server
        // construct executor class
        try (
            ServerSocket serverSocket = new ServerSocket(5001);
            Socket marketSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(marketSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
        ) {
            String inputLine;
            Executor brokerExec = new Executor();

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("Market Connecting")) {
                    out.println("Give the market an ID");
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
