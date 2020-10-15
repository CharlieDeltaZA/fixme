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

    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        if (productAvailable(products, order)) {
            System.out.println("Passed 'available' check.");
            return next.process(products, order, marketID);
        }
        else return "Rejected";
    }

    // Checks if the requested product is available in stocks
    private boolean productAvailable(Stock products, ArrayList<String> order) {
        ArrayList<Product> stock = products.getProducts();
        String orderProd = order.get(2);
        boolean found = false;

        for (Product product : stock) {
            if (product.getName().equalsIgnoreCase(orderProd) && product.getQuantity() > 0) {
                found = true;
                break;
            }
        }

        return found;
    }
}
