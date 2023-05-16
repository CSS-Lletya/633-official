package com.rs.utilities;

public class Rational {

    private long num, denom;

    public Rational(double d) {
        String s = String.valueOf(d);
        int digitsDec = s.length() - 1 - s.indexOf('.');

        int denom = 1;
        for (int i = 0; i < digitsDec; i++) {
            d *= 10;
            denom *= 10;
        }
        int num = (int) Math.round(d);

        this.num = num;
        this.denom = denom;
    }

    public Rational(long num, long denom) {
        this.num = num;
        this.denom = denom;
    }

    public static Rational toRational(double number) {
        return toRational(number, 4);
    }

    public static Rational toRational(double number, int largestRightOfDecimal) {

        long sign = 1;
        if (number < 0) {
            number = -number;
            sign = -1;
        }

        final long SECOND_MULTIPLIER_MAX = (long) Math.pow(10, largestRightOfDecimal - 1);
        final long FIRST_MULTIPLIER_MAX = SECOND_MULTIPLIER_MAX * 10L;
        final double ERROR = Math.pow(10, -largestRightOfDecimal - 1);
        long firstMultiplier = 1;
        long secondMultiplier = 1;
        boolean notIntOrIrrational = false;
        long truncatedNumber = (long) number;
        Rational rationalNumber = new Rational((long) (sign * number * FIRST_MULTIPLIER_MAX), FIRST_MULTIPLIER_MAX);

        double error = number - truncatedNumber;
        while ((error >= ERROR) && (firstMultiplier <= FIRST_MULTIPLIER_MAX)) {
            secondMultiplier = 1;
            firstMultiplier *= 10;
            while ((secondMultiplier <= SECOND_MULTIPLIER_MAX) && (secondMultiplier < firstMultiplier)) {
                double difference = (number * firstMultiplier) - (number * secondMultiplier);
                truncatedNumber = (long) difference;
                error = difference - truncatedNumber;
                if (error < ERROR) {
                    notIntOrIrrational = true;
                    break;
                }
                secondMultiplier *= 10;
            }
        }

        if (notIntOrIrrational) {
            rationalNumber = new Rational(sign * truncatedNumber, firstMultiplier - secondMultiplier);
        }
        return rationalNumber;
    }

    public long getNum() {
        return num;
    }

    public long denom() {
        return denom;
    }

    public String toString() {
        return Utility.asFraction(num, denom);
    }
}