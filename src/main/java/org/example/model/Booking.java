package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Booking {

    private long bookingId;
    private long userId;
    private String bookingType;
    private double price;
}
