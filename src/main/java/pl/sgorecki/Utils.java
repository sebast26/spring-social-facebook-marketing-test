package pl.sgorecki;

import java.util.Random;
import java.util.UUID;

/**
 * @author Sebastian Górecki
 */
public class Utils {
	private Utils() {
		throw new AssertionError();
	}

	public static String getRandomString() {
		return UUID.randomUUID().toString();
	}

	public static long getRandomLong(long min, long max) {
		Random random = new Random();
		return min + ((long) random.nextDouble() * (max - min));
	}
}
