package co.nz.camel.tutorial.aws;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.aws.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetImageIntegrationTest {

	private static String TEST_KEY = "vernon/296984/original.jpg";

	@Resource
	private AwsS3ClientProxy awsClient;

	private String localClasspathImagePath;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GetImageIntegrationTest.class);

	@Test
	public void testDownloadImageFromAWS() throws Exception {
		awsClient.getFile(TEST_KEY, new ConsumePostProcessor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				LOGGER.info("exchange:{} ", exchange);

				Message message = exchange.getIn();

				String awsS3Key = (String) message.getHeader("CamelAwsS3Key");
				Long awsS3ContentLength = (Long) message
						.getHeader("CamelAwsS3ContentLength");

				String awsS3ETag = (String) message.getHeader("CamelAwsS3ETag");

				LOGGER.info("awsS3Key:{} ", awsS3Key);
				LOGGER.info("awsS3ContentLength:{} ", awsS3ContentLength);
				LOGGER.info("awsS3ETag:{} ", awsS3ETag);

				String fileName = awsS3Key.substring(
						awsS3Key.lastIndexOf("/") + 1, awsS3Key.length());
				LOGGER.info("fileName:{} ", fileName);

				InputStream imageStream = message.getBody(InputStream.class);
				File imageFile = AwsClientUtils.writeTempFileToClasspath(
						fileName, imageStream);
				localClasspathImagePath = imageFile.getAbsolutePath();
				LOGGER.info("localClasspathImagePath:{} ",
						localClasspathImagePath);
			}
		});
	}

}
