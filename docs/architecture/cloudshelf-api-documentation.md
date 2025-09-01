# 🔌 API Documentation & Strategy

> Comprehensive API design, documentation, and management strategy for CloudShelf online bookstore ensuring scalable, secure, and developer-friendly interfaces

This document defines the API architecture, design patterns, versioning strategy, and developer experience for CloudShelf's RESTful APIs and real-time interfaces.

---

## 🌐 API Architecture Overview

CloudShelf implements a comprehensive API-first architecture with multiple interface types:

- **🔗 RESTful APIs** - Core CRUD operations and business logic
- **📡 GraphQL Interface** - Flexible query capabilities for complex data retrieval
- **⚡ WebSocket Connections** - Real-time updates and live features
- **🔄 Event-Driven APIs** - Asynchronous processing and notifications
- **📋 Admin APIs** - Management and administrative operations

---

## 📷 Setup Screenshots

### **📋 API Gateway Configuration**

![API Gateway Setup and Configuration](screenshots/01-api-gateway-configuration.png)

### **📖 OpenAPI Documentation**

![Swagger/OpenAPI Documentation Interface](screenshots/02-openapi-documentation.png)

### **🔑 API Authentication Setup**

![API Authentication and Authorization](screenshots/03-api-authentication-setup.png)

### **📊 API Analytics Dashboard**

![API Usage Analytics and Monitoring](screenshots/04-api-analytics-dashboard.png)

### **🔄 API Versioning Strategy**

![API Version Management](screenshots/05-api-versioning-strategy.png)

### **⚡ Real-time API Testing**

![WebSocket and Real-time Testing](screenshots/06-realtime-api-testing.png)

---

## 🏗️ RESTful API Design

### **📚 Book Catalog API**

**OpenAPI Specification**:

