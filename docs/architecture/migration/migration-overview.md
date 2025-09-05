# 🔄 CloudShelf Migration Guide

> Complete migration strategy from Phase 1 (DynamoDB) to Phase 2 (PostgreSQL + VPC) with minimal downtime

This guide provides a comprehensive migration strategy to transition your CloudShelf application from Phase 1's simple serverless architecture to Phase 2's production-grade infrastructure with enhanced security and capabilities.

---

## 🎯 Migration Overview

### **🚀 Migration Goals**

- ✅ **Zero data loss** - Preserve all books, users, and order history
- ✅ **Minimal downtime** - Blue/green deployment for seamless transition
- ✅ **Enhanced architecture** - VPC security with PostgreSQL performance
- ✅ **Backward compatibility** - Maintain all existing API functionality
- ✅ **Improved performance** - Better query capabilities and response times

### **📊 Migration Strategy Matrix**

| Approach              | Downtime      | Complexity | Cost   | Risk   | Best For                  |
| --------------------- | ------------- | ---------- | ------ | ------ | ------------------------- |
| **Blue/Green**        | 0-5 minutes   | Medium     | High   | Low    | Production environments   |
| **Rolling Migration** | 10-30 minutes | High       | Medium | Medium | Development/staging       |
| **Big Bang**          | 2-6 hours     | Low        | Low    | High   | Non-critical environments |

**✅ Recommended**: Blue/Green migration for production workloads

---

## 🏗️ Migration Architecture

### **🔄 Blue/Green Migration Pattern**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        CloudShelf Migration Strategy                           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  👤 Users                                                                       │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                    🌍 CloudFront CDN                                    │   │
│  │               (Routes to Active Environment)                           │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                     📡 API Gateway                                      │   │
│  │                (Switch Origins During Migration)                       │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│       │                                                                         │
│       ├─────────────────────────┬───────────────────────────────────────────┘   │
│       ▼                         ▼                                               │
│  ┌─────────────────────┐   ┌─────────────────────────────────────────────┐     │
│  │     💙 BLUE         │   │              💚 GREEN                       │     │
│  │  (Phase 1 - Live)   │   │         (Phase 2 - Staging)                │     │
│  │                     │   │                                             │     │
│  │  ⚡ Lambda Funcs    │   │  🌐 VPC Private Network                     │     │
│  │  ┌─────────────┐    │   │  ┌─────────────────────────────────────┐   │     │
│  │  │📊 DynamoDB  │    │   │  │          ⚡ Lambda Funcs           │   │     │
│  │  │             │    │   │  │      (VPC-enabled)                  │   │     │
│  │  │• Books      │◄───┼───┼─►│                                     │   │     │
│  │  │• Users      │ Sync  │  │  ┌─────────────┐  ┌─────────────┐   │   │     │
│  │  │• Orders     │    │   │  │  │🗃️PostgreSQL│  │📊 DynamoDB  │   │   │     │
│  │  │• Carts      │    │   │  │  │             │  │             │   │   │     │
│  │  └─────────────┘    │   │  │  │• Books      │  │• Carts      │   │   │     │
│  │                     │   │  │  │• Users      │  │• Sessions   │   │   │     │
│  │  📈 Active Traffic  │   │  │  │• Orders     │  │             │   │   │     │
│  │  👥 Live Users      │   │  │  └─────────────┘  └─────────────┘   │   │     │
│  └─────────────────────┘   │  └─────────────────────────────────────┘   │     │
│                             │                                             │     │
│                             │  🧪 Testing & Validation                   │     │
│                             │  📊 Performance Benchmarks                 │     │
│                             └─────────────────────────────────────────────┘     │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                    🔄 Migration Control Plane                          │   │
│  │                                                                         │   │
│  │  📊 Data Sync Jobs        🧪 Validation Tests       🚦 Traffic Switch   │   │
│  │  • Real-time sync         • API compatibility       • Blue → Green      │   │
│  │  • Batch migrations       • Data integrity         • Gradual rollout   │   │
│  │  • Conflict resolution    • Performance tests      • Instant rollback  │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Data Migration Strategy

