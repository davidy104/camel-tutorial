package co.nz.camel.tutorial.extend.predicate;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyPredicateRouteBuilder extends RouteBuilder {

	@Resource
	private MyPredicate predicate;

	@Override
	public void configure() throws Exception {
		from("direct:mprStart").filter().method(predicate, "isWhatIWant")
				.to("mock:mprBoston");
	}
}