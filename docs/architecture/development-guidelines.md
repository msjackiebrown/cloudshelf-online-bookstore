# Development Guidelines & Standards

## Overview

This document establishes development standards, guidelines, and best practices for the CloudShelf project team.

## Team Roles & Responsibilities

### Solutions Architect

- **Responsibilities**:
  - Technology stack decisions and ADRs
  - System architecture design and documentation
  - Integration patterns and guidelines
  - Project structure and naming conventions
  - Security and performance standards
- **Deliverables**:
  - Architecture diagrams
  - ADRs (Architecture Decision Records)
  - Integration patterns documentation
  - Development guidelines
  - Basic project templates

### Developer

- **Responsibilities**:
  - Implement business logic based on architect specifications
  - Write unit and integration tests
  - Code reviews and quality assurance
  - Bug fixes and feature implementation
  - Performance optimization at code level
- **Deliverables**:
  - Production-ready Lambda functions
  - Unit test suites
  - Integration tests
  - Code documentation
  - Feature implementations

### DevOps Engineer

- **Responsibilities**:
  - Infrastructure as Code (IaC)
  - CI/CD pipeline setup and maintenance
  - Environment management (dev/staging/prod)
  - Deployment automation
  - Monitoring and alerting setup
- **Deliverables**:
  - AWS CDK/CloudFormation templates
  - GitHub Actions workflows
  - Deployment scripts
  - Environment configurations
  - Monitoring dashboards

## Project Structure Standards

### Root Directory Structure

```
cloudshelf-online-bookstore/
├── docs/                           # All documentation
│   ├── requirements/               # Business requirements
│   ├── architecture/               # Technical architecture
│   └── adrs/                      # Architecture Decision Records
├── src/                           # Source code
│   ├── lambda/                    # Lambda function templates
│   └── infrastructure/            # IaC templates (DevOps)
├── tests/                         # End-to-end tests (DevOps)
├── scripts/                       # Build and utility scripts (DevOps)
└── .github/                       # CI/CD workflows (DevOps)
```

### Lambda Service Structure (Developer Implementation)

```
src/lambda/{service-name}/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cloudshelf/{service}/
│   │   │       ├── handler/        # Lambda entry points
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/     # Data access layer
│   │   │       ├── model/          # Domain models
│   │   │       ├── dto/            # Data transfer objects
│   │   │       ├── config/         # Configuration classes
│   │   │       ├── exception/      # Custom exceptions
│   │   │       └── util/           # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback.xml
│   └── test/
│       └── java/                   # Unit and integration tests
├── pom.xml                        # Maven configuration
├── README.md                      # Service documentation
└── local-test/                    # Local testing scripts
```

## Coding Standards

### Java Coding Standards

#### Code Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Braces**: Opening brace on same line
- **Import Organization**: Group imports, remove unused

#### Naming Conventions

```java
// Classes: PascalCase
public class BookCatalogHandler

// Methods: camelCase
public List<Book> searchBooks(String query)

// Variables: camelCase
private String bookTitle;

// Constants: UPPER_SNAKE_CASE
private static final int MAX_SEARCH_RESULTS = 100;

// Packages: lowercase with dots
package com.cloudshelf.bookcatalog.handler;
```

#### Documentation Standards

```java
/**
 * Handles book catalog operations for the CloudShelf bookstore.
 *
 * This handler processes API Gateway requests for book search,
 * retrieval, and catalog management operations.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-09-01
 */
public class BookCatalogHandler {

    /**
     * Searches for books based on the provided query criteria.
     *
     * @param query the search query containing title, author, or keywords
     * @param limit maximum number of results to return (default: 20)
     * @return list of books matching the search criteria
     * @throws ValidationException if query parameters are invalid
     * @throws DatabaseException if database connection fails
     */
    public List<Book> searchBooks(String query, int limit) {
        // Implementation by Developer
    }
}
```

### Error Handling Standards

#### Exception Hierarchy

