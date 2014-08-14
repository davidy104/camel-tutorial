package co.nz.camel.tutorial.extend.smb;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apacheextras.camel.component.jcifs.SmbGenericFile;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.junit.Test;

public class JcifsIntegrationTest extends CamelTestSupport {

	private static final String TEST_DOWNLOAD_FILE_NAME = "test123.jpg";
	private static final String TEST_UPLOAD_FILE_NAME = "/james.jpg";

	@Produce(uri = "direct:uploadStart")
	ProducerTemplate producerTemplate;

	@Test
	public void testReadRemoteFile() throws Exception {
		PropertyPlaceholderDelegateRegistry registry = (PropertyPlaceholderDelegateRegistry) context
				.getRegistry();
		JndiRegistry jndiRegistry = (JndiRegistry) registry.getRegistry();
		jndiRegistry.bind("myFilter", new MyFileFilter("abc.jpg"));

		// String endpointUri =
		// "smb://diskstation.propellerhead.co.nz/development/Projects/Auckland%20Museum/Data/Images/20140804/Human%20History?filter=#myFilter&localWorkDirectory=/tmp";

		String endpointUri = "smb://diskstation.propellerhead.co.nz/development/Projects/Auckland%20Museum/Data/Images/20140804/Human%20History?fileName=53-i.tif&localWorkDirectory=/tmp";
		Exchange exchange = this.consumer.receive(endpointUri);
		System.out.println("exchange: " + exchange);
		Map<String, Object> headers = exchange.getIn().getHeaders();
		for (Map.Entry<String, Object> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
		System.out.println("body class: "
				+ exchange.getIn().getBody().getClass());

		System.out.println(" if SmbGenericFile instance: "
				+ (exchange.getIn().getBody() instanceof SmbGenericFile));

		SmbGenericFile file = exchange.getIn().getBody(SmbGenericFile.class);
		System.out.println("filename: " + file.getFileName());
		System.out.println("file body: " + file.getBody());
		System.out.println("file: " + file.getFile().getClass());

		SmbFile smbFile = (SmbFile) file.getFile();
		SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFile);
		// writeTempFileToClasspath(TEST_DOWNLOAD_FILE_NAME,
		// smbFileInputStream);

		 File temp = new File("/tmp/53-i.tif");
		 System.out.println("tmp file: " + temp.getAbsolutePath());
		 ByteArrayInputStream bis = new ByteArrayInputStream(
		 FileUtils.readFileToByteArray(temp));

		// ByteArrayInputStream bis = new ByteArrayInputStream(smbFile.getURL()
		// .getFile().getBytes());

		BufferedImage img = ImageIO.read(bis);
		final BufferedImage bufferedImage = Scalr.resize(img, Mode.AUTOMATIC,
				1024, 1024);
		ImageOutputStream output = ImageIO.createImageOutputStream(new File(
				"/tmp/testoutput.tif"));
		ImageIO.write(bufferedImage, "tif", output);
		
		bufferedImage.flush();
		output.flush();
		
		
//		Thread.sleep(5000);
		
	}

	// @Test
	// public void testWriteToRemoteFolder() throws Exception {
	// MockEndpoint mock = getMockEndpoint("mock:result");
	// URL url = JcifsIntegrationTest.class.getResource(TEST_UPLOAD_FILE_NAME);
	// File file = new File(url.getFile());
	// InputStream is = new FileInputStream(file);
	// producerTemplate.requestBodyAndHeader("direct:uploadStart", file,
	// "CamelFileName", "james.jpg");
	//
	// System.out.println("body : "
	// + mock.getExchanges().get(0).getIn().getBody());
	//
	// }
	//
	// @Override
	// protected RouteBuilder createRouteBuilder() throws Exception {
	// return new UploadFileRoute();
	// }
	//
	// private static class UploadFileRoute extends RouteBuilder {
	// @Override
	// public void configure() throws Exception {
	// from("direct:uploadStart")
	// .to("smb://diskstation.propellerhead.co.nz/development/Projects/Auckland%20Museum/Data/Images/Upload")
	// .to("mock:result");
	// }
	// }

	// 53-i.tif
	private static class MyFileFilter<T> implements GenericFileFilter<T> {
		private String fileName;

		public MyFileFilter(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public boolean accept(GenericFile<T> file) {
			return file.getFileName().equals(fileName);
		}
	}

	public static File writeTempFileToClasspath(String fileName,
			InputStream inputStream) {
		String classpath = JcifsIntegrationTest.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		File tempFile = null;
		tempFile = new File(classpath + fileName);
		if (!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				new RuntimeException("create file error", e);
			}
		}

		if (tempFile != null) {
			try {
				byte[] content = IOUtils.toByteArray(inputStream);
				FileUtils.writeByteArrayToFile(tempFile, content);
			} catch (IOException e) {
			}
		}
		return tempFile;
	}
}
