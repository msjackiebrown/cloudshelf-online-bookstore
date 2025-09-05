# 🗂️ CloudShelf DynamoDB Setup (Phase 1)

> Simple, serverless data storage implementation for learning and MVP deployment

This guide provides setup instructions for DynamoDB-only data storage, implementing the Phase 1 strategy from [ADR-002-Revised: Phased Data Storage Strategy](../cloudshelf-adr-002-revised-phased-data-storage.md).

---

## 🎯 Phase 1 Overview

### **🚀 Why Start with DynamoDB-Only?**

**Learning Benefits**:
- ✅ **No VPC complexity** - Fully managed, serverless
- ✅ **Faster setup** - Get working app in hours, not days
- ✅ **Cost effective** - Pay per request, no idle database costs
- ✅ **AWS best practices** - Learn serverless-first patterns
- ✅ **Real application** - Full CloudShelf functionality

**What You Get**:
- 📚 **Book catalog browsing** and search
- 🛒 **Shopping cart** functionality  
- 👤 **User accounts** and authentication
- 📦 **Order processing** and history
- 🌐 **Public API** endpoints

---

## 🏛️ Architecture Overview

### **🗂️ Phase 1 DynamoDB Architecture**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        CloudShelf Phase 1 Architecture                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  Internet Users                                                                │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        🌍 CloudFront CDN                                │   │
│  │                    (Global Content Delivery)                           │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                      🌐 API Gateway (Public)                           │   │
│  │                   RESTful APIs - No VPC Required                       │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                    ⚡ Lambda Functions (Public)                        │   │
│  │                                                                         │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │   │
│  │  │Book Catalog │  │Shopping Cart│  │User Accounts│  │Order Process│   │   │
│  │  │   Lambda    │  │   Lambda    │  │   Lambda    │  │   Lambda    │   │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                      🗂️ DynamoDB Tables (Managed)                      │   │
│  │                                                                         │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │   │
│  │  │cloudshelf-  │  │cloudshelf-  │  │cloudshelf-  │  │cloudshelf-  │   │   │
│  │  │books        │  │carts        │  │users        │  │orders       │   │   │
│  │  │             │  │             │  │             │  │             │   │   │
│  │  │• Category   │  │• User ID    │  │• User ID    │  │• User ID    │   │   │
│  │  │• Book ID    │  │• Cart ID    │  │• Profile    │  │• Order ID   │   │   │
│  │  │• Title      │  │• Items      │  │• Email      │  │• Items      │   │   │
│  │  │• Author     │  │• TTL        │  │• Created    │  │• Total      │   │   │
│  │  │• Price      │  │             │  │             │  │• Status     │   │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  External Services (Also No VPC Required):                                     │
│  • 📦 S3 Bucket (Book images, static assets)                                   │
│  • 🔐 Cognito (User authentication - optional)                                 │
│  • 📧 SES (Email notifications - optional)                                     │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Key Benefits**:
- 🚫 **No VPC Required** - All services are AWS-managed and public
- ⚡ **Serverless Scale** - Auto-scaling based on demand
- 💰 **Cost Efficient** - Pay only for what you use
- 🔧 **Easy Maintenance** - No infrastructure management

---

## 📊 DynamoDB Table Design

### **Table 1: Books Catalog (`cloudshelf-books`)**

**Purpose**: Store all book information for browsing and purchasing

```json
{
  "TableName": "cloudshelf-books",
  "BillingMode": "PAY_PER_REQUEST",
  "KeySchema": [
    {"AttributeName": "category", "KeyType": "HASH"},
    {"AttributeName": "book_id", "KeyType": "RANGE"}
  ],
  "AttributeDefinitions": [
    {"AttributeName": "category", "AttributeType": "S"},
    {"AttributeName": "book_id", "AttributeType": "S"},
    {"AttributeName": "title", "AttributeType": "S"}
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "TitleSearchIndex",
      "KeySchema": [
        {"AttributeName": "title", "KeyType": "HASH"}
      ],
      "Projection": {"ProjectionType": "ALL"}
    }
  ]
}
```

