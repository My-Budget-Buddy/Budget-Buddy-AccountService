
Budget Buddy Account Service
============================

This is a Spring application that is meant to run in conjunction with all of the other Budget Buddy services. To build, download java 17 and maven, and run this command:

    mvn clean package -DskipTests

This service expects to have its own database running, which can be configured with the `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` environment variables. The service also communicates with the [transactions service](https://github.com/My-Budget-Buddy/Budget-Buddy-TransactionService). For that, you need the transactions service to also be running, as well as the [eureka discovery service](https://github.com/My-Budget-Buddy/Budget-Buddy-DiscoveryService), and possibly to configure the `EUREKA_URL` environment variable.

## Overview
The Account Service is a core component of the Budget Buddy personal finance application. This microservice is responsible for handling user accounts, providing basic CRUD operations to manage account data effectively.

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Usage](#usage)
6. [API Documentation](#api-documentation)
7. [Testing](#testing)

## Architecture
The Account Service is built with the following technologies:
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Testing:** JUnit
- **Communication:** RESTful APIs, using RESTClient for inter-service communication

## Installation

### Prerequisites
- JDK 17
- Maven
- PostgreSQL setup

### Steps
1. **Clone the repository:**
    ```sh
    git clone https://github.com/yourusername/accountservice.git
    cd accountservice
    ```

2. **Build the project:**
    ```sh
    mvn clean install
    ```

3. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```

## Configuration
Configure your database and other settings in the `application.yml` file located in `src/main/resources`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/yourdbname
    username: yourusername
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
  ```

## Usage
The Account Service provides endpoints for managing user accounts. You can access these endpoints using tools like Postman or CURL.

## API Documentation

### Endpoints

**Get Accounts by User ID**
- **URL:** `GET /accounts`
- **Description:** Retrieve accounts by the `userId`.
- **Response:**
  ```javascript
  [
    {
      "id": 1,
      "userId": "user123",
      "type": "CHECKING",
      "accountNumber": "12345678",
      "routingNumber": "87654321",
      "institution": "Bank A",
      "investmentRate": 0.01,
      "startingBalance": 1000.00,
      "currentBalance": 1050.00
    }
  ]
    ```
  
 #### Get Individual Account by ID
- **URL:** `GET /accounts/{accountId}`
- **Description:** Retrieve an account by the `accountId`.
- **Response:**
  ```javascript
  {
    "id": 1,
    "userId": "user123",
    "type": "CHECKING",
    "accountNumber": "12345678",
    "routingNumber": "87654321",
    "institution": "Bank A",
    "investmentRate": 0.01,
    "startingBalance": 1000.00,
    "currentBalance": 1050.00
  }
  ```

#### Create Account
- **URL**: `POST /transactions/user/{userId}/createTransaction`
- **Description**: Create a new transaction.
- **Request**:
    ```javascript
    {
      "userId": 1,
      "accountId": 101,
      "vendorName": "Apple",
      "amount": 1200.99,
      "category": "Shopping",
      "description": "Purchase of electronics",
      "date": "2024-05-01"
    }
    ```
- **Response:**
    ```javascript
    {
      "id": 1,
      "userId": "user123",
      "type": "CHECKING",
      "accountNumber": "12345678",
      "routingNumber": "87654321",
      "institution": "Bank A",
      "investmentRate": 0.01,
      "startingBalance": 1000.00,
      "currentBalance": 1000.00
    }
    ```
#### Update Account

- **URL:** `PUT /accounts/{accountId}`
- **Description:** Update an existing account.
- **Request:**
    ```javascript
    {
      "type": "SAVINGS",
      "accountNumber": "12345678",
      "routingNumber": "87654321",
      "institution": "Bank A",
      "investmentRate": 0.02,
      "startingBalance": 2000.00
    }
    ```
- **Response:** 200 OK

#### Delete Account
- **URL:** `DELETE /accounts/{accountId}`
- **Description:** Delete an account by its ID.
- **Response:** 204 NO CONTENT

#### Delete All Accounts
- **URL:** `DELETE /accounts`
- **Description:** Delete all accounts for a user.
- **Response:** 204 NO CONTENT

## Testing
To run the tests, use the following command:

```bash
mvn test
```
