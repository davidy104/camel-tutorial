package co.nz.camel.tutorial.error.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.error.dlc.DlcRouteBuilder;

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

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupDLCRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext, dlcRouteBuilder);
	}

}
