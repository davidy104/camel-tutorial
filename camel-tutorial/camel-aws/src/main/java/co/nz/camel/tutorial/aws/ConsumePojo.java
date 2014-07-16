package co.nz.camel.tutorial.aws;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.aws.config.AwsClientConfig;

@Component(value = "s3ImageConsumer")
public class ConsumePojo {

	@Resource
	private AwsClientConfig awsClientConfig;

	@Resource
	private CamelContext camelContext;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConsumePojo.class);

	public Exchange downloadImage(String imageKey) {
		LOGGER.info("downloadImage start:{} ", imageKey);
		ConsumerTemplate template = camelContext.createConsumerTemplate();
		Exchange exchange = template
				.receive(
						"aws-s3://"
								+ awsClientConfig.getBucketName()
								+ "?amazonS3Client=#amazonS3Client&delay=2000&maxMessagesPerPoll=1&prefix="
								+ imageKey + "", 5000L);

		return exchange;
	}

}
