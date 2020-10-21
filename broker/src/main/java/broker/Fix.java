package broker;

// constructs a fix message from the order parameters
// creates its checksum

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
}
