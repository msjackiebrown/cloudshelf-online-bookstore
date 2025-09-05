# ADR-002-REVISED: Phased Data Storage Strategy for CloudShelf

**Date**: 2025-09-05  
**Status**: ✅ Accepted (Revised)  
**Decision Makers**: Solutions Architect  
**Previous Version**: ADR-002 (PostgreSQL + DynamoDB)

---

## Context

The original ADR-002 assumed immediate production deployment with PostgreSQL RDS requiring VPC complexity. For learning and iterative development, we need a simpler starting point that maintains a clear upgrade path to production architecture.

---

## Decision

**Implement a phased data storage strategy** starting with DynamoDB-only for simplicity, with a clear migration path to the production PostgreSQL + DynamoDB architecture.

---

## 🎯 Phase-Based Implementation

### **Phase 1: Learning & MVP (DynamoDB-Only)**

**Storage Strategy**: Single DynamoDB service for all data

| Data Type          | DynamoDB Table      | Purpose             | Design Pattern                          |
| ------------------ | ------------------- | ------------------- | --------------------------------------- |
| **Book Catalog**   | `cloudshelf-books`  | Product information | Partition by category, sort by book ID  |
| **Shopping Carts** | `cloudshelf-carts`  | User cart sessions  | Partition by user ID, TTL for cleanup   |
| **User Accounts**  | `cloudshelf-users`  | Customer profiles   | Partition by user ID                    |
| **Orders**         | `cloudshelf-orders` | Purchase history    | Partition by user ID, sort by timestamp |

**Benefits**:

- ✅ **No VPC required** - Fully managed serverless
- ✅ **Faster setup** - No networking complexity
- ✅ **Cost effective** - Pay per request, no idle costs
- ✅ **Working application** - All functionality available
- ✅ **AWS best practices** - Serverless-first approach

**Limitations**:

- ⚠️ **Limited complex queries** - No SQL joins or aggregations
- ⚠️ **Basic search** - No full-text search capabilities
- ⚠️ **No ACID transactions** - Eventual consistency model

### **Phase 2: Production Evolution (PostgreSQL + DynamoDB)**

**Storage Strategy**: Hybrid approach optimizing each data pattern

| Data Type          | Service        | Rationale                              |
| ------------------ | -------------- | -------------------------------------- |
| **Book Catalog**   | PostgreSQL RDS | Complex queries, search, relationships |
| **Shopping Carts** | DynamoDB       | High performance, flexible schema      |
| **User Accounts**  | PostgreSQL RDS | Structured data, ACID compliance       |
| **Orders**         | PostgreSQL RDS | Transactional integrity, reporting     |

**Migration Benefits**:

- ✅ **Enhanced capabilities** - SQL queries, full-text search
- ✅ **Better performance** - Optimized for each use case
- ✅ **Production security** - VPC isolation for sensitive data
- ✅ **Advanced features** - Complex reporting, analytics

---

## 🚀 **Phase 1 Architecture (Start Here)**

```
Internet Users
    ↓
🌍 CloudFront (CDN)
    ↓
🌐 API Gateway (Public)
    ↓
⚡ Lambda Functions (No VPC)
    ↓
🗂️ DynamoDB (All Data)
```

**Required AWS Services**:

- ✅ **DynamoDB** - 4 tables for all data storage
- ✅ **Lambda** - Public deployment (no VPC)
- ✅ **API Gateway** - Public RESTful APIs
- ✅ **CloudFront** - Content delivery
- ✅ **S3** - Static website hosting

**Infrastructure Complexity**: **Low** - No VPC, networking, or database management

---

## 🏢 **Phase 2 Architecture (Production)**

```
Internet Users
    ↓
🌍 CloudFront (CDN)
    ↓
🌐 API Gateway (Public)
    ↓
⚡ Lambda Functions (VPC Private Subnets)
    ├── 🗃️ PostgreSQL RDS (Books, Users, Orders)
    └── 🗂️ DynamoDB (Shopping Carts)
```

**Additional AWS Services**:

- ✅ **VPC** - Private networking
- ✅ **RDS PostgreSQL** - Relational data
- ✅ **Security Groups** - Network access control
- ✅ **Subnets** - Network isolation

**Infrastructure Complexity**: **High** - Full production security and performance

---

## 📊 **DynamoDB Table Design (Phase 1)**

### **Books Table**

```json
{
  "TableName": "cloudshelf-books",
  "KeySchema": [
    { "AttributeName": "category", "KeyType": "HASH" },
    { "AttributeName": "book_id", "KeyType": "RANGE" }
  ],
  "Attributes": {
    "book_id": "String",
    "title": "String",
    "author": "String",
    "price": "Number",
    "description": "String",
    "isbn": "String",
    "stock_quantity": "Number",
    "image_url": "String",
    "created_at": "String"
  }
}
```

### **Shopping Carts Table**

```json
{
  "TableName": "cloudshelf-carts",
  "KeySchema": [
    { "AttributeName": "user_id", "KeyType": "HASH" },
    { "AttributeName": "cart_id", "KeyType": "RANGE" }
  ],
  "TTL": {
    "AttributeName": "expires_at",
    "Enabled": true
  }
}
```

---

## 🔄 **Migration Strategy (Phase 1 → Phase 2)**

### **Data Migration Approach**

1. **Setup Phase 2 Infrastructure**:

   - Deploy VPC and RDS PostgreSQL
   - Create production Lambda functions

2. **Parallel Data Sync**:

   - Migrate book catalog: DynamoDB → PostgreSQL
   - Keep shopping carts in DynamoDB
   - Migrate users and orders: DynamoDB → PostgreSQL

3. **Traffic Migration**:
   - Blue/green deployment strategy
   - Gradual traffic shifting
   - Rollback capability maintained

### **Migration Benefits**

**From DynamoDB-Only to Hybrid**:

- 📈 **Enhanced Search** - PostgreSQL full-text search
- 🔍 **Complex Queries** - SQL joins and aggregations
- 📊 **Reporting** - Business intelligence capabilities
- 🔒 **Enhanced Security** - VPC network isolation
- ⚡ **Performance Optimization** - Right tool for each data pattern

---

## 🎯 **Recommendation**

### **For Learning CloudShelf**:

**Start with Phase 1** - Get a working application quickly, learn AWS serverless patterns, understand the business domain.

### **For Production Deployment**:

**Implement Phase 2** - Add security, performance, and advanced features when ready for production.

**Timeline Suggestion**:

- **Weeks 1-2**: Phase 1 implementation and testing
- **Weeks 3-4**: Phase 2 infrastructure setup
- **Weeks 5-6**: Data migration and production deployment

---

## 🔗 **Related Documentation**

- 📊 [**DynamoDB Setup Guide**](../dynamodb/cloudshelf-dynamodb-setup.md) - Phase 1 implementation
- 🗃️ [**RDS Setup Guide**](../rds/cloudshelf-rds-setup.md) - Phase 2 migration
- 🌐 [**VPC Setup Guide**](../vpc/cloudshelf-vpc-setup.md) - Phase 2 networking
- 🔄 [**Migration Guide**](cloudshelf-phase-migration-guide.md) - DynamoDB to PostgreSQL

---

_📋 **Documentation Status**: Updated for phased approach | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Phase-based strategy implementation_
