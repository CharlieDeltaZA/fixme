package market.order;

import market.Stock;

import java.util.ArrayList;

public class Order {

    private final ArrayList<String> query = new ArrayList<>();
    private int marketID;

    // splits the fix message into readable components that are put into a list
    public void constructOrder(String msg, int ID) {
        marketID = ID;
        String[] arr = msg.split("\\|");

        String brokerID = arr[0].split("=")[1];
        String query = arr[2].split("=")[1];
        String product = arr[3].split("=")[1];
        String quantity = arr[4].split("=")[1];
        String money = arr[5].split("=")[1];

        this.query.add(brokerID);
        this.query.add(query);
        this.query.add(product);
        this.query.add(quantity);
        this.query.add(money);
    }

    // constructs the chain of events and assigns each link in the chain the next process
    // the string 'res' is fulfilled if any link in the chain returns 'rejected' or when success has been
    // reached in which case a fix message is returned

    // AVAILABLE CLASS:
    // check if product to sell or buy is in the stocks
    // CHECKS CLASS:
    // sell: check if cost of selling 1 product is higher than 1 product selling price in stocks.txt
    // buy: check if there is enough stock to buy the requested quantity
    // buy: check if there is enough funds available to buy requested quantity = (quantity * stock price per product)
    // UPDATE CLASS:
    // update stocks arraylist && save stocks to file && database
    // MESSAGE CLASS:
    // return fix message
    public String doOrder(Stock products) {
        Chain available = new Available();
        Chain checks = new Checks();
        Chain update = new Update();
        Chain message = new Message();

        available.setNext(checks);
        checks.setNext(update);
        update.setNext(message);

        String res =  available.process(products, this.query, marketID);

        if (res.equalsIgnoreCase("Rejected")) {
            products.saveToDatabase(query, "rejected");
            return createMsg();
        }
        return res;
    }

    // creates and returns a fix message if the outcome of the checks was 'rejected'
    private String createMsg() {
        String[] info = new String[6];

        info[0] = this.query.get(1);
        info[1] = this.query.get(2);
        info[2] = this.query.get(3);
        info[3] = this.query.get(4);
        info[4] = this.query.get(0);
        info[5] = "Rejected";

        MarketFix fix = new MarketFix(info);
        return fix.constructFix(this.marketID);
    }
}
