package net.mloeks.aoc22.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Supports only additions and multiplications with at most two operands!
 */
public class PoorMansEvaluator {

    public static BigInteger eval(final String expr) {
        Pattern p = Pattern.compile("([\\d]+)([*|+])([\\d]+)");
        Matcher m = p.matcher(expr.replaceAll(" ", ""));
        if (m.find()) {
            BigInteger op1 = new BigInteger(m.group(1));
            String operation = m.group(2);
            BigInteger op2 = new BigInteger(m.group(3));

            if ("*".equals(operation)) {
                return op1.multiply(op2);
            } else if ("+".equals(operation)) {
                return op1.add(op2);
            } else throw new IllegalArgumentException("Unsupported operations: " + operation);
        } else throw new IllegalArgumentException("Cannot evaluate: " + expr);
    }

}