### **🗂️ Data Categories & Migration Approach**

| Data Type       | Volume | Migration Strategy | Sync Method                | Downtime Impact |
| --------------- | ------ | ------------------ | -------------------------- | --------------- |
| **📚 Books**    | High   | Batch migration    | Initial load + incremental | None            |
| **👥 Users**    | Medium | Batch migration    | Initial load + incremental | None            |
| **📦 Orders**   | High   | Batch migration    | Initial load + incremental | None            |
| **🛒 Carts**    | Low    | Hybrid approach    | Keep in DynamoDB           | None            |
| **🔑 Sessions** | Low    | Real-time          | Live migration             | None            |

### **🔄 Phased Data Migration Plan**

#### **Phase A: Preparation (2-4 hours)**

```
📊 Pre-Migration Steps:
├── 📋 Data Audit
│   ├── Export complete DynamoDB data
│   ├── Validate data integrity
│   └── Document current schema
├── 🗃️ PostgreSQL Setup
│   ├── Create target schema
│   ├── Set up indexes
│   └── Configure connection pools
└── 🧪 Migration Testing
    ├── Test migration scripts
    ├── Validate data transformation
    └── Performance benchmarks
```

#### **Phase B: Initial Data Load (1-3 hours)**

```
📦 Bulk Data Migration:
├── 📚 Books (Read-heavy, stable data)
│   ├── Export from DynamoDB
│   ├── Transform to PostgreSQL schema
│   └── Bulk insert with COPY command
├── 👥 Users (Medium volume, occasional updates)
│   ├── Export user profiles
│   ├── Transform authentication data
│   └── Import with conflict resolution
└── 📦 Orders (Historical data, append-only)
    ├── Export order history
    ├── Preserve order relationships
    └── Import with referential integrity
```

#### **Phase C: Incremental Sync (Ongoing)**

```
🔄 Real-time Synchronization:
├── 📊 DynamoDB Streams
│   ├── Capture changes in real-time
│   ├── Transform to PostgreSQL format
│   └── Apply updates with conflict resolution
├── 🚦 Dual-write Pattern
│   ├── Write to both databases
│   ├── Use PostgreSQL as source of truth
│   └── Monitor for sync issues
└── 🧪 Continuous Validation
    ├── Compare data between systems
    ├── Alert on discrepancies
    └── Automated reconciliation
```

#### **Phase D: Cutover (5-15 minutes)**

```
🚀 Production Switch:
├── 🔒 Freeze Phase 1 writes
├── 🔄 Final sync verification
├── 🚦 Switch API Gateway origins
├── 🧪 Validate Phase 2 functionality
└── 📊 Monitor all systems
```

---

## 🛠️ Migration Implementation

### **🔧 Migration Tools & Scripts**

#### **Data Export Script**

```bash
# Export DynamoDB tables
aws dynamodb scan --table-name CloudShelf-Books --output json > books.json
aws dynamodb scan --table-name CloudShelf-Users --output json > users.json
aws dynamodb scan --table-name CloudShelf-Orders --output json > orders.json
aws dynamodb scan --table-name CloudShelf-Carts --output json > carts.json
```

#### **PostgreSQL Schema Creation**

```sql
-- Books table with enhanced search capabilities
CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    stock_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    search_vector tsvector GENERATED ALWAYS AS (
        to_tsvector('english', title || ' ' || author || ' ' || COALESCE(description, ''))
    ) STORED
);

-- Create indexes for performance
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_search ON books USING GIN(search_vector);
```

#### **Data Transformation Function**

