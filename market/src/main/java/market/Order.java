package market;

public class Order {

    private String brokerID;
    private String query;
    private String product;
    private String quantity;
    private String money;

    public void constructOrder(String msg) {
        String[] arr = msg.split("\\|");

        brokerID = arr[0].split("=")[1];
        query = arr[2].split("=")[1];
        product = arr[3].split("=")[1];
        quantity = arr[4].split("=")[1];
        money = arr[5].split("=")[1];
    }

    public String doOrder(Initialize products) {
        return "Accepted";
    }
}
