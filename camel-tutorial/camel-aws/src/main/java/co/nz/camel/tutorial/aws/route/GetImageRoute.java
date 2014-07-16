package co.nz.camel.tutorial.aws.route;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.aws.config.AwsClientConfig;

//CamelAwsS3Key
//@Component
public class GetImageRoute extends RouteBuilder {

	@Resource
	private AwsClientConfig awsClientConfig;

	@Override
	public void configure() throws Exception {
		from(
				"aws-s3://"
						+ awsClientConfig.getBucketName()
						+ "?amazonS3Client=#amazonS3Client&delay=5000&maxMessagesPerPoll=1")
				.autoStartup(false).log("get image: ${body}").to("mock:result")
				.filter().simple("${head.CamelAwsS3Key} == '^C.*'")
				.to("file:target?autoCreate=true").end();

	}
}
