package com.clipsa.utilities;

public class LogFormatter {




    public static String formatSubscriptionLog(String farmerName, String item, String amount){
        return String.format("%s subscribed to %s and paid %s", farmerName, item, amount);
    }
}