```java
// Base application exception
public abstract class CloudShelfException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    protected CloudShelfException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = new HashMap<>();
    }
}

// Service-specific exceptions
public class BookNotFoundException extends CloudShelfException {
    public BookNotFoundException(String bookId) {
        super("BOOK_NOT_FOUND", "Book with ID " + bookId + " not found");
        getDetails().put("bookId", bookId);
    }
}

public class ValidationException extends CloudShelfException {
    public ValidationException(String field, String message) {
        super("VALIDATION_ERROR", message);
        getDetails().put("field", field);
    }
}
```

#### Error Response Format

```java
public class ErrorResponse {
    private String code;           // Error code (e.g., "BOOK_NOT_FOUND")
    private String message;        // Human-readable message
    private Map<String, Object> details;  // Additional context
    private String timestamp;      // ISO 8601 timestamp
    private String requestId;      // For tracking
}
```

## Testing Standards

### Unit Testing Guidelines

```java
@ExtendWith(MockitoExtension.class)
class BookCatalogHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookCatalogHandler handler;

    @Test
    @DisplayName("Should return books when valid search query provided")
    void shouldReturnBooksForValidQuery() {
        // Given
        String query = "java programming";
        List<Book> expectedBooks = Arrays.asList(
            new Book("1", "Java: The Complete Reference"),
            new Book("2", "Effective Java")
        );
        when(bookRepository.searchBooks(query)).thenReturn(expectedBooks);

        // When
        List<Book> actualBooks = handler.searchBooks(query, 20);

        // Then
        assertThat(actualBooks)
            .hasSize(2)
            .extracting(Book::getTitle)
            .containsExactly("Java: The Complete Reference", "Effective Java");
    }

    @Test
    @DisplayName("Should throw ValidationException when query is null")
    void shouldThrowValidationExceptionForNullQuery() {
        // When/Then
        assertThatThrownBy(() -> handler.searchBooks(null, 20))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Search query cannot be null or empty");
    }
}
```

### Integration Testing Guidelines

- Use Testcontainers for database testing
- Test against actual AWS services in development environment
- Mock external dependencies (API Gateway, CloudWatch)

### Test Coverage Standards

- **Minimum Coverage**: 80% line coverage
- **Branch Coverage**: 70% minimum
- **Critical Paths**: 95% coverage for business logic

## Security Guidelines

### Secrets Management

```java
// ❌ NEVER hardcode credentials
private static final String DB_PASSWORD = "mypassword123";

// ✅ Use environment variables and IAM roles
private String getDbEndpoint() {
    return System.getenv("DB_ENDPOINT");
}

// ✅ Use AWS Secrets Manager for sensitive data
private String getApiKey() {
    return secretsManager.getSecretValue("cloudshelf/api-keys").secretString();
}
```

### Input Validation

```java
public class InputValidator {

    public static void validateBookId(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new ValidationException("bookId", "Book ID cannot be null or empty");
        }
        if (!bookId.matches("^[a-zA-Z0-9-_]{1,50}$")) {
            throw new ValidationException("bookId", "Book ID format is invalid");
        }
    }

    public static void validateSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ValidationException("query", "Search query cannot be null or empty");
        }
        if (query.length() > 255) {
            throw new ValidationException("query", "Search query too long (max 255 characters)");
        }
    }
}
```

### SQL Injection Prevention

```java
// ✅ Use parameterized queries
String sql = "SELECT * FROM books WHERE title LIKE ? AND author LIKE ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, "%" + title + "%");
stmt.setString(2, "%" + author + "%");

// ✅ Use JPA/Hibernate query methods
@Query("SELECT b FROM Book b WHERE b.title LIKE %:title% AND b.author LIKE %:author%")
List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);
```

## Performance Guidelines

### Lambda Optimization

