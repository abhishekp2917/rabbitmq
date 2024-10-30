package org.example.constants;


public final class ConsumerAcknowledgementMode {

    private ConsumerAcknowledgementMode() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
    public static final String AUTO = "AUTO";
    public static final String MANUAL = "MANUAL";
    public static final String NONE = "NONE";
}
