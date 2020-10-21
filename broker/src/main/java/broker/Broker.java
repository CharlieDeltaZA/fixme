package broker;

public class Broker {

    // initiates an interface for the user to connect to the router or quit
    // enables users to enter in an order of the market
    public static void main(String[] args) {

        Business business = new Business();

        clearTerminal();

        if (business.getInitialization()) {
            business.takeOrders();
        }
    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
