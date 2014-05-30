package co.nz.camel.tutorial.extend.predicate;

import org.springframework.stereotype.Component;

@Component
public class MyPredicate {
	public boolean isWhatIWant(String body) {
		return ((body != null) && body.contains("Boston"));
	}
}
