package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Media {

    private long mediaId;
    private String mediaType;
    private double fileSizeInMB;
    private String mediaPath;
}