```yaml
openapi: 3.0.3
info:
  title: CloudShelf Book Catalog API
  version: 2.1.0
  description: |
    RESTful API for managing and browsing the CloudShelf book catalog.

    ## Features
    - Full-text search with filters
    - Category browsing and hierarchy
    - Book details and metadata
    - Reviews and ratings
    - Inventory management

    ## Authentication
    - Public endpoints for browsing
    - JWT authentication for user-specific features
    - API key authentication for admin operations

  contact:
    name: CloudShelf API Team
    email: api-support@cloudshelf.com
    url: https://api.cloudshelf.com/docs
  license:
    name: MIT
    url: https://opensource.org/licenses/MIT

servers:
  - url: https://api.cloudshelf.com/v2
    description: Production server
  - url: https://staging-api.cloudshelf.com/v2
    description: Staging server
  - url: https://dev-api.cloudshelf.com/v2
    description: Development server

security:
  - BearerAuth: []
  - ApiKeyAuth: []
  - {}

paths:
  /books:
    get:
      summary: Search and list books
      description: |
        Retrieve books with advanced search and filtering capabilities.
        Supports full-text search, category filtering, price ranges, and sorting.
      operationId: searchBooks
      tags:
        - Books
      parameters:
        - name: q
          in: query
          description: Search query (title, author, ISBN, keywords)
          schema:
            type: string
            maxLength: 200
          example: "javascript programming"
        - name: category
          in: query
          description: Filter by category ID
          schema:
            type: integer
            minimum: 1
          example: 42
        - name: author
          in: query
          description: Filter by author name
          schema:
            type: string
            maxLength: 100
          example: "Douglas Crockford"
        - name: price_min
          in: query
          description: Minimum price filter
          schema:
            type: number
            format: decimal
            minimum: 0
          example: 10.00
        - name: price_max
          in: query
          description: Maximum price filter
          schema:
            type: number
            format: decimal
            minimum: 0
          example: 50.00
        - name: sort
          in: query
          description: Sort order
          schema:
            type: string
            enum: [relevance, price_asc, price_desc, rating, newest, bestseller]
            default: relevance
          example: "rating"
        - name: limit
          in: query
          description: Number of results per page
          schema:
            type: integer
            minimum: 1
            maximum: 100
            default: 20
          example: 20
        - name: offset
          in: query
          description: Number of results to skip (pagination)
          schema:
            type: integer
            minimum: 0
            default: 0
          example: 40
        - name: include_unavailable
          in: query
          description: Include out-of-stock books
          schema:
            type: boolean
            default: false
      responses:
        "200":
          description: Books found successfully
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookSearchResponse"
              examples:
                programming_books:
                  summary: Programming books search
                  value:
                    books:
                      - id: 1234
                        title: "JavaScript: The Good Parts"
                        subtitle: "The Definitive Guide"
                        authors:
                          - first_name: "Douglas"
                            last_name: "Crockford"
                        price: 29.99
                        list_price: 39.99
                        currency: "USD"
                        rating_average: 4.5
                        rating_count: 1247
                        cover_image_url: "https://cdn.cloudshelf.com/covers/1234.jpg"
                        availability:
                          in_stock: true
                          stock_quantity: 15
                          estimated_delivery: "2024-01-15"
                    pagination:
                      total_count: 156
                      current_page: 1
                      total_pages: 8
                      has_next: true
                      has_previous: false
        "400":
          $ref: "#/components/responses/BadRequest"
        "500":
          $ref: "#/components/responses/InternalServerError"

  /books/{bookId}:
    get:
      summary: Get book details
      description: Retrieve detailed information about a specific book
      operationId: getBookById
      tags:
        - Books
      parameters:
        - name: bookId
          in: path
          required: true
          description: Unique identifier for the book
          schema:
            type: integer
            minimum: 1
          example: 1234
        - name: include_reviews
          in: query
          description: Include customer reviews
          schema:
            type: boolean
            default: false
        - name: include_recommendations
          in: query
          description: Include recommended books
          schema:
            type: boolean
            default: false
      responses:
        "200":
          description: Book details retrieved successfully
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookDetail"
        "404":
          $ref: "#/components/responses/NotFound"
        "500":
          $ref: "#/components/responses/InternalServerError"

  /books/{bookId}/reviews:
    get:
      summary: Get book reviews
      description: Retrieve customer reviews for a specific book
      operationId: getBookReviews
      tags:
        - Books
        - Reviews
      parameters:
        - name: bookId
          in: path
          required: true
          schema:
            type: integer
            minimum: 1
        - name: sort
          in: query
          schema:
            type: string
            enum: [newest, oldest, highest_rating, lowest_rating, most_helpful]
            default: most_helpful
        - name: rating_filter
          in: query
          description: Filter by star rating
          schema:
            type: integer
            minimum: 1
            maximum: 5
        - name: verified_only
          in: query
          description: Show only verified purchase reviews
          schema:
            type: boolean
            default: false
        - name: limit
          in: query
          schema:
            type: integer
            minimum: 1
            maximum: 50
            default: 10
        - name: offset
          in: query
          schema:
            type: integer
            minimum: 0
            default: 0
      responses:
        "200":
          description: Reviews retrieved successfully
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ReviewsResponse"

    post:
      summary: Submit book review
      description: Submit a customer review for a book
      operationId: submitBookReview
      tags:
        - Books
        - Reviews
      security:
        - BearerAuth: []
      parameters:
        - name: bookId
          in: path
          required: true
          schema:
            type: integer
            minimum: 1
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/ReviewSubmission"
      responses:
        "201":
          description: Review submitted successfully
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Review"
        "400":
          $ref: "#/components/responses/BadRequest"
        "401":
          $ref: "#/components/responses/Unauthorized"
        "409":
          description: Review already exists for this user
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"

components:
  securitySchemes:
    BearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: JWT token from Cognito authentication
    ApiKeyAuth:
      type: apiKey
      in: header
      name: X-API-Key
      description: API key for administrative operations

  schemas:
    BookSearchResponse:
      type: object
      required:
        - books
        - pagination
      properties:
        books:
          type: array
          items:
            $ref: "#/components/schemas/BookSummary"
        pagination:
          $ref: "#/components/schemas/Pagination"
        filters_applied:
          type: object
          description: Summary of filters applied to the search
        search_metadata:
          type: object
          properties:
            query_time_ms:
              type: integer
              description: Query execution time in milliseconds
            total_matches:
              type: integer
              description: Total number of matching books
            suggestions:
              type: array
              items:
                type: string
              description: Search suggestions for better results

    BookSummary:
      type: object
      required:
        - id
        - title
        - authors
        - price
        - currency
      properties:
        id:
          type: integer
          description: Unique book identifier
        title:
          type: string
          maxLength: 500
          description: Book title
        subtitle:
          type: string
          maxLength: 500
          description: Book subtitle
        authors:
          type: array
          items:
            $ref: "#/components/schemas/Author"
          minItems: 1
        price:
          type: number
          format: decimal
          minimum: 0
          description: Current selling price
        list_price:
          type: number
          format: decimal
          minimum: 0
          description: Original list price
        currency:
          type: string
          pattern: "^[A-Z]{3}$"
          description: Price currency code (ISO 4217)
        rating_average:
          type: number
          format: decimal
          minimum: 0
          maximum: 5
          description: Average customer rating
        rating_count:
          type: integer
          minimum: 0
          description: Number of customer ratings
        cover_image_url:
          type: string
          format: uri
          description: URL to book cover image
        thumbnail_url:
          type: string
          format: uri
          description: URL to book thumbnail image
        availability:
          $ref: "#/components/schemas/Availability"
        category:
          $ref: "#/components/schemas/Category"
        format:
          type: string
          enum: [hardcover, paperback, ebook, audiobook]
        publication_date:
          type: string
          format: date
        publisher:
          $ref: "#/components/schemas/Publisher"

    BookDetail:
      allOf:
        - $ref: "#/components/schemas/BookSummary"
        - type: object
          properties:
            isbn_13:
              type: string
              pattern: "^[0-9]{13}$"
            isbn_10:
              type: string
              pattern: "^[0-9]{10}$"
            description:
              type: string
              description: Detailed book description
            page_count:
              type: integer
              minimum: 1
            language:
              type: string
              description: Book language
            dimensions:
              $ref: "#/components/schemas/BookDimensions"
            weight:
              type: number
              format: decimal
              description: Book weight in grams
            edition:
              type: string
              description: Book edition information
            series:
              $ref: "#/components/schemas/Series"
            tags:
              type: array
              items:
                type: string
              description: Book tags and keywords
            reviews_summary:
              $ref: "#/components/schemas/ReviewsSummary"
            recommendations:
              type: array
              items:
                $ref: "#/components/schemas/BookSummary"
              description: Recommended books
            purchase_options:
              type: array
              items:
                $ref: "#/components/schemas/PurchaseOption"

    Author:
      type: object
      required:
        - first_name
        - last_name
      properties:
        id:
          type: integer
        first_name:
          type: string
          maxLength: 100
        last_name:
          type: string
          maxLength: 100
        bio:
          type: string
          description: Author biography
        birth_date:
          type: string
          format: date
        nationality:
          type: string
          maxLength: 50
        website_url:
          type: string
          format: uri

    Category:
      type: object
      required:
        - id
        - name
        - slug
      properties:
        id:
          type: integer
        name:
          type: string
          maxLength: 100
        slug:
          type: string
          maxLength: 100
        description:
          type: string
        parent_category:
          $ref: "#/components/schemas/Category"
        subcategories:
          type: array
          items:
            $ref: "#/components/schemas/Category"

    Availability:
      type: object
      required:
        - in_stock
        - stock_quantity
      properties:
        in_stock:
          type: boolean
        stock_quantity:
          type: integer
          minimum: 0
        estimated_delivery:
          type: string
          format: date
          description: Estimated delivery date for in-stock items
        preorder_available:
          type: boolean
          description: Whether the item is available for preorder
        preorder_release_date:
          type: string
          format: date
        backorder_allowed:
          type: boolean

    Publisher:
      type: object
      required:
        - id
        - name
      properties:
        id:
          type: integer
        name:
          type: string
          maxLength: 200
        website_url:
          type: string
          format: uri
        country:
          type: string
          maxLength: 50

    Pagination:
      type: object
      required:
        - total_count
        - current_page
        - total_pages
        - has_next
        - has_previous
      properties:
        total_count:
          type: integer
          minimum: 0
          description: Total number of items
        current_page:
          type: integer
          minimum: 1
          description: Current page number
        total_pages:
          type: integer
          minimum: 1
          description: Total number of pages
        has_next:
          type: boolean
          description: Whether there is a next page
        has_previous:
          type: boolean
          description: Whether there is a previous page
        page_size:
          type: integer
          minimum: 1
          description: Number of items per page

    Review:
      type: object
      required:
        - id
        - rating
        - user_name
        - created_at
      properties:
        id:
          type: integer
        rating:
          type: integer
          minimum: 1
          maximum: 5
        title:
          type: string
          maxLength: 200
        content:
          type: string
          maxLength: 5000
        user_name:
          type: string
          description: Reviewer display name
        is_verified_purchase:
          type: boolean
        helpful_votes:
          type: integer
          minimum: 0
        total_votes:
          type: integer
          minimum: 0
        created_at:
          type: string
          format: date-time
        updated_at:
          type: string
          format: date-time

    ReviewSubmission:
      type: object
      required:
        - rating
      properties:
        rating:
          type: integer
          minimum: 1
          maximum: 5
        title:
          type: string
          maxLength: 200
        content:
          type: string
          maxLength: 5000

    ReviewsResponse:
      type: object
      required:
        - reviews
        - pagination
        - summary
      properties:
        reviews:
          type: array
          items:
            $ref: "#/components/schemas/Review"
        pagination:
          $ref: "#/components/schemas/Pagination"
        summary:
          $ref: "#/components/schemas/ReviewsSummary"

    ReviewsSummary:
      type: object
      properties:
        average_rating:
          type: number
          format: decimal
          minimum: 0
          maximum: 5
        total_reviews:
          type: integer
          minimum: 0
        rating_distribution:
          type: object
          properties:
            five_star:
              type: integer
              minimum: 0
            four_star:
              type: integer
              minimum: 0
            three_star:
              type: integer
              minimum: 0
            two_star:
              type: integer
              minimum: 0
            one_star:
              type: integer
              minimum: 0

    Error:
      type: object
      required:
        - error
        - message
      properties:
        error:
          type: string
          description: Error code
        message:
          type: string
          description: Human-readable error message
        details:
          type: object
          description: Additional error details
        request_id:
          type: string
          description: Unique request identifier for debugging

  responses:
    BadRequest:
      description: Bad request - invalid parameters
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"
          examples:
            invalid_parameter:
              value:
                error: "INVALID_PARAMETER"
                message: "The 'limit' parameter must be between 1 and 100"
                details:
                  parameter: "limit"
                  provided_value: 200
                  valid_range: "1-100"

    Unauthorized:
      description: Authentication required
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"

    NotFound:
      description: Resource not found
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"

    InternalServerError:
      description: Internal server error
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"
```

