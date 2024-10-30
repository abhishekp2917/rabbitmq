package org.example.constants;


public final class ExchangeType {

    private ExchangeType() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
    public static final String DIRECT = "direct";
    public static final String FANOUT = "fanout";
    public static final String HEADERS = "headers";
    public static final String TOPIC = "topic";
}
