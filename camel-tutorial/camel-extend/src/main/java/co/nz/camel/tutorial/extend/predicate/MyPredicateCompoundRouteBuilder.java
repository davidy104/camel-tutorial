package co.nz.camel.tutorial.extend.predicate;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import static org.apache.camel.builder.PredicateBuilder.and;

@Component
public class MyPredicateCompoundRouteBuilder extends RouteBuilder {

	@Resource
	private MyPredicate predicate;

	@Override
	public void configure() throws Exception {
		MyPredicate predicate = new MyPredicate();

		from("direct:mpcrStart").filter(
				and(xpath("/someXml/city"), method(predicate, "isWhatIWant")))
				.to("mock:mpcrBoston");
	}
}