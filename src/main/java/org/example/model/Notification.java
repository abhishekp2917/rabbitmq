package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notification {

    public long notificationId;
    public long userId;
    public String description;
}
