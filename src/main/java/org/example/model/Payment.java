package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Payment {

    private long paymentId;
    private long payeeId;
    private long receiverId;
    private String paymentMethod;
    private double amount;
}
