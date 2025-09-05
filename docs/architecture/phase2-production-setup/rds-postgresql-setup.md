# 🗃️ RDS PostgreSQL Setup

> Implementation guide for RDS PostgreSQL database following ADR-002 architecture strategy

This guide provides setup instructions for Amazon RDS PostgreSQL, implementing the relational database decisions documented in [ADR-002: RDS Database Engine Selection](../cloudshelf-architecture-decisions.md#adr-002-rds-database-engine-selection).

---

## 🔒 Database Security Essentials

### **🛡️ RDS Security Requirements**

**Critical Security Configurations**:

- **Private Subnet Deployment** - Database never accessible from internet
- **Security Group Restrictions** - Only Lambda functions can connect
- **Encryption at Rest** - All data encrypted using AWS KMS
- **Backup Encryption** - Automated backups are also encrypted
- **SSL/TLS Connections** - Enforce encrypted connections only

**Security Checklist**:

- [ ] Deploy RDS in private subnets only
- [ ] Configure security group to allow Lambda access only
- [ ] Enable encryption at rest with KMS
- [ ] Use strong database passwords (AWS Secrets Manager)
- [ ] Enable automated backups with encryption
- [ ] Configure VPC security groups properly

---

## 🏛️ Architecture Overview

Based on **ADR-002**, PostgreSQL RDS provides the book catalog storage layer for CloudShelf with:

- **📚 Book Catalog Operations** - Complex relational queries and transactions
- **🔍 Advanced Search** - Built-in full-text search capabilities
- **📊 Analytics Support** - Complex reporting and business intelligence
- **🔒 Data Integrity** - ACID compliance for transactional operations

**Architecture Decision Reference**: See [ADR-002](../cloudshelf-architecture-decisions.md#adr-002) for the complete rationale behind PostgreSQL selection.

### **🗃️ RDS Architecture Design**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        CloudShelf RDS PostgreSQL Architecture                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                          VPC: CloudShelf VPC                           │   │
│  │                            (10.0.0.0/16)                               │   │
│  │                                                                         │   │
│  │  ┌─────────────────────────────────────────────────────────────────┐   │   │
│  │  │                      Public Subnets                             │   │   │
│  │  │                    (10.0.1.0/24, 10.0.3.0/24)                  │   │   │
│  │  │                                                                 │   │   │
│  │  │  ┌─────────────────┐                 ┌─────────────────┐       │   │   │
│  │  │  │  Internet       │                 │   API Gateway   │       │   │   │
│  │  │  │  Gateway        │                 │   (Future)      │       │   │   │
│  │  │  │                 │                 │                 │       │   │   │
│  │  │  └─────────────────┘                 └─────────────────┘       │   │   │
│  │  └─────────────────────────────────────────────────────────────────┘   │   │
│  │                                    │                                   │   │
│  │                                    ▼                                   │   │
│  │  ┌─────────────────────────────────────────────────────────────────┐   │   │
│  │  │                     Private Subnets                             │   │   │
│  │  │                   (10.0.2.0/24, 10.0.4.0/24)                   │   │   │
│  │  │                                                                 │   │   │
│  │  │  ┌─────────────────┐                                           │   │   │
│  │  │  │   Lambda Fn     │                                           │   │   │
│  │  │  │  (Book Catalog) │                                           │   │   │
│  │  │  │                 │                                           │   │   │
│  │  │  │ Security Group: │                                           │   │   │
│  │  │  │cloudshelf-lambda│                                           │   │   │
│  │  │  │-sg              │                                           │   │   │
│  │  │  └─────────────────┘                                           │   │   │
│  │  │           │                                                     │   │   │
│  │  │           ▼                                                     │   │   │
│  │  │  ┌─────────────────────────────────────────────────────────┐   │   │   │
│  │  │  │              PostgreSQL RDS                             │   │   │   │
│  │  │  │                                                         │   │   │   │
│  │  │  │  ┌─────────────────────────────────────────────────┐   │   │   │   │
│  │  │  │  │      Primary Instance (AZ-a)                   │   │   │   │   │
│  │  │  │  │  cloudshelf-book-catalog-db                    │   │   │   │   │
│  │  │  │  │                                                 │   │   │   │   │
│  │  │  │  │  • Engine: PostgreSQL 17.4-R1                   │   │   │   │   │
│  │  │  │  │  • Instance: db.t3.micro/small                 │   │   │   │   │
│  │  │  │  │  • Storage: 20-100GB gp3 SSD                   │   │   │   │   │
│  │  │  │  │  • Encryption: AWS KMS                         │   │   │   │   │
│  │  │  │  │  • Port: 5432                                  │   │   │   │   │
│  │  │  │  └─────────────────────────────────────────────────┘   │   │   │   │
│  │  │  │                           │                             │   │   │   │
│  │  │  │                           ▼ (Production Only)           │   │   │   │
│  │  │  │  ┌─────────────────────────────────────────────────┐   │   │   │   │
│  │  │  │  │      Standby Instance (AZ-b)                   │   │   │   │   │
│  │  │  │  │  Multi-AZ Failover                             │   │   │   │   │
│  │  │  │  │                                                 │   │   │   │   │
│  │  │  │  │  • Synchronous Replication                     │   │   │   │   │
│  │  │  │  │  • Automatic Failover                          │   │   │   │   │
│  │  │  │  │  • Same Configuration                          │   │   │   │   │
│  │  │  │  └─────────────────────────────────────────────────┘   │   │   │   │
│  │  │  │                                                         │   │   │   │
│  │  │  │  Security Group: cloudshelf-rds-sg                             │   │   │   │
│  │  │  │  • Inbound: Port 5432 from Book Catalog Lambda SG only     │   │   │   │
│  │  │  │  • Outbound: None (default deny)                           │   │   │   │
│  │  │  └─────────────────────────────────────────────────────────────┘   │   │   │
│  │  └─────────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                            Security Features                            │   │
│  │                                                                         │   │
│  │  Network Security:                    Data Security:                   │   │
│  │  • Private subnet deployment          • Encryption at rest (KMS)       │   │
│  │  • No public IP assignment            • Encrypted backups              │   │
│  │  • Security group restrictions        • SSL/TLS in transit             │   │
│  │  • VPC-only access                    • IAM database authentication    │   │
│  │                                                                         │   │
│  │  Availability Features:               Connection Architecture:          │   │
│  │  • Multi-AZ deployment (prod)         • Lambda VPC integration         │   │
│  │  • Automated backups (7-30 days)      • Connection pooling             │   │
│  │  • Point-in-time recovery             • Environment variables          │   │
│  │  • Maintenance windows                • Parameter Store credentials     │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                         Database Schema Architecture                    │   │
│  │                                                                         │   │
│  │  Database: cloudshelf_catalog                                          │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐       │   │
│  │  │     Books       │  │   Categories    │  │   Book_Meta     │       │   │
│  │  │                 │  │                 │  │                 │       │   │
│  │  │ • id (PK)       │  │ • id (PK)       │  │ • book_id (FK)  │       │   │
│  │  │ • title         │  │ • name          │  │ • reviews       │       │   │
│  │  │ • author        │  │ • description   │  │ • ratings       │       │   │
│  │  │ • isbn          │  │ • created_at    │  │ • metadata      │       │   │
│  │  │ • price         │  │                 │  │   (JSON)        │       │   │
│  │  │ • stock_qty     │  │                 │  │                 │       │   │
│  │  │ • category_id   │──┼─────────────────┼─►│                 │       │   │
│  │  │ • description   │  │                 │  │                 │       │   │
│  │  │ • created_at    │  │                 │  │                 │       │   │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────┘       │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

_**Placeholder for RDS-PostgreSQL-Architecture-Diagram.png** - Complete PostgreSQL RDS deployment architecture with VPC integration, security groups, Multi-AZ configuration, and Lambda connectivity patterns_

---

## 🔐 Prerequisites: VPC and Security Setup

Before creating RDS instances, ensure the network foundation is established per ADR-001.

### **📋 Required Infrastructure**

| Component           | Basic Setup                        | Production Setup                           |
| ------------------- | ---------------------------------- | ------------------------------------------ |
| **VPC**             | CloudShelf VPC with private subnet | CloudShelf VPC with private subnets        |
| **Security Groups** | `cloudshelf-rds-sg` configured     | `cloudshelf-rds-sg` configured             |
| **DB Subnet Group** | ❌ **Not required** (single AZ)    | ✅ `cloudshelf-db-subnet-group` (Multi-AZ) |
| **Route Tables**    | Private subnet routing             | Private subnet routing                     |

### **🎓 Learning Paths**

**🚀 Basic Setup (Start Here)**

- Single availability zone deployment
- Deploy directly in private subnet
- Faster setup, easier to understand
- Perfect for learning and development

**🏢 Production Setup (Advanced)**

- Multi-AZ high availability
- Requires DB subnet group (see [High Availability Setup](../vpc/cloudshelf-vpc-high-availability-setup.md))
- Automatic failover capability
- Higher cost but production-ready

### **🛠️ Infrastructure Validation**

⚠️ **Important**: Complete [VPC Setup Guide](../vpc/cloudshelf-vpc-setup.md) first (Steps 1-6 minimum).

**Basic Setup Requirements:**

- ✅ VPC and Security Group Setup completed (Steps 1-6)
- ✅ Private subnet available (at least one)
- ✅ Database security group (`cloudshelf-rds-sg`) configured
- ✅ Lambda security group configured for database access

**Production Setup Additional Requirements:**

- ✅ **Step 7: DB subnet group (`cloudshelf-db-subnet-group`) created**
- ✅ Multiple availability zones configured

---

## 📊 Architecture Configuration

### **Database Strategy**

Following ADR-002 polyglot persistence approach:

| Data Pattern      | Storage Solution | Rationale                                                  |
| ----------------- | ---------------- | ---------------------------------------------------------- |
| **Book Catalog**  | PostgreSQL RDS   | Complex relationships, ACID transactions, advanced queries |
| **Shopping Cart** | DynamoDB         | High performance, flexible schema, serverless scaling      |

### **Configuration Specifications**

| Component            | Tutorial/Learning  | Production         | Rationale                                      |
| -------------------- | ------------------ | ------------------ | ---------------------------------------------- |
| **Template**         | Dev/Test           | Production         | Enables HA features for learning at lower cost |
| **Engine**           | PostgreSQL 17.4-R1 | PostgreSQL 17.4-R1 | Advanced features, JSON support, reliability   |
| **Instance Class**   | `db.t3.micro`      | `db.t3.small`      | Cost-effective, burstable performance          |
| **Storage**          | 20 GB gp3          | 100 GB gp3         | General Purpose SSD for balanced performance   |
| **Multi-AZ**         | Yes (for learning) | Yes                | High availability practice and production      |
| **Backup Retention** | 7 days             | 30 days            | Compliance and recovery requirements           |

### **Network Architecture**

```
Internet Gateway
    ↓
Public Subnets (10.0.1.0/24)
    ↓ (No direct access)
Private Subnets (10.0.2.0/24)
    ↓
RDS Instance (cloudshelf-book-catalog-db)
```

---

## 🚀 Implementation Guide

### **Step 1: Access RDS Console**

Navigate to AWS RDS service to begin database provisioning with standard create method for full architectural control.

### **Step 2: Select PostgreSQL Engine**

Configure PostgreSQL engine with the following specifications:

- **Database creation method**: Standard create
- **Engine type**: PostgreSQL
- **Engine version**: Latest stable (17.4-R1)
- **Template**: **Dev/Test** (enables production features at lower cost)

### **Step 3: Configure Instance and Storage**

Define instance specifications and storage configuration:

- **DB instance identifier**: `cloudshelf-book-catalog-db`
- **Master username**: `cloudshelf_admin`
- **Instance class**: Environment-appropriate sizing
- **Storage type**: General Purpose SSD (gp3)
- **Storage autoscaling**: Enabled

### **Step 4: Network and Security Setup**

Configure VPC integration and security group associations:

**Network Configuration:**

**Basic Setup (Single AZ):**

- **VPC**: CloudShelf VPC (private deployment)
- **Subnet group**: Select private subnet directly
- **Public access**: Disabled (security requirement)
- **Security groups**: `cloudshelf-rds-sg` only
- **Database port**: 5432 (PostgreSQL standard)

**Production Setup (Multi-AZ):**

- **VPC**: CloudShelf VPC (private deployment)
- **DB subnet group**: `cloudshelf-db-subnet-group`
- **Multi-AZ deployment**: Enabled
- **Public access**: Disabled (security requirement)
- **Security groups**: `cloudshelf-rds-sg` only
- **Database port**: 5432 (PostgreSQL standard)

**Security Configuration:**

- **Encryption at rest**: Enabled
- **Backup retention**: Environment-appropriate
- **Delete protection**: Enabled for production

![RDS Final Configuration](RDS-Configuration-Verification.png)
_Verify: PostgreSQL 17.4-R1 engine, private VPC deployment, cloudshelf-rds-sg security group, encryption enabled_

### **Step 5: Deployment Validation**

**Post-Deployment Verification:**

![RDS Instance Status](RDS-Instance-Status-Verification.png)
_Verify: Instance status "Available", endpoint accessible, security groups properly configured_

**Validation Checklist:**

- ✅ PostgreSQL 17.4-R1 engine selected
- ✅ VPC and subnet configuration correct
- ✅ Security group properly assigned (`cloudshelf-rds-sg`)
- ✅ Instance sizing appropriate for environment
- ✅ Encryption at rest enabled
- ✅ Backup and maintenance windows configured
- ✅ Public access disabled (private subnet only)

---

## Post-Deployment Architecture

### Connection Architecture

| Component    | Value                                                       | Purpose                           |
| ------------ | ----------------------------------------------------------- | --------------------------------- |
| **Endpoint** | `cloudshelf-book-catalog-db.xxxxx.region.rds.amazonaws.com` | Lambda function connection target |
| **Port**     | `5432`                                                      | PostgreSQL standard port          |
| **Database** | `cloudshelf_catalog`                                        | Application database              |

### Security Validation

**Network Security:**

- ✅ RDS deployed in private subnets
- ✅ No public IP assignment
- ✅ Security group restricts access to Lambda functions only

**Access Control:**

- ✅ Master credentials managed securely
- ✅ Database-level user accounts for application access
- ✅ Encryption enabled for data at rest

---

## Database Schema Architecture

### Application Database Structure

```sql
-- High-level database architecture
CREATE DATABASE cloudshelf_catalog;
CREATE USER cloudshelf_app WITH PASSWORD 'managed-via-parameter-store';
GRANT ALL PRIVILEGES ON DATABASE cloudshelf_catalog TO cloudshelf_app;
```

### Schema References

- 📊 [**Database Schema Model**](rds-data-model.md) - Entity relationships and table design
- 📝 [**Sample Data Structure**](rds-sample-data.sql) - Reference data for development

---

## Architectural Best Practices

### **Security Architecture**

- ✅ Private subnet deployment only
- ✅ Security group-based access control
- ✅ Encryption at rest and in transit
- ✅ Parameter Store for credential management

### **High Availability Design**

- ✅ Multi-AZ deployment for production
- ✅ Automated backup configuration
- ✅ Cross-region backup replication (production)
- ✅ Database delete protection

### **Performance Architecture**

- ✅ Instance class sizing based on workload analysis
- ✅ Storage autoscaling for growth management
- ✅ Connection pooling design for Lambda integration
- ✅ Read replica strategy for scaling (future)

---

## Integration Architecture

### Lambda Function Integration

**Connection Pattern**: Lambda functions connect via VPC networking

- Lambda functions deployed in same VPC
- Security group rules allow Lambda-to-RDS communication
- Connection pooling for efficient resource usage
- Environment variables for connection configuration

### **Database Schema Strategy**

- Normalized design with JSON flexibility (PostgreSQL advantage)
- Book catalog tables with relational integrity
- User account management with proper constraints
- Order history and transaction tracking

---

---

## 📊 Database Schema Setup

### **Essential Tables for CloudShelf**

After RDS deployment, create the core database schema for the book catalog:

#### **Books Table**

```sql
-- Core book catalog table
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER DEFAULT 0,
    description TEXT,
    category VARCHAR(100),
    published_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for fast searches
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_category ON books(category);
```

#### **Categories Table (Optional)**

```sql
-- Book categories for organization
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Update books table to reference categories
ALTER TABLE books ADD COLUMN category_id INTEGER REFERENCES categories(id);
```

### **Sample Data for Testing**

```sql
-- Insert sample categories
INSERT INTO categories (name, description) VALUES
    ('Fiction', 'Fiction books'),
    ('Technology', 'Technology and programming books'),
    ('Business', 'Business and entrepreneurship');

-- Insert sample books
INSERT INTO books (title, author, isbn, price, stock_quantity, description, category_id) VALUES
    ('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.99, 50, 'Classic American novel', 1),
    ('Clean Code', 'Robert C. Martin', '9780132350884', 42.99, 25, 'A handbook of agile software craftsmanship', 2),
    ('The Lean Startup', 'Eric Ries', '9780307887894', 26.99, 30, 'How constant innovation creates successful businesses', 3);
```

### **Database Connection from Lambda**

```python
# Example Lambda database connection
import psycopg2
import os

def get_db_connection():
    return psycopg2.connect(
        host=os.environ['DB_HOST'],
        database=os.environ['DB_NAME'],
        user=os.environ['DB_USER'],
        password=os.environ['DB_PASSWORD'],
        port=5432,
        sslmode='require'
    )
```

---

## 📚 Related Architecture Documentation

- 🏛️ [**ADR-002: PostgreSQL Selection**](../cloudshelf-architecture-decisions.md#adr-002) - Complete database choice rationale
- 🏛️ [**All Architecture Decisions**](../cloudshelf-architecture-decisions.md) - Context for database architecture
- 🌐 [**VPC Setup**](../vpc/cloudshelf-vpc-reference.md) - Required network foundation
- 🗂️ [**DynamoDB Setup**](../dynamodb/setup-dynamodb.md) - Complementary NoSQL storage
- ⚡ [**Lambda Setup**](../lambda/setup-lambda.md) - Database connectivity integration

---

_Part of the CloudShelf Solutions Architecture documentation_

- Connection pooling managed at application layer

### API Gateway Integration

**Data Flow**: API Gateway → Lambda → RDS

- Stateless Lambda functions for database operations
- Connection management per Lambda invocation
- Error handling and retry logic at application layer

---

## Quick Reference

### **Key Architectural Values**

- **Instance Identifier**: `cloudshelf-book-catalog-db`
- **Engine**: PostgreSQL (latest stable)
- **Network**: Private subnet deployment
- **Security**: `cloudshelf-rds-sg` security group
- **Access**: Lambda functions only

### **Next Architectural Components**

1. ⚡ [Lambda Function Setup](../lambda/cloudshelf-lambda-setup.md)
2. 🌐 [API Gateway Configuration](../apigateway/cloudshelf-apigateway-setup.md)
3. 📊 [Database Schema Design](rds-data-model.md)

---

**External Reference**: [AWS RDS Documentation](https://docs.aws.amazon.com/rds/)

_Part of the CloudShelf Solutions Architecture documentation_  
_Last updated: September 3, 2025_
