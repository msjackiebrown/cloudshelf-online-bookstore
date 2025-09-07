# 🗃️ CloudShelf RDS PostgreSQL Setup (Phase 1)

> PostgreSQL database setup using default VPC for realistic serverless learning

This guide implements the database component of Phase 1, providing relational database capabilities while maintaining simplicity through AWS default VPC configuration.

---

## 🎯 Phase 1 Database Strategy

### **🏗️ Why PostgreSQL in Default VPC?**

**Learning Benefits**:

- ✅ **Real relational database** experience with SQL
- ✅ **VPC networking basics** without custom configuration complexity
- ✅ **Security group concepts** for database access control
- ✅ **Existing Lambda code compatibility** (no DynamoDB conversion needed)
- ✅ **Smooth progression** to Phase 2 custom VPC

**Database Allocation**:

```yaml
PostgreSQL RDS (Phase 1):
  - Books catalog (complex queries, search)
  - User management (profiles, relationships)
  - Order processing (ACID transactions)

DynamoDB (Simplified):
  - Shopping carts (high-performance key-value)
  - User sessions (TTL support)
```

---

## 🏛️ Default VPC Architecture

### **🌐 Network Design**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        Default VPC (AWS Managed)                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  🌍 Internet Gateway (Auto-configured)                                         │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                     Public Subnets                                     │   │
│  │              (Auto-assigned in each AZ)                                │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                    Private Subnets                                     │   │
│  │  ┌─────────────────────────────────────────────────────────────────┐   │   │
│  │  │                 🗃️ RDS PostgreSQL                              │   │   │
│  │  │              (Multi-AZ Capable)                                │   │   │
│  │  │         ┌─────────────┐    ┌─────────────┐                     │   │   │
│  │  │         │   AZ-1a     │    │   AZ-1b     │                     │   │   │
│  │  │         │  Primary    │    │  Standby    │                     │   │   │
│  │  │         └─────────────┘    └─────────────┘                     │   │   │
│  │  └─────────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       ▲                                                                         │
│       │                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                  ⚡ Lambda Functions                                    │   │
│  │              (VPC-connected for RDS access)                             │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🚀 Implementation Guide

### **Step 1: Verify Default VPC**

Ensure your AWS account has a default VPC (most regions have this automatically).

1. **🖥️ Open VPC Console**

   - Navigate to: `AWS Console → VPC → Your VPCs`
   - Look for VPC marked as `Default: Yes`

2. **✅ Verify Default VPC Components**
   ```
   Default VPC should have:
   ✅ Internet Gateway attached
   ✅ Public subnets in each AZ
   ✅ Private subnets in each AZ (for RDS)
   ✅ Default security group
   ✅ Route tables configured
   ```

![Default VPC Verification](../screenshots/rds/Default-VPC-Verification-Step1.png)

---

### **Step 2: Create RDS Security Group**

Create a dedicated security group for PostgreSQL database access.

1. **🔒 Create RDS Security Group**

   - Navigate to: `EC2 Console → Security Groups → Create Security Group`
   - **Name**: `cloudshelf-rds-sg-phase1`
   - **Description**: `CloudShelf RDS PostgreSQL access for Phase 1`
   - **VPC**: Select your default VPC

2. **📝 Configure Inbound Rules**

   ```yaml
   Inbound Rules:
     - Type: PostgreSQL (5432)
       Source: Custom (will set to Lambda SG later)
       Description: Lambda function access to PostgreSQL
   ```

3. **📤 Configure Outbound Rules**
   ```yaml
   Outbound Rules:
     - Type: All Traffic
       Destination: 0.0.0.0/0
       Description: Default outbound (can be restricted later)
   ```

![RDS Security Group Creation](../screenshots/rds/RDS-Security-Group-Creation-Step2.png)

---

### **Step 3: Create Lambda Security Group**

Create security group for Lambda functions that need database access.

1. **⚡ Create Lambda Security Group**

   - **Name**: `cloudshelf-lambda-sg-phase1`
   - **Description**: `CloudShelf Lambda functions for Phase 1`
   - **VPC**: Default VPC

2. **📝 Configure Lambda Security Group**

   ```yaml
   Inbound Rules:
     - None needed (Lambda doesn't receive direct connections)

   Outbound Rules:
     - Type: PostgreSQL (5432)
       Destination: cloudshelf-rds-sg-phase1
       Description: Access to CloudShelf PostgreSQL database
     - Type: HTTPS (443)
       Destination: 0.0.0.0/0
       Description: Internet access for DynamoDB, API calls
   ```

