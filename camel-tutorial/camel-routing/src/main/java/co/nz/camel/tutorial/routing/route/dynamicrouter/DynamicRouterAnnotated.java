package co.nz.camel.tutorial.routing.route.dynamicrouter;

import java.util.Map;

import org.apache.camel.Consume;
import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DynamicRouterAnnotated {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DynamicRouterAnnotated.class);

	private static final String PROPERTY_NAME_INVOKED = "invoked";

	/**
	 * Returns the next endpoint to route a message to or null to finish
	 * routing. This implementation leverages Camel's <a
	 * href="http://camel.apache.org/bean-integration.html">Bean injection</a>
	 * to pass parts of the Camel Exchange to the method for processing. This
	 * can help the code be easier to maintain as it does not need the extra
	 * boilerplate code for extracting the relative data from the Exchange.
	 * <p/>
	 * This implementation stores an int property with the message exchange that
	 * is used to drive the routing behavior. This method will be called from
	 * multiple threads, one per message, so storing message specific state as a
	 * property is a good strategy.
	 * 
	 * @param body
	 *            the IN message converted to a String using Camel Bean
	 *            injection
	 * @param properties
	 *            the properties map associated with the Camel Exchange
	 * @return next endpoint uri(s) to route to or <tt>null</tt> to finish
	 *         routing
	 */

	@Consume(uri = "direct:dymaStart")
	@DynamicRouter(delimiter = ",")
	public String routeMe(String body,
			@Properties Map<String, Object> properties) {
		LOGGER.info("Exchange.SLIP_ENDPOINT = {}, invoked = {}",
				properties.get(Exchange.SLIP_ENDPOINT),
				properties.get(PROPERTY_NAME_INVOKED));

		// Store a property with the message exchange that will drive the
		// routing
		// decisions of this Dynamic Router implementation.
		int invoked = 0;
		Object current = properties.get(PROPERTY_NAME_INVOKED); // property will
																// be null on
																// first call
		if (current != null) {
			invoked = Integer.valueOf(current.toString());
		}
		invoked++;
		properties.put(PROPERTY_NAME_INVOKED, invoked);

		if (invoked == 1) {
			return "mock:a";
		} else if (invoked == 2) {
			return "mock:b,mock:c";
		} else if (invoked == 3) {
			return "direct:dymOther";
		} else if (invoked == 4) {
			return "mock:result";
		}

		// no more, so return null
		return null;
	}
}
