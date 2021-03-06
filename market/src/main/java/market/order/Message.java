package market.order;

import market.Stock;

import java.util.ArrayList;

public class Message implements Chain{

    private Chain next;

    @Override
    public void setNext(Chain next) {
        this.next = next;
    }

    // once the fix message has been made it is returned all along the chain back to the 'Order' class
    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        return createMsg(order, marketID);
    }

    // prepares all relevant info that is used to construct the market fix message
    private String createMsg(ArrayList<String> order, int marketID) {
        String[] info = new String[6];

        info[0] = order.get(1);
        info[1] = order.get(2);
        info[2] = order.get(3);
        info[3] = order.get(4);
        info[4] = order.get(0);
        info[5] = "Accepted";

        MarketFix fix = new MarketFix(info);
        return fix.constructFix(marketID);
    }
}
