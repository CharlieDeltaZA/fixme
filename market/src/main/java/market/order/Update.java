package market.order;

import market.Product;
import market.Stock;

import java.util.ArrayList;

public class Update implements  Chain{

    private Chain next;

    @Override
    public void setNext(Chain next) {
        this.next = next;
    }

    // does the updating of stocks and if successful moves on to the next process
    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        if (save(products, order)) {
            System.out.println("Updated stock.");
            return next.process(products, order, marketID);
        }
        else return "Rejected";
    }

    // constructs the new product listing object and replaces the existing one
    // saves and updates the product listing file and database by calling the 'Stock' class methods
    private boolean save(Stock products, ArrayList<String> order) {
        String command = order.get(1).toLowerCase();
        String item = order.get(2);
        int quantityReq = Integer.parseInt(order.get(3));
        ArrayList<Product> stock = products.getProducts();

        Product selected = selectProduct(stock, item);

        switch (command) {
            case "buy" -> {
                int qnty = selected.getQuantity();
                selected.setQuantity(qnty - quantityReq);

                stock = updateList(stock, selected);
                return products.saveProducts(stock, order);
            }
            case "sell" -> {
                int qnty = selected.getQuantity();
                selected.setQuantity(qnty + quantityReq);

                stock = updateList(stock, selected);
                return products.saveProducts(stock, order);
            }
        }
        return false;
    }

    // replaces the changed product object (after the transaction) with the new product object
    private ArrayList<Product> updateList(ArrayList<Product> stock, Product item) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getName().equalsIgnoreCase(item.getName())) {
                stock.set(i, item);
                return stock;
            }
        }
        return stock;
    }

    // retrieves specific product object from the list
    private Product selectProduct(ArrayList<Product> stock, String item) {
        for (Product prod: stock) {
            if (prod.getName().equalsIgnoreCase(item)) return prod;
        }
        return stock.get(0);
    }
}
