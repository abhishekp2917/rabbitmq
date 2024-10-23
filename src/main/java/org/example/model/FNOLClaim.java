package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FNOLClaim {

    public long claimId;
    public String policyNumber;
    public String claimType;
    public String damageDescription;
    public Boolean damage;
    public Boolean livable;
    public Boolean fatal;
}

