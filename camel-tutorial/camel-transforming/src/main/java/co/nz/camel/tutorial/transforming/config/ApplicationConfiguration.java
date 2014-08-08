package co.nz.camel.tutorial.transforming.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;
import co.nz.camel.tutorial.transforming.csv.CsvRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichWithAggregatorRouteBuilder;
import co.nz.camel.tutorial.transforming.enrich.EnrichXsltRouteBuilder;
import co.nz.camel.tutorial.transforming.jaxb.JaxbRouteBuilder;
import co.nz.camel.tutorial.transforming.json.JsonJacksonRouteBuilder;
import co.nz.camel.tutorial.transforming.json.JsonRouteBuilder;
import co.nz.camel.tutorial.transforming.normalizer.NormalizerRouteBuilder;
import co.nz.camel.tutorial.transforming.simple.SimpleRouteBuilder;
import co.nz.camel.tutorial.transforming.xmljson.XmlJsonRouteBuilder;
import co.nz.camel.tutorial.transforming.xmljson.XmlToJsonJacksonRoute;
import co.nz.camel.tutorial.transforming.xquery.XqueryParamRouteBuilder;
import co.nz.camel.tutorial.transforming.xquery.XqueryRouteBuilder;
import co.nz.camel.tutorial.transforming.xslt.XsltParamRouteBuilder;
import co.nz.camel.tutorial.transforming.xslt.XsltRouteBuilder;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.transforming")
@Import(value = { CamelSpringContextConfig.class })
public class ApplicationConfiguration {
	@Resource
	private CamelContext camelContext;

	@Resource
	private NormalizerRouteBuilder NormalizerRouteBuilder;

	@Resource
	private EnrichRouteBuilder enrichRouteBuilder;

	@Resource
	private EnrichWithAggregatorRouteBuilder enrichWithAggregatorRouteBuilder;

	@PostConstruct
	public void initializeRoutes() throws Exception {
		camelContext.addRoutes(NormalizerRouteBuilder);
		camelContext.addRoutes(new SimpleRouteBuilder());
		camelContext.addRoutes(new JaxbRouteBuilder());

		initXQueryTransformingRoutes();
		initXsltTransformingRoutes();
		initJsonTransforming();
		initXmlJsonTransformingRoutes();
		initCsvTransforming();
		initEnrichTransforming();

	}

	private void initXQueryTransformingRoutes() throws Exception {
		camelContext.addRoutes(new XqueryParamRouteBuilder());
		camelContext.addRoutes(new XqueryRouteBuilder());
	}

	private void initXsltTransformingRoutes() throws Exception {
		camelContext.addRoutes(new XsltParamRouteBuilder());
		camelContext.addRoutes(new XsltRouteBuilder());
	}

	private void initJsonTransforming() throws Exception {
		camelContext.addRoutes(new JsonJacksonRouteBuilder());
		camelContext.addRoutes(new JsonRouteBuilder());
	}

	private void initXmlJsonTransformingRoutes() throws Exception {
		camelContext.addRoutes(new XmlJsonRouteBuilder());
		camelContext.addRoutes(new XmlToJsonJacksonRoute());
	}

	private void initCsvTransforming() throws Exception {
		camelContext.addRoutes(new CsvRouteBuilder());
	}

	private void initEnrichTransforming() throws Exception {
		camelContext.addRoutes(new EnrichXsltRouteBuilder());
		camelContext.addRoutes(enrichWithAggregatorRouteBuilder);
		camelContext.addRoutes(enrichRouteBuilder);
	}
}
