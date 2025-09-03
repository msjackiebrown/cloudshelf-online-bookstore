# Architecture Decision Records (ADRs)

This document captures key architecture decisions made for the CloudShelf Online Bookstore project. Each decision includes context, options considered, rationale, and consequences.

---

## ADR-001: VPC Creation Timing and Strategy

**Date**: 2025-09-01  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to determine when to create the VPC and networking components in relation to other AWS resources, considering dependencies and best practices for infrastructure provisioning.

### Decision

**Create VPC and networking foundation FIRST**, before any application resources.

### Implementation Order

```
Phase 1: Network Foundation (FIRST)
├── 1. VPC Creation
├── 2. Subnets (Public & Private)
├── 3. Internet Gateway
├── 4. Route Tables
├── 5. Security Groups (All needed groups)
└── 6. NAT Gateway (if required)

Phase 2: Data Layer (SECOND)
├── 7. RDS Subnet Groups
├── 8. RDS Instance
└── 9. DynamoDB Tables

Phase 3: Application Layer (THIRD)
├── 10. Lambda Functions
├── 11. API Gateway
└── 12. S3 Buckets

Phase 4: Distribution Layer (FOURTH)
└── 13. CloudFront Distribution
```

### Options Considered

| Approach                 | Pros                                                            | Cons                                     | Decision |
| ------------------------ | --------------------------------------------------------------- | ---------------------------------------- | -------- |
| **VPC First (Chosen)**   | No circular dependencies, security from start, clear foundation | Upfront network planning required        | ✅       |
| **Resource-by-Resource** | Incremental approach, less planning                             | Dependency conflicts, security gaps      | ❌       |
| **All-at-Once**          | Complete infrastructure                                         | Complex troubleshooting, harder rollback | ❌       |

### Rationale

**Why VPC Must Come First:**

1. **Dependency Chain**: Most AWS resources require VPC/subnet references
2. **Security Foundation**: Security groups must exist before resources that use them
3. **Circular Dependency Prevention**: VPC → Security Groups → Application Resources
4. **IP Address Planning**: CIDR blocks must be planned before resource creation

### Architecture Strategy

**Network Foundation First**:

- VPC and subnets provide the network foundation
- Security groups define access controls
- All application resources deploy within established boundaries

**Security Group Strategy**:

- Create all security groups during network foundation phase
- Define security rules before deploying application resources
- Avoid circular dependencies between resources and security groups

### Consequences

**Positive:**

- ✅ No dependency conflicts during infrastructure creation
- ✅ Security designed from the ground up
- ✅ Clear network boundaries established early
- ✅ Easier to troubleshoot and modify individual components
- ✅ Supports modular infrastructure deployment strategy

**Considerations:**

- ⚠️ Requires upfront network planning and CIDR design
- ⚠️ Initial complexity before seeing application functionality
- ⚠️ Must understand networking concepts before starting

---

## ADR-002: RDS Database Engine Selection

**Date**: 2025-09-01  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to select an appropriate relational database engine for the CloudShelf book catalog that balances functionality, performance, cost, and team expertise while supporting future scalability requirements.

### Decision

**PostgreSQL** is selected as the database engine for the CloudShelf RDS instance.

### Options Considered

| Engine                | Pros                                                                          | Cons                                                  | Decision      |
| --------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------- | ------------- |
| **PostgreSQL**        | Advanced features (JSON, indexing, extensions), open source, strong community | Slightly steeper learning curve                       | ✅ **Chosen** |
| **MySQL**             | Widely used, simple, fast for read-heavy workloads                            | Fewer advanced features, limited JSON support         | ❌            |
| **Aurora PostgreSQL** | Managed, highly available, auto-scaling, PostgreSQL compatible                | Higher cost, AWS-specific, overkill for initial scale | ❌            |
| **Aurora MySQL**      | Managed, highly available, auto-scaling, MySQL compatible                     | Higher cost, AWS-specific, limited advanced features  | ❌            |
| **Oracle/SQL Server** | Enterprise features, legacy support                                           | Expensive licensing, overkill for this use case       | ❌            |

### Rationale

**Why PostgreSQL:**

