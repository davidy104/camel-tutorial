package co.nz.camel.tutorial.splitjoin;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.junit.Test;

public class WriteToFileRouteTest extends CamelTestSupport {

	@Produce(uri = "direct:tofile")
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:beforeToFile")
	private MockEndpoint beforeToFileEndpoint;

	private static final String TEST_IMAGE = "/abc.jpg";

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new ToFileRoute();
	}

	@Test
	public void test() throws Exception {
		URL url = WriteToFileRouteTest.class.getResource(TEST_IMAGE);

		final BufferedImage img = ImageIO.read(url);
		final BufferedImage bufferedImage = Scalr.resize(img, Mode.AUTOMATIC,
				1275, 1275);

		final ByteArrayOutputStream output = new ByteArrayOutputStream() {
			@Override
			public synchronized byte[] toByteArray() {
				return this.buf;
			}
		};

		ImageIO.write(bufferedImage, "jpeg", output);
		InputStream is = new ByteArrayInputStream(output.toByteArray(), 0,
				output.size());
		producerTemplate.requestBody(output);
	}

	private static class ToFileRoute extends RouteBuilder {

		@Override
		public void configure() throws Exception {
			from("direct:tofile")
					.setProperty("fileName", constant("test.jpeg"))
					.to("mock:beforeToFile")
					.to("file://outbox?fileName=${property.fileName}");
		}

	}
}
