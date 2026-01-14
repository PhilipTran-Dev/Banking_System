# Spring Boot Backend API

Backend RESTful API built with Spring Boot.  
The project is currently in the initial phase with entities and repositories implemented.

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Lombok
- Maven

---
## 🏗️ Current Status
Project initialization  
Database entities defined  
JPA repositories implemented  


---
##Database

classDiagram
    class USER {
        +VARCHAR uid (PK)
        +VARCHAR firstname
        +VARCHAR lastname
        +VARCHAR password
        +VARCHAR email (UNIQUE)
        +VARCHAR tag (UNIQUE)
        +VARCHAR gender
        +DATE dob
        +INTEGER tel
        +DATE createdAt
        +DATE updatedAt
        +getters()
        +setters()
    }

    class CARD {
        +VARCHAR cardId (PK)
        +VARCHAR cardNumber (UNIQUE)
        +VARCHAR balance
        +VARCHAR billingAddress
        +INTEGER cvv
        +VARCHAR cardHolder
        +INTEGER Pin
        +DATE issuedAt
        +DATE createdAt
        +DATE updatedAt
        +getters()
        +setters()
    }

    class ACCOUNT {
        +VARCHAR accountId (PK)
        +VARCHAR currency
        +VARCHAR symbol
        +VARCHAR code
        +VARCHAR accountName
        +VARCHAR accountNumber (UNIQUE)
        +INTEGER balance
        +VARCHAR label
        +DATE createdAt
        +DATE updatedAt
        +getters()
        +setters()
    }

    class TRANSACTIONS {
        +VARCHAR txId (PK)
        +VARCHAR txFee
        +VARCHAR description
        +VARCHAR sender
        +VARCHAR receiver
        +VARCHAR status
        +VARCHAR type
        +DATE createdAt
        +DATE updatedAt
        +getters()
        +setters()
    }

    USER -- CARD : has
    USER -- ACCOUNT : owns
    USER -- TRANSACTIONS : initiates
    TRANSACTIONS -- ACCOUNT : impacts
