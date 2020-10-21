package market.order;

import market.Stock;

import java.util.ArrayList;

// interface for each link class in the chain

public interface Chain {

    public abstract void setNext(Chain next);
    public abstract String process(Stock products, ArrayList<String> order, int marketID);
}
