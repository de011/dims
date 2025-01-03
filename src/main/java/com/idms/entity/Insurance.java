package com.idms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String policyNumber;
    private String effectiveDate;
    private String expireDate;
}
