package co.nz.camel.tutorial.error.shared;

@SuppressWarnings("serial")
public class FlakyException extends Exception {
	public FlakyException() {
		super();
	}

	public FlakyException(String message) {
		super(message);
	}
}
