package co.nz.camel.tutorial.error.dlc;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.error.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DlcTest {

	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:dlcResult")
	private MockEndpoint mockResult;

	@EndpointInject(uri = "mock:dlcError")
	private MockEndpoint mockError;

	@Test
	public void testDlq() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.expectedBodiesReceived("Foo");
		mockResult.message(0).property(Exchange.EXCEPTION_CAUGHT).isNull();
		mockResult.message(0).header("myHeader").isEqualTo("changed");

		mockError.expectedMessageCount(1);
		mockError.expectedBodiesReceived("KaBoom");
		mockError.message(0).property(Exchange.EXCEPTION_CAUGHT).isNotNull();
		mockError.message(0).property(Exchange.FAILURE_ROUTE_ID)
				.isEqualTo("myDlcRoute");
		mockError.message(0).header("myHeader").isEqualTo("changed");

		template.sendBodyAndHeader("direct:dlcStart", "Foo", "myHeader",
				"original");
		template.sendBodyAndHeader("direct:dlcStart", "KaBoom", "myHeader",
				"original");

		mockResult.assertIsSatisfied();
		mockError.assertIsSatisfied();
	}

	@Test
	public void testDlqMultistep() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.expectedBodiesReceived("Foo");
		mockResult.message(0).property(Exchange.EXCEPTION_CAUGHT).isNull();
		mockResult.message(0).header("myHeader").isEqualTo("changed");

		mockError.expectedMessageCount(1);
		mockError.expectedBodiesReceived("KaBoom");
		mockError.message(0).property(Exchange.EXCEPTION_CAUGHT).isNotNull();
		mockError.message(0).property(Exchange.FAILURE_ROUTE_ID)
				.isEqualTo("myFlakyRoute");
		mockError.message(0).header("myHeader").isEqualTo("flaky");

		template.sendBodyAndHeader("direct:dlcMultiroute", "Foo", "myHeader",
				"original");
		template.sendBodyAndHeader("direct:dlcMultiroute", "KaBoom",
				"myHeader", "original");

		mockResult.assertIsSatisfied();
		mockError.assertIsSatisfied();
	}

	@Test
	public void testDlqMultistepOriginal() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.expectedBodiesReceived("Foo");
		mockResult.message(0).property(Exchange.EXCEPTION_CAUGHT).isNull();
		mockResult.message(0).header("myHeader").isEqualTo("changed");

		mockError.expectedMessageCount(1);
		mockError.expectedBodiesReceived("KaBoom");
		mockError.message(0).property(Exchange.EXCEPTION_CAUGHT).isNotNull();
		mockError.message(0).property(Exchange.FAILURE_ROUTE_ID)
				.isEqualTo("myFlakyRouteOriginal");
		mockError.message(0).header("myHeader").isEqualTo("multistep");

		template.sendBodyAndHeader("direct:dlcMultirouteOriginal", "Foo",
				"myHeader", "original");
		template.sendBodyAndHeader("direct:dlcMultirouteOriginal", "KaBoom",
				"myHeader", "original");

		mockResult.assertIsSatisfied();
		mockError.assertIsSatisfied();
	}

	@Test
	public void testDlqUseOriginal() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.expectedBodiesReceived("Foo");
		mockResult.message(0).property(Exchange.EXCEPTION_CAUGHT).isNull();
		mockResult.message(0).header("myHeader").isEqualTo("changed");

		mockError.expectedMessageCount(1);
		mockError.expectedBodiesReceived("KaBoom");
		mockError.message(0).property(Exchange.EXCEPTION_CAUGHT).isNotNull();
		mockError.message(0).property(Exchange.FAILURE_ROUTE_ID)
				.isEqualTo("myDlcOriginalRoute");
		mockError.message(0).header("myHeader").isEqualTo("original");

		template.sendBodyAndHeader("direct:dlcUseOriginal", "Foo", "myHeader",
				"original");
		template.sendBodyAndHeader("direct:dlcUseOriginal", "KaBoom",
				"myHeader", "original");

		mockResult.assertIsSatisfied();
		mockError.assertIsSatisfied();
	}
}
