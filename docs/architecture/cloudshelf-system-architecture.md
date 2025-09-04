# CloudShelf System Architecture

## Overview

CloudShelf is a serverless online bookstore built on AWS, following microservices architecture patterns with event-driven design.

## High-Level Architecture

![CloudShelf Architecture](cloudshelf-architecture-diagram.png)

**📋 Implementation Guides**: Individual setup guides contain practical diagrams and implementation details for each AWS service component.

## Service Breakdown

### Frontend & Content Delivery

- **Purpose**: Serve static website content to users globally
- **Technology**: AWS S3 Static Website Hosting + CloudFront CDN
- **Content**: HTML, CSS, JavaScript files (no framework dependencies)
- **Performance**: Global edge caching with CloudFront for sub-2-second page loads
- **Security**: HTTPS-only access, origin access control from CloudFront to S3
- **Architecture Decision**: Static website hosting chosen for simplicity, cost-effectiveness, and performance

### Book Catalog Service

- **Purpose**: Manage book inventory, search, and catalog operations
- **Technology**: AWS Lambda (Java 21) + PostgreSQL RDS
- **API Endpoints**: `/books/*`
- **Data Storage**: Structured relational data for books, authors, categories
- **Reference**: ADR-002 (PostgreSQL), ADR-004 (Lambda), ADR-005 (Java 21)

### Shopping Cart Service

- **Purpose**: Manage user shopping carts and cart operations
- **Technology**: AWS Lambda (Java 21) + DynamoDB
- **API Endpoints**: `/cart/*`
- **Data Storage**: NoSQL for fast cart operations and user sessions
- **Reference**: ADR-003 (DynamoDB), ADR-004 (Lambda), ADR-005 (Java 21)

## Technology Stack

### Frontend & Content Delivery

- **Frontend**: Static HTML, CSS, JavaScript (Vanilla JS - no framework)
- **Hosting**: AWS S3 Static Website Hosting
- **CDN**: CloudFront for global content delivery
- **Protocol**: HTTPS-only with SSL/TLS certificates

### Runtime & Languages

- **Backend**: Java 21 (LTS) with AWS Lambda
- **Build Tool**: Maven 3.8+
- **Package Management**: Maven Central Repository

### AWS Services

- **Frontend Hosting**: S3 Static Website + CloudFront CDN
- **Compute**: AWS Lambda (serverless functions)
- **API Management**: API Gateway (REST APIs)
- **Databases**:
  - PostgreSQL RDS (structured book data)
  - DynamoDB (cart sessions)
- **Security**: IAM roles, Lambda execution roles
- **Monitoring**: CloudWatch (logs and metrics)

### Development Tools

- **IDE**: IntelliJ IDEA / VS Code
- **Version Control**: Git + GitHub
- **Documentation**: Markdown + Mermaid diagrams

## Data Flow

### Book Search Flow

1. Client → API Gateway → Book Catalog Lambda
2. Lambda → PostgreSQL RDS (query books)
3. RDS → Lambda → API Gateway → Client

### Shopping Cart Flow

1. Client → API Gateway → Shopping Cart Lambda
2. Lambda → DynamoDB (cart operations)
3. DynamoDB → Lambda → API Gateway → Client

## Security Architecture

### Authentication & Authorization

- API Gateway handles CORS and rate limiting
- Lambda execution roles with least privilege
- Database connections via IAM roles (no hardcoded credentials)

### Data Security

- RDS encrypted at rest and in transit
- DynamoDB encrypted at rest
- Secrets managed via AWS Secrets Manager

## Performance & Scalability

### Lambda Scaling

- Auto-scaling based on demand
- Cold start optimization with GraalVM (future consideration)
- Connection pooling for RDS

### Database Scaling

- PostgreSQL RDS with read replicas (future)
- DynamoDB on-demand scaling
- Caching layer considerations (future)

## Deployment Architecture

### Environment Strategy

- **Development**: Individual developer stacks
- **Staging**: Pre-production testing
- **Production**: Multi-AZ deployment

### Infrastructure as Code

- AWS CDK (Cloud Development Kit)
- Separate stacks per environment
- Automated CI/CD pipelines

## Integration Patterns

### API Design

- RESTful API design principles
- JSON request/response format
- HTTP status codes for error handling
- API versioning strategy

### Error Handling

- Standardized error response format
- Lambda error handling patterns
- Dead letter queues for failed requests

### Monitoring & Observability

- CloudWatch logs for all Lambda functions
- CloudWatch metrics for performance monitoring
- Distributed tracing with X-Ray (future)

## Future Considerations

### Phase 2 Enhancements

- User authentication service (Cognito)
- Order processing service
- Payment integration
- Inventory management

### Performance Optimizations

- Caching layer (ElastiCache)
- Caching layer (ElastiCache)
- Database read replicas
- Lambda provisioned concurrency

### Advanced Features

- Real-time notifications (WebSocket API)
- Search optimization (OpenSearch)
- Recommendation engine
- Analytics and reporting