**Sample Book Item**:
```json
{
  "category": "Technology",
  "book_id": "tech-001",
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "price": 42.99,
  "stock_quantity": 25,
  "description": "A handbook of agile software craftsmanship",
  "image_url": "https://cloudshelf-images.s3.amazonaws.com/clean-code.jpg",
  "created_at": "2025-09-05T10:00:00Z",
  "updated_at": "2025-09-05T10:00:00Z"
}
```

### **Table 2: Shopping Carts (`cloudshelf-carts`)**

**Purpose**: Store user shopping cart sessions with automatic cleanup

```json
{
  "TableName": "cloudshelf-carts",
  "BillingMode": "PAY_PER_REQUEST",
  "KeySchema": [
    {"AttributeName": "user_id", "KeyType": "HASH"},
    {"AttributeName": "cart_id", "KeyType": "RANGE"}
  ],
  "AttributeDefinitions": [
    {"AttributeName": "user_id", "AttributeType": "S"},
    {"AttributeName": "cart_id", "AttributeType": "S"}
  ],
  "TimeToLiveSpecification": {
    "AttributeName": "expires_at",
    "Enabled": true
  }
}
```

**Sample Cart Item**:
```json
{
  "user_id": "user-12345",
  "cart_id": "cart-2025-09-05",
  "items": [
    {
      "book_id": "tech-001",
      "category": "Technology",
      "title": "Clean Code",
      "price": 42.99,
      "quantity": 1
    }
  ],
  "total_amount": 42.99,
  "item_count": 1,
  "created_at": "2025-09-05T10:00:00Z",
  "updated_at": "2025-09-05T10:30:00Z",
  "expires_at": 1725811200
}
```

### **Table 3: User Accounts (`cloudshelf-users`)**

**Purpose**: Store customer profile and account information

```json
{
  "TableName": "cloudshelf-users",
  "BillingMode": "PAY_PER_REQUEST",
  "KeySchema": [
    {"AttributeName": "user_id", "KeyType": "HASH"}
  ],
  "AttributeDefinitions": [
    {"AttributeName": "user_id", "AttributeType": "S"},
    {"AttributeName": "email", "AttributeType": "S"}
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "EmailIndex",
      "KeySchema": [
        {"AttributeName": "email", "KeyType": "HASH"}
      ],
      "Projection": {"ProjectionType": "ALL"}
    }
  ]
}
```

**Sample User Item**:
```json
{
  "user_id": "user-12345",
  "email": "customer@example.com",
  "first_name": "John",
  "last_name": "Doe",
  "phone": "+1-555-0123",
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "ST",
    "postal_code": "12345",
    "country": "US"
  },
  "created_at": "2025-09-05T10:00:00Z",
  "last_login": "2025-09-05T10:00:00Z",
  "account_status": "active"
}
```

### **Table 4: Orders (`cloudshelf-orders`)**

**Purpose**: Store order history and transaction records

```json
{
  "TableName": "cloudshelf-orders",
  "BillingMode": "PAY_PER_REQUEST",
  "KeySchema": [
    {"AttributeName": "user_id", "KeyType": "HASH"},
    {"AttributeName": "order_id", "KeyType": "RANGE"}
  ],
  "AttributeDefinitions": [
    {"AttributeName": "user_id", "AttributeType": "S"},
    {"AttributeName": "order_id", "AttributeType": "S"},
    {"AttributeName": "order_date", "AttributeType": "S"}
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "OrderDateIndex",
      "KeySchema": [
        {"AttributeName": "order_date", "KeyType": "HASH"},
        {"AttributeName": "order_id", "KeyType": "RANGE"}
      ],
      "Projection": {"ProjectionType": "ALL"}
    }
  ]
}
```

**Sample Order Item**:
```json
{
  "user_id": "user-12345",
  "order_id": "order-2025-09-05-001",
  "order_date": "2025-09-05",
  "items": [
    {
      "book_id": "tech-001",
      "title": "Clean Code",
      "price": 42.99,
      "quantity": 1
    }
  ],
  "subtotal": 42.99,
  "tax": 3.44,
  "shipping": 5.99,
  "total": 52.42,
  "status": "confirmed",
  "shipping_address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "ST",
    "postal_code": "12345"
  },
  "created_at": "2025-09-05T10:00:00Z",
  "updated_at": "2025-09-05T10:00:00Z"
}
```