### **🛒 Shopping Cart API**

**Cart Management Endpoints**:

```javascript
// Shopping Cart API Implementation
class ShoppingCartAPI {
  constructor() {
    this.dynamodb = new AWS.DynamoDB.DocumentClient();
    this.tableName = "CloudShelf-ShoppingCart";
  }

  /**
   * GET /cart/{userId}
   * Retrieve user's shopping cart
   */
  async getCart(userId, sessionId = null) {
    const params = {
      TableName: this.tableName,
      Key: {
        userId: userId || sessionId,
        cartId: "current",
      },
    };

    try {
      const result = await this.dynamodb.get(params).promise();

      if (!result.Item) {
        return this.createEmptyCart(userId, sessionId);
      }

      // Enrich cart items with current book data
      const enrichedCart = await this.enrichCartWithBookData(result.Item);

      return {
        statusCode: 200,
        body: JSON.stringify({
          cart: enrichedCart,
          message: "Cart retrieved successfully",
        }),
        headers: {
          "Content-Type": "application/json",
          "Cache-Control": "no-cache",
        },
      };
    } catch (error) {
      return this.handleError(error, "Failed to retrieve cart");
    }
  }

  /**
   * POST /cart/{userId}/items
   * Add item to shopping cart
   */
  async addToCart(userId, sessionId, itemData) {
    const { bookId, quantity = 1 } = itemData;

    // Validate book exists and is available
    const book = await this.validateBookAvailability(bookId, quantity);
    if (!book.available) {
      return {
        statusCode: 400,
        body: JSON.stringify({
          error: "BOOK_UNAVAILABLE",
          message: book.message,
        }),
      };
    }

    const timestamp = new Date().toISOString();
    const cartKey = userId || sessionId;

    // Get current cart
    const currentCart = await this.getCurrentCart(cartKey);

    // Update or add item
    const existingItemIndex = currentCart.items.findIndex(
      (item) => item.bookId === bookId
    );

    if (existingItemIndex >= 0) {
      // Update existing item
      currentCart.items[existingItemIndex].quantity += quantity;
      currentCart.items[existingItemIndex].lastModified = timestamp;
    } else {
      // Add new item
      currentCart.items.push({
        bookId,
        title: book.title,
        price: book.price,
        coverImage: book.cover_image_url,
        quantity,
        addedAt: timestamp,
        lastModified: timestamp,
      });
    }

    // Recalculate totals
    currentCart.totalItems = currentCart.items.reduce(
      (sum, item) => sum + item.quantity,
      0
    );
    currentCart.totalAmount = currentCart.items.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
    currentCart.lastModified = timestamp;

    // Set TTL for 30 days
    currentCart.expiresAt = Math.floor(Date.now() / 1000) + 30 * 24 * 60 * 60;

    // Save updated cart
    await this.saveCart(cartKey, currentCart);

    // Publish cart event
    await this.publishCartEvent("cart.item.added", {
      userId: userId || null,
      sessionId: sessionId || null,
      bookId,
      quantity,
      cartTotal: currentCart.totalAmount,
    });

    return {
      statusCode: 200,
      body: JSON.stringify({
        cart: currentCart,
        message: "Item added to cart successfully",
      }),
    };
  }

  /**
   * PUT /cart/{userId}/items/{bookId}
   * Update item quantity in cart
   */
  async updateCartItem(userId, sessionId, bookId, updateData) {
    const { quantity } = updateData;

    if (quantity <= 0) {
      return await this.removeFromCart(userId, sessionId, bookId);
    }

    const cartKey = userId || sessionId;
    const currentCart = await this.getCurrentCart(cartKey);

    const itemIndex = currentCart.items.findIndex(
      (item) => item.bookId === bookId
    );

    if (itemIndex === -1) {
      return {
        statusCode: 404,
        body: JSON.stringify({
          error: "ITEM_NOT_FOUND",
          message: "Item not found in cart",
        }),
      };
    }

    // Validate availability for new quantity
    const book = await this.validateBookAvailability(bookId, quantity);
    if (!book.available) {
      return {
        statusCode: 400,
        body: JSON.stringify({
          error: "INSUFFICIENT_STOCK",
          message: book.message,
        }),
      };
    }

    // Update item
    currentCart.items[itemIndex].quantity = quantity;
    currentCart.items[itemIndex].lastModified = new Date().toISOString();

    // Recalculate totals
    this.recalculateCartTotals(currentCart);

    // Save updated cart
    await this.saveCart(cartKey, currentCart);

    return {
      statusCode: 200,
      body: JSON.stringify({
        cart: currentCart,
        message: "Cart item updated successfully",
      }),
    };
  }

  /**
   * DELETE /cart/{userId}/items/{bookId}
   * Remove item from cart
   */
  async removeFromCart(userId, sessionId, bookId) {
    const cartKey = userId || sessionId;
    const currentCart = await this.getCurrentCart(cartKey);

    const initialLength = currentCart.items.length;
    currentCart.items = currentCart.items.filter(
      (item) => item.bookId !== bookId
    );

    if (currentCart.items.length === initialLength) {
      return {
        statusCode: 404,
        body: JSON.stringify({
          error: "ITEM_NOT_FOUND",
          message: "Item not found in cart",
        }),
      };
    }

    // Recalculate totals
    this.recalculateCartTotals(currentCart);
    currentCart.lastModified = new Date().toISOString();

    // Save updated cart
    await this.saveCart(cartKey, currentCart);

    // Publish cart event
    await this.publishCartEvent("cart.item.removed", {
      userId: userId || null,
      sessionId: sessionId || null,
      bookId,
      cartTotal: currentCart.totalAmount,
    });

    return {
      statusCode: 200,
      body: JSON.stringify({
        cart: currentCart,
        message: "Item removed from cart successfully",
      }),
    };
  }

  /**
   * DELETE /cart/{userId}
   * Clear entire cart
   */
  async clearCart(userId, sessionId) {
    const cartKey = userId || sessionId;

    const params = {
      TableName: this.tableName,
      Key: {
        userId: cartKey,
        cartId: "current",
      },
    };

    await this.dynamodb.delete(params).promise();

    // Publish cart event
    await this.publishCartEvent("cart.cleared", {
      userId: userId || null,
      sessionId: sessionId || null,
    });

    return {
      statusCode: 200,
      body: JSON.stringify({
        message: "Cart cleared successfully",
      }),
    };
  }

  // Helper methods
  async validateBookAvailability(bookId, requestedQuantity) {
    try {
      const book = await this.bookCatalogService.getBook(bookId);

      if (!book) {
        return {
          available: false,
          message: "Book not found",
        };
      }

      if (book.status !== "active") {
        return {
          available: false,
          message: "Book is not available for purchase",
        };
      }

      if (book.stock_quantity < requestedQuantity) {
        return {
          available: false,
          message: `Only ${book.stock_quantity} items available in stock`,
        };
      }

      return {
        available: true,
        ...book,
      };
    } catch (error) {
      return {
        available: false,
        message: "Unable to verify book availability",
      };
    }
  }

  async enrichCartWithBookData(cart) {
    if (!cart.items || cart.items.length === 0) {
      return cart;
    }

    const bookIds = cart.items.map((item) => item.bookId);
    const books = await this.bookCatalogService.getBooksByIds(bookIds);
    const bookMap = new Map(books.map((book) => [book.id, book]));

    cart.items = cart.items.map((item) => {
      const book = bookMap.get(item.bookId);
      return {
        ...item,
        currentPrice: book?.price || item.price,
        availability: book?.stock_quantity > 0,
        priceChanged: book && book.price !== item.price,
      };
    });

    return cart;
  }

  recalculateCartTotals(cart) {
    cart.totalItems = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    cart.totalAmount = cart.items.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
  }

  async publishCartEvent(eventType, eventData) {
    const eventBridge = new AWS.EventBridge();

    const event = {
      Source: "cloudshelf.shopping-cart",
      DetailType: eventType,
      Detail: JSON.stringify({
        timestamp: new Date().toISOString(),
        ...eventData,
      }),
    };

    await eventBridge
      .putEvents({
        Entries: [event],
      })
      .promise();
  }
}
```

