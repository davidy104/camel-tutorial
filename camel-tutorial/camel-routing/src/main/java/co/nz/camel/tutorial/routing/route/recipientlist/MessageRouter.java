package co.nz.camel.tutorial.routing.route.recipientlist;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Routing bean used to determine a list of endpoints to be triggered by a
 * recipient list.
 */
public class MessageRouter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessageRouter.class);

	public String getEndpointsToRouteMessageTo(Exchange exchange) {
		LOGGER.info("MessageRouter start:{}");
		String orderType = exchange.getIn()
				.getHeader("orderType", String.class);
		if (orderType == null) {
			return "direct:unrecognized";
		} else if (orderType.equals("priority")) {
			return "direct:order.priority,direct:billing";
		} else {
			return "direct:order.normal,direct:billing";
		}
	}
}