---

## 🚀 Implementation Guide

### **Step 1: Create DynamoDB Tables**

Navigate to the DynamoDB console and create each table:

#### **Create Books Table**

1. **Go to DynamoDB Console** → **Tables** → **Create table**
2. **Configure table**:
   - **Table name**: `cloudshelf-books`
   - **Partition key**: `category` (String)
   - **Sort key**: `book_id` (String)
   - **Billing mode**: Pay-per-request

![DynamoDB Books Table Creation](screenshots/dynamodb-books-table-creation.png)
_Configure the books table with category and book_id as keys_

3. **Add Global Secondary Index**:
   - **Index name**: `TitleSearchIndex`
   - **Partition key**: `title` (String)
   - **Projection**: All attributes

![DynamoDB Books GSI Configuration](screenshots/dynamodb-books-gsi-configuration.png)
_Add title search index for book lookup functionality_

#### **Create Shopping Carts Table**

1. **Create table**:
   - **Table name**: `cloudshelf-carts`
   - **Partition key**: `user_id` (String)
   - **Sort key**: `cart_id` (String)
   - **Billing mode**: Pay-per-request

2. **Enable TTL**:
   - **TTL attribute**: `expires_at`
   - **Status**: Enabled

![DynamoDB Carts Table with TTL](screenshots/dynamodb-carts-table-ttl.png)
_Shopping carts table with TTL for automatic cleanup_

#### **Create Users Table**

1. **Create table**:
   - **Table name**: `cloudshelf-users`
   - **Partition key**: `user_id` (String)
   - **Billing mode**: Pay-per-request

2. **Add Global Secondary Index**:
   - **Index name**: `EmailIndex`
   - **Partition key**: `email` (String)
   - **Projection**: All attributes

![DynamoDB Users Table with Email Index](screenshots/dynamodb-users-email-index.png)
_Users table with email lookup capability_

#### **Create Orders Table**

1. **Create table**:
   - **Table name**: `cloudshelf-orders`
   - **Partition key**: `user_id` (String)
   - **Sort key**: `order_id` (String)
   - **Billing mode**: Pay-per-request

2. **Add Global Secondary Index**:
   - **Index name**: `OrderDateIndex`
   - **Partition key**: `order_date` (String)
   - **Sort key**: `order_id` (String)
   - **Projection**: All attributes

![DynamoDB Orders Table with Date Index](screenshots/dynamodb-orders-date-index.png)
_Orders table with date-based querying capability_

---

### **Step 2: Populate Sample Data**

#### **Add Sample Books**

Use the DynamoDB console or AWS CLI to add sample book data:

```json
[
  {
    "category": "Fiction",
    "book_id": "fic-001",
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "isbn": "9780743273565",
    "price": 12.99,
    "stock_quantity": 50,
    "description": "Classic American novel about the Jazz Age",
    "image_url": "https://cloudshelf-images.s3.amazonaws.com/great-gatsby.jpg",
    "created_at": "2025-09-05T10:00:00Z",
    "updated_at": "2025-09-05T10:00:00Z"
  },
  {
    "category": "Technology",
    "book_id": "tech-001",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "price": 42.99,
    "stock_quantity": 25,
    "description": "A handbook of agile software craftsmanship",
    "image_url": "https://cloudshelf-images.s3.amazonaws.com/clean-code.jpg",
    "created_at": "2025-09-05T10:00:00Z",
    "updated_at": "2025-09-05T10:00:00Z"
  },
  {
    "category": "Business",
    "book_id": "bus-001",
    "title": "The Lean Startup",
    "author": "Eric Ries",
    "isbn": "9780307887894",
    "price": 26.99,
    "stock_quantity": 30,
    "description": "How constant innovation creates successful businesses",
    "image_url": "https://cloudshelf-images.s3.amazonaws.com/lean-startup.jpg",
    "created_at": "2025-09-05T10:00:00Z",
    "updated_at": "2025-09-05T10:00:00Z"
  }
]
```

