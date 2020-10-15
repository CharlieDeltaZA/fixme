package market.order;

import market.Product;
import market.Stock;

import java.util.ArrayList;

public class Checks implements  Chain {

    private Chain next;

    @Override
    public void setNext(Chain next) {
        this.next = next;
    }

    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        if (extensiveChecks(products, order)) {
            System.out.println("Passed 'extensive' checks.");
            return next.process(products, order, marketID);
        }
        else return "Rejected";
    }

    private boolean extensiveChecks(Stock products, ArrayList<String> order) {
        String command = order.get(1).toLowerCase();
        String item = order.get(2);
        ArrayList<Product> stock = products.getProducts();

        Product selected = selectProduct(stock, item);

        // CHECKS:
        // BUY: check if there is enough stock to buy the requested quantity
        // BUY: check if there is enough funds available to buy requested quantity = (quantity * stock price per product)
        // SELL: check if cost of selling 1 product is higher than 1 product selling price in stocks.txt

        switch (command) {
            case "buy" -> {
                int amountReq = Integer.parseInt(order.get(3));

                if (selected.getQuantity() < amountReq) return false;
                else {
                    int fundsAvailable = Integer.parseInt(order.get(4));
                    int costPer = selected.getCost();
                    int totalCost = costPer * amountReq;

                    if (fundsAvailable >= totalCost) return true;
                    else return false;
                }
            }
            case "sell" -> {
                int costPer = Integer.parseInt(order.get(4));

                if (selected.getCost() < costPer) return false;
                else return true;
            }
        }
        return false;
    }

    private Product selectProduct(ArrayList<Product> stock, String item) {
        for (Product prod: stock) {
            if (prod.getName().equalsIgnoreCase(item)) return prod;
        }
        return stock.get(0);
    }
}
