# 🗂️ DynamoDB Tables Setup

> Implementation guide for DynamoDB shopping cart storage following ADR-003 architecture strategy

This guide provides setup instructions for DynamoDB tables, implementing the NoSQL storage decisions documented in [ADR-003: DynamoDB for Shopping Cart Storage](../cloudshelf-architecture-decisions.md#adr-003-dynamodb-for-shopping-cart-storage).

---

## 🏛️ Architecture Overview

Based on **ADR-003**, DynamoDB provides the shopping cart storage layer for CloudShelf with:

- **🛒 Shopping Cart Storage** - High-performance NoSQL operations for cart data
- **⚡ Fast Access Patterns** - Single-digit millisecond latency for cart operations
- **📈 Serverless Scaling** - Automatic scaling based on demand
- **💰 Cost Efficiency** - Pay-per-request pricing model

**Architecture Decision Reference**: See [ADR-003](../cloudshelf-architecture-decisions.md#adr-003) for the complete rationale behind this NoSQL approach.

---

## 📷 Setup Screenshots

### **�️ DynamoDB Console Overview**

![DynamoDB Console Overview](screenshots/01-dynamodb-console-overview.png)

### **🛒 Shopping Cart Table Creation**

![Create Shopping Cart Table](screenshots/02-create-shopping-cart-table.png)

### **🔑 Primary Key Configuration**

![Primary Key Configuration](screenshots/03-primary-key-configuration.png)

### **⚙️ Table Settings Configuration**

![Table Settings Configuration](screenshots/04-table-settings-configuration.png)

### **🚀 Table Creation Review**

![Table Creation Review](screenshots/05-table-creation-review.png)

### **✅ Shopping Cart Table Created**

![Shopping Cart Table Created Successfully](screenshots/06-shopping-cart-table-created.png)

---

## 🔐 Architecture Configuration

### **📋 Table Design Strategy**

Following ADR-003 polyglot persistence approach:

| Data Pattern      | Storage Solution | Rationale                                             |
| ----------------- | ---------------- | ----------------------------------------------------- |
| **Shopping Cart** | DynamoDB         | High performance, flexible schema, serverless scaling |
| **Book Catalog**  | PostgreSQL RDS   | Complex relationships, ACID transactions              |

### **🗂️ DynamoDB Table Specifications**

| Configuration     | Value                      | Purpose                              |
| ----------------- | -------------------------- | ------------------------------------ |
| **Table Name**    | `cloudshelf-shopping-cart` | Clear naming convention              |
| **Partition Key** | `userId` (String)          | User-centric data organization       |
| **Capacity Mode** | On-demand                  | Variable e-commerce traffic patterns |
| **Encryption**    | AWS Managed                | Security by default                  |

---

## 🚀 Implementation Steps

### **Step 1: Create DynamoDB Table**

1. **🖥️ Access DynamoDB Console**

   - Sign in to AWS Management Console
   - Navigate to DynamoDB service

2. **⚙️ Table Configuration**

   ```
   Table Name: cloudshelf-shopping-cart
   Partition Key: userId (String)
   Capacity Mode: On-demand
   Encryption: AWS managed key
   ```

3. **📊 Advanced Settings**

   - Enable point-in-time recovery (production)
   - Configure deletion protection (production)
   - Set up CloudWatch alarms for monitoring

4. **✅ Create Table**
   - Review configuration settings
   - Click "Create table"

### **Step 2: Verify Table Creation**

1. **🔍 Table Status Check**

   - Verify table status shows "Active"
   - Confirm partition key configuration
   - Validate capacity mode settings

2. **📊 Test Data Operations**
   - Use "Explore table items" for initial testing
   - Verify read/write operations work correctly

---

## 🏗️ Implementation Notes

### **Data Model Pattern**

Following ADR-003 architecture strategy:

```json
{
  "userId": "user123",
  "cartItems": [
    {
      "bookId": "book001",
      "title": "The Great Gatsby",
      "quantity": 2,
      "addedAt": "2025-09-01T10:00:00Z"
    }
  ],
  "lastUpdated": "2025-09-01T10:05:00Z"
}
```

### **Schema Evolution Strategy**

- New attributes can be added without migrations
- Flexible JSON-like document structure
- No downtime for schema changes

### **Performance Optimization**

- User-centric partition key for data locality
- On-demand scaling for variable traffic
- Single-table design for optimal performance

---

## 📚 Related Architecture Documentation

- 🏛️ [**ADR-003: DynamoDB Strategy**](../cloudshelf-architecture-decisions.md#adr-003) - Complete NoSQL architecture rationale
- 🏛️ [**All Architecture Decisions**](../cloudshelf-architecture-decisions.md) - Context for data storage choices
- 🗃️ [**RDS Setup**](../setup-rds.md) - Relational database for catalog operations
- ⚡ [**Lambda Setup**](../lambda/setup-lambda.md) - Compute layer integration

---

_Part of the CloudShelf Solutions Architecture documentation_
