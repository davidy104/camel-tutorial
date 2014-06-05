package co.nz.camel.tutorial.error.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

/**
 * 
 * @author dyuan
 * 
 */
@Configuration
public class CamelRouteRegistryConfig {

	@Resource
	private CamelContext camelContext;

	@Resource
	private DlcRouteBuilder dlcRouteBuilder;

	@Resource
	private RetryCustomRouteBuilder retryCustomRouteBuilder;

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupDLCRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext, dlcRouteBuilder);
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupLoggingRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new LoggingRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupRetryRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new RetryRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupRetryconditionalRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new RetryConditionalRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupRetryCustomRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				retryCustomRouteBuilder);
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupExceptionRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new ExceptionRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupDotryRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new DoTryRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupOnCompletionRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new OnCompletionRouteBuilder(),
				new OnCompletionMultipleRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupDynamicOnCompletionRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new DynamicOnCompletionRouteBuilder());
	}
}
