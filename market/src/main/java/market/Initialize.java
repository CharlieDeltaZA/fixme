package market;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Initialize {

    private final ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> initializeStock() {

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
        return products;
    }

    private void populateProduct(String[] product) {

        try {
            Product item = new Product();
            item.setName(product[0]);
            item.setQuantity(Integer.parseInt(product[1]));
            item.setCost(Integer.parseInt(product[2]));
            this.products.add(item);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Initialize.java -> populateProduct(): " + e);
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Initialize.java -> populateProduct(): " + e);
        }
    }
}