1. **Advanced Feature Set**:

   - Native JSON/JSONB support for flexible data structures
   - Advanced indexing capabilities (GIN, GiST, partial indexes)
   - Full-text search capabilities with tsvector
   - Extensible with custom functions and data types

2. **Future-Proof Architecture**:

   - JSON fields enable schema evolution without migrations
   - Advanced search capabilities can replace external search services
   - Strong ACID compliance for transactional integrity
   - Supports both relational and semi-structured data patterns

3. **Cost and Performance**:

   - Open source with no licensing costs
   - Excellent performance for read-heavy workloads
   - Efficient query planning and optimization
   - Well-suited for AWS RDS deployment

4. **Ecosystem and Support**:
   - Strong community and extensive documentation
   - Rich ecosystem of tools and extensions
   - Compatible with modern development frameworks
   - Long-term stability and active development

### Architecture Implications

**Database Design Benefits**:

- Schema flexibility with JSON columns for extensible book metadata
- Built-in full-text search capabilities without external dependencies
- Strong referential integrity and constraint enforcement
- Advanced indexing strategies for complex queries

**Integration Considerations**:

- Excellent compatibility with serverless Lambda functions
- Optimized for REST API query patterns
- Compatible with caching strategies (Redis/ElastiCache)
- Supports complex reporting and analytics queries

### Migration Path

**Scalability Evolution**:

- Phase 1: Single RDS instance with PostgreSQL
- Phase 2: Read replicas for scaling read operations
- Phase 3: Consider Aurora PostgreSQL for high-availability requirements
- Phase 4: Implement sharding or multi-region if needed

### Consequences

**Positive**:

- ✅ Advanced features support complex business requirements
- ✅ JSON support enables schema evolution without downtime
- ✅ Full-text search capabilities reduce external dependencies
- ✅ Strong ACID compliance ensures data integrity
- ✅ Cost-effective open source licensing
- ✅ Excellent AWS RDS integration and support

**Considerations**:

- ⚠️ Slightly higher learning curve compared to MySQL
- ⚠️ May be over-engineered for very simple use cases
- ⚠️ Requires PostgreSQL-specific optimization knowledge

---

## ADR-003: DynamoDB for Shopping Cart Storage

**Date**: 2025-09-01  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to select an appropriate storage solution for shopping cart data that provides fast performance, flexible schema evolution, and cost-effective scaling for user session data.

### Decision

**DynamoDB** is selected for shopping cart storage, complementing PostgreSQL RDS for the book catalog.

### Options Considered

| Storage Option    | Pros                                                  | Cons                                       | Decision      |
| ----------------- | ----------------------------------------------------- | ------------------------------------------ | ------------- |
| **DynamoDB**      | Fast performance, flexible schema, serverless scaling | NoSQL learning curve, eventual consistency | ✅ **Chosen** |
| **RDS (same DB)** | Single database, ACID transactions, familiar SQL      | Performance impact, rigid schema           | ❌            |
| **ElastiCache**   | Very fast, good for session data                      | Volatile, requires backup strategy         | ❌            |
| **S3**            | Cost-effective, durable                               | Not designed for frequent updates          | ❌            |

### Rationale

**Why DynamoDB for Shopping Carts:**

1. **Performance Characteristics**:

   - Single-digit millisecond latency for cart operations
   - Predictable performance at any scale
   - Auto-scaling based on demand
   - No connection pooling or database connection management

2. **Schema Flexibility**:

   - Shopping cart requirements evolve frequently
   - Easy to add new attributes without schema migrations
   - JSON-like document structure supports complex cart data

3. **Cost Efficiency**:

   - Pay-per-request pricing model
   - No idle database costs
   - Automatic scaling eliminates over-provisioning

4. **Operational Benefits**:
   - Fully managed service (no maintenance)
   - Built-in security and encryption
   - Integration with AWS Lambda and API Gateway

### Architecture Integration

**Data Separation Strategy**:

| Use Case      | Service  | Why?                                                               |
| ------------- | -------- | ------------------------------------------------------------------ |
| Book Catalog  | RDS      | Structured data, relationships, complex queries, ACID transactions |
| Shopping Cart | DynamoDB | High performance, flexible schema, easy to evolve                  |

