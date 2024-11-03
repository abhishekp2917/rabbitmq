package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FNOLClaim {

    private long claimId;
    private String policyNumber;
    private String claimType;
    private String damageDescription;
    private Boolean damage;
    private Boolean livable;
    private Boolean fatal;
}

