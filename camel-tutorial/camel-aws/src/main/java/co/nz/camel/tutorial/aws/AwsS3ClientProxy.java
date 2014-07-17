package co.nz.camel.tutorial.aws;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.aws.config.AwsClientConfig;

@Component(value = "awsClient")
public class AwsS3ClientProxy {

	@Resource
	private AwsClientConfig awsClientConfig;

	@Resource
	private CamelContext camelContext;

	@Produce
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AwsS3ClientProxy.class);

	public void getFile(String fileKey,
			ConsumePostProcessor consumePostProcessor) throws Exception {
		LOGGER.info("downloadImage start:{} ", fileKey);
		ConsumerTemplate template = camelContext.createConsumerTemplate();
		Exchange exchange = template
				.receive(
						"aws-s3://"
								+ awsClientConfig.getBucketName()
								+ "?amazonS3Client=#amazonS3Client&delay=2000&maxMessagesPerPoll=1&prefix="
								+ fileKey + "", 5000L);

		if (consumePostProcessor != null) {
			consumePostProcessor.process(exchange);
		}
	}

	public void createBucket(String bucketName) {
		String endpointUri = "aws-s3://" + bucketName
				+ "?amazonS3Client=#amazonS3Client";

		// template.sendb(endpointUri, null);
	}
}