### Data Model Design

**DynamoDB Table Architecture**:

- **Partition Key**: `userId` (ensures user cart data is co-located)
- **Attributes**: Flexible JSON structure for cart contents
- **Capacity Mode**: On-demand for variable traffic patterns
- **Schema Evolution**: New attributes can be added without migrations

### Consequences

**Positive**:

- ✅ Fast, predictable performance for cart operations
- ✅ Schema flexibility supports evolving requirements
- ✅ Cost-effective scaling with usage patterns
- ✅ No database maintenance overhead
- ✅ Seamless integration with serverless architecture
- ✅ Natural separation of concerns (catalog vs. cart)

**Considerations**:

- ⚠️ Eventually consistent reads (strong consistency available when needed)
- ⚠️ NoSQL query patterns different from SQL
- ⚠️ Cross-service data consistency requires careful design

**Risk Mitigation**:

- Strong consistency available for critical cart operations
- Book catalog data remains in RDS for complex queries
- Cart and catalog integration handled at application layer

---

## Data Storage Architecture Summary

The CloudShelf application uses a **polyglot persistence** approach, selecting the optimal database for each use case:

### RDS PostgreSQL (Book Catalog)

- **Strengths**: Complex relationships, ACID transactions, advanced queries
- **Use Cases**: Book inventory, user accounts, order history, analytics
- **Schema**: Normalized relational design with JSON flexibility

### DynamoDB (Shopping Cart)

- **Strengths**: High performance, flexible schema, serverless scaling
- **Use Cases**: Active shopping carts, user sessions, temporary data
- **Schema**: Document-based with user-centric partition design

This hybrid approach optimizes for both **data integrity** (RDS) and **performance** (DynamoDB) while maintaining cost efficiency and operational simplicity.

---

## ADR-004: Lambda Serverless Architecture Strategy

**Date**: 2025-09-01  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to define the compute architecture for CloudShelf application logic, considering scalability, cost efficiency, operational overhead, and integration with chosen data storage solutions (PostgreSQL RDS and DynamoDB).

### Decision

**AWS Lambda serverless functions** are selected as the primary compute architecture for CloudShelf application logic.

### Options Considered

| Compute Option    | Pros                                                   | Cons                                               | Decision      |
| ----------------- | ------------------------------------------------------ | -------------------------------------------------- | ------------- |
| **AWS Lambda**    | Serverless scaling, pay-per-use, no server management  | Cold starts, execution time limits                 | ✅ **Chosen** |
| **EC2 Instances** | Full control, persistent connections, no time limits   | Server management, fixed costs, scaling complexity | ❌            |
| **ECS/Fargate**   | Container orchestration, better for long-running tasks | More complex, container management overhead        | ❌            |
| **EKS**           | Kubernetes ecosystem, advanced orchestration           | High complexity, operational overhead              | ❌            |

### Rationale

**Why Lambda for CloudShelf:**

1. **Serverless Benefits**:

   - Automatic scaling from zero to thousands of concurrent executions
   - Pay-per-request pricing model aligns with variable e-commerce traffic
   - No server provisioning, patching, or maintenance overhead
   - Built-in fault tolerance and high availability

2. **Integration Advantages**:

   - Native integration with API Gateway for RESTful APIs
   - Direct SDK access to DynamoDB for cart operations
   - VPC connectivity for secure RDS PostgreSQL access
   - Seamless integration with Cognito for authentication

3. **Operational Efficiency**:
   - Automatic scaling handles traffic spikes (holiday shopping, promotions)
   - Development team focuses on business logic, not infrastructure
   - Built-in monitoring and logging with CloudWatch
   - Version management and rollback capabilities

### Architecture Pattern

**Function Organization Strategy**:

| Function Purpose        | Integration Pattern             | Data Access                                |
| ----------------------- | ------------------------------- | ------------------------------------------ |
| **Book Catalog API**    | API Gateway → Lambda → RDS      | PostgreSQL for complex queries             |
| **Shopping Cart API**   | API Gateway → Lambda → DynamoDB | NoSQL for high-performance cart operations |
| **User Authentication** | API Gateway → Lambda → Cognito  | JWT validation and user management         |
| **Order Processing**    | Lambda → RDS + DynamoDB         | Cross-service coordination                 |

