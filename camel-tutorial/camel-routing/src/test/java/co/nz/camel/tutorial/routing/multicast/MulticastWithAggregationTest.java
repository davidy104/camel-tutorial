package co.nz.camel.tutorial.routing.multicast;

import static org.junit.Assert.assertEquals;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class MulticastWithAggregationTest {

	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiWastart")
	protected ProducerTemplate template;

	@Test
	public void testAggregationOfResponsesFromMulticast()
			throws InterruptedException {
		String response = (String) template.requestBody(MESSAGE_BODY);
		assertEquals("first response,second response", response);
	}
}
