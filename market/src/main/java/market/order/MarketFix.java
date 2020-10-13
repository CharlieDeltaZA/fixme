package market.order;

public class MarketFix {

    private String brokerID;
    private String query;
    private String product;
    private String quantity;
    private String money;
    private String outcome;

    public MarketFix(String[] input) {

        try {
            query = "35=" + input[0].trim() + "|";
            product = "7=" + input[1].trim() + "|";
            quantity = "46=" + input[2].trim() + "|";
            money = "99=" + input[3].trim() + "|";
            brokerID = "22=" + input[4].trim() + "|";
            outcome = "69=" + input[5].trim() + "|";
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> MarketFix.java -> MarketFix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> MarketFix.java -> MarketFix(): " + e);
        }
    }

    public String constructFix(int marketID) {

        String fixMsg = "8=" + marketID + "|";
        fixMsg += "9=" + (brokerID.length() + query.length() + product.length() +
                quantity.length() + money.length()) + outcome.length() + "|";
        fixMsg += brokerID;
        fixMsg += query;
        fixMsg += product;
        fixMsg += quantity;
        fixMsg += money;
        fixMsg += outcome;
        fixMsg += "10=" + createChecksum() + "|";

        return fixMsg;
    }

    private String createChecksum() {
        int total = 0;
        int result = 0;

        try {
            String[] csArr = {query, product, quantity, money, brokerID, outcome};

            for (String s : csArr) {
                for (int j = 0; j < s.length(); j++) {
                    total += s.charAt(j);
                }
            }
            result = total % 256;

        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> MarketFix.java -> createChecksum(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> MarketFix.java -> createChecksum(): " + e);
        }

        if (result < 100) {
            return "0" + result;
        } else {
            return "" + result;
        }
    }
}