**Security Architecture**:

- Lambda functions in VPC private subnets for database access
- IAM roles with least-privilege permissions per function
- Environment variables for secure configuration management
- Security groups controlling network access patterns

### Development Strategy

**Infrastructure-First Approach**:

- Create placeholder Lambda functions with basic handlers
- Establish all IAM roles, VPC connectivity, and API Gateway integrations
- Validate infrastructure and security before code delivery
- Enable parallel development of infrastructure and application logic

**Function Design Principles**:

- Single responsibility per Lambda function
- Stateless design for optimal scaling
- Connection pooling for database efficiency
- Proper error handling and retry logic

### Performance Considerations

**Optimization Strategies**:

- Memory allocation sizing based on function requirements
- Provisioned concurrency for critical, latency-sensitive functions
- Connection reuse across Lambda invocations
- Timeout configuration appropriate for operation complexity

**Cold Start Mitigation**:

- Keep functions warm for critical paths
- Optimize function initialization code
- Use Java 21 runtime optimizations for startup performance

### Consequences

**Positive**:

- ✅ Automatic scaling handles variable e-commerce traffic patterns
- ✅ Cost efficiency with pay-per-use pricing model
- ✅ Reduced operational overhead and infrastructure management
- ✅ Fast development and deployment cycles
- ✅ Native integration with AWS services (API Gateway, DynamoDB, RDS)
- ✅ Built-in monitoring, logging, and observability

**Considerations**:

- ⚠️ Cold start latency for infrequently accessed functions
- ⚠️ 15-minute execution time limit for long-running processes
- ⚠️ Vendor lock-in to AWS Lambda platform
- ⚠️ Debugging complexity in distributed serverless environment

**Risk Mitigation**:

- Provisioned concurrency for critical user-facing functions
- Break down long processes into smaller, chained functions
- Implement comprehensive logging and distributed tracing
- Design for portability using standard interfaces where possible

### Deployment and Monitoring

**Deployment Patterns**:

- Blue/green deployments using Lambda aliases
- Canary releases for gradual traffic shifting
- Environment-specific configurations (dev/staging/production)

**Observability Strategy**:

- CloudWatch metrics for performance monitoring
- X-Ray tracing for distributed request tracking
- Custom business metrics for cart and catalog operations
- Alarms for error rates and performance thresholds

---

## ADR-005: Java 21 Programming Language Selection

**Date**: 2025-09-01  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to select a programming language and runtime for CloudShelf Lambda functions that balances performance, developer productivity, ecosystem maturity, and long-term maintainability while integrating effectively with AWS services and chosen data stores.

### Decision

**Java 21 (Amazon Corretto)** is selected as the primary programming language for CloudShelf Lambda functions.

### Options Considered

| Language        | Pros                                                                 | Cons                                                     | Decision      |
| --------------- | -------------------------------------------------------------------- | -------------------------------------------------------- | ------------- |
| **Java 21**     | Enterprise-grade, strong typing, excellent AWS SDK, mature ecosystem | Larger deployment packages, potential cold start impact  | ✅ **Chosen** |
| **Python 3.11** | Fast startup, concise syntax, extensive libraries                    | Dynamic typing, runtime errors, performance limitations  | ❌            |
| **Node.js 18**  | Fast startup, JSON-native, JavaScript expertise                      | Single-threaded, callback complexity, type safety issues | ❌            |
| **Go**          | Fast startup, compiled, efficient memory usage                       | Smaller ecosystem, less enterprise tooling               | ❌            |
| **C# .NET**     | Strong typing, excellent tooling, good performance                   | Microsoft ecosystem lock-in, less AWS community          | ❌            |

### Rationale

**Why Java 21 for CloudShelf:**

1. **Enterprise Readiness**:

   - Strong type system reduces runtime errors and improves code reliability
   - Mature ecosystem with extensive libraries for e-commerce functionality
   - Enterprise-grade tooling and debugging capabilities
   - Proven track record in large-scale, mission-critical applications

