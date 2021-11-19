package kz.lyssechko.cardGenerator;

import java.util.Random;

public class CreditCardNumberGenerator {
    private static Random random = new Random(System.currentTimeMillis());

    private static final String bin = "51694971";
    private static final Integer length = 16;

    public static String generate() {
        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder stringBuilder = new StringBuilder(bin);


        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            stringBuilder.append(digit);
        }

        int checkDigit = getCheckDigit(stringBuilder.toString());
        stringBuilder.append(checkDigit);

        return stringBuilder.toString();
    }


    private static int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));
            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;

        return ((mod == 0) ? 0 : 10 - mod);
    }
}
