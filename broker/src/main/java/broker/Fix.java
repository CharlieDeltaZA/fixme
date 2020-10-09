package broker;

public class Fix {

    private String query;
    private String product;
    private String quantity;
    private String money;

    public Fix(String[] input) {

        try {
            query = "35=" + input[0].trim() + "|";
            product = "7=" + input[1].trim() + "|";
            quantity = "46=" + input[2].trim() + "|";
            money = "99=" + input[3].trim() + "|";
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Fix.java -> Fix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Fix.java -> Fix(): " + e);
        }
    }

    public String constructFix(int ID) {

        String fixMsg = "8=" + ID + "|";
        fixMsg += "9=" + (query.length() + product.length() + quantity.length() + money.length()) + "|";
        fixMsg += query;
        fixMsg += product;
        fixMsg += quantity;
        fixMsg += money;
        fixMsg += "10=" + createChecksum() + "|";

        return fixMsg;
    }

    private String createChecksum() {
        int total = 0;
        int result = 0;

        try {
            String[] csArr = {query, product, quantity, money};

            for (String s : csArr) {
                for (int j = 0; j < s.length(); j++) {
                    total += s.charAt(j);
                }
            }
            result = total % 256;

        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Fix.java -> createChecksum(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Fix.java -> createChecksum(): " + e);
        }

        if (result < 100) {
            return "0" + result;
        } else {
            return "" + result;
        }
    }

    public boolean validateFix(String msg) {
        String[] arr = msg.split("\\|");
        String checksum = "";
        int result = 0;
        int total = 0;

        String qry = arr[2] + "|";
        String obj = arr[3] + "|";
        String qty = arr[4] + "|";
        String money = arr[5] + "|";

        try {
            String[] chArr = {qry, obj, qty, money};

            for (String s : chArr) {
                for (int j = 0; j < s.length(); j++) {
                    total += s.charAt(j);
                }
            }
            result = total % 256;
            checksum = msg.split("\\|")[6].split("=")[1];

        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Fix.java -> validateFix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Fix.java -> validateFix(): " + e);
        }
        return result == Integer.parseInt(checksum);
    }

    public boolean compareFix(String one, String two) {
        return one.equals(two);
    }
}
