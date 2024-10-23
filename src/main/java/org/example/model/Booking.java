package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Booking {

    public long bookingId;
    public long userId;
    public String bookingType;
    public double price;
}