3. **🔗 Update RDS Security Group**
   - Go back to `cloudshelf-rds-sg-phase1`
   - Edit inbound rules
   - Set PostgreSQL rule source to `cloudshelf-lambda-sg-phase1`

![Lambda Security Group Creation](../screenshots/rds/Lambda-Security-Group-Creation-Step3.png)

---

### **Step 4: Create RDS Subnet Group**

Configure subnet group for RDS placement in default VPC.

1. **🌐 Create DB Subnet Group**

   - Navigate to: `RDS Console → Subnet Groups → Create DB Subnet Group`
   - **Name**: `cloudshelf-default-vpc-subnet-group`
   - **Description**: `CloudShelf RDS subnet group using default VPC`
   - **VPC**: Default VPC

2. **📍 Select Subnets**
   ```yaml
   Subnet Selection:
     - Select at least 2 subnets in different AZs
     - Choose private subnets if available
     - Include subnets in primary AZs (us-east-1a, us-east-1b, etc.)
   ```

![RDS Subnet Group Creation](../screenshots/rds/RDS-Subnet-Group-Creation-Step4.png)

---

### **Step 5: Create PostgreSQL RDS Instance**

Create the PostgreSQL database instance for CloudShelf.

1. **🗃️ Launch RDS Instance**

   - Navigate to: `RDS Console → Databases → Create Database`
   - **Creation Method**: Standard Create
   - **Engine Type**: PostgreSQL

2. **⚙️ Configure Database Settings**

   ```yaml
   Engine Version: PostgreSQL 15.x (latest)
   Templates: Free tier (for learning)

   Settings:
     DB Instance Identifier: cloudshelf-postgres-phase1
     Master Username: cloudshelf_admin
     Master Password: [Generate secure password]

   Instance Configuration:
     DB Instance Class: db.t3.micro (free tier eligible)
     Storage Type: General Purpose SSD (gp2)
     Allocated Storage: 20 GB (free tier)
     Storage Autoscaling: Disabled (keep simple)
   ```

3. **🌐 Network Configuration**

   ```yaml
   Connectivity:
     VPC: Default VPC
     DB Subnet Group: cloudshelf-default-vpc-subnet-group
     Public Access: No (private access only)
     VPC Security Groups: cloudshelf-rds-sg-phase1
     Availability Zone: No preference

   Database Port: 5432 (default PostgreSQL port)
   ```

4. **🔧 Additional Configuration**

   ```yaml
   Database Options:
     Initial Database Name: cloudshelf
     DB Parameter Group: default.postgres15
     Option Group: default:postgres-15

   Backup:
     Backup Retention: 7 days (learning appropriate)
     Backup Window: Default
     Copy Tags to Snapshots: Yes

   Monitoring:
     Enhanced Monitoring: Disabled (keep simple)

   Maintenance:
     Auto Minor Version Upgrade: Yes
     Maintenance Window: Default

   Deletion Protection: Disabled (for learning environment)
   ```

![RDS Instance Creation](../screenshots/rds/RDS-Instance-Creation-Step5.png)

---

### **Step 6: Configure Database Schema**

Set up the CloudShelf database schema and sample data.

1. **🔗 Connect to Database**

   **Using psql (if installed locally)**:

   ```bash
   psql -h your-rds-endpoint.region.rds.amazonaws.com -U cloudshelf_admin -d cloudshelf
   ```

   **Using AWS Cloud9 or EC2 instance**:

   ```bash
   # Install PostgreSQL client
   sudo yum install postgresql15 -y  # Amazon Linux
   # or
   sudo apt-get install postgresql-client -y  # Ubuntu

   # Connect to database
   psql -h your-rds-endpoint -U cloudshelf_admin -d cloudshelf
   ```

