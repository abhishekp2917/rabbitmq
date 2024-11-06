package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mail {

    private long mailId;
    private String from;
    private String to;
    private String subject;
    private String body;
}
