package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Payment {

    public long paymentId;
    public long payeeId;
    public long receiverId;
    public String paymentMethod;
    public double amount;
}
