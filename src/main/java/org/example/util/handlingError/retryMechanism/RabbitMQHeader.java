package org.example.util.handlingError.retryMechanism;

import lombok.*;

import java.util.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQHeader {

    private static final String KEYWORD_QUEUE_WAIT = "wait";
    private List<RabbitMQHeaderXDeath> xDeaths = new ArrayList<>(2);
    private String xFirstDeathExchange = "";
    private String xFirstDeathQueue = "";
    private String xFirstDeathReason = "";


    public RabbitMQHeader(Map<String, Object> headers) {
        if (headers != null) {
            var xFirstDeathExchange = Optional.ofNullable(headers.get("x-first-death-exchange"));
            var xFirstDeathQueue = Optional.ofNullable(headers.get("x-first-death-queue"));
            var xFirstDeathReason = Optional.ofNullable(headers.get("x-first-death-reason"));
            xFirstDeathExchange.ifPresent(s -> this.setXFirstDeathExchange(s.toString()));
            xFirstDeathQueue.ifPresent(s -> this.setXFirstDeathQueue(s.toString()));
            xFirstDeathReason.ifPresent(s -> this.setXFirstDeathReason(s.toString()));
            var xDeathHeaders = (List<Map<String, Object>>) headers.get("x-death");
            if (xDeathHeaders != null) {
                for (Map<String, Object> x : xDeathHeaders) {
                    RabbitMQHeaderXDeath hdrDeath = new RabbitMQHeaderXDeath();
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
                    xDeaths.add(hdrDeath);
                }
            }
        }
    }

    public int getFailedRetryCount() {
        for (var xDeath : xDeaths) {
            if (xDeath.getExchange().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)
                    && xDeath.getQueue().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)) {
                return xDeath.getCount();
            }
        }
        return 0;
    }
}
