package co.nz.camel.tutorial.aws;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
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
	private ConsumePojo s3ImageConsumer;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GetImageIntegrationTest.class);

	@Test
	public void testDownloadImageFromAWS() throws Exception {
		Exchange exchange = s3ImageConsumer.downloadImage(TEST_KEY);
		LOGGER.info("exchange:{} ", exchange);

		String awsS3Key = (String) exchange.getIn().getHeader("CamelAwsS3Key");
		String awsS3ContentLength = (String) exchange.getIn().getHeader(
				"CamelAwsS3ContentLength");
		LOGGER.info("awsS3Key:{} ", awsS3Key);
		LOGGER.info("awsS3ContentLength:{} ", awsS3ContentLength);
	}

}
