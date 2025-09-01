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

   ```
   RDS → Requires: VPC, Subnets, Security Groups
   Lambda → Requires: VPC, Subnets, Security Groups (if VPC-enabled)
   ALB → Requires: VPC, Public Subnets, Security Groups
   ```

2. **Security Foundation**: Security groups must exist before resources that use them

   - Database instances require security group references at creation time
   - Lambda functions need security groups for VPC connectivity
   - Load balancers must have security groups defined before deployment

3. **Circular Dependency Prevention**:

   - ❌ **Wrong**: Create RDS → Need Security Group → Need VPC
   - ✅ **Right**: Create VPC → Security Groups → RDS

4. **IP Address Planning**: CIDR blocks must be planned before resource creation

### Best Practice Implementation

**Step 1: VPC and Core Networking**

- Create VPC with CIDR block: `10.0.0.0/16`
- Enable DNS hostnames and DNS support
- Create public subnet: `10.0.1.0/24` (first AZ)
- Create private subnet: `10.0.2.0/24` (second AZ)
- Attach Internet Gateway to VPC
- Configure route tables for public/private subnet routing

**Step 2: Security Groups (All at Once)**

- `cloudshelf-lambda-sg`: Security group for Lambda functions
- `cloudshelf-rds-sg`: Security group for RDS database
  - Allow inbound TCP port 5432 from Lambda security group
  - Deny all other inbound traffic
- `cloudshelf-alb-sg`: Security group for load balancer (if needed)
  - Allow inbound HTTP (80) and HTTPS (443) from internet

**Step 3: Application Resources**

- RDS instance references the database security group
- Lambda functions reference the Lambda security group
- All resources deployed within the established VPC

### Security Group Creation Strategy

**Create ALL security groups in Phase 1**, even for resources not yet created:

- ✅ `cloudshelf-lambda-sg` (for future Lambda functions)
- ✅ `cloudshelf-rds-sg` (for future RDS instance)
- ✅ `cloudshelf-alb-sg` (for future load balancer)

**Benefits:**

- No circular dependencies
- Security rules defined upfront
- Easy resource association later
- Clear security boundaries from start

### Common Mistakes to Avoid

❌ **Don't Do This:**

- Attempting to create RDS instance before VPC and security groups exist
- Creating resources in random order without considering dependencies
- Mixing security group creation with application resource deployment

✅ **Do This Instead:**

- Plan and create VPC foundation first
- Create all security groups before any application resources
- Follow systematic deployment order: Network → Security → Data → Application

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

### Reference Implementation

For CloudShelf project, follow this exact sequence:

1. **First**: Network Foundation (VPC, subnets, gateways, route tables)
2. **Second**: Security Groups (all security groups for future resources)
3. **Third**: Data Layer (RDS instance and DynamoDB tables)
4. **Fourth**: Application Layer (Lambda functions)
5. **Fifth**: API Layer (API Gateway)
6. **Sixth**: Distribution Layer (S3 and CloudFront)

This approach ensures proper dependency management and successful resource deployment.

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

- **Schema Flexibility**: JSON columns for extensible book metadata
- **Search Capabilities**: Built-in full-text search without external dependencies
- **Data Integrity**: Strong referential integrity and constraint enforcement
- **Performance**: Advanced indexing strategies for complex queries

**Integration Considerations**:

- **Lambda Compatibility**: Excellent Python/Node.js PostgreSQL drivers
- **API Performance**: Optimized for REST API query patterns
- **Caching Strategy**: Compatible with Redis/ElastiCache for query caching
- **Analytics**: Supports complex reporting and business intelligence queries

### Migration Path

**Development → Production Evolution**:

- **Phase 1**: Single RDS instance with PostgreSQL
- **Phase 2**: Read replicas for scaling read operations
- **Phase 3**: Consider Aurora PostgreSQL for high-availability requirements
- **Phase 4**: Implement sharding or multi-region if needed

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

**Architectural Validation**:

- ✅ Schema design compatible with both PostgreSQL and MySQL (future flexibility)
- ✅ Query patterns optimized for PostgreSQL capabilities
- ✅ Development team can leverage advanced PostgreSQL features
- ✅ Migration path established for scaling requirements

---
