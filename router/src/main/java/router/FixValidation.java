package router;

public class FixValidation {

    // Validates the FIX message received from a broker.
    public boolean validateFix(String msg) {
        String[] arr = msg.split("\\|");
        String checksum = "";
        int result = 0;
        int total = 0;

        if (arr.length < 7) return false;

        
        try {
            String qry = arr[2] + "|";
            String obj = arr[3] + "|";
            String qty = arr[4] + "|";
            String money = arr[5] + "|";

            String[] chArr = {qry, obj, qty, money};

            for (String s : chArr) {
                for (int j = 0; j < s.length(); j++) {
                    total += s.charAt(j);
                }
            }
            result = total % 256;
            checksum = msg.split("\\|")[6].split("=")[1];

        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> FixValidation.java -> validateFix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> FixValidation.java -> validateFix(): " + e);
        }
        return result == Integer.parseInt(checksum);
    }

    // Validates the FIX message received from a market.
    // Returns a number of different ints depending on validity of the message
    // so that the router can return an appropriate message.
    public int validateMarketFix(String msg) {
        String checksum = "";
        int result = 0;
        int total = 0;
        
        if (msg == null) return (-1);
        
        try {
            String[] arr = msg.split("\\|");
            if (arr.length < 9) return 0;
            
            String brokerID = arr[2] + "|";
            String qry = arr[3] + "|";
            String obj = arr[4] + "|";
            String qty = arr[5] + "|";
            String money = arr[6] + "|";
            String outcome = arr[7] + "|";

            String[] chArr = {qry, obj, qty, money, brokerID, outcome};

            for (String s : chArr) {
                for (int j = 0; j < s.length(); j++) {
                    total += s.charAt(j);
                }
            }
            result = total % 256;
            checksum = msg.split("\\|")[8].split("=")[1];

            int res = 0;
            if (result == Integer.parseInt(checksum)) res = 1;
            
            return res;
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> FixValidation.java -> validateMarketFix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> FixValidation.java -> validateMarketFix(): " + e);
        } catch (NumberFormatException e) {
            System.out.println("Number Format err -> FixValidation.java -> validateMarketFix(): " + e);
        }
        return 0;
    }
}
