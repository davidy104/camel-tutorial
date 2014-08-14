package co.nz.camel.tutorial.extend;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apacheextras.camel.component.jcifs.SmbGenericFile;
import org.junit.Test;

public class FileTest extends CamelTestSupport {

	@Test
	public void test() throws Exception {
		Exchange exchange = this.consumer.receive(
				"file://inbox?fileName=abc.jpg&localWorkDirectory=/tmp", 3000);
		Object body = exchange.getIn().getBody();
		System.out.println("body class: " + body.getClass());

		System.out.println(" if SmbGenericFile instance: "
				+ (body instanceof SmbGenericFile));

		Map<String, Object> headers = exchange.getIn().getHeaders();
		for (Map.Entry<String, Object> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

	}
}
