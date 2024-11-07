package org.example.util.handlingError.retryMechanism;

import lombok.*;
import java.util.*;

/**
 * Represents headers in RabbitMQ messages that contain metadata about message failures,
 * such as the exchange, queue, and reason for the first death, and a list of deaths (x-death).
 *
 * This class is designed to capture and parse message header details when a message fails
 * in RabbitMQ, making it easier to implement retry and error-handling mechanisms.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQHeader {

    // Keyword used to identify if a failed message pertains to a retry queue.
    private static final String KEYWORD_QUEUE_WAIT = "wait";

    // List of headers representing the death (failure) history of a message.
    private List<RabbitMQHeaderXDeath> xDeaths = new ArrayList<>(2);

    // Metadata fields capturing details about the first message failure.
    private String xFirstDeathExchange = "";
    private String xFirstDeathQueue = "";
    private String xFirstDeathReason = "";

    /**
     * Constructor that initializes the RabbitMQHeader object based on the provided headers map.
     * Extracts and maps relevant header data to the object's fields.
     *
     * @param headers A map representing the headers of a RabbitMQ message.
     */
    public RabbitMQHeader(Map<String, Object> headers) {
        if (headers != null) {
            // Extract and set the first death exchange, queue, and reason if present.
            var xFirstDeathExchange = Optional.ofNullable(headers.get("x-first-death-exchange"));
            var xFirstDeathQueue = Optional.ofNullable(headers.get("x-first-death-queue"));
            var xFirstDeathReason = Optional.ofNullable(headers.get("x-first-death-reason"));

            xFirstDeathExchange.ifPresent(s -> this.setXFirstDeathExchange(s.toString()));
            xFirstDeathQueue.ifPresent(s -> this.setXFirstDeathQueue(s.toString()));
            xFirstDeathReason.ifPresent(s -> this.setXFirstDeathReason(s.toString()));

            // Extract and parse the x-death header, which contains detailed failure history.
            var xDeathHeaders = (List<Map<String, Object>>) headers.get("x-death");
            if (xDeathHeaders != null) {
                for (Map<String, Object> x : xDeathHeaders) {
                    RabbitMQHeaderXDeath hdrDeath = new RabbitMQHeaderXDeath();

                    // Extract individual details from the x-death entry and map them to the RabbitMQHeaderXDeath object.
                    var reason = Optional.ofNullable(x.get("reason"));
                    var count = Optional.ofNullable(x.get("count"));
                    var exchange = Optional.ofNullable(x.get("exchange"));
                    var queue = Optional.ofNullable(x.get("queue"));
                    var routingKeys = Optional.ofNullable(x.get("routing-keys"));
                    var time = Optional.ofNullable(x.get("time"));

                    reason.ifPresent(s -> hdrDeath.setReason(s.toString()));
                    count.ifPresent(s -> hdrDeath.setCount(Integer.parseInt(s.toString())));
                    exchange.ifPresent(s -> hdrDeath.setExchange(s.toString()));
                    queue.ifPresent(s -> hdrDeath.setQueue(s.toString()));
                    routingKeys.ifPresent(r -> {
                        var listR = (List<String>) r;
                        hdrDeath.setRoutingKeys(listR);
                    });
                    time.ifPresent(d -> hdrDeath.setTime((Date) d));

                    // Add the parsed x-death object to the list.
                    xDeaths.add(hdrDeath);
                }
            }
        }
    }

    /**
     * Retrieves the number of failed retries that occurred in a queue matching the wait criteria.
     * This method helps track retry attempts and identify whether the message has been through
     * a retry queue.
     *
     * @return The retry count if the x-death entry matches the "wait" queue keyword, or 0 if not found.
     */
    public int getFailedRetryCount() {
        for (var xDeath : xDeaths) {
            if (xDeath.getExchange().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)
                    && xDeath.getQueue().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)) {
                return xDeath.getCount(); // Return the retry count if matched.
            }
        }
        return 0; // Return 0 if no matching x-death entry is found.
    }
}