---

## 🔄 API Versioning Strategy

### **📈 Version Management**

**Versioning Implementation**:

```javascript
// API versioning and backward compatibility
class APIVersionManager {
  constructor() {
    this.supportedVersions = ["1.0", "2.0", "2.1"];
    this.defaultVersion = "2.1";
    this.deprecatedVersions = ["1.0"];
    this.versionHandlers = new Map();

    this.initializeVersionHandlers();
  }

  initializeVersionHandlers() {
    // Version 1.0 handlers (deprecated)
    this.versionHandlers.set("1.0", {
      transformRequest: this.transformV1Request.bind(this),
      transformResponse: this.transformV1Response.bind(this),
      deprecationWarning:
        "API version 1.0 is deprecated. Please upgrade to v2.1",
    });

    // Version 2.0 handlers
    this.versionHandlers.set("2.0", {
      transformRequest: this.transformV2Request.bind(this),
      transformResponse: this.transformV2Response.bind(this),
      deprecationWarning: null,
    });

    // Version 2.1 handlers (current)
    this.versionHandlers.set("2.1", {
      transformRequest: (req) => req, // No transformation needed
      transformResponse: (res) => res, // No transformation needed
      deprecationWarning: null,
    });
  }

  async handleVersionedRequest(event, context) {
    // Extract version from header, path, or query parameter
    const version = this.extractVersion(event);

    if (!this.supportedVersions.includes(version)) {
      return {
        statusCode: 400,
        body: JSON.stringify({
          error: "UNSUPPORTED_VERSION",
          message: `API version ${version} is not supported`,
          supported_versions: this.supportedVersions,
        }),
      };
    }

    const handler = this.versionHandlers.get(version);

    try {
      // Transform request to current format
      const transformedRequest = handler.transformRequest(event);

      // Process request with current business logic
      const response = await this.processRequest(transformedRequest);

      // Transform response to requested version format
      const transformedResponse = handler.transformResponse(response);

      // Add deprecation warnings if needed
      if (handler.deprecationWarning) {
        transformedResponse.headers = {
          ...transformedResponse.headers,
          "X-Deprecation-Warning": handler.deprecationWarning,
          "X-Sunset-Date": "2024-12-31", // Example sunset date
        };
      }

      return transformedResponse;
    } catch (error) {
      return this.handleVersionedError(error, version);
    }
  }

  extractVersion(event) {
    // Priority: Header > Path > Query > Default
    return (
      event.headers["X-API-Version"] ||
      event.pathParameters?.version ||
      event.queryStringParameters?.version ||
      this.defaultVersion
    );
  }

  // Version 1.0 transformations (example)
  transformV1Request(event) {
    // Convert old field names to new ones
    if (event.body) {
      const body = JSON.parse(event.body);

      // Map old field names
      if (body.book_id) {
        body.bookId = body.book_id;
        delete body.book_id;
      }

      if (body.qty) {
        body.quantity = body.qty;
        delete body.qty;
      }

      event.body = JSON.stringify(body);
    }

    return event;
  }

  transformV1Response(response) {
    if (response.body) {
      const body = JSON.parse(response.body);

      // Convert new field names back to old ones for v1 clients
      if (body.cart && body.cart.items) {
        body.cart.items = body.cart.items.map((item) => ({
          book_id: item.bookId,
          qty: item.quantity,
          title: item.title,
          price: item.price,
        }));
      }

      // Remove new fields that didn't exist in v1
      if (body.cart) {
        delete body.cart.lastModified;
        delete body.cart.expiresAt;
      }

      response.body = JSON.stringify(body);
    }

    return response;
  }

  // Version 2.0 transformations
  transformV2Request(event) {
    // Minimal transformation - v2.0 is similar to v2.1
    return event;
  }

  transformV2Response(response) {
    if (response.body) {
      const body = JSON.parse(response.body);

      // Remove v2.1 specific fields for v2.0 compatibility
      if (body.cart && body.cart.items) {
        body.cart.items = body.cart.items.map((item) => {
          const { priceChanged, ...v2Item } = item;
          return v2Item;
        });
      }

      response.body = JSON.stringify(body);
    }

    return response;
  }
}
```

