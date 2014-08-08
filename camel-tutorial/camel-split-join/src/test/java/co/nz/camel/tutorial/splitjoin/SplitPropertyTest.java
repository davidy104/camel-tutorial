package co.nz.camel.tutorial.splitjoin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplitPropertyTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:spOut")
	private MockEndpoint resultEndpoint;

	@Produce
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:cspOut")
	private MockEndpoint custSplitResultEndpoint;

	@EndpointInject(uri = "mock:cspResult")
	private MockEndpoint finalResultEndpoint;

	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new SplitPropertyRoute(),
				new CustomBeanSplitPropertyRoute() };
	}

	@Test
	public void testSplit() throws Exception {
		List<String> sizeList = new ArrayList<String>();
		sizeList.add("123:123");
		sizeList.add("234:567");
		producerTemplate.sendBodyAndProperty("direct:spIn", "testBody",
				"sizelist", sizeList);

		String result = resultEndpoint.getExchanges().get(0).getIn()
				.getBody(String.class);
		System.out.println("result: " + result);

		Map<String, Object> properties = resultEndpoint.getExchanges().get(0)
				.getProperties();
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
	}

	@Test
	public void testCustomSplit() throws Exception {

		Map<String, String> sizeMap = new HashMap<>();
		sizeMap.put("normal", "123:123");
		sizeMap.put("standard", "234:567");

		Map<String, Object> headers = new HashMap<>();
		headers.put("sizeMap", sizeMap);
		headers.put("ip", "123");

		producerTemplate
				.sendBodyAndHeaders("direct:cspIn", "testBody", headers);

		String result = custSplitResultEndpoint.getExchanges().get(0).getIn()
				.getBody(String.class);
		System.out.println("result: " + result);

		headers = custSplitResultEndpoint.getExchanges().get(0).getIn()
				.getHeaders();
		for (Map.Entry<String, Object> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

		System.out.println("another exchange ");
		headers = custSplitResultEndpoint.getExchanges().get(1).getIn()
				.getHeaders();
		for (Map.Entry<String, Object> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

		System.out.println("final result:");
		headers = finalResultEndpoint.getExchanges().get(0).getIn()
				.getHeaders();
		for (Map.Entry<String, Object> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

	}

	private static class SplitPropertyRoute extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from("direct:spIn").split(simple("${property.sizelist}"))
					.to("mock:spOut").end();
		}
	}

	private static class CustomBeanSplitPropertyRoute extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from("direct:cspIn").split()
					.method(new CustomPropertySplitBean(), "splitMessage")
					.to("mock:cspOut").end().to("mock:cspResult");
		}
	}

}
