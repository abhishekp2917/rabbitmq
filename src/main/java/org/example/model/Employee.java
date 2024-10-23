package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Employee {

    public long employeeId;
    public String firstName;
    public String lastName;
    public LocalDateTime dob;
}
