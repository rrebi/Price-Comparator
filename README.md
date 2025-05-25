# Price-Comparator
Java backend for a **Price Comparator Market** — track grocery prices across multiple supermarket chains.

Features:
- View **discounts**
- View **price history**
- Find **cheaper substitutes**
- Register **price alerts**
- Evaluate **basket savings**

---

## Project Structure

The code is organized in:
```
price-comparator/
├── controller/      # REST API endpoints
├── model/           # Data models (Product, PriceEntry, Discount, DTOs)
├── repository/      # CSV-based repositories (price and discount readers)
├── service/         # Core business logic and processing
```

---
## Build & Run Instructions

**Requirements:**
- JDK 17+ (JDK 21)
- Gradle
- IDE (VSCode, IntelliJ, etc.)

**Steps:**
```bash
git clone https://github.com/your-username/price-comparator.git
cd price-comparator
./gradlew bootRun
```

Server starts on:  
 `http://localhost:8080`

---

## API Endpoints & Usage

### CSV Format
Data files should follow this pattern:

- Prices: `store_YYYY-MM-DD.csv`
- Discounts: `store_discount_YYYY-MM-DD.csv`

You can use the included mock data or create your own.

---

### View Best Discounts
```http
GET /products/best-discounts
```

### View New Discounts
```http
GET /products/new-discounts
```

### Product Price History
```http
GET /products/{productId}/history?store=lidl
```

###  Filtered Price History (by Store, Brand, Category)
```http
GET /products/history?store=lidl&brand=Zuzu&category=lactate
```

### Find Substitutes for a Product
```http
GET /products/{productId}/substitutes?store=lidl
```

### Register a Price Alert
```http
POST /products/alerts
```

**Request Body:**
```json
{
  "productId": "P001",
  "targetPrice": 7.5,
  "store": "lidl"
}
```

### Check Triggered Alerts
```http
GET /products/alerts/matches
```

### Delete a Price Alert
```http
DELETE /products/alerts/P001
```

### Evaluate Basket Cost
```http
POST /products/basket?store=lidl
```

**Request Body:**
```json
["P001", "P002"]
```

**Returns:**
- Best price (with discount) per product
- Total cost

---
