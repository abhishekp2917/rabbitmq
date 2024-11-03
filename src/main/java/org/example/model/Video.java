package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Video {

    private long videoId;
    private String videoType;
    private double videoSizeInMB;
    private int priority;
}
