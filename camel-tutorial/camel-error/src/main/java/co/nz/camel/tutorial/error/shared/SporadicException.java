package co.nz.camel.tutorial.error.shared;

@SuppressWarnings("serial")
public class SporadicException extends Exception {
    public SporadicException() {
        super();
    }

    public SporadicException(String message) {
        super(message);
    }
}