```python
import json
import psycopg2
from decimal import Decimal

def migrate_books_data():
    """Migrate books from DynamoDB to PostgreSQL"""

    # Load DynamoDB export
    with open('books.json', 'r') as f:
        dynamo_data = json.load(f)

    # Connect to PostgreSQL
    conn = psycopg2.connect(
        host=os.environ['RDS_ENDPOINT'],
        database='cloudshelf',
        user=os.environ['DB_USER'],
        password=os.environ['DB_PASSWORD']
    )
    cursor = conn.cursor()

    # Transform and insert data
    for item in dynamo_data['Items']:
        book_data = {
            'id': item['bookId']['S'],
            'title': item['title']['S'],
            'author': item['author']['S'],
            'description': item.get('description', {}).get('S', ''),
            'price': Decimal(item['price']['N']),
            'category': item.get('category', {}).get('S', 'General'),
            'isbn': item.get('isbn', {}).get('S'),
            'stock_quantity': int(item.get('stockQuantity', {}).get('N', 0))
        }

        cursor.execute("""
            INSERT INTO books (id, title, author, description, price, category, isbn, stock_quantity)
            VALUES (%(id)s, %(title)s, %(author)s, %(description)s, %(price)s, %(category)s, %(isbn)s, %(stock_quantity)s)
            ON CONFLICT (id) DO UPDATE SET
                title = EXCLUDED.title,
                author = EXCLUDED.author,
                description = EXCLUDED.description,
                price = EXCLUDED.price,
                category = EXCLUDED.category,
                stock_quantity = EXCLUDED.stock_quantity,
                updated_at = NOW()
        """, book_data)

    conn.commit()
    cursor.close()
    conn.close()
```

### **⚡ Lambda Function Updates**

#### **Database Connection Handler**

```python
import os
import psycopg2
from psycopg2.pool import SimpleConnectionPool

# Global connection pool
connection_pool = None

def get_db_connection():
    """Get PostgreSQL connection from pool"""
    global connection_pool

    if connection_pool is None:
        connection_pool = SimpleConnectionPool(
            1, 20,  # min and max connections
            host=os.environ['RDS_ENDPOINT'],
            database='cloudshelf',
            user=os.environ['DB_USER'],
            password=os.environ['DB_PASSWORD'],
            port=5432
        )

    return connection_pool.getconn()

def return_db_connection(conn):
    """Return connection to pool"""
    global connection_pool
    connection_pool.putconn(conn)
```

#### **Hybrid Data Access Pattern**

```python
def get_book_details(book_id):
    """Get book details from PostgreSQL with fallback to DynamoDB"""

    # Try PostgreSQL first (Phase 2)
    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        cursor.execute("""
            SELECT id, title, author, description, price, category, stock_quantity
            FROM books
            WHERE id = %s
        """, (book_id,))

        result = cursor.fetchone()
        cursor.close()
        return_db_connection(conn)

        if result:
            return {
                'bookId': result[0],
                'title': result[1],
                'author': result[2],
                'description': result[3],
                'price': float(result[4]),
                'category': result[5],
                'stockQuantity': result[6]
            }

    except Exception as e:
        print(f"PostgreSQL error: {e}")

        # Fallback to DynamoDB (Phase 1)
        dynamodb = boto3.resource('dynamodb')
        table = dynamodb.Table('CloudShelf-Books')

        response = table.get_item(Key={'bookId': book_id})
        return response.get('Item')
```

---

## 🧪 Migration Testing Strategy

### **📋 Pre-Migration Testing**

#### **🔍 Data Validation Tests**

```python
def validate_data_integrity():
    """Comprehensive data integrity checks"""

    checks = []

    # Count validation
    dynamo_count = get_dynamodb_record_count('CloudShelf-Books')
    postgres_count = get_postgresql_record_count('books')
    checks.append({
        'test': 'Record Count Match',
        'passed': dynamo_count == postgres_count,
        'dynamo': dynamo_count,
        'postgres': postgres_count
    })

    # Sample data comparison
    sample_books = get_sample_books_dynamodb(100)
    for book in sample_books:
        postgres_book = get_book_postgresql(book['bookId'])
        checks.append({
            'test': f'Book {book["bookId"]} Data Match',
            'passed': compare_book_data(book, postgres_book),
            'differences': get_data_differences(book, postgres_book)
        })

    return checks
```

#### **⚡ Performance Benchmark Tests**

