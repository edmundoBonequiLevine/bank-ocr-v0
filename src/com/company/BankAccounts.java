package com.company;

public class BankAccounts {

    /**
     * Expresion Regular que
     * se utiliza para validar un número de cuenta sea analizado correctamente (9 dígitos)
     */
    public static final String VALID_NUMBER__ACCOUNT_REGEX = "\\d{9}";

    /**
     * Expresion Regular usada para validar que los caracteres ingresados en la cuenta
     * sean numeros o el caracter ilegible "?"
     */
    public static final String ALLOWED_NUMBER_REGEX = "[\\d?]{9}";

    private String accountNumber;
    private Integer checksum;
    private RESULT result;

    public enum RESULT {
        ERR, ILL, OK
    }

    public BankAccounts(String accountNumber) {
        if (accountNumber != null) {
            if (accountNumber.matches(ALLOWED_NUMBER_REGEX)) {
                this.accountNumber = accountNumber;

                if (!accountNumber.contains("?")) {
                    checksum = checksumCalculator(accountNumber);
                } else {
                    checksum = null;
                }

                result = validateAccount(accountNumber, checksum);
            } else {
                throw new IllegalArgumentException(
                        "Solo puede contener numeros o el caracter ilegible '?'");
            }
        } else {
            throw new IllegalArgumentException("numero de cuenta es nulo");
        }
    }


    public static Integer checksumCalculator(String accountNumber) {

        if (accountNumber != null
                && accountNumber.length() == 9) {

            int total = 0;
            int positionNumber = 1;
            String[] accountNumbers = new StringBuilder(accountNumber).reverse().toString().split("");

            for (String number : accountNumbers) {
                total += Integer.parseInt(number) * positionNumber++;
            }

            return total % 11;

        } else {
            return null;
        }

    }

    private static RESULT validateAccount(String accountNumber, Integer checksum) {

        if (accountNumber == null
                || !accountNumber.matches(VALID_NUMBER__ACCOUNT_REGEX)) {
            return RESULT.ILL;
        } else if (checksum == null || checksum != 0) {
            return RESULT.ERR;
        } else {
            return RESULT.OK;
        }

    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public RESULT getResult() {
        return result;
    }

}
