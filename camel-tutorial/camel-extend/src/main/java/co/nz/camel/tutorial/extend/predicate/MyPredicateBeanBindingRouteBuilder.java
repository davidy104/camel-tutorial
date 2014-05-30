package co.nz.camel.tutorial.extend.predicate;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyPredicateBeanBindingRouteBuilder extends RouteBuilder {

	@Resource
	MyPredicateBeanBinding predicate;

	@Override
	public void configure() throws Exception {
		MyPredicateBeanBinding predicate = new MyPredicateBeanBinding();

		from("direct:mpbbrStart").filter().method(predicate, "isWhatIWant")
				.to("mock:mpbbrBoston");
	}
}
