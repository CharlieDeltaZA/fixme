package broker;

public class Broker {

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
