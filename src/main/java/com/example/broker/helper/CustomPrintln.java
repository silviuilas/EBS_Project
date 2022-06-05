package com.example.broker.helper;

public class CustomPrintln {
    static boolean isDebugging = true;

    public static void print(String s){
        if(isDebugging) {
            System.out.println(s);
        }
    }
}
