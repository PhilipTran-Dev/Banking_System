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
                                           +----------------------------------+
                                           |               CARD               |
                                           +----------------------------------+
                                           | cardId: VARCHAR (PK)             |
                                           | cardNumber: VARCHAR (UNIQUE)     |
                                           | balance: VARCHAR                 |
                                           | billingAddress: VARCHAR          |
                                           | cvv: INTEGER                     |
                                           | cardHolder: VARCHAR              |
                                           | Pin: INTEGER                     |
      +--------------------------------+   | issuedAt: DATE                   |
      |              USER              |---| createdAt: DATE                  |
      +--------------------------------+   | updatedAt: DATE                  |
      | uid: VARCHAR (PK)              |   +----------------------------------+
      | firstname: VARCHAR             |   | getters() and setters()          |
      | lastname: VARCHAR              |   +----------------------------------+
      | password: VARCHAR              |
      | email: VARCHAR (UNIQUE)        |
      | tag: VARCHAR (UNIQUE)          |
      | gender: VARCHAR                |
      | dob: DATE                      |
      | tel: INTEGER                   |
      | createdAt: DATE                |
      | updatedAt: DATE                |
      +--------------------------------+
      | getters() and setters()        |
      +--------------------------------+
          |            |
          |            |
          |            v
          |   +----------------------------------+
          |   |             ACCOUNT              |
          |   +----------------------------------+
          |   | accountId: VARCHAR (PK)          |
          |   | currency: VARCHAR                |
          |   | symbol: VARCHAR                  |
          |   | code: VARCHAR                    |
          |   | accountName: VARCHAR             |
          |   | accountNumber: VARCHAR (UNIQUE)  |
          |   | balance: INTEGER                 |
          |   | label: VARCHAR                   |
          |   | createdAt: DATE                  |
          |   | updatedAt: DATE                  |
          |   +----------------------------------+
          |   | getters() and setters()          |
          |   +----------------------------------+
          |            ^
          |            |
          v            |
+----------------------------------+
|          TRANSACTIONS            |
+----------------------------------+
| txId: VARCHAR (PK)               |
| txFee: VARCHAR                   |
| description: VARCHAR             |
| sender: VARCHAR                  |
| receiver: VARCHAR                |
| status: VARCHAR                  |
| type: VARCHAR                    |
| createdAt: DATE                  |
| updatedAt: DATE                  |
+----------------------------------+
| getters() and setters()          |
+----------------------------------+