### **📋 API Documentation Generation**

**Automated Documentation Pipeline**:

```javascript
// Automated API documentation generation
class APIDocumentationGenerator {
  constructor() {
    this.openAPISpec = {
      openapi: "3.0.3",
      info: {
        title: "CloudShelf API",
        version: "2.1.0",
      },
      paths: {},
      components: {
        schemas: {},
        responses: {},
        securitySchemes: {},
      },
    };
  }

  async generateDocumentation() {
    // Generate documentation from code annotations
    await this.parseControllerAnnotations();

    // Generate code examples
    await this.generateCodeExamples();

    // Generate interactive documentation
    await this.generateSwaggerUI();

    // Generate SDK documentation
    await this.generateSDKDocs();

    return this.openAPISpec;
  }

  async parseControllerAnnotations() {
    // Parse JSDoc annotations from Lambda functions
    const lambdaFunctions = await this.findLambdaFunctions();

    for (const func of lambdaFunctions) {
      const annotations = this.parseJSDocAnnotations(func.code);
      const pathSpec = this.generatePathSpec(annotations);

      Object.assign(this.openAPISpec.paths, pathSpec);
    }
  }

  generateCodeExamples() {
    const examples = {
      javascript: this.generateJavaScriptExamples(),
      python: this.generatePythonExamples(),
      curl: this.generateCurlExamples(),
      postman: this.generatePostmanCollection(),
    };

    return examples;
  }

  generateJavaScriptExamples() {
    return {
      "search-books": `
