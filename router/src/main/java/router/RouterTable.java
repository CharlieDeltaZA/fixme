package router;

import java.util.ArrayList;

public class RouterTable {

    private final ArrayList<Instance> brokers = new ArrayList<>();
    private final ArrayList<Instance> markets = new ArrayList<>();

    public Instance getBrokerById(int brokerID) {
        for (Instance brok: brokers) {
            if (brok.getID() == brokerID) return brok;
        }
        return null;
    }

    public Instance getMarketById(int marketID) {
        for (Instance mark: markets) {
            if (mark.getID() == marketID) return mark;
        }
        return null;
    }

    public void addBroker(Instance broker) {
        brokers.add(broker);
    }

    public void addMarket(Instance market) {
        markets.add(market);
    }

    public void removeBroker(Instance broker) {
        brokers.remove(broker);
    }

    public void removeMarket(Instance market) {
        markets.remove(market);
    }
}
