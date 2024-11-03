package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Audio {

    private long audioId;
    private String audioType;
    private double audioSizeInMB;
}
