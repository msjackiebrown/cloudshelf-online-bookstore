# 📚 Phase 1 Setup Guides

> Complete implementation guides for CloudShelf Phase 1 - Simple serverless architecture

This folder contains all the detailed setup guides needed to implement Phase 1 of CloudShelf. Follow these guides in order to build a fully functional online bookstore using serverless AWS technologies.

---

## 🎯 Phase 1 Quick Reference

### **✅ What You'll Build**

- 📚 **Online bookstore** - Complete book catalog and shopping functionality
- ⚡ **Hybrid database architecture** - PostgreSQL RDS + DynamoDB for optimal performance
- 🗃️ **PostgreSQL RDS** - Relational data (books, users, orders) with ACID compliance
- 🗂️ **DynamoDB** - High-performance operations (cart, sessions) with single-digit millisecond latency
- 🌍 **Global CDN** - Fast content delivery worldwide with CloudFront
- 📱 **Unified REST API** - Complete backend API integrating both databases

### **⏱️ Implementation Time**

- **Total**: 4-6 hours for complete hybrid setup
- **Prerequisites**: 30 minutes
- **PostgreSQL RDS**: 45 minutes
- **DynamoDB**: 30 minutes
- **Lambda VPC integration**: 60 minutes
- **API Gateway**: 30 minutes
- **S3 + CloudFront**: 35 minutes
- **Testing & validation**: 45 minutes

---

## 📋 Setup Order & Dependencies

### **🏗️ Required Setup Sequence**

