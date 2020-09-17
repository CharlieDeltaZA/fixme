package com.fixme.fix;

public class Fix {

    public String constructFix(int ID, String[] input) {

        String[] trimArr = new String[4];

        for (int i = 0; i < input.length; i++) {
            trimArr[i] = input[i].trim();
        }

        String fixMsg = "8=" + ID;
        fixMsg += "|9=" + trimArr[0].length() + trimArr[1].length() + trimArr[2].length() + trimArr[3].length() + 4;
        fixMsg += "|35=" + trimArr[0];
        fixMsg += "|7=" + trimArr[1];
        fixMsg += "|46=" + trimArr[2];
        fixMsg += "|99=" + trimArr[3];
        fixMsg += "|10=" + createChecksum();

        if (validateFix(fixMsg)) {
            return fixMsg;
        } else {
            return null;
        }
    }

    private String createChecksum() {
        // creates checksum
    }

    public boolean validateFix(String msg) {
        // uses checksum to see if fix is true
    }

    public boolean compareFix(String one, String two) {
        return one.equals(two);
    }
}