// Search for books
const response = await fetch('https://api.cloudshelf.com/v2/books?q=javascript&limit=10', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + authToken
    }
});

const data = await response.json();
console.log('Found books:', data.books);
            `,
      "add-to-cart": `
// Add book to cart
const response = await fetch('https://api.cloudshelf.com/v2/cart/user123/items', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + authToken
    },
    body: JSON.stringify({
        bookId: 1234,
        quantity: 2
    })
});

const cartData = await response.json();
console.log('Updated cart:', cartData.cart);
            `,
    };
  }

  generatePythonExamples() {
    return {
      "search-books": `
import requests

# Search for books
response = requests.get(
    'https://api.cloudshelf.com/v2/books',
    params={'q': 'javascript', 'limit': 10},
    headers={
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {auth_token}'
    }
)

data = response.json()
print(f"Found {len(data['books'])} books")
            `,
      "add-to-cart": `
import requests

# Add book to cart
response = requests.post(
    'https://api.cloudshelf.com/v2/cart/user123/items',
    json={
        'bookId': 1234,
        'quantity': 2
    },
    headers={
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {auth_token}'
    }
)

cart_data = response.json()
print(f"Cart total: ${cart_data["cart"]["totalAmount"]}")
            `,
    };
  }

  async generateSwaggerUI() {
    const swaggerHTML = `
<!DOCTYPE html>
<html>
<head>
    <title>CloudShelf API Documentation</title>
    <link rel="stylesheet" type="text/css" href="https://unpkg.com/swagger-ui-dist@3.47.1/swagger-ui.css" />
    <style>
        .swagger-ui .topbar { display: none; }
        .swagger-ui .info { margin: 20px 0; }
    </style>
</head>
<body>
    <div id="swagger-ui"></div>
    <script src="https://unpkg.com/swagger-ui-dist@3.47.1/swagger-ui-bundle.js"></script>
    <script>
        SwaggerUIBundle({
            url: '/api-spec.json',
            dom_id: '#swagger-ui',
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIBundle.presets.standalone
            ],
            layout: "BaseLayout",
            deepLinking: true,
            tryItOutEnabled: true,
            supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch']
        });
    </script>
</body>
</html>
        `;

    return swaggerHTML;
  }
}
```

---

## ⚡ Real-Time API Features

### **🔄 WebSocket Implementation**

**Real-Time Updates**:

