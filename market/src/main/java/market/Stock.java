package market;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Stock {

    private ArrayList<Product> products = new ArrayList<>();
    private MongoClient mongoClient;
    private final boolean save;

    // opens connection to database if 'save' flag is used
    // reads from the stock listing file to create a list of current products
    public Stock(boolean save) {
        String line;
        this.save = save;

        // creating a mongoDB connection pool
        if (save) mongoClient = MongoClients.create();

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

    // takes each line from the stock listing file and constructs it into the 'Product' class
    // added to the products list
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

    // called when a fix message must be returned to the broker after a transaction is successful or rejected
    // saves changes to the stock to the stock listing file
    // saves changes to the stock to the database (if flag is true)
    public boolean saveProducts(ArrayList<Product> products, ArrayList<String> order) {
        this.products = products;
        String line = "";

        for (Product prod: products) line += prod.getName() + ":" + prod.getQuantity() + ":" + prod.getCost() + "\n";

        saveToDatabase(order, "successful");

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

    // takes the current order, broker ID, data & time and constructs it into a document
    // saves the document to the mongoDB database
    public void saveToDatabase(ArrayList<String> order, String outcome) {

        if (save) {
            int brokerID = Integer.parseInt(order.get(0));
            String command = order.get(1).toLowerCase();
            String item = order.get(2);
            int quantityReq = Integer.parseInt(order.get(3));
            int funds = Integer.parseInt(order.get(4));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            try {
                MongoDatabase database = mongoClient.getDatabase("fixme");
                MongoCollection collection = database.getCollection("transactions");

                if (outcome.equalsIgnoreCase("rejected")) {
                    Document document = new Document("broker_id", brokerID).append("operation", command)
                            .append("product", item).append("quantity", quantityReq).append("funds", funds)
                            .append("date", dtf.format(now))
                            .append("result", "rejected");
                    collection.insertOne(document);
                    System.out.println("Saved failed transaction to database");
                } else {
                    Document document = new Document("broker_id", brokerID).append("operation", command)
                            .append("product", item).append("quantity", quantityReq).append("funds", funds)
                            .append("date", dtf.format(now))
                            .append("result", "successful");
                    collection.insertOne(document);
                    System.out.println("Saved successful transaction to database");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error retrieving info from the database.");
            } catch (MongoException e) {
                System.out.println("Error writing transaction to the database.");
            }
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
