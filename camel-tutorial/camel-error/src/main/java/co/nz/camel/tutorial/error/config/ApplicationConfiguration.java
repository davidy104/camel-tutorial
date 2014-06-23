package co.nz.camel.tutorial.error.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;
import co.nz.camel.tutorial.error.dlc.DlcRouteBuilder;
import co.nz.camel.tutorial.error.dotry.DoTryRouteBuilder;
import co.nz.camel.tutorial.error.exception.ExceptionRouteBuilder;
import co.nz.camel.tutorial.error.logging.LoggingRouteBuilder;
import co.nz.camel.tutorial.error.oncompletion.OnCompletionMultipleRouteBuilder;
import co.nz.camel.tutorial.error.oncompletion.OnCompletionRouteBuilder;
import co.nz.camel.tutorial.error.retry.RetryRouteBuilder;
import co.nz.camel.tutorial.error.retryconditional.RetryConditionalRouteBuilder;
import co.nz.camel.tutorial.error.retrycustom.RetryCustomRouteBuilder;
import co.nz.camel.tutorial.error.synchronizations.DynamicOnCompletionRouteBuilder;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.error")
@Import(value = { CamelSpringContextConfig.class })
public class ApplicationConfiguration {
	@Resource
	private DlcRouteBuilder dlcRouteBuilder;

	@Resource
	private RetryCustomRouteBuilder retryCustomRouteBuilder;

	@Resource
	private CamelContext camelContext;

	@PostConstruct
	public void initializeRoutes() throws Exception {
		camelContext.addRoutes(dlcRouteBuilder);

		camelContext.addRoutes(new LoggingRouteBuilder());

		camelContext.addRoutes(new RetryRouteBuilder());
		camelContext.addRoutes(new RetryConditionalRouteBuilder());
		camelContext.addRoutes(retryCustomRouteBuilder);

		camelContext.addRoutes(new ExceptionRouteBuilder());
		camelContext.addRoutes(new DoTryRouteBuilder());

		camelContext.addRoutes(new OnCompletionRouteBuilder());
		camelContext.addRoutes(new OnCompletionMultipleRouteBuilder());

		camelContext.addRoutes(new DynamicOnCompletionRouteBuilder());

	}
}
