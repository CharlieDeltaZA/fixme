package market;

import java.io.*;
import java.util.ArrayList;

public class Stock {

    private ArrayList<Product> products = new ArrayList<>();

    public Stock() {
        String line;

        try {
            FileReader fr = new FileReader("stocks.txt");
            BufferedReader bf = new BufferedReader(fr);

            while ((line = bf.readLine()) != null) {

                while (line.equals("")) {
                    line = bf.readLine();
                }
                String[] item = line.split(":");
                populateProduct(item);
            }
            fr.close();
            bf.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find file stocks.txt");
        } catch (IOException e) {
            System.out.println("Unable to read from stocks.txt");
        }
    }

    private void populateProduct(String[] product) {

        try {
            Product item = new Product();
            item.setName(product[0]);
            item.setQuantity(Integer.parseInt(product[1]));
            item.setCost(Integer.parseInt(product[2]));
            this.products.add(item);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Stock.java -> populateProduct(): " + e);
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Stock.java -> populateProduct(): " + e);
        }
    }

    public boolean saveProducts(ArrayList<Product> products) {
        this.products = products;
        String line = "";

        for (Product prod: products) line += prod.getName() + ":" + prod.getQuantity() + ":" + prod.getCost() + "\n";

        try {
            FileWriter fw = new FileWriter("stocks.txt");
            fw.write(line);
            fw.close();
            System.out.println("Saved stock to file.");
            return true;
        } catch (IOException e) {
            System.out.print("Error while saving to stocks.txt -> Stock.java -> saveProducts(): " + e);
        }
        return false;
    }

    private void saveToDatabase(ArrayList<Product> products) {
        
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
