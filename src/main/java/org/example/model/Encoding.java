package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Encoding {

    public long encodingId;
    public String mediaType;
    public double fileSizeInMB;
}
