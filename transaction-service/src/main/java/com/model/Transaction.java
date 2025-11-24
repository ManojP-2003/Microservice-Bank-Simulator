package com.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String transactionId;
    private String type;  // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    private Date timestamp;
    private String status;  // SUCCESS, FAILED
    private String sourceAccount;
    private String destinationAccount;

    public Transaction() {
    }

    public Transaction(String id, String transactionId, String type,
                       double amount, Date timestamp, String status,
                       String sourceAccount, String destinationAccount) {
        this.id = id;
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }


    public String getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
}
