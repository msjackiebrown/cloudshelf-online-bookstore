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
