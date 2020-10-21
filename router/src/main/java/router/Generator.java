package router;

public class Generator {

    // Generate random ID between 100,000 and 499,999 for markets.
    public int genMarketID() {
        return (int) (Math.random() * (100001 - 499999 + 1) + 499999);
    }

    // Generate random ID between 500,000 and 999,999 for brokers.
    public int genBrokerID() {
        return (int) (Math.random() * (500001 - 999999 + 1) + 999999);
    }
}
