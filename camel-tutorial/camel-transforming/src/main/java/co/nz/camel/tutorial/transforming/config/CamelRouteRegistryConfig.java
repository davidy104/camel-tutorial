package co.nz.camel.tutorial.transforming.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.transforming.csv.CsvRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichWithAggregatorRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichXsltRouteBuilder;
import co.nz.camel.tutorial.transforming.jaxb.JaxbRouteBuilder;
import co.nz.camel.tutorial.transforming.json.JsonJacksonRouteBuilder;
import co.nz.camel.tutorial.transforming.json.JsonRouteBuilder;
import co.nz.camel.tutorial.transforming.simple.SimpleRouteBuilder;
import co.nz.camel.tutorial.transforming.xmljson.XmlJsonRouteBuilder;
import co.nz.camel.tutorial.transforming.xquery.XqueryParamRouteBuilder;
import co.nz.camel.tutorial.transforming.xquery.XqueryRouteBuilder;
import co.nz.camel.tutorial.transforming.xslt.XsltParamRouteBuilder;
import co.nz.camel.tutorial.transforming.xslt.XsltRouteBuilder;

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
	private EnrichRouteBuilder enrichRouteBuilder;

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupSimpleTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new SimpleRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupJaxbTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new JaxbRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupXQueryTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new XqueryParamRouteBuilder(), new XqueryRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupXsltTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new XsltParamRouteBuilder(), new XsltRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupJsonTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new JsonJacksonRouteBuilder(), new JsonRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupXmlJsonTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new XmlJsonRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupCsvTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new CsvRouteBuilder());
	}

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupEnrichTransforming()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new EnrichXsltRouteBuilder(),
				new EnrichWithAggregatorRouteBuilder(), enrichRouteBuilder);
	}
}
