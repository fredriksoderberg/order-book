# Order book

### Build and run

#### Docker

Requires Java 17

`mvn clean package -DskipTests`

`docker-compose up -d`

#### Tests

`mvn clean test`

### Usage

The application will accept these http requests to port `8080`

#### GET /order/{id}

Response status:`200 OK`, `404 Not found`

Example: `curl -v "localhost:8080/order/1"`


#### POST /order 

Response status: `200 OK`, `400 Bad request`

Example: `curl -v -H 'Content-Type: application/json' "localhost:8080/order" -X POST -d '{"ticker": "SAVE", "volume": 1, "price": 200.5, "currency": "SEK", "transactionType": "BUY"}'` 

#### GET /order_summary/{ticker}

Response status:`200 OK`, `400 Bad request`

Example: `curl -v "localhost:8080/SAVE?date=2022-04-23"`