```java
// ✅ Initialize connections outside handler method
public class BookCatalogHandler {
    private final BookService bookService = new BookService();

    public APIGatewayProxyResponseEvent handleRequest(
        APIGatewayProxyRequestEvent request, Context context) {
        // Handler logic here
    }
}

// ✅ Use connection pooling for databases
private static final DataSource dataSource = createConnectionPool();

// ✅ Cache frequently accessed data
@Cacheable(value = "categories", key = "#root.methodName")
public List<Category> getAllCategories() {
    return categoryRepository.findAll();
}
```

### Database Optimization

```java
// ✅ Use proper indexing (DDL by DevOps)
CREATE INDEX idx_books_title_author ON books(title, author);
CREATE INDEX idx_books_category ON books(category_id);

// ✅ Use pagination for large result sets
public Page<Book> searchBooks(String query, Pageable pageable) {
    return bookRepository.findByTitleContaining(query, pageable);
}

// ✅ Use batch operations when appropriate
public void updateBookPrices(List<BookPriceUpdate> updates) {
    bookRepository.saveAll(updates.stream()
        .map(this::updateBookPrice)
        .collect(Collectors.toList()));
}
```

## Logging Guidelines

### Structured Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class BookCatalogHandler {
    private static final Logger logger = LoggerFactory.getLogger(BookCatalogHandler.class);

    public APIGatewayProxyResponseEvent handleRequest(
        APIGatewayProxyRequestEvent request, Context context) {

        // Set request context
        MDC.put("requestId", context.getAwsRequestId());
        MDC.put("functionName", context.getFunctionName());
        MDC.put("httpMethod", request.getHttpMethod());
        MDC.put("path", request.getPath());

        logger.info("Processing book catalog request");

        try {
            // Business logic
            logger.info("Request processed successfully");
            return successResponse;
        } catch (Exception e) {
            logger.error("Request processing failed: {}", e.getMessage(), e);
            return errorResponse;
        } finally {
            MDC.clear();
        }
    }
}
```

### Log Levels

- **ERROR**: Application errors, exceptions
- **WARN**: Potential issues, deprecated usage
- **INFO**: General application flow, business events
- **DEBUG**: Detailed debugging information (dev/staging only)
- **TRACE**: Very detailed tracing (development only)

## Version Control Guidelines

### Branch Strategy

- **main**: Production-ready code
- **develop**: Integration branch for features
- **feature/**: Feature development branches
- **hotfix/**: Emergency production fixes
- **release/**: Release preparation branches

### Commit Message Format

```
type(scope): description

body (optional)

footer (optional)
```

Examples:

```
feat(book-catalog): add advanced search functionality
fix(shopping-cart): resolve cart item duplication issue
docs(architecture): update integration patterns
test(book-catalog): add unit tests for search service
```

### Pull Request Standards

- **Title**: Clear, descriptive summary
- **Description**: What changed and why
- **Testing**: How the change was tested
- **Documentation**: Updates to docs if needed
- **Breaking Changes**: Any backward compatibility issues

## Quality Gates

### Pre-commit Checks (Developer)

- [ ] Code compiles without warnings
- [ ] All unit tests pass
- [ ] Code coverage meets minimum thresholds
- [ ] Static analysis passes (SonarQube)
- [ ] No security vulnerabilities
- [ ] Documentation updated

### Pre-deployment Checks (DevOps)

- [ ] Integration tests pass
- [ ] Performance tests pass
- [ ] Security scans complete
- [ ] Infrastructure tests pass
- [ ] Rollback plan documented

## Monitoring & Alerting

### Key Metrics to Track

- **Lambda Performance**: Duration, errors, throttles
- **Database Performance**: Connection count, query time
- **Business Metrics**: Books searched, carts created
- **API Metrics**: Request count, response times, error rates

### Alert Thresholds

- **Error Rate**: > 1% over 5 minutes
- **Response Time**: > 2 seconds average over 10 minutes
- **Lambda Duration**: > 10 seconds
- **Database Connections**: > 80% of pool

This establishes clear boundaries and standards for each team role while providing concrete guidelines for implementation.
