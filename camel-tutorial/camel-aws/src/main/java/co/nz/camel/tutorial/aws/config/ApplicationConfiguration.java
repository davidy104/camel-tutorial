package co.nz.camel.tutorial.aws.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.aws")
@Import(value = { CamelSpringContextConfig.class })
@PropertySource("classpath:aws.properties")
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private Environment environment;

	private static final String BUCKET_NAME = "aws.bucketName";
	private static final String ACCESS_KEY = "aws.accessKey";
	private static final String SECRET_KEY = "aws.secretKey";

	@Bean
	public AwsClientConfig awsClientConfig() {
		return AwsClientConfig.getBuilder(
				environment.getRequiredProperty(ACCESS_KEY),
				environment.getRequiredProperty(SECRET_KEY),
				environment.getRequiredProperty(BUCKET_NAME)).build();
	}

	@Bean
	public AmazonS3 amazonS3Client() {
		AwsClientConfig awsClientConfig = awsClientConfig();
		final AWSCredentials credentials = new BasicAWSCredentials(
				awsClientConfig.getAccessKey(), awsClientConfig.getSecretKey());
		final ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		return new AmazonS3Client(credentials, clientConfig);
	}

	@PostConstruct
	public void initializeCamelContext() throws Exception {
		SpringCamelContext springCamelContext = (SpringCamelContext) camelContext;
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("amazonS3Client", amazonS3Client());
		springCamelContext.setRegistry(registry);
	}
}
