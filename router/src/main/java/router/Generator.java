package router;

import java.util.ArrayList;

public class Generator {

    public int genMarketID() {
        // assign it between 100,000 and 999,999
        return (int) (Math.random() * (100001 - 999999 + 1) + 999999);
    }

    public int genBrokerID(ArrayList<Integer> brokers) {
        // assign it between 100,000 and 999,999
        int id = 1;
        while (id == 1 || brokers.contains(id)) {
            id = (int) (Math.random() * (100001 - 999999 + 1) + 999999);
        }
        return id;
    }
}
