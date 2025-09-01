# Book Catalog Service Template

## Overview

This template provides the foundational structure for the Book Catalog microservice. The **Developer** team will implement the business logic based on these architectural specifications.

## Service Specifications

### Technology Stack

- **Runtime**: Java 21 (AWS Lambda)
- **Build Tool**: Maven 3.8+
- **Database**: PostgreSQL RDS
- **API**: REST via API Gateway

### Architecture References

- **ADR-002**: PostgreSQL RDS for structured book data
- **ADR-004**: AWS Lambda for serverless compute
- **ADR-005**: Java 21 for runtime environment

## API Endpoints (Developer Implementation Required)

### Book Operations

- `GET /books` - List all books with pagination
- `GET /books/{id}` - Get specific book details
- `GET /books/search?q={query}` - Search books by title/author/ISBN
- `GET /books/category/{category}` - Get books by category
- `POST /books` - Create new book (admin)
- `PUT /books/{id}` - Update book (admin)
- `DELETE /books/{id}` - Delete book (admin)

### Category Operations

- `GET /categories` - List all book categories
- `GET /categories/{id}` - Get specific category

## Service Architecture (Developer Implementation Required)

### Handler Pattern

```java
// Developer to implement full handler logic
public class BookCatalogHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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
public class BookService {

    public List<Book> searchBooks(String query, int limit, int offset) {
        // Developer: Implement search logic with PostgreSQL full-text search
    }

    public Optional<Book> getBookById(String id) {
        // Developer: Implement retrieval logic
    }

    public Book createBook(Book book) {
        // Developer: Implement creation logic with validation
    }

    // Developer: Add all other business methods
}
```

### Repository Pattern

```java
// Developer to implement data access layer
public interface BookRepository {
    Optional<Book> findById(String id);
    List<Book> findAll(Pageable pageable);
    List<Book> searchByTitleOrAuthor(String query, Pageable pageable);
    List<Book> findByCategory(String categoryId, Pageable pageable);
    Book save(Book book);
    void deleteById(String id);
}
```

## Next Steps for Development Team

1. **Developer Tasks**:

   - Implement all handler, service, and repository classes
   - Add comprehensive unit and integration tests
   - Implement proper error handling and validation
   - Add logging and monitoring

2. **DevOps Tasks**:
   - Create RDS instance and configure security groups
   - Set up Lambda function with proper IAM roles
   - Configure API Gateway endpoints
   - Set up CI/CD pipeline for deployment

## Related Documentation

- [System Architecture](../../../docs/architecture/cloudshelf-system-architecture.md)
- [Integration Patterns](../../../docs/architecture/cloudshelf-integration-patterns.md)
- [Development Guidelines](../../../docs/architecture/cloudshelf-development-guidelines.md)