```javascript
// WebSocket API for real-time features
class WebSocketManager {
  constructor() {
    this.apiGateway = new AWS.ApiGatewayManagementApi({
      endpoint: process.env.WEBSOCKET_ENDPOINT,
    });
    this.connections = new Map();
  }

  async handleConnect(event) {
    const connectionId = event.requestContext.connectionId;
    const userId = this.extractUserId(event);

    // Store connection
    this.connections.set(connectionId, {
      userId,
      connectedAt: new Date().toISOString(),
      subscriptions: new Set(),
    });

    // Send welcome message
    await this.sendToConnection(connectionId, {
      type: "connection_established",
      message: "Connected to CloudShelf real-time updates",
      connectionId,
    });

    return { statusCode: 200 };
  }

  async handleDisconnect(event) {
    const connectionId = event.requestContext.connectionId;
    this.connections.delete(connectionId);
    return { statusCode: 200 };
  }

  async handleMessage(event) {
    const connectionId = event.requestContext.connectionId;
    const message = JSON.parse(event.body);

    switch (message.action) {
      case "subscribe_cart_updates":
        await this.subscribeToCartUpdates(connectionId, message.userId);
        break;
      case "subscribe_price_alerts":
        await this.subscribeToPriceAlerts(connectionId, message.bookIds);
        break;
      case "subscribe_inventory_alerts":
        await this.subscribeToInventoryAlerts(connectionId, message.bookIds);
        break;
      case "ping":
        await this.sendToConnection(connectionId, { type: "pong" });
        break;
      default:
        await this.sendToConnection(connectionId, {
          type: "error",
          message: "Unknown action",
        });
    }

    return { statusCode: 200 };
  }

  async subscribeToCartUpdates(connectionId, userId) {
    const connection = this.connections.get(connectionId);
    if (connection) {
      connection.subscriptions.add(`cart:${userId}`);

      await this.sendToConnection(connectionId, {
        type: "subscription_confirmed",
        subscription: "cart_updates",
        userId,
      });
    }
  }

  async notifyCartUpdate(userId, cartData) {
    const subscriptionKey = `cart:${userId}`;

    for (const [connectionId, connection] of this.connections) {
      if (connection.subscriptions.has(subscriptionKey)) {
        await this.sendToConnection(connectionId, {
          type: "cart_updated",
          userId,
          cart: cartData,
          timestamp: new Date().toISOString(),
        });
      }
    }
  }

  async notifyPriceChange(bookId, oldPrice, newPrice) {
    const subscriptionKey = `price:${bookId}`;

    for (const [connectionId, connection] of this.connections) {
      if (connection.subscriptions.has(subscriptionKey)) {
        await this.sendToConnection(connectionId, {
          type: "price_changed",
          bookId,
          oldPrice,
          newPrice,
          changePercentage: (((newPrice - oldPrice) / oldPrice) * 100).toFixed(
            2
          ),
          timestamp: new Date().toISOString(),
        });
      }
    }
  }

  async notifyInventoryUpdate(bookId, stockLevel) {
    const subscriptionKey = `inventory:${bookId}`;

    for (const [connectionId, connection] of this.connections) {
      if (connection.subscriptions.has(subscriptionKey)) {
        let alertType = "inventory_updated";

        if (stockLevel === 0) {
          alertType = "out_of_stock";
        } else if (stockLevel <= 5) {
          alertType = "low_stock";
        } else if (stockLevel > 0) {
          alertType = "back_in_stock";
        }

        await this.sendToConnection(connectionId, {
          type: alertType,
          bookId,
          stockLevel,
          timestamp: new Date().toISOString(),
        });
      }
    }
  }

  async sendToConnection(connectionId, data) {
    try {
      await this.apiGateway
        .postToConnection({
          ConnectionId: connectionId,
          Data: JSON.stringify(data),
        })
        .promise();
    } catch (error) {
      if (error.statusCode === 410) {
        // Connection no longer exists
        this.connections.delete(connectionId);
      } else {
        console.error("Failed to send message:", error);
      }
    }
  }

  // Batch notifications for efficiency
  async sendBatchNotifications(notifications) {
    const promises = notifications.map((notification) =>
      this.sendToConnection(notification.connectionId, notification.data)
    );

    await Promise.allSettled(promises);
  }
}
```

---

## 🔒 API Security & Rate Limiting

### **🛡️ Security Implementation**

**API Security Framework**:

