package broker;

public class Validation {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public boolean validateInput(String input) {
        String[] arr;

        try {
            arr = input.split("-");

            if (arr.length != 4) {
                System.out.println(ANSI_RED + "Incorrect format! Incorrect number of parameters.\n" + ANSI_RESET);
                return false;
            }
            if (!arr[0].trim().equalsIgnoreCase("Sell") && !arr[0].trim().equalsIgnoreCase("Buy")) {
                System.out.println(ANSI_RED + "Incorrect format! Invalid 'Buy' or 'Sell' parameter.\n" + ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(arr[2].trim()) <= 0) {
                System.out.println(ANSI_RED + "Invalid quantity! Please ensure you make an order for a quantity above 0.\n" + ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(arr[3].trim()) <= 0) {
                System.out.println(ANSI_RED + "Invalid funds amount! Please ensure you make an order with funds above 0.\n" + ANSI_RESET);
                return false;
            }
            if (!validateString(arr[1].trim()) || !validateNumber(arr[2].trim()) || !validateNumber(arr[3].trim())) {
                System.out.println(ANSI_RED + "Incorrect format! Please pay attention to what must be digits or characters only.\n" + ANSI_RESET);
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Validation.java -> validateInput(): " + e);
            return false;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Validation.java -> validateInput(): " + e);
            return false;
        }
    }

    private boolean validateNumber(String str) {

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean validateString(String str) {

        str = str.toLowerCase();
        char[] charArray = str.toCharArray();

        for (char ch : charArray) {
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }
        return true;
    }
}
