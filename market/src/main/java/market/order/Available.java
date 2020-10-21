package market.order;

import market.Product;
import market.Stock;

import java.util.ArrayList;

public class Available implements Chain {

    private Chain next;

    @Override
    public void setNext(Chain next) {
        this.next = next;
    }

    // does the check and if successful moves on to the next check
    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        if (productAvailable(products, order)) {
            System.out.println("Passed 'available' check.");
            return next.process(products, order, marketID);
        }
        else return "Rejected";
    }

    // checks if the requested product is available in the stock listing
    private boolean productAvailable(Stock products, ArrayList<String> order) {
        ArrayList<Product> stock = products.getProducts();
        String orderProd = order.get(2);
        String command = order.get(1).toLowerCase();
        boolean found = false;

        for (Product product : stock) {
            if (command.equals("buy")) {
                if (product.getName().equalsIgnoreCase(orderProd) && product.getQuantity() > 0) {
                    found = true;
                    break;
                }
            } else {
                if (product.getName().equalsIgnoreCase(orderProd)) {
                    found = true;
                    break;
                }
            }

        }
        return found;
    }
}
