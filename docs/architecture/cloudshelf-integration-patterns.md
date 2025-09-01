# Integration Patterns & Guidelines

## Overview

This document defines integration patterns, API contracts, and development guidelines for the CloudShelf system.

## API Gateway Integration Pattern

### Request/Response Flow

```
Client Request → API Gateway → Lambda Function → Database → Response
```

### API Gateway Configuration

- **CORS**: Enabled for web client access
- **Authentication**: Future integration with AWS Cognito
- **Rate Limiting**: 1000 requests per minute per client
- **Request Validation**: JSON schema validation at gateway level

## Lambda Function Patterns

### Handler Structure

```java
public class ServiceHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        // 1. Parse and validate request
        // 2. Route to appropriate service method
        // 3. Handle business logic
        // 4. Format and return response
    }
}
```

### HTTP Method Routing

- **GET**: Retrieve resources (books, cart items)
- **POST**: Create new resources
- **PUT**: Update existing resources
- **DELETE**: Remove resources
- **PATCH**: Partial updates

### Response Format Standards

#### Success Response

```json
{
  "statusCode": 200,
  "headers": {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*"
  },
  "body": {
    "data": {
      /* response data */
    },
    "message": "Success",
    "timestamp": "2025-09-01T10:30:00Z"
  }
}
```

#### Error Response

```json
{
  "statusCode": 400,
  "headers": {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*"
  },
  "body": {
    "error": {
      "code": "INVALID_REQUEST",
      "message": "Detailed error description",
      "details": {
        /* additional error context */
      }
    },
    "timestamp": "2025-09-01T10:30:00Z"
  }
}
```

## Database Integration Patterns

### PostgreSQL RDS Pattern (Book Catalog)

#### Connection Management

```java
// Use HikariCP connection pool
// Configure via environment variables
// Implement connection retry logic
```

#### Query Patterns

- **Read Operations**: SELECT with proper indexing
- **Write Operations**: INSERT/UPDATE with transaction handling
- **Search Operations**: Full-text search with PostgreSQL features

#### Data Access Layer

```java
public interface BookRepository {
    Optional<Book> findById(String bookId);
    List<Book> findByCategory(String category);
    List<Book> searchBooks(String query);
    Book save(Book book);
    void deleteById(String bookId);
}
```

### DynamoDB Pattern (Shopping Cart)

#### Key Design

- **Partition Key**: `userId` (string)
- **Sort Key**: `itemId` (string)
- **GSI**: `cartId-timestamp-index` for cart session tracking

#### Access Patterns

```java
// Single item operations
DynamoDbClient.getItem()
DynamoDbClient.putItem()
DynamoDbClient.updateItem()
DynamoDbClient.deleteItem()

// Batch operations
DynamoDbClient.batchGetItem()
DynamoDbClient.batchWriteItem()
```

#### Data Model

```java
@DynamoDbBean
public class CartItem {
    @DynamoDbPartitionKey
    private String userId;

    @DynamoDbSortKey
    private String itemId;

    // Additional attributes...
}
```

## Error Handling Patterns

### Exception Hierarchy

```java
// Base exception
public class CloudShelfException extends RuntimeException

// Service-specific exceptions
public class BookNotFoundException extends CloudShelfException
public class CartOperationException extends CloudShelfException
public class DatabaseConnectionException extends CloudShelfException
```

### Lambda Error Handling

```java
try {
    // Business logic
    return successResponse(result);
} catch (ValidationException e) {
    return errorResponse(400, "VALIDATION_ERROR", e.getMessage());
} catch (NotFoundException e) {
    return errorResponse(404, "NOT_FOUND", e.getMessage());
} catch (Exception e) {
    logger.error("Unexpected error", e);
    return errorResponse(500, "INTERNAL_ERROR", "An unexpected error occurred");
}
```

## Security Patterns

### IAM Role Permissions

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["rds:DescribeDBInstances", "rds-db:connect"],
      "Resource": "arn:aws:rds-db:region:account:dbuser:db-instance/*"
    }
  ]
}
```

### Environment Variables

```java
// Access database connection info
String dbEndpoint = System.getenv("DB_ENDPOINT");
String dbName = System.getenv("DB_NAME");

// No hardcoded credentials - use IAM roles
```

## Logging Patterns

### Structured Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        // Set correlation ID for request tracking
        MDC.put("requestId", context.getAwsRequestId());
        MDC.put("functionName", context.getFunctionName());

        logger.info("Processing request: method={}, path={}",
            request.getHttpMethod(), request.getPath());

        try {
            // Process request
            logger.info("Request completed successfully");
            return response;
        } catch (Exception e) {
            logger.error("Request failed", e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}
```

## Testing Patterns

### Unit Testing

```java
@Test
public void testBookCatalogHandler() {
    // Arrange
    APIGatewayProxyRequestEvent request = createTestRequest();
    Context context = createTestContext();

    // Act
    APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

    // Assert
    assertEquals(200, response.getStatusCode());
}
```

### Integration Testing

- Use Testcontainers for local database testing
- Mock AWS services for unit tests
- End-to-end testing with deployed Lambda functions

## Configuration Management

### Environment-Based Configuration

```yaml
# Development
DB_ENDPOINT: dev-bookstore.cluster-xyz.us-east-1.rds.amazonaws.com
LOG_LEVEL: DEBUG

# Production
DB_ENDPOINT: prod-bookstore.cluster-abc.us-east-1.rds.amazonaws.com
LOG_LEVEL: INFO
```

### Feature Flags

```java
// Use AWS AppConfig for feature flag management
boolean newSearchEnabled = FeatureFlags.isEnabled("new-search-algorithm");
```

## Performance Patterns

### Connection Pooling

```java
// Configure HikariCP for RDS connections
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10);
config.setMinimumIdle(5);
config.setConnectionTimeout(30000);
config.setIdleTimeout(600000);
config.setMaxLifetime(1800000);
```

### Caching Strategy

```java
// In-memory caching for frequently accessed data
@Cacheable(value = "books", key = "#bookId")
public Book getBook(String bookId) {
    return bookRepository.findById(bookId);
}
```

## Monitoring & Observability

### CloudWatch Metrics

- Lambda duration and error rates
- Database connection counts
- API Gateway request metrics

### Custom Metrics

```java
// Publish custom business metrics
CloudWatchClient.putMetricData(PutMetricDataRequest.builder()
    .namespace("CloudShelf/Books")
    .metricData(MetricDatum.builder()
        .metricName("BooksSearched")
        .value(1.0)
        .unit(StandardUnit.COUNT)
        .build())
    .build());
```

## Development Guidelines

### Code Organization

```
src/
├── main/
│   ├── java/
│   │   └── com/cloudshelf/
│   │       ├── bookcatalog/
│   │       │   ├── handler/          # Lambda handlers
│   │       │   ├── service/          # Business logic
│   │       │   ├── repository/       # Data access
│   │       │   ├── model/           # Domain models
│   │       │   └── config/          # Configuration
│   │       └── common/              # Shared utilities
│   └── resources/
│       ├── application.properties
│       └── logback.xml
└── test/
    └── java/                        # Unit and integration tests
```

### Naming Conventions

- **Classes**: PascalCase (e.g., `BookCatalogHandler`)
- **Methods**: camelCase (e.g., `searchBooks`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RESULTS`)
- **Packages**: lowercase (e.g., `com.cloudshelf.bookcatalog`)

### Documentation Standards

- JavaDoc for all public methods
- README.md for each service
- Architecture Decision Records (ADRs) for major decisions
- API documentation with OpenAPI/Swagger
