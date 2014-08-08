package co.nz.camel.tutorial.splitjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

public class CustomPropertySplitBean {
	public List<Message> splitMessage(
			@Header(value = "sizeMap") Map<String, String> headerMap,
			@Body String body) {
		List<Message> answer = new ArrayList<Message>();

		for (Map.Entry<String, String> entry : headerMap.entrySet()) {

			DefaultMessage message = new DefaultMessage();
			message.setHeader("size", entry.getValue());
			message.setHeader("imgresizeType", entry.getKey());
			message.setBody(body);
			answer.add(message);
		}

		return answer;
	}

}
