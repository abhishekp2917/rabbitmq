package org.example.constants;


public final class QueueType {

    private QueueType() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
    public static final String CLASSIC = "classic";
    public static final String QUORUM = "quorum";
    public static final String STREAM = "stream";
}
