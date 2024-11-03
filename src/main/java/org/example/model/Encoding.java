package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Encoding {

    private long encodingId;
    private String mediaType;
    private double fileSizeInMB;
}
