package com.sts.core.util;

/**
 * This class would be used to get tracking id in alpha-numric form
 *
 */
public class ConversionUtil {

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	private static int RADIX = digits.length;// 62 (26+26+10): case sensitive
												// alpha-numeric

	private static int charIndexInDigits(char c) {
		for (int indx = 0; indx < RADIX; indx++) {
			if (digits[indx] == c) {
				return indx;
			}
		}
		throw new NumberFormatException("Invalid Character found");
	}

	public static String getAlhpaNumeric(int numericValue) {
		return getAlphaNumeric(new Long(numericValue));
	}

	public static String getAlphaNumeric(Long numericValue) {
		if (numericValue == null) {
			return null;
		}
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (numericValue < 0);

		if (!negative) {
			numericValue = -numericValue;
		}

		while (numericValue <= -RADIX) {
			buf[charPos--] = digits[(int) (-(numericValue % RADIX))];
			numericValue = numericValue / RADIX;
		}
		buf[charPos] = digits[(int) (-numericValue)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (65 - charPos));

	}

	public static Long getNumeric(String alphaNumericValue) {
		if (alphaNumericValue == null) {
			throw new NumberFormatException("null");
		}

		boolean negative = false;
		int digit, indx = 0, len = alphaNumericValue.length();
		long multmin, result = 0, limit = -Long.MAX_VALUE;

		if (len > 0) {
			char firstChar = alphaNumericValue.charAt(0);
			if (firstChar < '0') { // Possible leading "+" or "-"
				if (firstChar == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
				} else if (firstChar != '+') {
					throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
				}

				if (len == 1) { // Cannot have lone "+" or "-"
					throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
				}
				indx++;
			}
			multmin = limit / RADIX;
			while (indx < len) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				digit = charIndexInDigits(alphaNumericValue.charAt(indx++));
				if (digit < 0) {
					throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
				}
				if (result < multmin) {
					throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
				}
				result *= RADIX;
				if (result < limit + digit) {
					throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException("For input string: \"" + alphaNumericValue + "\"");
		}
		return negative ? result : -result;
	}
}
