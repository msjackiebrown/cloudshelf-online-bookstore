# 📚 Phase 1 Setup Guides

> Complete implementation guides for CloudShelf Phase 1 - Simple serverless architecture

This folder contains all the detailed setup guides needed to implement Phase 1 of CloudShelf. Follow these guides in order to build a fully functional online bookstore using serverless AWS technologies.

---

## 🎯 Phase 1 Quick Reference

### **✅ What You'll Build**

- 📚 **Online bookstore** - Complete book catalog and shopping functionality
- ⚡ **Serverless architecture** - No servers to manage
- 🌍 **Global CDN** - Fast content delivery worldwide
- 🗂️ **NoSQL database** - DynamoDB for all data storage
- 📱 **REST API** - Complete backend API for frontend integration

### **⏱️ Implementation Time**

- **Total**: 2-5 hours for complete setup
- **Prerequisites**: 30 minutes
- **Core services**: 3.5-4.5 hours
- **Testing & validation**: 30 minutes

---

## 📋 Setup Order & Dependencies

### **🏗️ Required Setup Sequence**

```
Phase 1 Implementation Flow:
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│  1️⃣ DynamoDB Tables (30 min)           👤 Customer Data                     │
│  ├─── Books                             ├─── User profiles                  │
│  ├─── Users                             ├─── Authentication                 │
│  ├─── Orders                            └─── Order history                  │
│  └─── Carts                                                                 │
│       ↓                                                                     │
│  2️⃣ Lambda Functions (45 min)          ⚡ Business Logic                     │
│  ├─── Book Catalog API                  ├─── CRUD operations               │
│  ├─── User Management                   ├─── Search functionality           │
│  ├─── Shopping Cart                     └─── Order processing               │
│  └─── Order Processing                                                      │
│       ↓                                                                     │
│  3️⃣ API Gateway (30 min)               🌐 API Layer                         │
│  ├─── REST endpoints                    ├─── Request routing               │
│  ├─── CORS configuration                ├─── Response formatting            │
│  └─── Lambda integration                └─── Error handling                │
│       ↓                                                                     │
│  4️⃣ S3 Static Website (20 min)         🖥️ Frontend                         │
│  ├─── Web hosting                       ├─── HTML/CSS/JS files             │
│  ├─── Static assets                     └─── API integration               │
│  └─── Public access                                                        │
│       ↓                                                                     │
│  5️⃣ CloudFront CDN (15 min)            🌍 Global Distribution              │
│  ├─── S3 origin                         ├─── Caching rules                 │
│  ├─── API integration                   ├─── SSL certificate               │
│  └─── Custom domain                     └─── Performance optimization      │
│       ↓                                                                     │
│  6️⃣ Basic Monitoring (15 min)          📊 Essential Observability         │
│  ├─── CloudWatch logs                   ├─── Error alarms                  │
│  ├─── Simple dashboard                  ├─── Billing alerts               │
│  └─── Basic troubleshooting             └─── Cost monitoring               │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📚 Detailed Setup Guides

### **1️⃣ Database Setup (30 minutes)**

📖 **Guide**: [`cloudshelf-dynamodb-setup.md`](cloudshelf-dynamodb-setup.md)

**What you'll create**:

- ✅ Books table with search capabilities
- ✅ Users table for authentication
- ✅ Orders table for purchase history
- ✅ Carts table for shopping sessions

**Key outcomes**:

- 🗂️ **NoSQL data model** designed for performance
- 🔑 **Primary keys and indexes** for efficient queries
- 📊 **Sample data** loaded for testing
- ⚡ **High performance** with single-digit millisecond latency

### **2️⃣ Basic IAM Setup (15 minutes)**

📖 **Guide**: [`basic-iam-setup.md`](basic-iam-setup.md)

**What you'll create**:

- ✅ Lambda execution role with essential permissions
- ✅ CloudWatch logging access for troubleshooting
- ✅ DynamoDB read/write permissions for shopping cart
- ✅ AWS managed policies for quick, secure setup

**Key outcomes**:

- 🔒 **Essential security** - Functions have needed permissions only
- 📊 **Automatic logging** - CloudWatch access for debugging
- 🛡️ **Best practices** - AWS managed policies where appropriate
- ⚡ **Quick setup** - Working security in 15 minutes

### **3️⃣ Serverless Functions (45 minutes)**

📖 **Guide**: [`cloudshelf-lambda-setup.md`](cloudshelf-lambda-setup.md)

**What you'll create**:

- ✅ Book Catalog Lambda (search, get details)
- ✅ User Management Lambda (registration, profile)
- ✅ Shopping Cart Lambda (add, remove, view)
- ✅ Order Processing Lambda (checkout, history)

**Key outcomes**:

- ⚡ **Auto-scaling** functions that handle any load
- 🔒 **IAM roles** with least privilege access
- 📊 **Error handling** with proper HTTP responses
- 🧪 **Testing endpoints** for validation

### **4️⃣ API Layer (30 minutes)**

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

### **5️⃣ Web Hosting (20 minutes)**

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

### **6️⃣ Global CDN (15 minutes)**

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

### **7️⃣ Basic Monitoring (15 minutes)**

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

### **8️⃣ Testing & Validation (30 minutes)**

## 🧪 Testing & Validation

### **📋 Phase 1 Testing Checklist**

#### **🗂️ Database Tests**

- [ ] **Create book record** - Add new book to catalog
- [ ] **Search functionality** - Find books by title/author
- [ ] **User registration** - Create new user account
- [ ] **Shopping cart** - Add/remove items from cart
- [ ] **Order creation** - Complete purchase workflow

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

| Service         | Usage Level               | Monthly Cost (USD) |
| --------------- | ------------------------- | ------------------ |
| **DynamoDB**    | 1M reads, 100K writes     | $1.25 - $3.00      |
| **Lambda**      | 100K requests/month       | $0.20 - $0.50      |
| **API Gateway** | 100K API calls            | $0.35 - $1.00      |
| **S3**          | 1GB storage, 10K requests | $0.25 - $0.50      |
| **CloudFront**  | 10GB transfer             | $0.85 - $2.00      |
| **CloudWatch**  | Basic monitoring          | $0.10 - $1.00      |
| **Total**       | Light usage               | **$3.00 - $8.00**  |

**💡 Cost Optimization Tips**:

- ✅ **Free tier eligible** - Most services have generous free tiers
- ✅ **Pay per use** - Only pay for actual usage
- ✅ **No upfront costs** - Zero infrastructure investment
- ✅ **Automatic scaling** - Costs scale with usage

---

## 🚀 After Phase 1

### **🎯 What's Next?**

#### **📈 Phase 1 → Phase 2 Migration Path**

- **When to migrate**: After understanding Phase 1 architecture
- **Why migrate**: Need for enhanced security, complex queries, compliance
- **Migration time**: 1-2 days with proper planning
- **Migration guide**: [`../migration/migration-overview.md`](../migration/migration-overview.md)

#### **🏗️ Phase 2 Benefits**

- 🔒 **Enhanced security** - VPC isolation and network controls
- 🗃️ **PostgreSQL database** - Complex queries and reporting
- 📊 **Advanced monitoring** - Comprehensive observability
- 🏢 **Enterprise features** - Compliance and audit trails

### **📚 Learning Path**

1. **Master Phase 1** - Understand serverless patterns thoroughly
2. **Learn VPC concepts** - Network isolation and security
3. **Database migration** - NoSQL to SQL data modeling
4. **Production operations** - Monitoring and troubleshooting

---

## 📖 Quick Start

### **🚀 Begin Phase 1 Setup**

1. **📋 Prerequisites**

   - AWS account with admin access
   - AWS CLI installed and configured
   - Basic understanding of REST APIs

2. **⏱️ Time allocation**

   - Block 3-5 hours for complete setup
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

**🚀 Ready to start? Begin with [DynamoDB Setup](dynamodb-setup.md)!**

_📋 **Folder Status**: Complete Setup Guides | ✅ **Phase 1 Ready**: Yes | 🔄 **Last Updated**: Organization_  
_🎯 **Phase**: Basic Setup | 👥 **Audience**: Beginners | 📋 **Duration**: 2-5 hours_
