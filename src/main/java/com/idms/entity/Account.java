package com.idms.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer acctId;

    @NotBlank(message = "Contract sales price cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Invalid contract sales price")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal contractSalesPrice;

    @NotBlank(message = "Account type is required")
    @Size(max = 50, message = "Account type must not exceed 50 characters")
    @Column(length = 50, nullable = false)
    private String acctType;

    @PositiveOrZero(message = "Sales group person ID must be zero or positive")
    private Integer salesGroupPerson1Id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractDate;

    @NotBlank(message = "Collateral stock number is required")
    @Size(max = 50, message = "Collateral stock number must not exceed 50 characters")
    @Column(length = 50, nullable = false)
    private String collateralStockNumber;

    @Positive(message = "Collateral year model must be positive")
    @Column(nullable = false)
    private Integer collateralYearModel;

    @NotBlank(message = "Collateral make is required")
    @Size(max = 50, message = "Collateral make must not exceed 50 characters")
    @Column(length = 50, nullable = false)
    private String collateralMake;

    @NotBlank(message = "Collateral model is required")
    @Size(max = 50, message = "Collateral model must not exceed 50 characters")
    @Column(length = 50, nullable = false)
    private String collateralModel;

    @NotBlank(message = "Borrower first name is required")
    @Size(max = 100, message = "Borrower first name must not exceed 100 characters")
    @Column(length = 100, nullable = false)
    private String borrower1FirstName;

    @NotBlank(message = "Borrower last name is required")
    @Size(max = 100, message = "Borrower last name must not exceed 100 characters")
    @Column(length = 100, nullable = false)
    private String borrower1LastName;

    @NotBlank(message = "Balance cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Invalid balance")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;


    public Integer getAcctId() {
        return acctId;
    }

    public void setAcctId(Integer acctId) {
        this.acctId = acctId;
    }

    public BigDecimal getContractSalesPrice() {
        return contractSalesPrice;
    }

    public void setContractSalesPrice(BigDecimal contractSalesPrice) {
        this.contractSalesPrice = contractSalesPrice;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public Integer getSalesGroupPerson1Id() {
        return salesGroupPerson1Id;
    }

    public void setSalesGroupPerson1Id(Integer salesGroupPerson1Id) {
        this.salesGroupPerson1Id = salesGroupPerson1Id;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public String getCollateralStockNumber() {
        return collateralStockNumber;
    }

    public void setCollateralStockNumber(String collateralStockNumber) {
        this.collateralStockNumber = collateralStockNumber;
    }

    public Integer getCollateralYearModel() {
        return collateralYearModel;
    }

    public void setCollateralYearModel(Integer collateralYearModel) {
        this.collateralYearModel = collateralYearModel;
    }

    public String getCollateralMake() {
        return collateralMake;
    }

    public void setCollateralMake(String collateralMake) {
        this.collateralMake = collateralMake;
    }

    public String getCollateralModel() {
        return collateralModel;
    }

    public void setCollateralModel(String collateralModel) {
        this.collateralModel = collateralModel;
    }

    public String getBorrower1FirstName() {
        return borrower1FirstName;
    }

    public void setBorrower1FirstName(String borrower1FirstName) {
        this.borrower1FirstName = borrower1FirstName;
    }

    public String getBorrower1LastName() {
        return borrower1LastName;
    }

    public void setBorrower1LastName(String borrower1LastName) {
        this.borrower1LastName = borrower1LastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}