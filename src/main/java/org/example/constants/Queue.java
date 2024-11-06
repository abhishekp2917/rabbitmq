package org.example.constants;

public final class Queue {

    private Queue() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

    public static final String Q_AUDIO_ENCODING = "q.audio.encoding";
    public static final String Q_AUDIO_COMPRESSION = "q.audio.compression";
    public static final String Q_BOOKING_EVENTS = "q.booking.events";
    public static final String Q_BOOKING_JOURNEY = "q.booking.journey";
    public static final String Q_CLAIM_ESCALATED = "q.claim.escalated";
    public static final String Q_CLAIM_NORMAL = "q.claim.normal";
    public static final String Q_ENCODING_VIDEO = "q.encoding.video";
    public static final String Q_ENCODING_VIDEO_DLX = "q.encoding.video.dlx";
    public static final String Q_MAIL_1 = "q.mail.1";
    public static final String Q_MAIL_2 = "q.mail.2";
    public static final String Q_MEDIA_UPLOAD_AWS = "q.media.upload.aws";
    public static final String Q_MEDIA_UPLOAD_GCP = "q.media.upload.gcp";
    public static final String Q_MEDIA_UPLOAD_AZURE = "q.payment.creditCard.azure";
    public static final String Q_MESSAGE_JSON = "q.message.json";
    public static final String Q_MESSAGE_TEXT = "q.message.text";
    public static final String Q_NOTIFICATION_OTP = "q.notification.otp";
    public static final String Q_NOTIFICATION_PAYMENT = "q.notification.payment";
    public static final String Q_NOTIFICATION_SUBSCRIBER = "q.notification.subscriber";
    public static final String Q_ORDER_NOTIFICATION = "q.order.notification";
    public static final String Q_ORDER_PAYMENT = "q.order.payment";
    public static final String Q_PAYMENT_CREDIT_CARD = "q.payment.creditCard";
    public static final String Q_PAYMENT_CREDIT_CARD_DEAD = "q.payment.creditCard.dead";
    public static final String Q_PAYMENT_CREDIT_CARD_WAIT = "q.payment.creditCard.wait";
    public static final String Q_TRANSACTION_PURCHASE = "q.transaction.purchase";
    public static final String Q_VIDEO_ENCODING_720P = "q.video.encoding.720p";
    public static final String Q_PRIORITY_MESSAGE = "q.priority.message";
}
