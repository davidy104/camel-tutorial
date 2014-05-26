package co.nz.camel.tutorial.routing.model;

/**
 * Utility class that increments the age of a {@link Cheese}.
 */
public class CheeseRipener {
	public static void ripen(Cheese cheese) {
		cheese.setAge(cheese.getAge() + 1);
	}
}