```python
def performance_benchmarks():
    """Compare Phase 1 vs Phase 2 performance"""

    tests = [
        {
            'name': 'Get Book Details',
            'function': lambda: get_book_details('test-book-id'),
            'target_latency': 100  # ms
        },
        {
            'name': 'Search Books',
            'function': lambda: search_books('python programming'),
            'target_latency': 200  # ms
        },
        {
            'name': 'Get User Orders',
            'function': lambda: get_user_orders('test-user-id'),
            'target_latency': 150  # ms
        }
    ]

    results = []
    for test in tests:
        # Warm-up runs
        for _ in range(3):
            test['function']()

        # Actual timing
        times = []
        for _ in range(10):
            start = time.time()
            test['function']()
            times.append((time.time() - start) * 1000)

        avg_latency = sum(times) / len(times)
        results.append({
            'test': test['name'],
            'avg_latency_ms': avg_latency,
            'target_ms': test['target_latency'],
            'passed': avg_latency <= test['target_latency'],
            'all_times': times
        })

    return results
```

### **🚦 During Migration Monitoring**

#### **📊 Real-time Sync Monitoring**

```python
def monitor_migration_progress():
    """Monitor data sync progress and health"""

    metrics = {
        'sync_lag': measure_sync_lag(),
        'error_rate': get_sync_error_rate(),
        'throughput': get_sync_throughput(),
        'conflicts': get_conflict_count(),
        'data_drift': detect_data_drift()
    }

    # Alert thresholds
    alerts = []
    if metrics['sync_lag'] > 30:  # seconds
        alerts.append('High sync lag detected')
    if metrics['error_rate'] > 0.01:  # 1%
        alerts.append('High error rate in sync')
    if metrics['conflicts'] > 10:
        alerts.append('Data conflicts detected')

    return {
        'metrics': metrics,
        'alerts': alerts,
        'health': 'healthy' if not alerts else 'warning'
    }
```

---

## 🚀 Migration Execution Plan

### **📅 Timeline & Milestones**

#### **Week 1: Preparation**

- [ ] **Day 1-2**: Phase 2 infrastructure setup
- [ ] **Day 3-4**: Data migration scripts development
- [ ] **Day 5**: Migration testing in staging environment

#### **Week 2: Migration Execution**

- [ ] **Day 1**: Final preparation and go/no-go decision
- [ ] **Day 2**: Execute migration (maintenance window)
- [ ] **Day 3-5**: Monitor and optimize Phase 2
- [ ] **Day 6-7**: Phase 1 cleanup and documentation

### **🎯 Go/No-Go Criteria**

#### **✅ Go Criteria**

- [ ] **All tests passing** - Data integrity and performance tests
- [ ] **Infrastructure ready** - Phase 2 fully deployed and tested
- [ ] **Team prepared** - Migration team trained and ready
- [ ] **Rollback plan** - Tested and verified rollback procedures
- [ ] **Monitoring ready** - All alerting and dashboards configured

#### **🚫 No-Go Criteria**

- [ ] **Test failures** - Any critical test failures
- [ ] **Performance issues** - Phase 2 slower than Phase 1
- [ ] **Infrastructure problems** - Any component not ready
- [ ] **Team concerns** - Any team member blocking issues
- [ ] **Business impact** - High-impact business events

### **⏰ Migration Day Checklist**

#### **Pre-Migration (T-2 hours)**

- [ ] **Final sync check** - Ensure data is up to date
- [ ] **Team assembly** - All migration team members present
- [ ] **Communication** - Notify stakeholders of maintenance start
- [ ] **Monitoring** - Verify all monitoring systems active
- [ ] **Rollback prep** - Ensure rollback plan ready to execute

#### **Migration Execution (T-0 to T+30 minutes)**

- [ ] **T-0**: Put Phase 1 in read-only mode
- [ ] **T+5**: Execute final data sync
- [ ] **T+10**: Verify data consistency
- [ ] **T+15**: Switch API Gateway to Phase 2
- [ ] **T+20**: Verify all APIs responding correctly
- [ ] **T+25**: Run smoke tests
- [ ] **T+30**: Resume full operations or rollback

#### **Post-Migration (T+30 minutes to T+24 hours)**

