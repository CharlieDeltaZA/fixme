package com.fixme.fix;

public class Fix {

    private final String query;
    private final String product;
    private final String quantity;
    private final String money;

    public Fix(String[] input) {
        String[] trimArr = new String[4];

        for (int i = 0; i < input.length; i++) {
            trimArr[i] = input[i].trim();
        }
        query = trimArr[0];
        product = trimArr[1];
        quantity = trimArr[2];
        money = trimArr[3];
    }

    public String constructFix(int ID) {

        String fixMsg = "8=" + ID + "|";
        fixMsg += "9=" + (query.length() + product.length() + quantity.length() + money.length() + 15) + "|";
        fixMsg += "35=" + query + "|";
        fixMsg += "7=" + product + "|";
        fixMsg += "46=" + quantity + "|";
        fixMsg += "99=" + money + "|";
        fixMsg += "10=" + createChecksum() + "|";

        if (validateFix(fixMsg)) {
            return fixMsg;
        } else {
            return null;
        }
    }

    private String createChecksum() {
        int total = 0;
        int result;
        String csQuery = query + "|";
        String csProduct = product + "|";
        String csQuantity = quantity + "|";
        String csMoney= money + "|";

        try {
            String[] csArr = new String[4];
            csArr[0] = csQuery;
            csArr[1] = csProduct;
            csArr[2] = csQuantity;
            csArr[3] = csMoney;

            for (int i = 0; i < csArr.length; i++) {
                for (int j = 0; j < csArr[i].length(); j++) {
                    total += (int)csArr[i][j];
                }
            }
            result = total % 256;

            if (result < 100) {
                return "0" + result;
            } else {
                return "" + result;
            }
        } catch (NullPointerException e) {
            System.out.println("Null pointer err -> Fix.java -> createChecksum(): " + e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index err -> Fix.java -> createChecksum(): " + e);
        }
    }

    public boolean validateFix(String msg) {
        // uses checksum to see if fix is true
    }

    public boolean compareFix(String one, String two) {
        return one.equals(two);
    }
}