```
Phase 1 Implementation Flow:
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│  1️⃣ PostgreSQL RDS (45 min)            � Relational Data                   │
│  ├─── Books database                    ├─── Complex queries                │
│  ├─── Users & authentication            ├─── Data integrity                 │
│  └─── Orders & history                  └─── ACID compliance                │
│       ↓                                                                     │
│  2️⃣ DynamoDB Tables (30 min)           ⚡ High-Performance Data             │
│  ├─── Shopping carts                    ├─── Single-digit ms latency       │
│  └─── User sessions                     └─── Auto-scaling                   │
│       ↓                                                                     │
│  3️⃣ Basic IAM Setup (20 min)           🔒 Hybrid Security                  │
│  ├─── Lambda execution roles            ├─── VPC permissions               │
│  ├─── PostgreSQL RDS access             ├─── DynamoDB permissions          │
│  └─── CloudWatch logging                └─── Least privilege access        │
│       ↓                                                                     │
│  4️⃣ Hybrid Lambda Functions (60 min)   🔗 Hybrid Integration               │
│  ├─── Book Catalog (PostgreSQL)         ├─── Database connectivity         │
│  ├─── User Management (PostgreSQL)      ├─── VPC integration               │
│  ├─── Shopping Cart (DynamoDB)          └─── Unified business logic        │
│  └─── Order Processing (Both DBs)                                           │
│       ↓                                                                     │
│  5️⃣ API Gateway (30 min)               🌐 Unified API Layer                │
│  ├─── REST endpoints                    ├─── Request routing               │
│  ├─── CORS configuration                ├─── Response formatting            │
│  └─── Lambda integration                └─── Error handling                │
│       ↓                                                                     │
│  6️⃣ S3 Static Website (20 min)         🖥️ Frontend                         │
│  ├─── Web hosting                       ├─── HTML/CSS/JS files             │
│  ├─── Static assets                     └─── API integration               │
│  └─── Public access                                                        │
│       ↓                                                                     │
│  7️⃣ CloudFront CDN (15 min)            🌍 Global Distribution              │
│  ├─── S3 origin                         ├─── Caching rules                 │
│  ├─── API integration                   ├─── SSL certificate               │
│  └─── Custom domain                     └─── Performance optimization      │
│       ↓                                                                     │
│  8️⃣ Basic Monitoring (15 min)          📊 Essential Observability         │
│  ├─── CloudWatch logs                   ├─── Error alarms                  │
│  ├─── Simple dashboard                  ├─── Billing alerts               │
│  └─── Basic troubleshooting             └─── Cost monitoring               │
│       ↓                                                                     │
│  9️⃣ Testing & Validation (45 min)      🧪 End-to-End Verification         │
│  ├─── Database connectivity tests       ├─── API endpoint validation       │
│  ├─── Hybrid workflow testing           ├─── Performance benchmarks        │
│  └─── Error handling verification       └─── Complete functionality        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📚 Detailed Setup Guides

### **1️⃣ PostgreSQL RDS Setup (45 minutes)**

📖 **Guide**: [`cloudshelf-rds-default-vpc-setup.md`](cloudshelf-rds-default-vpc-setup.md)

**What you'll create**:

- ✅ PostgreSQL RDS instance in default VPC
- ✅ Books table with relational data and search
- ✅ Users table for authentication and profiles
- ✅ Orders table for purchase history and ACID compliance

**Key outcomes**:

- �️ **Relational data model** with proper relationships and constraints
- 🔑 **Foreign keys and indexes** for data integrity and performance
- 📊 **Sample data** loaded for testing
- 🛡️ **ACID compliance** for critical business transactions

### **2️⃣ DynamoDB High-Performance Tables (30 minutes)**

📖 **Guide**: [`cloudshelf-dynamodb-setup.md`](cloudshelf-dynamodb-setup.md)

**What you'll create**:

- ✅ Shopping carts table for real-time cart operations
- ✅ User sessions table with automatic TTL cleanup

**Key outcomes**:

- ⚡ **Single-digit millisecond latency** for cart operations
- 🔄 **Auto-scaling** for traffic spikes
- 🗑️ **Automatic cleanup** with TTL for sessions
- 🎯 **Optimized for performance** - only high-speed operations

### **3️⃣ Basic IAM Setup (20 minutes)**

📖 **Guide**: [`cloudshelf-basic-iam-setup.md`](cloudshelf-basic-iam-setup.md)

**What you'll create**:

- ✅ Lambda execution role with VPC permissions
- ✅ PostgreSQL RDS access for relational operations
- ✅ DynamoDB read/write permissions for cart and sessions
- ✅ CloudWatch logging access for troubleshooting

**Key outcomes**:

- 🔒 **Hybrid database security** - Proper access to both PostgreSQL and DynamoDB
- 🌐 **VPC connectivity** - Lambda functions can reach RDS in VPC
- 📊 **Comprehensive logging** - CloudWatch access for both database types
- ⚡ **Quick setup** - Working hybrid security in 20 minutes

### **4️⃣ Hybrid Lambda Functions (60 minutes)**

📖 **Guide**: [`cloudshelf-basic-lambda-setup.md`](cloudshelf-basic-lambda-setup.md)

**What you'll create**:

- ✅ Book Catalog Lambda (PostgreSQL: search, details, complex queries)
- ✅ User Management Lambda (PostgreSQL: registration, profiles, authentication)
- ✅ Shopping Cart Lambda (DynamoDB: add, remove, real-time updates)
- ✅ Order Processing Lambda (Both DBs: checkout with transaction coordination)

**Key outcomes**:

- 🔗 **Hybrid database integration** - Functions connecting to both PostgreSQL and DynamoDB
- 🌐 **VPC configuration** - Lambda functions properly connected to RDS
- ⚡ **Optimized performance** - Right database for the right operation
- 🔒 **Secure connectivity** - Proper IAM roles for both database types
- 📊 **Error handling** with proper HTTP responses
- 🧪 **Testing endpoints** for validation

### **5️⃣ API Layer (30 minutes)**

📖 **Guide**: [`cloudshelf-apigateway-setup.md`](cloudshelf-apigateway-setup.md)

**What you'll create**:

- ✅ REST API with resource structure
- ✅ HTTP methods (GET, POST, PUT, DELETE)
- ✅ Lambda proxy integration
- ✅ CORS configuration for web access

**Key outcomes**:

- 🌐 **RESTful API** following best practices
- 🔗 **Lambda integration** with proper request/response mapping
- 🌍 **CORS enabled** for browser compatibility
- 📋 **API documentation** with example requests

### **6️⃣ Web Hosting (20 minutes)**

📖 **Guide**: [`cloudshelf-s3-setup.md`](cloudshelf-s3-setup.md)

**What you'll create**:

- ✅ S3 bucket configured for static web hosting
- ✅ Public access policies for website content
- ✅ Index and error page configuration
- ✅ Sample HTML page for testing

**Key outcomes**:

- 🖥️ **Static website hosting** with high availability
- 🔓 **Public access** configured securely
- 📱 **Mobile-responsive** design ready for content
- 🚀 **Fast loading** with S3's global infrastructure

### **7️⃣ Global CDN (15 minutes)**

📖 **Guide**: [`cloudshelf-cloudfront-setup.md`](cloudshelf-cloudfront-setup.md)

**What you'll create**:

- ✅ CloudFront distribution with S3 origin
- ✅ Custom cache behaviors for API calls
- ✅ SSL certificate for HTTPS
- ✅ Performance optimization settings

**Key outcomes**:

- 🌍 **Global content delivery** with edge locations
- ⚡ **Sub-second loading** times worldwide
- 🔒 **HTTPS by default** for security
- 📊 **Caching optimization** for better performance

### **8️⃣ Basic Monitoring (15 minutes)**

📖 **Guide**: [`basic-cloudwatch-monitoring.md`](basic-cloudwatch-monitoring.md)

**What you'll create**:

- ✅ Lambda function log access for debugging
- ✅ Basic error alarms for Lambda and API Gateway
- ✅ Simple CloudShelf dashboard for overview
- ✅ Billing alerts for cost control

**Key outcomes**:

- 🔍 **Essential troubleshooting** - Quick access to logs and errors
- 🚨 **Error notifications** - Get alerted when functions fail
- 💰 **Cost awareness** - Monitor AWS spending
- 📊 **Basic observability** - Simple dashboard for health overview

---

### **9️⃣ Testing & Validation (45 minutes)**

## 🧪 Testing & Validation

### **📋 Phase 1 Testing Checklist**

#### **�️ Hybrid Database Tests**

- [ ] **PostgreSQL RDS Connection** - Verify Lambda can connect to RDS in VPC
- [ ] **Book catalog operations** - Create, search, and retrieve books from PostgreSQL
- [ ] **User management** - Registration and authentication via PostgreSQL
- [ ] **Order processing** - ACID transactions for order creation in PostgreSQL
- [ ] **DynamoDB cart operations** - High-speed add/remove cart items
- [ ] **Session management** - User sessions with TTL auto-cleanup in DynamoDB
- [ ] **Hybrid workflows** - Order checkout using both databases

#### **⚡ API Tests**

- [ ] **GET /books** - Retrieve book catalog
- [ ] **GET /books/{id}** - Get specific book details
- [ ] **POST /users** - User registration endpoint
- [ ] **POST /carts** - Add items to cart
- [ ] **POST /orders** - Create new order

#### **🖥️ Frontend Tests**

- [ ] **Static site loads** - Homepage displays correctly
- [ ] **API integration** - Frontend calls backend successfully
- [ ] **CORS working** - No browser security errors
- [ ] **Responsive design** - Works on mobile devices
- [ ] **Performance** - Page loads under 3 seconds

### **🧪 Sample Test Commands**

#### **API Testing with cURL**

```bash
# Test book catalog
curl -X GET https://your-api-id.execute-api.region.amazonaws.com/prod/books