- [ ] **Immediate**: Monitor error rates and performance
- [ ] **T+1 hour**: Comprehensive functionality testing
- [ ] **T+4 hours**: Performance validation
- [ ] **T+12 hours**: Extended monitoring review
- [ ] **T+24 hours**: Migration success confirmation

---

## 🔄 Rollback Strategy

### **🚨 Emergency Rollback Plan**

#### **Immediate Rollback (< 5 minutes)**

```bash
# Switch API Gateway back to Phase 1
aws apigateway update-stage \
    --rest-api-id $API_ID \
    --stage-name prod \
    --patch-ops op=replace,path=/variables/lambdaAlias,value=blue

# Verify rollback
curl -X GET $API_ENDPOINT/books/health
```

#### **Data Rollback Considerations**

- ✅ **Read operations** - Immediate rollback possible
- ⚠️ **Write operations** - May need data reconciliation
- 🔒 **New data** - Phase 2 data needs to be preserved
- 📊 **Sync state** - Resume sync from last checkpoint

### **🧹 Post-Rollback Actions**

1. **📊 Analyze failure** - Root cause analysis
2. **🔄 Plan remediation** - Fix identified issues
3. **🧪 Retest thoroughly** - Validate fixes
4. **📅 Reschedule migration** - New timeline with lessons learned

---

## 📊 Success Metrics & KPIs

### **📈 Migration Success Criteria**

| Metric          | Target         | Measurement             |
| --------------- | -------------- | ----------------------- |
| **Data Loss**   | 0%             | Record count comparison |
| **API Latency** | < 500ms p95    | CloudWatch metrics      |
| **Error Rate**  | < 0.1%         | API Gateway logs        |
| **Downtime**    | < 5 minutes    | Service availability    |
| **Performance** | Same or better | Benchmark comparison    |

### **📊 Post-Migration Monitoring**

#### **Week 1: Intensive Monitoring**

- **Every 15 minutes**: Health checks and error rates
- **Hourly**: Performance metrics review
- **Daily**: Data integrity validation

#### **Week 2-4: Regular Monitoring**

- **Hourly**: Automated health checks
- **Daily**: Performance trend analysis
- **Weekly**: Comprehensive system review

#### **Month 1+: Steady State**

- **Continuous**: Automated monitoring and alerting
- **Weekly**: Performance optimization reviews
- **Monthly**: Architecture and cost optimization

---

## 📚 Migration Documentation

### **📋 Required Documentation**

- [ ] **Migration runbook** - Step-by-step execution guide
- [ ] **Rollback procedures** - Emergency response plan
- [ ] **Monitoring playbook** - Operational procedures
- [ ] **Troubleshooting guide** - Common issues and solutions
- [ ] **Performance baselines** - Pre and post-migration metrics

### **🎓 Training Materials**

- [ ] **Team training** - Migration process and responsibilities
- [ ] **Operational training** - Phase 2 system management
- [ ] **Emergency procedures** - Incident response training
- [ ] **Knowledge transfer** - Document lessons learned

---

## 🎯 Next Steps

### **🚀 Ready to Migrate?**

1. **📖 Review Phase 2 setup** - Ensure Phase 2 is fully deployed
2. **🧪 Complete testing** - Validate all migration components
3. **📋 Prepare checklist** - Customize for your environment
4. **👥 Assemble team** - Ensure all stakeholders ready
5. **📅 Schedule migration** - Choose appropriate maintenance window

### **📚 Additional Resources**

- 📖 [**Data Migration Detailed Guide**](data-migration-guide.md)
- 🚀 [**Go-Live Checklist**](go-live-checklist.md)
- 📊 [**Monitoring Setup Guide**](../phase2-production-setup/monitoring-and-logging.md)
- 🔒 [**Security Validation**](../phase2-production-setup/security-hardening.md)

---

**🔄 Ready to migrate to production-grade architecture? Let's ensure a smooth transition!**

_📋 **Documentation Status**: Complete | ✅ **Migration Ready**: Yes | 🔄 **Last Updated**: Migration Planning_  
_🎯 **Phase**: Migration | 👥 **Audience**: DevOps Teams | 📋 **Duration**: 1-2 days_
