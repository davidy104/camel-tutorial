package co.nz.camel.tutorial.extend.produce;

import org.apache.camel.Produce;
import org.springframework.stereotype.Component;

@Component(value = "proxyProduce")
public class ProxyProduce {
	@Produce(uri = "activemq:queue:sayhello")
	ProxyPojo myProxy;

	public String doSomething(String name) {
		return myProxy.sayHello(name);
	}
}
