#  Microservice Bank Simulator – Full Project Documentation

A complete banking system built using **Spring Boot Microservices**, **Eureka Discovery Server**, **Spring Cloud Gateway**, **Feign Client**, and **MongoDB**, fully containerized using **Docker Compose**.

This project demonstrates:

* Microservice architecture
* Service discovery
* Inter-service communication
* Gateway routing
* Circuit breaker 
* Logging & Exception Handling
* Independent databases per service
* Postman testing

---

#  Project Architecture

```
                         +---------------------------+
                         |      Eureka Server        |
                         |     (Service Registry)    |
                         +-------------+-------------+
                                       |
                                       |
  +---------------------+      +-------▼--------+      +------------------------+
  | Account Service     |<---->|   API Gateway  |<---->|      Client (Postman) |
  | Port: 8081          |      | Port: 8080     |      +------------------------+
  | MongoDB (accounts)  |      | URL routing    |
  +---------------------+      +-------+--------+
                                       |
   +---------------------+      +------+-------+      +----------------------+
   | Transaction Service |<---->| Feign Client |----->| Notification Service |
   | Port: 8082          |      +--------------+      | Port: 8083           |
   | MongoDB (txn logs)  |                          | MongoDB (notifications)|
   +---------------------+                          +------------------------+
```

---

#  Microservices Overview

## 1️⃣ Eureka Discovery Server (Port: 8761)

* Central registry where all services register themselves.
* API Gateway uses Eureka to discover services dynamically.
* Supports load balancing.

---

## 2️⃣ API Gateway (Port: 8080)

Handles all external requests and forwards them to appropriate services.

### **Routes**

| Service              | Gateway Route           | Internal Route                              |
| -------------------- | ----------------------- | ------------------------------------------- |
| Account Service      | `/api/accounts/**`      | `ACCOUNT-SERVICE/accounts/**`               |
| Transaction Service  | `/api/transactions/**`  | `TRANSACTION-SERVICE/api/transactions/**`   |
| Notification Service | `/api/notifications/**` | `NOTIFICATION-SERVICE/api/notifications/**` |

---

## 3️⃣ Account Service (Port: 8081)

Handles all bank account operations:

✔ Create account
✔ Get account
✔ Update balance
✔ Update status
✔ Delete account

Uses MongoDB for persistence.

---

## 4️⃣ Transaction Service (Port: 8082)

Handles:

✔ Deposit
✔ Withdraw
✔ Transfer (with Feign call to Account Service)
✔ Transaction history

Uses Feign client:

```java
@FeignClient(name = "account-service")
```

Also triggers Notification service.

---

## 5️⃣ Notification Service** (Port: 8083)

Simple service that receives notification messages from transactions.

---

# 🗄 Databases

| Service              | DB name            | Stores               |
| -------------------- | ------------------ | -------------------- |
| Account Service      | `accounts_db`      | account documents    |
| Transaction Service  | `transactions_db`  | all transaction logs |
| Notification Service | `notifications_db` | notification logs    |

All databases are independent for microservice isolation.

---

#  Docker Setup

## Start project:

```
docker compose up -d
```

## Stop project:

```
docker compose down
```

## Rebuild after code changes:

```
mvn clean package
docker compose build
docker compose up -d
```

---

#  POSTMAN API Documentation

##  Account Service APIs (through API Gateway – port 8080)

---

### 1. Create Account

POST
`http://localhost:8080/api/accounts`

Body

```json
{
  "holderName": "Virat",
  "balance": 5000
}
```

---

### 2. Get Account

GET
`http://localhost:8080/api/accounts/{accountNumber}`

---

### 3. Update Balance

PUT
`http://localhost:8080/api/accounts/{accountNumber}/balance?amount=1000`

`amount` can be positive or negative.

---

### 4. Update Status

**PUT**
`http://localhost:8080/api/accounts/{accountNumber}/status?status=INACTIVE`

---

### 5. Delete Account

**DELETE**
`http://localhost:8080/api/accounts/{accountNumber}`

---

##  Transaction Service APIs (Gateway – 8080)

---

### 1. Deposit

POST
`http://localhost:8080/api/transactions/deposit?accountNumber=XYZ123&amount=500`

---

### 2. Withdraw

**POST**
`http://localhost:8080/api/transactions/withdraw?accountNumber=XYZ123&amount=500`

---

### 3. Transfer

**POST**
`http://localhost:8080/api/transactions/transfer?from=ACC1&to=ACC2&amount=500`

Triggers:
✔ Debit from sender
✔ Credit to receiver
✔ Notification service call

---

### 4. Get transaction history

GET
`http://localhost:8080/api/transactions/account/{accountNumber}`

---

##  Notification Service

### Send notification

POST
`http://localhost:8080/api/notifications/send`

Body

```json
{
  "message": "Transfer Successful"
}
```

---

#  Exception Handling Implemented

| Exception                     | Meaning                               |
| ----------------------------- | ------------------------------------- |
| AccountNotFoundException      | Invalid account                       |
| AccountAlreadyExistsException | Duplicate account number              |
| InvalidAccountDataException   | Bad request                           |
| InsufficientBalanceException  | Not enough funds                      |
| AccountInactiveException      | Cannot operate inactive account       |
| GlobalExceptionHandler        | Handles all errors with JSON response |

---

#  Response Example (Error Handling

```json
{
  "timestamp": "2025-12-01T14:47:00",
  "status": 404,
  "error": "NOT_FOUND",
  "errorCode": "ACCOUNT_NOT_FOUND",
  "message": "Account MAN1234 not found"
}
```

---

#  Testing Checklist

### ✔ Start Docker

### ✔ Open Eureka Dashboard

`http://localhost:8761`

You must see:

* ACCOUNT-SERVICE – UP
* TRANSACTION-SERVICE – UP
* NOTIFICATION-SERVICE – UP
* API-GATEWAY – UP

### ✔ Test all Postman APIs

### ✔ Verify database entries in MongoDB Compass

---

#  Tech Stack

* Spring Boot 3+
* Spring Cloud Netflix Eureka
* Spring Cloud Gateway
* Spring Cloud OpenFeign
* MongoDB + Spring Data
* Docker + Docker Compose
* SLF4J Logging
* Exception Handling Layer

---

#  Key Features (as required in PDF

✔ Microservice based banking system
✔ Independent persistence for each service
✔ Auto account number generation (3 initials + 4 random numbers)
✔ Inter-service communication using Feign
✔ Distributed system with Service Discovery
✔ API Gateway routing
✔ Logging & exception handling
✔ Containerized deployment
✔ REST API tested fully with Postman

---

#  How to Run the Project

```
git clone https://github.com/ManojP-2003/Microservice-Bank-Simulator
cd Microservice-Bank-Simulator
docker compose up -d
```

Done — system is ready 

---


