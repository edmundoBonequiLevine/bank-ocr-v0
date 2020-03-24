package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OCRFileReader {

    private BufferedReader bufferedReader;
    private static final Map<String, Character> ocrDigitsMap = new HashMap<>();
    public static final String NUMBERS_TO_SPLIT_REGEX = "(?<=\\G.{3})";
    BankAccounts bankAccounts;

    String zero = " _ " +
            "| |" +
            "|_|";

    String one = "   " +
            "  |" +
            "  |";

    String two = " _ " +
            " _|" +
            "|_ ";

    String three = " _ " +
            " _|" +
            " _|";

    String four = "   " +
            "|_|" +
            "  |";

    String five = " _ " +
            "|_ " +
            " _|";

    String six = " _ " +
            "|_ " +
            "|_|";

    String seven = " _ " +
            "  |" +
            "  |";

    String eight = " _ " +
            "|_|" +
            "|_|";

    String nine = " _ " +
            "|_|" +
            " _|";

    public OCRFileReader() throws IOException {
        ocrDigitsMap.put(zero, '0');
        ocrDigitsMap.put(one, '1');
        ocrDigitsMap.put(two, '2');
        ocrDigitsMap.put(three, '3');
        ocrDigitsMap.put(four, '4');
        ocrDigitsMap.put(five, '5');
        ocrDigitsMap.put(six, '6');
        ocrDigitsMap.put(seven, '7');
        ocrDigitsMap.put(eight, '8');
        ocrDigitsMap.put(nine, '9');

        this.bufferedReader = Files.newBufferedReader(Paths.get("data/accounts_template.txt"));
    }

    public void printAccounts() throws IOException {

        List<String> results = new ArrayList<>();

        while (bufferedReader.ready() && (bankAccounts = new BankAccounts(ocrParser(getNextAccount()))) != null) {

           // imprimir en consola
            System.out.println(String.format("%s %s", bankAccounts.getAccountNumber(),
                    bankAccounts.getResult()));

            results.add(String.format("%s %s", bankAccounts.getAccountNumber(),
                    bankAccounts.getResult()));
        }

        if (results.size() > 0) {
            printResultInFile(results);
        }
    }

    public void printResultInFile(List<String> results) {
        try (FileWriter fw = new FileWriter("accounts_result.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (String result : results) {
                out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNextAccount() throws IOException {

        if (bufferedReader.ready()) {

            List<StringBuilder> ocrDigits = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                String line = bufferedReader.readLine();

                if (line != null) {
                    int j = 0;

                    for (String digit : String.format("%-27s", line).split(
                            NUMBERS_TO_SPLIT_REGEX)) {
                        try {
                            ocrDigits.set(j, ocrDigits.get(j).append(digit));
                        } catch (IndexOutOfBoundsException e) {
                            ocrDigits.add(new StringBuilder(digit));
                        }
                        j++;
                    }
                }
            }

            bufferedReader.readLine();

            if (!ocrDigits.isEmpty()) {
                List<String> digits = new ArrayList<>();
                for (StringBuilder sb : ocrDigits) {
                    digits.add(sb.toString());
                }
                return digits;
            }
        }
        return null;

    }

    public String ocrParser(final List<String> digits) {

        if (digits == null || digits.isEmpty()) {
            return null;
        }

        StringBuilder digitBuilder = new StringBuilder();

        //El valor por default es el digito ilegible '?'
        for (String digit : digits) {
            digitBuilder.append(ocrDigitsMap
                    .getOrDefault(digit, '?'));
        }

        return digitBuilder.toString();
    }
}