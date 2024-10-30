package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Media {

    public long mediaId;
    public String mediaType;
    public double fileSizeInMB;
    public String mediaPath;
}
