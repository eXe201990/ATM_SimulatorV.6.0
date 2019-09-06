package ro.oldtech.curs.java.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import ro.oldtech.curs.java.enums.EnumBanca;

public class UtilGenerator {

	public static String generateCodCard() {
		String value = Integer.toString(randomNumber(1000, 9999)).concat("-")
				.concat(Integer.toString(randomNumber(1000, 9999))).concat("-")
				.concat(Integer.toString(randomNumber(1000, 9999))).concat("-")
				.concat(Integer.toString(randomNumber(1000, 9999)));
		return value;
	}

	public static String generateContBancar(String numeBanca) {

		String value = "RO".concat(Integer.toString(randomNumber(10, 99))).concat(numeBanca)
				.concat(Integer.toString(randomNumber(100000000, 999999999)));

		return value;
	}

	public static int generateLuna() {
		return randomNumber(1, 12);
	}

	public static int generateZii() {
		return randomNumber(1, 25);
	}

	public static int generatePinCode() {
		return randomNumber(1000, 9999);
	}

	public static int generateCodCvc() {
		return randomNumber(100, 999);
	}

	public static String getRandomNumeBanca() {
		final RandomEnum<EnumBanca> randEnum = new RandomEnum<EnumBanca>(EnumBanca.class);
		return randEnum.random().name();
	}

	public static int generateLimitaExtractie() {
		return randomNumber(1000, 3000);
	}

	private static int randomNumber(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
		// return (int) (Math.random() * (max - min)) + max;
	}

	public static String formatDate(final Date date) {

		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);

	}

}
