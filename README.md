# simple-stock-management-original

This is a Java Spring Boot application for managing inventories, items, and orders. The application provides RESTful endpoints to perform CRUD operations on these entities.

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- A running instance of H2 DB

## Getting Started

1. **Clone the repository**
    ```bash
    git clone https://github.com/mohripan/simple-stock-management-original.git
    cd simple-stock-management-original
    ```

2. **Configure the database**
    - Update the `application.properties` file with your database configurations:
        ```properties
        spring.datasource.url=jdbc:h2:mem:stockdb
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=
        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
        spring.h2.console.enabled=true
        spring.jpa.hibernate.ddl-auto=update
        spring.h2.console.path=/h2-console
        spring.application.name=simple-stock-management
        ```

3. **Build the project**
    ```bash
    mvn clean install
    ```

4. **Run the application**
    ```bash
    mvn spring-boot:run
    ```

The application will start on `http://localhost:8080`.

## API Endpoints

### Items

#### Get a list of items
- **URL:** `[GET] /items`
- **Parameters:**
    - `page` (optional, default: 1): Page number
    - `size` (optional, default: 10): Number of items per page
    - `name` (optional): Filter by item name
    - `minPrice` (optional): Filter by minimum price
    - `maxPrice` (optional): Filter by maximum price
    - `includeStock` (optional): Include stock information
    - `includeOrderHistory` (optional): Include order history
- **Example:**
    ```http
    [GET] http://localhost:8080/items?page=1&size=10&minPrice=1&maxPrice=20&includeStock=true
    ```

#### Get item by ID
- **URL:** `[GET] /items/{id}`
- **Parameters:**
    - `includeStock` (optional): Include stock information
    - `includeOrderHistory` (optional): Include order history
- **Example:**
    ```http
    [GET] http://localhost:8080/items/1?includeStock=true&includeOrderHistory=true
    ```

#### Create a new item
- **URL:** `[POST] /items`
- **Body:**
    ```json
    {
        "name": "Notebook",
        "price": 8.0
    }
    ```

#### Update an item
- **URL:** `[PUT] /items/{id}`
- **Body:**
    ```json
    {
        "name": "Updated Pen",
        "price": 6.0
    }
    ```

#### Delete an item
- **URL:** `[DELETE] /items/{id}`
- **Example:**
    ```http
    [DELETE] http://localhost:8080/items/1
    ```

### Inventories

#### Get a list of inventories
- **URL:** `[GET] /inventories`
- **Parameters:**
    - `page` (optional, default: 1): Page number
    - `size` (optional, default: 10): Number of inventories per page
- **Example:**
    ```http
    [GET] http://localhost:8080/inventories?page=1&size=10
    ```

#### Get inventory by item ID
- **URL:** `[GET] /inventories/{itemId}`
- **Example:**
    ```http
    [GET] http://localhost:8080/inventories/1
    ```

#### Create a new inventory
- **URL:** `[POST] /inventories`
- **Body:**
    ```json
    {
        "itemId": 1,
        "qty": 20,
        "type": "T"
    }
    ```

#### Update an inventory
- **URL:** `[PUT] /inventories`
- **Body:**
    ```json
    {
        "itemId": 1,
        "qty": 25,
        "type": "T"
    }
    ```

#### Delete an inventory
- **URL:** `[DELETE] /inventories/{itemId}`
- **Example:**
    ```http
    [DELETE] http://localhost:8080/inventories/1
    ```

### Orders

#### Get a list of orders
- **URL:** `[GET] /orders`
- **Parameters:**
    - `page` (optional, default: 1): Page number
    - `size` (optional, default: 10): Number of orders per page
    - `itemId` (optional): Filter by item ID
- **Example:**
    ```http
    [GET] http://localhost:8080/orders?page=1&size=10
    ```

#### Get order details by order number
- **URL:** `[GET] /orders/{orderNo}`
- **Example:**
    ```http
    [GET] http://localhost:8080/orders/O1
    ```

#### Create a new order
- **URL:** `[POST] /orders`
- **Body:**
    ```json
    {
        "itemId": 1,
        "qty": 10
    }
    ```

#### Update an order
- **URL:** `[PUT] /orders/{orderNo}`
- **Body:**
    ```json
    {
        "qty": 5
    }
    ```

#### Delete an order
- **URL:** `[DELETE] /orders/{orderNo}`
- **Example:**
    ```http
    [DELETE] http://localhost:8080/orders/O1
    ```