2. **📋 Create Database Schema**

   ```sql
   -- Books table for catalog management
   CREATE TABLE books (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       title VARCHAR(255) NOT NULL,
       author VARCHAR(255) NOT NULL,
       isbn VARCHAR(13) UNIQUE,
       price DECIMAL(10,2) NOT NULL,
       category VARCHAR(100),
       description TEXT,
       image_url VARCHAR(500),
       stock_quantity INTEGER DEFAULT 0,
       featured BOOLEAN DEFAULT false,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Users table for customer management
   CREATE TABLE users (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       email VARCHAR(255) UNIQUE NOT NULL,
       first_name VARCHAR(100),
       last_name VARCHAR(100),
       phone VARCHAR(20),
       address_line1 VARCHAR(255),
       address_line2 VARCHAR(255),
       city VARCHAR(100),
       state VARCHAR(50),
       postal_code VARCHAR(20),
       country VARCHAR(50) DEFAULT 'US',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Orders table for purchase management
   CREATE TABLE orders (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       user_id UUID REFERENCES users(id) ON DELETE CASCADE,
       order_number VARCHAR(50) UNIQUE NOT NULL,
       total_amount DECIMAL(10,2) NOT NULL,
       tax_amount DECIMAL(10,2) DEFAULT 0.00,
       shipping_amount DECIMAL(10,2) DEFAULT 0.00,
       status VARCHAR(50) DEFAULT 'pending',
       payment_status VARCHAR(50) DEFAULT 'pending',
       shipping_address TEXT,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Order items table for order details
   CREATE TABLE order_items (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
       book_id UUID REFERENCES books(id) ON DELETE RESTRICT,
       quantity INTEGER NOT NULL CHECK (quantity > 0),
       unit_price DECIMAL(10,2) NOT NULL,
       total_price DECIMAL(10,2) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Create indexes for better performance
   CREATE INDEX idx_books_category ON books(category);
   CREATE INDEX idx_books_author ON books(author);
   CREATE INDEX idx_books_featured ON books(featured);
   CREATE INDEX idx_users_email ON users(email);
   CREATE INDEX idx_orders_user_id ON orders(user_id);
   CREATE INDEX idx_orders_status ON orders(status);
   CREATE INDEX idx_order_items_order_id ON order_items(order_id);
   CREATE INDEX idx_order_items_book_id ON order_items(book_id);
   ```

3. **📊 Insert Sample Data**

   ```sql
   -- Sample books data
   INSERT INTO books (title, author, isbn, price, category, description, stock_quantity, featured) VALUES
   ('The AWS Solutions Architect Study Guide', 'Ben Piper', '9781119713081', 49.99, 'Technology', 'Comprehensive guide for AWS Solutions Architect certification', 25, true),
   ('Clean Code', 'Robert C. Martin', '9780132350884', 42.99, 'Programming', 'A handbook of agile software craftsmanship', 15, true),
   ('Design Patterns', 'Gang of Four', '9780201633610', 54.99, 'Programming', 'Elements of reusable object-oriented software', 20, false),
   ('The Phoenix Project', 'Gene Kim', '9780988262591', 17.99, 'Business', 'A novel about IT, DevOps, and helping your business win', 30, true),
   ('Microservices Patterns', 'Chris Richardson', '9781617294549', 44.99, 'Architecture', 'With examples in Java', 12, false),
   ('Site Reliability Engineering', 'Google', '9781491929124', 39.99, 'Operations', 'How Google runs production systems', 18, false);

   -- Sample users data
   INSERT INTO users (email, first_name, last_name, phone, city, state, country) VALUES
   ('john.doe@example.com', 'John', 'Doe', '555-0123', 'Seattle', 'WA', 'US'),
   ('jane.smith@example.com', 'Jane', 'Smith', '555-0124', 'Portland', 'OR', 'US'),
   ('mike.johnson@example.com', 'Mike', 'Johnson', '555-0125', 'San Francisco', 'CA', 'US');

   -- Sample orders data
   INSERT INTO orders (user_id, order_number, total_amount, tax_amount, shipping_amount, status, payment_status)
   SELECT
       u.id,
       'ORD-' || EXTRACT(EPOCH FROM NOW())::bigint || '-' || ROW_NUMBER() OVER(),
       92.98,
       8.37,
       5.99,
       'completed',
       'paid'
   FROM users u WHERE u.email = 'john.doe@example.com';

   -- Sample order items
   INSERT INTO order_items (order_id, book_id, quantity, unit_price, total_price)
   SELECT
       o.id,
       b.id,
       1,
       b.price,
       b.price
   FROM orders o
   CROSS JOIN books b
   WHERE o.order_number LIKE 'ORD-%'
   AND b.title IN ('The AWS Solutions Architect Study Guide', 'Clean Code')
   LIMIT 2;
   ```

![Database Schema Creation](../screenshots/rds/Database-Schema-Creation-Step6.png)

---

## 🧪 Testing & Validation

### **Step 7: Test Database Connectivity**

Verify that the database is properly configured and accessible.

