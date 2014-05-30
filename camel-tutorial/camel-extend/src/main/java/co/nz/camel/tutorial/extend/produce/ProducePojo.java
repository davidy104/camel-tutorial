package co.nz.camel.tutorial.extend.produce;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

@Component(value = "producer")
public class ProducePojo {
	@Produce
	private ProducerTemplate template;

	public String sayHello(String name) {
		return template.requestBody("activemq:queue:sayhello", name,
				String.class);
	}
}