2. **AWS Integration Excellence**:

   - AWS SDK for Java provides comprehensive service coverage
   - Native support for AWS Lambda with optimized runtime
   - Excellent integration with RDS PostgreSQL via JDBC drivers
   - Strong DynamoDB SDK with type-safe operations

3. **Performance Characteristics**:

   - JVM optimizations provide excellent runtime performance
   - Efficient memory management for long-running Lambda functions
   - Connection pooling capabilities for database operations
   - Compiled bytecode reduces interpretation overhead

4. **Development Productivity**:

   - Strong IDE support with IntelliJ IDEA and VS Code
   - Comprehensive testing frameworks (JUnit, Mockito, TestContainers)
   - Build tooling with Maven/Gradle for dependency management
   - Static analysis tools for code quality and security

5. **Long-term Maintainability**:
   - Java 21 LTS provides long-term support and stability
   - Large developer talent pool for team scaling
   - Extensive documentation and community resources
   - Backward compatibility ensures future upgrade paths

### Architecture Integration

**Lambda Function Structure**:

```
Handler Pattern: com.cloudshelf.[module].Handler
Runtime: Java 21 (Amazon Corretto)
Packaging: JAR with dependencies
Build Tool: Maven or Gradle
```

**Database Integration Patterns**:

| Data Store         | Integration Approach         | Java Libraries                       |
| ------------------ | ---------------------------- | ------------------------------------ |
| **PostgreSQL RDS** | JDBC with connection pooling | HikariCP, PostgreSQL JDBC            |
| **DynamoDB**       | AWS SDK with enhanced client | AWS SDK v2, DynamoDB Enhanced Client |
| **Cognito**        | JWT validation libraries     | JJWT, AWS Cognito SDK                |

**Performance Optimizations**:

- Connection pooling for RDS to minimize connection overhead
- DynamoDB Enhanced Client for type-safe operations
- GraalVM native image compilation for reduced cold starts (future consideration)
- Proper memory allocation sizing for JVM heap management

### Development Strategy

**Build and Deployment**:

- Maven-based build system with AWS-specific plugins
- JAR packaging with dependencies for Lambda deployment
- Environment-specific configuration via environment variables
- Automated testing with unit tests and integration tests

**Code Organization**:

- Domain-driven design with clear module boundaries
- Separation of concerns between business logic and AWS service integration
- Dependency injection for testability and maintainability
- Standardized error handling and logging patterns

### Cold Start Mitigation

**JVM Optimization Strategies**:

- Provisioned concurrency for critical user-facing functions
- Optimize JAR size by excluding unnecessary dependencies
- Use AWS Lambda SnapStart for improved initialization performance
- Consider GraalVM native images for future optimization

**Application-Level Optimizations**:

- Lazy initialization of heavy resources
- Static initialization of AWS service clients
- Connection pooling with persistent connections across invocations

### Consequences

**Positive**:

- ✅ Strong type safety reduces runtime errors and improves reliability
- ✅ Excellent AWS service integration with comprehensive SDK support
- ✅ Enterprise-grade tooling and development experience
- ✅ Large talent pool and extensive community resources
- ✅ Long-term stability with Java 21 LTS support
- ✅ Superior performance for compute-intensive operations
- ✅ Robust testing and debugging capabilities

**Considerations**:

- ⚠️ Larger deployment package sizes compared to interpreted languages
- ⚠️ Potential cold start latency due to JVM initialization
- ⚠️ Memory overhead requires careful Lambda memory allocation
- ⚠️ Build complexity compared to simpler scripting languages

**Risk Mitigation**:

- Use provisioned concurrency for latency-sensitive functions
- Optimize JAR packaging and dependency management
- Implement proper memory sizing based on function requirements
- Consider AWS Lambda SnapStart for improved cold start performance

### Technology Stack Integration

**Complete CloudShelf Stack**:

- **Compute**: AWS Lambda with Java 21 (Corretto)
- **API**: API Gateway with Lambda proxy integration
- **Data**: PostgreSQL RDS + DynamoDB
- **Security**: IAM roles + Cognito authentication
- **Monitoring**: CloudWatch + X-Ray tracing