1. **🔍 Test Basic Queries**

   ```sql
   -- Test data retrieval
   SELECT COUNT(*) as total_books FROM books;
   SELECT COUNT(*) as total_users FROM users;
   SELECT COUNT(*) as total_orders FROM orders;

   -- Test joins and relationships
   SELECT
       u.first_name,
       u.last_name,
       o.order_number,
       o.total_amount,
       o.status
   FROM users u
   JOIN orders o ON u.id = o.user_id;

   -- Test book search functionality
   SELECT title, author, price, category
   FROM books
   WHERE category = 'Technology'
   OR title ILIKE '%AWS%';
   ```

2. **📊 Verify Performance**

   ```sql
   -- Check index usage
   EXPLAIN ANALYZE SELECT * FROM books WHERE category = 'Technology';
   EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john.doe@example.com';
   ```

3. **🔒 Test Security Group Connectivity**
   - From Lambda function (will test in next guide)
   - Verify no public internet access
   - Confirm security group restrictions work

---

## 📝 Environment Configuration

### **Database Connection Details**

**For Lambda Environment Variables**:

```yaml
Environment Variables:
  DB_HOST: your-rds-endpoint.region.rds.amazonaws.com
  DB_PORT: 5432
  DB_NAME: cloudshelf
  DB_USER: cloudshelf_admin
  DB_PASSWORD: [Store in AWS Secrets Manager - see next guide]
```

**Connection String Format**:

```
postgresql://cloudshelf_admin:password@your-endpoint:5432/cloudshelf
```

### **Security Best Practices**

- ✅ **Password Storage**: Use AWS Secrets Manager (setup in Lambda guide)
- ✅ **Network Access**: Database only accessible from Lambda security group
- ✅ **Encryption**: RDS encryption at rest enabled
- ✅ **Backup Strategy**: 7-day automated backups for learning environment
- ✅ **Monitoring**: CloudWatch basic monitoring enabled

---

## 💰 Cost Optimization

### **Free Tier Benefits**

```yaml
Free Tier Coverage (12 months):
  - db.t3.micro instance: 750 hours/month
  - Storage: 20GB General Purpose SSD
  - Backup Storage: 20GB automated backups

Expected Monthly Cost:
  - Within Free Tier: $0
  - After Free Tier: $15-25/month for learning use
```

### **Cost Management Tips**

- ✅ **Stop/Start RDS**: For learning environment, stop RDS when not in use
- ✅ **Single AZ**: Use single AZ deployment for Phase 1
- ✅ **Monitor Usage**: Set up billing alerts
- ✅ **Cleanup**: Delete instance when learning complete

---

## ➡️ Next Steps

### **Immediate Actions**

1. **⚡ Lambda VPC Configuration**: Set up Lambda functions to connect to this RDS instance
2. **🔐 Secrets Management**: Configure AWS Secrets Manager for database credentials
3. **🌐 API Integration**: Connect RDS to API Gateway through Lambda
4. **📊 Monitoring Setup**: Basic CloudWatch monitoring for database

### **Development Tasks**

- **Book Catalog Service**: Connect existing Lambda to PostgreSQL
- **User Management Service**: Create Lambda for user operations
- **Order Processing Service**: Create Lambda for order management
- **Connection Pooling**: Implement efficient database connections

### **Related Guides**

- 🔗 **Next**: [Basic Lambda Setup](cloudshelf-basic-lambda-setup.md)
- 🔗 **Security**: [AWS Secrets Manager Setup](cloudshelf-secrets-manager-setup.md)
- 🔗 **Monitoring**: [RDS CloudWatch Monitoring](cloudshelf-rds-monitoring.md)

---

## 🎓 Learning Outcomes

After completing this guide, you will understand:

- ✅ **Default VPC architecture** and how AWS provides networking defaults
- ✅ **RDS PostgreSQL setup** in a managed VPC environment
- ✅ **Security group configuration** for database access control
- ✅ **Database schema design** for relational data storage
- ✅ **Network connectivity patterns** between Lambda and RDS

**You now have a production-like PostgreSQL database ready for CloudShelf Phase 1!**

---

_📋 **Guide Status**: Core Infrastructure | ✅ **Database Ready**: Yes | 🔄 **Next**: Lambda VPC Integration_  
_🏗️ **Architecture Phase**: Phase 1 | 👥 **Team**: Database + Network Setup | 📋 **Duration**: 45-60 minutes_
