package co.nz.camel.tutorial.transforming.enrich;

import org.springframework.stereotype.Component;

@Component(value = "myExpander")
public class AbbreviationExpander {
	public String expand(String abbreviation) {
		if ("MA".equalsIgnoreCase(abbreviation)) {
			return "Massachusetts";
		}

		if ("CA".equalsIgnoreCase(abbreviation)) {
			return "California";
		}

		throw new IllegalArgumentException("Unknown abbreviation '"
				+ abbreviation + ";");
	}
}
