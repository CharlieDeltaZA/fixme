package market.order;

import market.Stock;

import java.util.ArrayList;

public interface Chain {

    public abstract void setNext(Chain next);
    public abstract String process(Stock products, ArrayList<String> order, int marketID);
}