This Java-first approach ensures enterprise-grade reliability, maintainability, and performance for the CloudShelf e-commerce platform.

---

## ADR-006: IAM Policy Management Strategy

**Date**: 2025-09-03  
**Status**: ✅ Accepted  
**Decision Makers**: Solutions Architect

### Context

Need to determine the approach for managing IAM policies in CloudShelf architecture, considering governance, reusability, development workflow, and production requirements. The choice impacts security management, compliance auditing, and operational maintenance.

### Decision

**Use Customer-Managed Policies for production deployment**, with a phased approach:

1. **Development Phase**: Start with inline policies for rapid iteration
2. **Production Phase**: Migrate to customer-managed policies for governance

### Policy Creation Order

```
Phase 1: Create Custom Policies (FIRST)
├── 1. CloudShelf-RDS-BookCatalog-Access
├── 2. CloudShelf-DynamoDB-ShoppingCart-Access
├── 3. CloudShelf-Lambda-Invoke-Access
├── 4. CloudShelf-S3-Assets-Access
└── 5. CloudShelf-CloudWatch-Monitoring-Access

Phase 2: Create IAM Roles (SECOND)
├── 6. cloudshelf-book-catalog-lambda-role
├── 7. cloudshelf-shopping-cart-lambda-role
├── 8. cloudshelf-apigateway-execution-role
└── 9. Attach custom + AWS managed policies to roles

Phase 3: Configure Resource-Based Policies (THIRD)
├── 10. Lambda resource policies for API Gateway
├── 11. S3 bucket policies for CloudFront
└── 12. Security group configurations
```

### Options Considered

| Approach                     | Pros                                      | Cons                                           | Decision |
| ---------------------------- | ----------------------------------------- | ---------------------------------------------- | -------- |
| **Inline Policies Only**     | Simple, fast development, tightly coupled | Not reusable, hard to audit, poor governance   | ❌       |
| **Customer-Managed Only**    | Reusable, versioned, better governance    | More setup overhead, complex for development   | ❌       |
| **Phased Approach (Chosen)** | Fast development + production governance  | Requires migration step                        | ✅       |
| **AWS Managed Only**         | No maintenance, well-tested               | Too broad permissions, not CloudShelf-specific | ❌       |

### Rationale

**Why Phased Approach**:

1. **Development Velocity**: Inline policies allow rapid iteration during development
2. **Production Governance**: Customer-managed policies provide enterprise-grade governance
3. **Security Compliance**: Easier to audit and review centralized policies
4. **Policy Reusability**: Custom policies can be shared across similar roles
5. **Version Control**: Policy changes can be tracked and approved
6. **Least Privilege**: Granular control over specific CloudShelf resources

### Implementation Strategy

**Development Phase (Now)**:

```json
{
  "PolicyType": "Inline",
  "AttachedTo": "Role directly",
  "Benefits": ["Fast iteration", "Simple testing", "Rapid prototyping"],
  "Limitations": ["Not reusable", "Hard to audit", "No versioning"]
}
```

**Production Phase (Later)**:

```json
{
  "PolicyType": "Customer-Managed",
  "NamingConvention": "CloudShelf-{Service}-{Resource}-Access",
  "Benefits": ["Reusable", "Versioned", "Auditable", "Governance"],
  "Requirements": ["Approval workflow", "Version control", "Regular reviews"]
}
```

### Consequences

**Positive**:

- ✅ Faster development and testing cycles
- ✅ Production-ready governance model
- ✅ Clear migration path from dev to prod
- ✅ Better security compliance for enterprise
- ✅ Easier policy auditing and review

**Negative**:

- ⚠️ Requires migration step before production
- ⚠️ Additional overhead in production policy management
- ⚠️ Need to maintain both approaches temporarily

**Mitigation**:

- Document clear migration procedures
- Automate policy creation with Infrastructure as Code
- Implement policy validation and testing

### Monitoring

**Success Metrics**:

- Policy deployment time < 5 minutes
- Zero security incidents related to overprivileged access
- 100% policy compliance in production
- Policy review cycle completed quarterly

**Review Triggers**:

- New AWS service integration
- Security incident or audit finding
- Quarterly security review
- Major application architecture changes

---
