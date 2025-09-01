# Shopping Cart Service Template

## Overview

This template provides the foundational structure for the Shopping Cart microservice. The **Developer** team will implement the business logic based on these architectural specifications.

## Service Specifications

### Technology Stack

- **Runtime**: Java 21 (AWS Lambda)
- **Build Tool**: Maven 3.8+
- **Database**: DynamoDB
- **API**: REST via API Gateway

### Architecture References

- **ADR-003**: DynamoDB for fast cart operations
- **ADR-004**: AWS Lambda for serverless compute
- **ADR-005**: Java 21 for runtime environment

## API Endpoints (Developer Implementation Required)

### Cart Operations

- `GET /cart/{userId}` - Get user's shopping cart
- `POST /cart/{userId}/items` - Add item to cart
- `PUT /cart/{userId}/items/{itemId}` - Update item quantity
- `DELETE /cart/{userId}/items/{itemId}` - Remove item from cart
- `DELETE /cart/{userId}` - Clear entire cart
- `POST /cart/{userId}/checkout` - Process cart checkout

## Service Architecture (Developer Implementation Required)

### Handler Pattern

```java
// Developer to implement full handler logic
public class ShoppingCartHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Developer: Initialize services, repositories

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        // Developer: Implement routing logic based on HTTP method and path
        // Developer: Add proper error handling and response formatting
        // Developer: Follow integration patterns from architecture docs
    }
}
```

### Service Layer Pattern

```java
// Developer to implement business logic
public class CartService {

    public Cart getCart(String userId) {
        // Developer: Implement cart retrieval logic
    }

    public CartItem addItem(String userId, CartItem item) {
        // Developer: Implement add item logic with validation
    }

    public CartItem updateItem(String userId, String itemId, int quantity) {
        // Developer: Implement update logic
    }

    // Developer: Add all other business methods
}
```

### Repository Pattern

```java
// Developer to implement data access layer
public interface CartRepository {
    Optional<Cart> findByUserId(String userId);
    CartItem addItem(String userId, CartItem item);
    CartItem updateItem(String userId, String itemId, int quantity);
    void removeItem(String userId, String itemId);
    void clearCart(String userId);
}
```

## Next Steps for Development Team

1. **Developer Tasks**:

   - Implement all handler, service, and repository classes
   - Add comprehensive unit and integration tests
   - Implement proper error handling and validation
   - Add logging and monitoring

2. **DevOps Tasks**:
   - Create DynamoDB table with proper indexes
   - Set up Lambda function with proper IAM roles
   - Configure API Gateway endpoints
   - Set up CI/CD pipeline for deployment

## Related Documentation

- [System Architecture](../../../docs/architecture/cloudshelf-system-architecture.md)
- [Integration Patterns](../../../docs/architecture/cloudshelf-integration-patterns.md)
- [Development Guidelines](../../../docs/architecture/cloudshelf-development-guidelines.md)
