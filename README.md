# TradeSphere

TradeSphere is a Spring Boot-based backend application for cryptocurrency trading and portfolio management. It provides RESTful APIs for user authentication, coin data retrieval, watchlist management, and payment details handling.

---

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Coin APIs](#coin-apis)
  - [Watchlist APIs](#watchlist-apis)
  - [Payment Details APIs](#payment-details-apis)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- User registration and login with JWT authentication
- Two-factor authentication (2FA) support
- Cryptocurrency data (list, search, details, charts, top coins)
- User-specific watchlist management
- Payment details management

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgresQL or compatible database

### Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/abheeshtsingh2803/TradeSphere.git
   cd TradeSphere
   ```

2. **Configure the database:**
   - Edit `src/main/resources/application.properties` with your DB credentials.

3. **Build and run:**
   ```sh
   ./mvnw spring-boot:run
   ```

---

## API Endpoints

### Authentication

#### Register

- **POST** `/auth/signup`
- **Request Body:**
  ```json
  {
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "yourpassword",
    "mobile": "1234567890"
  }
  ```
- **Response:** [`AuthResponse`](src/main/java/com/example/response/AuthResponse.java)

#### Login

- **POST** `/auth/signin`
- **Request Body:**
  ```json
  {
    "email": "john@example.com",
    "password": "yourpassword"
  }
  ```
- **Response:** [`AuthResponse`](src/main/java/com/example/response/AuthResponse.java)
- **Notes:** If 2FA is enabled, you will receive a session and must verify OTP.

---

### Coin APIs

#### Get Coin List

- **GET** `/coins?page={page}`
- **Response:** List of coins

#### Get Coin Chart

- **GET** `/coins/{coinId}/chart?days={days}`
- **Response:** Market chart data for the coin

#### Search Coin

- **GET** `/coins/search?q={keyword}`
- **Response:** Search results

#### Get Top 50 Coins by Market Cap

- **GET** `/coins/top50`
- **Response:** Top 50 coins by market cap

#### Get Trading Coins

- **GET** `/coins/trading`
- **Response:** List of currently trading coins

#### Get Coin Details

- **GET** `/coins/details/{coinId}`
- **Response:** Detailed coin information

---

### Watchlist APIs

> All endpoints require `Authorization: Bearer <jwt>` header.

#### Get User Watchlist

- **GET** `/api/watchlist/user`
- **Response:** User's watchlist

#### Add Coin to Watchlist

- **PATCH** `/api/watchlist/add/coin/{coinId}`
- **Response:** The added [`Coin`](src/main/java/com/example/model/Coin.java)

---

### Payment Details APIs

> All endpoints require `Authorization: Bearer <jwt>` header.

#### Add Payment Details

- **POST** `/api/payment-details`
- **Request Body:**
  ```json
  {
    "accountNumber": "1234567890",
    "accountHolderName": "John Doe",
    "ifscString": "IFSC0001",
    "bankName": "Bank Name"
  }
  ```
- **Response:** [`PaymentDetails`](src/main/java/com/example/model/PaymentDetails.java)

#### Get User's Payment Details

- **GET** `/api/payment-details`
- **Response:** [`PaymentDetails`](src/main/java/com/example/model/PaymentDetails.java)

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Create a new Pull Request

---

## License

This project is licensed under the MIT License.

---

## References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
