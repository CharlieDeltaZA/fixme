package router;

public class FixValidation {

    public boolean validateFix(String msg) {
        String[] arr = msg.split("\\|");
        String checksum = "";
        int result = 0;
        int total = 0;

        if (arr.length < 7) return false;

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
            System.out.println("Null pointer err -> FixValidation.java -> validateFix(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> FixValidation.java -> validateFix(): " + e);
        }
        return result == Integer.parseInt(checksum);
    }

    public boolean compareFix(String one, String two) {
        return one.equals(two);
    }
}