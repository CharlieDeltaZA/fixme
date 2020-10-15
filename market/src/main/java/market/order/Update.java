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

    @Override
    public String process(Stock products, ArrayList<String> order, int marketID) {
        if (save(products, order)) return next.process(products, order, marketID);
        else return "Rejected";
    }

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

                stock = updateList(stock, selected);       ///////////////// CHECK IF NECESSARY
                return products.saveProducts(stock);
            }
            case "sell" -> {
                int qnty = selected.getQuantity();
                selected.setQuantity(qnty + quantityReq);

                stock = updateList(stock, selected);        ///////////////// CHECK IF NECESSARY
                return products.saveProducts(stock);
            }
        }
        return false;
    }

    private ArrayList<Product> updateList(ArrayList<Product> stock, Product item) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getName().equalsIgnoreCase(item.getName())) {
                stock.set(i, item);
                return stock;
            }
        }
        return stock;
    }

    private Product selectProduct(ArrayList<Product> stock, String item) {
        for (Product prod: stock) {
            if (prod.getName().equalsIgnoreCase(item)) return prod;
        }
        return stock.get(0);
    }
}
