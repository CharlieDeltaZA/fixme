package router;

import java.util.ArrayList;

public class Generator {

    public int genMarketID() {
        // assign it between 100,000 and 499,999
        return (int) (Math.random() * (100001 - 499999 + 1) + 499999);
    }

    public int genBrokerID() {
        // assign it between 500,000 and 999,999
        int id;
        // while (id == 1 || brokers.contains(id)) {
            id = (int) (Math.random() * (500001 - 999999 + 1) + 999999);
        // }
        return id;
    }
}