![DynamoDB Sample Data Population](screenshots/dynamodb-sample-data-population.png)
_Adding sample books to the books table_

---

### **Step 3: Validation and Testing**

#### **Verify Table Creation**

**Tables Checklist**:
- ✅ `cloudshelf-books` with category/book_id keys and TitleSearchIndex
- ✅ `cloudshelf-carts` with user_id/cart_id keys and TTL enabled
- ✅ `cloudshelf-users` with user_id key and EmailIndex
- ✅ `cloudshelf-orders` with user_id/order_id keys and OrderDateIndex

#### **Test Data Operations**

**Query Examples**:

```bash
# Get books by category
aws dynamodb query \
  --table-name cloudshelf-books \
  --key-condition-expression "category = :category" \
  --expression-attribute-values '{":category":{"S":"Technology"}}'

# Find user by email
aws dynamodb query \
  --table-name cloudshelf-users \
  --index-name EmailIndex \
  --key-condition-expression "email = :email" \
  --expression-attribute-values '{":email":{"S":"customer@example.com"}}'

# Get user's orders
aws dynamodb query \
  --table-name cloudshelf-orders \
  --key-condition-expression "user_id = :user_id" \
  --expression-attribute-values '{":user_id":{"S":"user-12345"}}'
```

![DynamoDB Query Testing](screenshots/dynamodb-query-testing.png)
_Testing table queries in DynamoDB console_

---

## ⚡ Lambda Integration Patterns

### **DynamoDB Connection from Lambda (No VPC)**

```python
import boto3
import json
from boto3.dynamodb.conditions import Key

# Initialize DynamoDB resource
dynamodb = boto3.resource('dynamodb')

# Table references
books_table = dynamodb.Table('cloudshelf-books')
carts_table = dynamodb.Table('cloudshelf-carts')
users_table = dynamodb.Table('cloudshelf-users')
orders_table = dynamodb.Table('cloudshelf-orders')

def get_books_by_category(category):
    """Get all books in a specific category"""
    response = books_table.query(
        KeyConditionExpression=Key('category').eq(category)
    )
    return response['Items']

def search_books_by_title(title):
    """Search books by title using GSI"""
    response = books_table.query(
        IndexName='TitleSearchIndex',
        KeyConditionExpression=Key('title').eq(title)
    )
    return response['Items']

def get_user_cart(user_id):
    """Get user's current shopping cart"""
    response = carts_table.query(
        KeyConditionExpression=Key('user_id').eq(user_id),
        ScanIndexForward=False,  # Get most recent cart
        Limit=1
    )
    return response['Items'][0] if response['Items'] else None

def create_order(user_id, cart_items, total_amount):
    """Create new order from cart"""
    import uuid
    from datetime import datetime
    
    order_id = f"order-{datetime.now().strftime('%Y-%m-%d')}-{str(uuid.uuid4())[:8]}"
    
    order_item = {
        'user_id': user_id,
        'order_id': order_id,
        'order_date': datetime.now().strftime('%Y-%m-%d'),
        'items': cart_items,
        'total': total_amount,
        'status': 'confirmed',
        'created_at': datetime.now().isoformat(),
        'updated_at': datetime.now().isoformat()
    }
    
    orders_table.put_item(Item=order_item)
    return order_item
```

**Key Benefits**:
- ✅ **No VPC configuration** required
- ✅ **Simple AWS SDK** integration
- ✅ **Automatic scaling** with Lambda
- ✅ **Built-in error handling** with AWS SDKs

---

## 📊 Performance and Cost Optimization

### **DynamoDB Best Practices**

**Partition Key Design**:
- ✅ **Books**: Category distributes load across book types
- ✅ **Carts**: User ID ensures even distribution
- ✅ **Users**: User ID provides unique distribution
- ✅ **Orders**: User ID groups related orders together

**Global Secondary Indexes**:
- ✅ **Title search** for book discovery
- ✅ **Email lookup** for user authentication
- ✅ **Date-based ordering** for order history