# Test book details
curl -X GET https://your-api-id.execute-api.region.amazonaws.com/prod/books/book-123

# Test user registration
curl -X POST https://your-api-id.execute-api.region.amazonaws.com/prod/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","name":"Test User"}'

# Test add to cart
curl -X POST https://your-api-id.execute-api.region.amazonaws.com/prod/carts \
  -H "Content-Type: application/json" \
  -d '{"userId":"user-123","bookId":"book-123","quantity":1}'
```

#### **Performance Testing**

```bash
# Test API response time
curl -w "@curl-format.txt" -o /dev/null -s "https://your-api-id.execute-api.region.amazonaws.com/prod/books"

# Test CloudFront cache
curl -I "https://your-cloudfront-domain.cloudfront.net"
```

---

## 🎯 Success Criteria

### **✅ Phase 1 Complete When...**

#### **🔧 Technical Requirements**

- [ ] **All services deployed** - DynamoDB, Lambda, API Gateway, S3, CloudFront, CloudWatch
- [ ] **API fully functional** - All endpoints responding correctly
- [ ] **Frontend operational** - Static site serving content
- [ ] **Data flow working** - Can create, read, update data
- [ ] **Monitoring active** - Basic logging and alarms configured
- [ ] **Performance targets met** - API responses under 500ms

#### **🏢 Business Requirements**

- [ ] **Book catalog live** - Can browse and search books
- [ ] **User accounts working** - Registration and login functional
- [ ] **Shopping cart active** - Add/remove items successfully
- [ ] **Order processing** - Complete purchase workflow
- [ ] **Global accessibility** - Site works worldwide

#### **📊 Performance Benchmarks**

- [ ] **API latency** - 95th percentile under 500ms
- [ ] **Website loading** - First contentful paint under 2s
- [ ] **Database queries** - Single-digit millisecond response
- [ ] **CDN cache hit ratio** - Above 80%
- [ ] **Error rate** - Under 0.1%

---

## 💰 Phase 1 Costs

### **Expected Monthly AWS Costs**

| Service            | Usage Level               | Monthly Cost (USD)  |
| ------------------ | ------------------------- | ------------------- |
| **PostgreSQL RDS** | db.t3.micro (20GB)        | $12.00 - $18.00     |
| **DynamoDB**       | High-performance ops only | $0.50 - $2.00       |
| **Lambda**         | 100K requests/month       | $0.20 - $0.50       |
| **API Gateway**    | 100K API calls            | $0.35 - $1.00       |
| **S3**             | 1GB storage, 10K requests | $0.25 - $0.50       |
| **CloudFront**     | 10GB transfer             | $0.85 - $2.00       |
| **CloudWatch**     | Basic monitoring          | $0.10 - $1.00       |
| **Total**          | Light usage               | **$14.25 - $25.00** |

**💡 Cost Optimization Tips**:

- ✅ **Industry-standard pricing** - Realistic costs for hybrid database architecture
- ✅ **RDS free tier** - 750 hours/month for new accounts (first 12 months)
- ✅ **DynamoDB optimized** - Only pay for high-performance operations
- ✅ **Automatic scaling** - Costs scale with actual usage

---

## 🚀 After Phase 1

### **🎯 What's Next?**

#### **📈 Phase 1 → Phase 2 Enhancement Path**

- **When to enhance**: After mastering Phase 1 hybrid architecture
- **Why enhance**: Need for custom VPC, advanced monitoring, enterprise features
- **Enhancement time**: 4-6 hours with proper planning
- **Enhancement guide**: [`../phase2-enhancements/README.md`](../phase2-enhancements/README.md)

#### **🏗️ Phase 2 Enhancements**

- 🔒 **Custom VPC** - Private subnets and advanced network security
- 🗃️ **Multi-AZ RDS** - High availability and automatic failover
- 📊 **Advanced monitoring** - X-Ray tracing and comprehensive observability
- 🏢 **Enterprise features** - Infrastructure as Code and compliance

### **📚 Learning Path**

1. **Master Phase 1** - Understand hybrid database patterns thoroughly
2. **Learn VPC concepts** - Private subnets and network security
3. **Advanced database patterns** - Multi-AZ, read replicas, performance tuning
4. **Production operations** - Monitoring, automation, and disaster recovery

---

## 📖 Quick Start

### **🚀 Begin Phase 1 Setup**

1. **📋 Prerequisites**

   - AWS account with admin access
   - AWS CLI installed and configured
   - Basic understanding of REST APIs

2. **⏱️ Time allocation**

   - Block 4-6 hours for complete hybrid setup
   - Can be done in stages over multiple sessions
   - Each guide builds on the previous

3. **🎯 Recommended approach**

   - Follow guides in exact order
   - Test each component before moving to next
   - Keep notes of any customizations

4. **🧪 Validation strategy**
   - Test each service as you build it
   - Use provided test commands
   - Verify end-to-end functionality

---

**🚀 Ready to start? Begin with [PostgreSQL RDS Setup](cloudshelf-rds-default-vpc-setup.md)!**

_📋 **Folder Status**: Complete Hybrid Setup Guides | ✅ **Phase 1 Ready**: Yes | 🔄 **Last Updated**: Hybrid Architecture_  
_🎯 **Phase**: Hybrid Database Setup | 👥 **Audience**: Beginners | 📋 **Duration**: 4-6 hours_
