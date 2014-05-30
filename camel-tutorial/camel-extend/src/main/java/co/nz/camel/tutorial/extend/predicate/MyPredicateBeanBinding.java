package co.nz.camel.tutorial.extend.predicate;

import org.apache.camel.language.XPath;
import org.springframework.stereotype.Component;

@Component
public class MyPredicateBeanBinding {
	public boolean isWhatIWant(@XPath("/someXml/city/text()") String city) {
		return "Boston".equals(city);
	}
}