**Cost Management**:
- ✅ **Pay-per-request** billing eliminates idle costs
- ✅ **TTL on carts** automatically cleans up old data
- ✅ **Efficient queries** minimize read costs
- ✅ **GSI projections** optimize secondary index costs

### **Expected Costs (Estimates)**

| Usage Level | Reads/Month | Writes/Month | Storage | Estimated Cost |
|-------------|-------------|--------------|---------|----------------|
| **Development** | 100K | 50K | 1GB | $2-5/month |
| **Small Business** | 1M | 500K | 10GB | $15-25/month |
| **Growing Business** | 10M | 5M | 100GB | $100-150/month |

---

## 🔄 Migration Path to Phase 2

### **When to Consider Migration**

**Phase 2 Triggers**:
- 📊 **Complex reporting needs** - Need SQL aggregations and joins
- 🔍 **Advanced search** - Full-text search capabilities required
- 📈 **Scale requirements** - Need read replicas or caching
- 🔒 **Enhanced security** - VPC isolation for compliance
- 💼 **Business growth** - Production-grade requirements

### **Migration Benefits**

**From DynamoDB-Only to PostgreSQL + DynamoDB**:
- 📈 **Enhanced search** - PostgreSQL full-text search
- 🔍 **Complex queries** - SQL joins for reporting
- 📊 **Business intelligence** - Analytics and dashboards
- 🔒 **Security enhancement** - VPC network isolation
- ⚡ **Performance optimization** - Optimized for each data pattern

---

## 🎯 Next Steps

### **Immediate Implementation**
1. ✅ **Create DynamoDB tables** (this guide)
2. ⚡ **Deploy Lambda functions** with DynamoDB integration
3. 🌐 **Setup API Gateway** endpoints
4. 🌍 **Configure CloudFront** for content delivery

### **Learning Progression**
1. 📚 **Master DynamoDB** - NoSQL patterns and best practices
2. ⚡ **Serverless architecture** - Lambda and API Gateway
3. 🔧 **AWS SDK integration** - Python/Node.js development
4. 📊 **Monitoring setup** - CloudWatch metrics and logging

### **Future Evolution**
1. 🗃️ **Phase 2 migration** - Add PostgreSQL for advanced features
2. 🔒 **VPC implementation** - Enhanced security architecture
3. 📈 **Performance optimization** - Caching and read replicas
4. 🌍 **Multi-region deployment** - Global scale architecture

---

## 📚 Related Documentation

- 📋 [**ADR-002-Revised**](../cloudshelf-adr-002-revised-phased-data-storage.md) - Phased data storage strategy
- ⚡ [**Lambda Setup Guide**](../lambda/cloudshelf-lambda-setup.md) - Serverless function deployment
- 🌐 [**API Gateway Setup**](../apigateway/cloudshelf-apigateway-setup.md) - RESTful API configuration
- 🗃️ [**RDS Setup Guide**](../rds/cloudshelf-rds-setup.md) - Phase 2 migration reference
- 🔄 [**Migration Guide**](cloudshelf-phase-migration-guide.md) - Phase 1 to Phase 2 evolution

---

## 📋 Quick Reference

### **DynamoDB Table Configuration**

```bash
# Table names for consistent reference
BOOKS_TABLE="cloudshelf-books"
CARTS_TABLE="cloudshelf-carts"  
USERS_TABLE="cloudshelf-users"
ORDERS_TABLE="cloudshelf-orders"

# Key patterns
BOOKS_KEY="category + book_id"
CARTS_KEY="user_id + cart_id"
USERS_KEY="user_id"
ORDERS_KEY="user_id + order_id"
```

### **Lambda Environment Variables**

```bash
BOOKS_TABLE_NAME=cloudshelf-books
CARTS_TABLE_NAME=cloudshelf-carts
USERS_TABLE_NAME=cloudshelf-users
ORDERS_TABLE_NAME=cloudshelf-orders
AWS_REGION=us-east-1
```

---

_📋 **Documentation Status**: Complete | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Phase 1 Implementation_  
_🎯 **Phase**: Beginner-Friendly Setup | 👥 **Team**: Solutions Architecture | 📋 **Next**: Lambda Integration_
