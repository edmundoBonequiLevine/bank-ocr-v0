package com.company;

public class Main {

    public static void main(String[] args) {
        try {
            new OCRFileReader().printAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}