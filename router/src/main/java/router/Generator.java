package router;

import java.util.ArrayList;

public class Generator {

    private ArrayList markets;
    private ArrayList brokers;

    public Generator(ArrayList markets, ArrayList brokers) {
        this.brokers = brokers;
        this.markets = markets;
    }

    public int genMarketID() {
        // assign it between 100,000,000 and 999,999,999
        int id = 1;
        while (id != 1 || !markets.contains(id)) {
            id = (int) (Math.random() * (100000001 - 999999999 + 1) + 999999999);
        }
        return id;
    }

    public int genBrokerID() {
        // assign it between 100,000,000 and 999,999,999
        int id = 1;
        while (id != 1 || !brokers.contains(id)) {
            id = (int) (Math.random() * (100000001 - 999999999 + 1) + 999999999);
        }
        return id;
    }
}