```javascript
// Comprehensive API security implementation
class APISecurityManager {
  constructor() {
    this.rateLimiters = new Map();
    this.suspiciousActivityDetector = new SuspiciousActivityDetector();
  }

  async authenticateRequest(event) {
    const authHeader =
      event.headers.Authorization || event.headers.authorization;

    if (!authHeader) {
      return this.createUnauthorizedResponse("Missing authorization header");
    }

    if (authHeader.startsWith("Bearer ")) {
      return await this.validateJWTToken(authHeader.substring(7));
    }

    if (event.headers["X-API-Key"]) {
      return await this.validateAPIKey(event.headers["X-API-Key"]);
    }

    return this.createUnauthorizedResponse("Invalid authorization method");
  }

  async validateJWTToken(token) {
    try {
      const cognitoJwtVerifier = CognitoJwtVerifier.create({
        userPoolId: process.env.COGNITO_USER_POOL_ID,
        tokenUse: "access",
        clientId: process.env.COGNITO_CLIENT_ID,
      });

      const payload = await cognitoJwtVerifier.verify(token);

      return {
        isValid: true,
        user: {
          id: payload.sub,
          email: payload.email,
          groups: payload["cognito:groups"] || [],
          scope: payload.scope,
        },
      };
    } catch (error) {
      return {
        isValid: false,
        error: "Invalid or expired token",
      };
    }
  }

  async validateAPIKey(apiKey) {
    // Validate API key against DynamoDB
    const params = {
      TableName: "CloudShelf-APIKeys",
      Key: { keyId: apiKey },
    };

    try {
      const result = await this.dynamodb.get(params).promise();

      if (!result.Item) {
        return { isValid: false, error: "Invalid API key" };
      }

      if (result.Item.isActive === false) {
        return { isValid: false, error: "API key is disabled" };
      }

      if (result.Item.expiresAt && result.Item.expiresAt < Date.now()) {
        return { isValid: false, error: "API key has expired" };
      }

      return {
        isValid: true,
        apiKey: {
          id: result.Item.keyId,
          name: result.Item.name,
          permissions: result.Item.permissions || [],
          rateLimit: result.Item.rateLimit || { requests: 1000, window: 3600 },
        },
      };
    } catch (error) {
      return { isValid: false, error: "Failed to validate API key" };
    }
  }

  async checkRateLimit(identifier, endpoint, rateLimitConfig) {
    const key = `${identifier}:${endpoint}`;
    const now = Date.now();
    const windowStart = now - rateLimitConfig.window * 1000;

    let limiter = this.rateLimiters.get(key);

    if (!limiter) {
      limiter = {
        requests: [],
        blocked: false,
        blockUntil: 0,
      };
      this.rateLimiters.set(key, limiter);
    }

    // Check if currently blocked
    if (limiter.blocked && now < limiter.blockUntil) {
      return {
        allowed: false,
        retryAfter: Math.ceil((limiter.blockUntil - now) / 1000),
        reason: "Rate limit exceeded",
      };
    }

    // Clean old requests
    limiter.requests = limiter.requests.filter(
      (timestamp) => timestamp > windowStart
    );

    // Check rate limit
    if (limiter.requests.length >= rateLimitConfig.requests) {
      limiter.blocked = true;
      limiter.blockUntil = now + rateLimitConfig.window * 1000;

      return {
        allowed: false,
        retryAfter: rateLimitConfig.window,
        reason: "Rate limit exceeded",
      };
    }

    // Allow request
    limiter.requests.push(now);
    limiter.blocked = false;

    return {
      allowed: true,
      remaining: rateLimitConfig.requests - limiter.requests.length,
      resetTime: Math.ceil(
        (windowStart + rateLimitConfig.window * 1000) / 1000
      ),
    };
  }

  async detectSuspiciousActivity(event, user) {
    const indicators = await this.suspiciousActivityDetector.analyze({
      ip: event.requestContext.identity.sourceIp,
      userAgent: event.headers["User-Agent"],
      userId: user?.id,
      endpoint: event.path,
      timestamp: new Date().toISOString(),
    });

    if (indicators.riskScore > 80) {
      await this.blockSuspiciousIP(event.requestContext.identity.sourceIp);
      return {
        blocked: true,
        reason: "Suspicious activity detected",
        indicators,
      };
    }

    return { blocked: false, riskScore: indicators.riskScore };
  }

  createUnauthorizedResponse(message) {
    return {
      statusCode: 401,
      body: JSON.stringify({
        error: "UNAUTHORIZED",
        message,
      }),
      headers: {
        "Content-Type": "application/json",
        "WWW-Authenticate": 'Bearer realm="CloudShelf API"',
      },
    };
  }

  createRateLimitResponse(rateLimitResult) {
    return {
      statusCode: 429,
      body: JSON.stringify({
        error: "RATE_LIMIT_EXCEEDED",
        message: "Too many requests",
        retryAfter: rateLimitResult.retryAfter,
      }),
      headers: {
        "Content-Type": "application/json",
        "Retry-After": rateLimitResult.retryAfter.toString(),
        "X-RateLimit-Limit": "1000",
        "X-RateLimit-Remaining": "0",
        "X-RateLimit-Reset": rateLimitResult.resetTime?.toString(),
      },
    };
  }
}

// Suspicious activity detection
class SuspiciousActivityDetector {
  async analyze(requestData) {
    let riskScore = 0;
    const indicators = [];

    // Check for rapid requests from same IP
    const recentRequests = await this.getRecentRequestsFromIP(requestData.ip);
    if (recentRequests > 100) {
      riskScore += 30;
      indicators.push("High request frequency from IP");
    }

    // Check for unusual user agent patterns
    if (this.isUnusualUserAgent(requestData.userAgent)) {
      riskScore += 20;
      indicators.push("Unusual user agent");
    }

    // Check for geographical anomalies
    const geoAnomaly = await this.checkGeographicalAnomaly(
      requestData.ip,
      requestData.userId
    );
    if (geoAnomaly) {
      riskScore += 25;
      indicators.push("Geographical anomaly detected");
    }

    // Check for SQL injection patterns
    if (this.hasSQLInjectionPatterns(requestData.endpoint)) {
      riskScore += 50;
      indicators.push("Potential SQL injection attempt");
    }

    return { riskScore, indicators };
  }

  isUnusualUserAgent(userAgent) {
    const suspiciousPatterns = [
      /bot/i,
      /crawler/i,
      /python-requests/i,
      /curl/i,
      /wget/i,
    ];

    return suspiciousPatterns.some((pattern) => pattern.test(userAgent));
  }

  hasSQLInjectionPatterns(endpoint) {
    const sqlPatterns = [
      /union\s+select/i,
      /drop\s+table/i,
      /'\s*or\s*'1'\s*=\s*'1/i,
      /--/,
      /\/\*/,
    ];

    return sqlPatterns.some((pattern) => pattern.test(endpoint));
  }
}
```

---

_This API documentation provides a comprehensive foundation for CloudShelf's developer experience, ensuring scalable, secure, and well-documented interfaces for all application functionality._
