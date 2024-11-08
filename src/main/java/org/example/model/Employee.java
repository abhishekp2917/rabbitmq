package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Employee {

    private long employeeId;
    private String firstName;
    private String lastName;
    private LocalDateTime dob;
}
