# 🚨 Disaster Recovery & Business Continuity

> Comprehensive disaster recovery and business continuity strategy for CloudShelf online bookstore ensuring minimal downtime and data protection

This document defines the disaster recovery (DR) architecture, recovery procedures, and business continuity planning for CloudShelf to ensure system resilience and data protection against various failure scenarios.

---

## 🏛️ Disaster Recovery Architecture Overview

CloudShelf implements a multi-layered disaster recovery strategy with automated failover capabilities:

- **🎯 Recovery Objectives** - RTO/RPO definitions and SLA commitments
- **🔄 Multi-Region Strategy** - Primary and disaster recovery region setup
- **💾 Data Backup & Replication** - Automated backup and cross-region replication
- **🚀 Automated Failover** - Intelligent traffic routing and service recovery
- **📋 Recovery Procedures** - Documented step-by-step recovery processes

---

## 📷 Setup Screenshots

### **🌍 Multi-Region Architecture**

![Multi-Region DR Architecture](screenshots/01-multi-region-architecture.png)

### **💾 RDS Cross-Region Backup**

![RDS Cross-Region Backup Setup](screenshots/02-rds-cross-region-backup.png)

### **🔄 DynamoDB Global Tables**

![DynamoDB Global Tables Configuration](screenshots/03-dynamodb-global-tables.png)

### **🚀 Route 53 Health Checks**

![Route 53 Health Checks and Failover](screenshots/04-route53-health-checks.png)

### **📊 DR Monitoring Dashboard**

![Disaster Recovery Monitoring](screenshots/05-dr-monitoring-dashboard.png)

### **🧪 DR Testing Results**

![DR Testing and Validation](screenshots/06-dr-testing-results.png)

---

## 🎯 Recovery Objectives & SLAs

### **📊 Business Impact Analysis**

**Service Tier Classification**:

| Service Component       | Business Impact             | RTO Target   | RPO Target   | Priority Level |
| ----------------------- | --------------------------- | ------------ | ------------ | -------------- |
| **Book Catalog API**    | High - Core revenue         | < 1 hour     | < 15 minutes | Critical       |
| **Shopping Cart API**   | Critical - Direct revenue   | < 30 minutes | < 5 minutes  | Critical       |
| **User Authentication** | High - User experience      | < 1 hour     | < 15 minutes | High           |
| **Payment Processing**  | Critical - Revenue loss     | < 15 minutes | < 1 minute   | Critical       |
| **Static Website**      | Medium - Brand presence     | < 2 hours    | < 30 minutes | Medium         |
| **Analytics/Reporting** | Low - Business intelligence | < 24 hours   | < 4 hours    | Low            |

### **💰 Financial Impact Assessment**

**Downtime Cost Analysis**:

```
Revenue Impact per Hour of Downtime:
├── Peak Hours (9 AM - 9 PM): $2,500/hour
├── Off-Peak Hours (9 PM - 9 AM): $800/hour
├── Weekend Hours: $1,200/hour
└── Holiday Hours: $4,000/hour

Annual Availability Target: 99.9% (8.77 hours downtime/year)
Maximum Acceptable Revenue Loss: $50,000/year
```

**DR Investment Justification**:

- **DR Infrastructure Cost**: $3,000/month ($36,000/year)
- **Break-even Point**: 18 hours of prevented downtime/year
- **ROI**: Positive if DR prevents > 1.5 hours downtime/month

---

## 🌍 Multi-Region Architecture

### **🏗️ Primary and DR Region Setup**

**Region Strategy**:

```
Primary Region: us-east-1 (N. Virginia)
├── Active Production Environment
├── Real-time customer traffic
├── Primary RDS instances
└── Main DynamoDB tables

DR Region: us-west-2 (Oregon)
├── Standby environment (warm standby)
├── Cross-region RDS replicas
├── DynamoDB Global Tables
└── Pre-deployed Lambda functions
```

**Service Distribution**:

| Service              | Primary Region      | DR Region                | Failover Method        |
| -------------------- | ------------------- | ------------------------ | ---------------------- |
| **API Gateway**      | us-east-1 (Active)  | us-west-2 (Standby)      | Route 53 health check  |
| **Lambda Functions** | us-east-1 (Active)  | us-west-2 (Standby)      | Code deployment ready  |
| **RDS PostgreSQL**   | us-east-1 (Primary) | us-west-2 (Read Replica) | Manual promotion       |
| **DynamoDB**         | us-east-1 (Active)  | us-west-2 (Global Table) | Automatic multi-master |
| **S3 Static Assets** | us-east-1 (Primary) | us-west-2 (CRR)          | CloudFront failover    |
| **CloudFront**       | Global (Primary)    | Global (Failover)        | Origin failover        |

### **🔄 Route 53 Failover Configuration**

**DNS Failover Setup**:

```json
{
  "HostedZoneId": "Z123456789",
  "RecordSets": [
    {
      "Name": "api.cloudshelf.com",
      "Type": "A",
      "SetIdentifier": "primary-us-east-1",
      "Failover": "PRIMARY",
      "AliasTarget": {
        "DNSName": "cloudshelf-api-useast1.execute-api.us-east-1.amazonaws.com",
        "EvaluateTargetHealth": true
      },
      "HealthCheckId": "primary-health-check"
    },
    {
      "Name": "api.cloudshelf.com",
      "Type": "A",
      "SetIdentifier": "secondary-us-west-2",
      "Failover": "SECONDARY",
      "AliasTarget": {
        "DNSName": "cloudshelf-api-uswest2.execute-api.us-west-2.amazonaws.com",
        "EvaluateTargetHealth": true
      },
      "HealthCheckId": "secondary-health-check"
    }
  ]
}
```

**Health Check Configuration**:

```json
{
  "Type": "HTTPS",
  "ResourcePath": "/health",
  "FullyQualifiedDomainName": "cloudshelf-api-useast1.execute-api.us-east-1.amazonaws.com",
  "Port": 443,
  "RequestInterval": 30,
  "FailureThreshold": 3,
  "AlarmConfiguration": {
    "AlarmName": "cloudshelf-primary-health-alarm",
    "ComparisonOperator": "LessThanThreshold",
    "EvaluationPeriods": 2,
    "MetricName": "HealthCheckStatus",
    "Threshold": 1,
    "AlarmAction": ["arn:aws:sns:us-east-1:ACCOUNT:cloudshelf-dr-alerts"]
  }
}
```

---

## 💾 Data Backup & Replication Strategy

### **🗃️ RDS PostgreSQL Backup Strategy**

**Automated Backup Configuration**:

```json
{
  "DBInstanceIdentifier": "cloudshelf-postgres-primary",
  "BackupRetentionPeriod": 30,
  "PreferredBackupWindow": "03:00-04:00",
  "PreferredMaintenanceWindow": "sun:04:00-sun:05:00",
  "DeletionProtection": true,
  "PerformanceInsightsEnabled": true,
  "MonitoringInterval": 60,
  "EnableCloudwatchLogsExports": ["postgresql"],
  "ReadReplicaConfigurations": [
    {
      "DBInstanceIdentifier": "cloudshelf-postgres-dr-replica",
      "AvailabilityZone": "us-west-2a",
      "MultiAZ": true,
      "PubliclyAccessible": false,
      "StorageEncrypted": true
    }
  ]
}
```

**Point-in-Time Recovery Setup**:

```sql
-- Verify point-in-time recovery capability
SELECT name, setting FROM pg_settings
WHERE name IN ('wal_level', 'archive_mode', 'archive_command');

-- Create backup validation job
CREATE OR REPLACE FUNCTION validate_backup_integrity()
RETURNS boolean AS $$
DECLARE
    table_count integer;
    data_integrity boolean := true;
BEGIN
    -- Count critical tables
    SELECT COUNT(*) INTO table_count
    FROM information_schema.tables
    WHERE table_schema = 'public'
    AND table_name IN ('books', 'categories', 'authors', 'users');

    -- Validate data integrity
    IF table_count < 4 THEN
        data_integrity := false;
    END IF;

    -- Log validation result
    INSERT INTO backup_validation_log (timestamp, status, table_count)
    VALUES (NOW(), data_integrity, table_count);

    RETURN data_integrity;
END;
$$ LANGUAGE plpgsql;
```

### **⚡ DynamoDB Backup & Replication**

**Global Tables Configuration**:

```json
{
  "GlobalTableName": "CloudShelf-ShoppingCart",
  "ReplicationGroup": [
    {
      "RegionName": "us-east-1",
      "BackupSpecification": {
        "BackupEnabled": true,
        "BackupType": "CONTINUOUS"
      },
      "PointInTimeRecoverySpecification": {
        "PointInTimeRecoveryEnabled": true
      }
    },
    {
      "RegionName": "us-west-2",
      "BackupSpecification": {
        "BackupEnabled": true,
        "BackupType": "CONTINUOUS"
      },
      "PointInTimeRecoverySpecification": {
        "PointInTimeRecoveryEnabled": true
      }
    }
  ],
  "StreamSpecification": {
    "StreamEnabled": true,
    "StreamViewType": "NEW_AND_OLD_IMAGES"
  }
}
```

**Automated Backup Schedule**:

```javascript
// DynamoDB backup automation
const AWS = require("aws-sdk");
const dynamodb = new AWS.DynamoDB();

const createBackup = async (tableName) => {
  const backupName = `${tableName}-backup-${new Date()
    .toISOString()
    .slice(0, 10)}`;

  try {
    const result = await dynamodb
      .createBackup({
        TableName: tableName,
        BackupName: backupName,
      })
      .promise();

    console.log(`Backup created: ${result.BackupDetails.BackupArn}`);

    // Tag backup for lifecycle management
    await dynamodb
      .tagResource({
        ResourceArn: result.BackupDetails.BackupArn,
        Tags: [
          { Key: "Environment", Value: "production" },
          { Key: "RetentionDays", Value: "90" },
          { Key: "BackupType", Value: "daily-automated" },
        ],
      })
      .promise();
  } catch (error) {
    console.error(`Backup failed for ${tableName}:`, error);
    throw error;
  }
};

// Schedule daily backups
const tables = ["CloudShelf-ShoppingCart", "CloudShelf-UserSessions"];
Promise.all(tables.map(createBackup));
```

### **📁 S3 Cross-Region Replication**

**CRR Configuration**:

```json
{
  "BucketName": "cloudshelf-static-assets",
  "ReplicationConfiguration": {
    "Role": "arn:aws:iam::ACCOUNT:role/cloudshelf-s3-replication-role",
    "Rules": [
      {
        "ID": "cloudshelf-crr-rule",
        "Status": "Enabled",
        "Prefix": "",
        "Destination": {
          "Bucket": "arn:aws:s3:::cloudshelf-static-assets-dr",
          "StorageClass": "STANDARD_IA",
          "EncryptionConfiguration": {
            "ReplicaKmsKeyID": "arn:aws:kms:us-west-2:ACCOUNT:key/dr-key-id"
          }
        },
        "DeleteMarkerReplication": {
          "Status": "Enabled"
        }
      }
    ]
  },
  "Versioning": {
    "Status": "Enabled"
  }
}
```

---

## 🚀 Automated Failover Procedures

### **🔄 Lambda Function Failover**

**Cross-Region Deployment Automation**:

```javascript
// Automated Lambda deployment to DR region
const AWS = require("aws-sdk");

class LambdaFailoverManager {
  constructor() {
    this.primaryRegion = "us-east-1";
    this.drRegion = "us-west-2";
  }

  async deployToDRRegion(functionName) {
    const primaryLambda = new AWS.Lambda({ region: this.primaryRegion });
    const drLambda = new AWS.Lambda({ region: this.drRegion });

    try {
      // Get current function configuration and code
      const functionConfig = await primaryLambda
        .getFunction({
          FunctionName: functionName,
        })
        .promise();

      const codeLocation = functionConfig.Code.Location;

      // Update DR region function
      await drLambda
        .updateFunctionCode({
          FunctionName: functionName.replace("-primary", "-dr"),
          S3Bucket: codeLocation.bucket,
          S3Key: codeLocation.key,
        })
        .promise();

      // Update environment variables for DR region
      await drLambda
        .updateFunctionConfiguration({
          FunctionName: functionName.replace("-primary", "-dr"),
          Environment: {
            Variables: {
              ...functionConfig.Configuration.Environment.Variables,
              REGION: this.drRegion,
              DB_ENDPOINT:
                "cloudshelf-postgres-dr.cluster-xxx.us-west-2.rds.amazonaws.com",
            },
          },
        })
        .promise();

      console.log(`DR deployment successful for ${functionName}`);
    } catch (error) {
      console.error(`DR deployment failed for ${functionName}:`, error);
      throw error;
    }
  }

  async promoteRDSReplica() {
    const rds = new AWS.RDS({ region: this.drRegion });

    try {
      await rds
        .promoteReadReplica({
          DBInstanceIdentifier: "cloudshelf-postgres-dr-replica",
        })
        .promise();

      console.log("RDS read replica promoted to primary");
    } catch (error) {
      console.error("RDS replica promotion failed:", error);
      throw error;
    }
  }
}
```

### **🔔 Automated Alert System**

**Disaster Detection and Response**:

```json
{
  "CloudWatchAlarms": [
    {
      "AlarmName": "cloudshelf-primary-region-failure",
      "MetricName": "HealthCheck",
      "Namespace": "AWS/Route53",
      "Statistic": "Minimum",
      "Period": 60,
      "EvaluationPeriods": 3,
      "Threshold": 1,
      "ComparisonOperator": "LessThanThreshold",
      "AlarmActions": [
        "arn:aws:sns:us-east-1:ACCOUNT:cloudshelf-dr-emergency",
        "arn:aws:lambda:us-east-1:ACCOUNT:function:cloudshelf-dr-orchestrator"
      ]
    }
  ],
  "SNSTopics": [
    {
      "TopicName": "cloudshelf-dr-emergency",
      "Subscriptions": [
        {
          "Protocol": "email",
          "Endpoint": "oncall-team@cloudshelf.com"
        },
        {
          "Protocol": "sms",
          "Endpoint": "+1-555-0123"
        },
        {
          "Protocol": "lambda",
          "Endpoint": "arn:aws:lambda:us-east-1:ACCOUNT:function:dr-automation"
        }
      ]
    }
  ]
}
```

---

## 📋 Recovery Procedures

### **🚨 Emergency Response Runbook**

**Incident Classification and Response Times**:

| Incident Level         | Description               | Response Time | Recovery Time Target |
| ---------------------- | ------------------------- | ------------- | -------------------- |
| **Level 1 - Critical** | Complete service outage   | < 15 minutes  | < 1 hour             |
| **Level 2 - High**     | Major service degradation | < 30 minutes  | < 2 hours            |
| **Level 3 - Medium**   | Minor service issues      | < 1 hour      | < 4 hours            |
| **Level 4 - Low**      | Non-customer affecting    | < 4 hours     | < 24 hours           |

**Level 1 Critical Incident Response**:

```markdown
## CRITICAL INCIDENT RESPONSE PROCEDURE

### Immediate Actions (0-15 minutes)

1. **Alert Acknowledgment**

   - On-call engineer acknowledges alert in PagerDuty
   - Assess scope and impact using monitoring dashboards
   - Initiate incident bridge call

2. **Initial Assessment**

   - Check Route 53 health checks status
   - Verify primary region availability
   - Review CloudWatch metrics for anomalies
   - Test basic API endpoints from external location

3. **Communication**
   - Post initial status update to #incidents Slack channel
   - Notify customer support team of potential impact
   - Prepare stakeholder communication

### Recovery Actions (15-60 minutes)

4. **Automatic Failover Verification**

   - Confirm Route 53 failover to DR region occurred
   - Verify DR region services are responding
   - Test critical user journeys in DR region

5. **Database Recovery**

   - If RDS failover needed, promote read replica
   - Validate data consistency between regions
   - Update application database connection strings

6. **Service Validation**
   - Execute smoke tests against DR region
   - Verify payment processing functionality
   - Confirm user authentication working

### Post-Recovery Actions (1+ hours)

7. **Monitoring and Validation**

   - Monitor DR region performance and error rates
   - Validate customer experience metrics
   - Check for data synchronization issues

8. **Communication Updates**
   - Provide recovery status to stakeholders
   - Update customer-facing status page
   - Document lessons learned
```

### **🔄 Service-Specific Recovery Procedures**

**Book Catalog Service Recovery**:

```bash
#!/bin/bash
# Book catalog service failover script

# 1. Verify RDS read replica status
aws rds describe-db-instances \
  --db-instance-identifier cloudshelf-postgres-dr-replica \
  --region us-west-2

# 2. Promote read replica if needed
aws rds promote-read-replica \
  --db-instance-identifier cloudshelf-postgres-dr-replica \
  --region us-west-2

# 3. Update Lambda environment variables
aws lambda update-function-configuration \
  --function-name cloudshelf-book-catalog-dr \
  --environment Variables='{
    "DB_HOST":"cloudshelf-postgres-dr-replica.cluster-xxx.us-west-2.rds.amazonaws.com",
    "REGION":"us-west-2"
  }' \
  --region us-west-2

# 4. Warm up connections
aws lambda invoke \
  --function-name cloudshelf-book-catalog-dr \
  --payload '{"warmup": true}' \
  --region us-west-2 \
  /tmp/warmup-response.json

# 5. Run smoke tests
curl -X GET "https://api-dr.cloudshelf.com/books?q=aws" \
  -H "Accept: application/json"
```

**Shopping Cart Service Recovery**:

```bash
#!/bin/bash
# Shopping cart service failover script

# 1. Verify DynamoDB Global Tables sync status
aws dynamodb describe-table \
  --table-name CloudShelf-ShoppingCart \
  --region us-west-2

# 2. Check for replication lag
aws dynamodb scan \
  --table-name CloudShelf-ShoppingCart \
  --select COUNT \
  --region us-west-2

# 3. Update Lambda configuration for DR region
aws lambda update-function-configuration \
  --function-name cloudshelf-shopping-cart-dr \
  --environment Variables='{
    "DYNAMODB_TABLE":"CloudShelf-ShoppingCart",
    "REGION":"us-west-2"
  }' \
  --region us-west-2

# 4. Test cart operations
curl -X POST "https://api-dr.cloudshelf.com/cart" \
  -H "Content-Type: application/json" \
  -d '{"userId": "test-user", "bookId": "12345", "quantity": 1}'
```

---

## 🧪 Disaster Recovery Testing

### **📅 DR Testing Schedule**

**Regular Testing Cadence**:

| Test Type                   | Frequency | Duration   | Scope                | Success Criteria         |
| --------------------------- | --------- | ---------- | -------------------- | ------------------------ |
| **Automated Failover Test** | Weekly    | 5 minutes  | DNS failover only    | < 60 second RTO          |
| **Component Failover Test** | Monthly   | 30 minutes | Single service       | Service restored         |
| **Full DR Exercise**        | Quarterly | 4 hours    | Complete environment | All services operational |
| **Annual DR Simulation**    | Annually  | 8 hours    | Business continuity  | Full business operations |

### **🔬 DR Test Automation**

**Automated Testing Framework**:

```javascript
// DR testing automation framework
class DRTestingFramework {
  constructor() {
    this.testResults = [];
    this.notifications = new AWS.SNS();
  }

  async runDRTest(testType) {
    const testStart = new Date();
    console.log(`Starting DR test: ${testType} at ${testStart}`);

    try {
      switch (testType) {
        case "dns-failover":
          await this.testDNSFailover();
          break;
        case "rds-promotion":
          await this.testRDSPromotion();
          break;
        case "full-dr":
          await this.testFullDRScenario();
          break;
      }

      const testEnd = new Date();
      const duration = testEnd - testStart;

      await this.recordTestResult({
        testType,
        status: "PASSED",
        duration,
        timestamp: testStart,
      });
    } catch (error) {
      await this.recordTestResult({
        testType,
        status: "FAILED",
        error: error.message,
        timestamp: testStart,
      });
      throw error;
    }
  }

  async testDNSFailover() {
    // Simulate primary region failure
    await this.simulateHealthCheckFailure();

    // Wait for DNS propagation
    await this.sleep(120000); // 2 minutes

    // Verify failover occurred
    const resolvedIP = await this.resolveDNS("api.cloudshelf.com");
    const expectedDRIP = await this.getDRRegionIP();

    if (resolvedIP !== expectedDRIP) {
      throw new Error("DNS failover did not occur");
    }

    // Test API responsiveness
    await this.testAPIEndpoints();

    // Restore primary region
    await this.restoreHealthCheck();
  }

  async testAPIEndpoints() {
    const endpoints = ["/health", "/books?q=test", "/cart"];

    for (const endpoint of endpoints) {
      const response = await fetch(`https://api.cloudshelf.com${endpoint}`);
      if (!response.ok) {
        throw new Error(`Endpoint ${endpoint} failed: ${response.status}`);
      }
    }
  }
}

// Schedule automated tests
const scheduler = new DRTestingFramework();
setInterval(() => {
  scheduler.runDRTest("dns-failover");
}, 7 * 24 * 60 * 60 * 1000); // Weekly
```

### **📊 DR Test Reporting**

**Test Results Dashboard**:

```json
{
  "DRTestMetrics": {
    "LastQuarterResults": {
      "TotalTests": 52,
      "PassedTests": 48,
      "FailedTests": 4,
      "SuccessRate": "92.3%",
      "AverageRTO": "45 seconds",
      "AverageRPO": "2 minutes"
    },
    "TrendAnalysis": {
      "RTOImprovement": "+15% vs previous quarter",
      "ReliabilityTrend": "Improving",
      "FailurePatterns": ["Network timeouts", "DNS propagation delays"]
    },
    "NextActions": [
      "Optimize DNS TTL settings",
      "Implement faster health check intervals",
      "Add redundant health check endpoints"
    ]
  }
}
```

---

## 💰 DR Cost Management

### **📊 DR Cost Optimization**

**Cost Structure Analysis**:

```
Monthly DR Costs:
├── Cross-Region Data Transfer: $150
├── RDS Read Replica: $200
├── DynamoDB Global Tables: $100
├── S3 Cross-Region Replication: $50
├── Route 53 Health Checks: $15
├── Lambda Cold Standby: $25
└── CloudWatch & SNS: $10
Total Monthly DR Cost: $550

Annual DR Investment: $6,600
Break-even Downtime Prevention: 2.6 hours/year
```

**Cost Optimization Strategies**:

- **RDS**: Use Aurora Global Database for better cost efficiency
- **S3**: Implement Intelligent Tiering for replicated data
- **Lambda**: Use Provisioned Concurrency only during peak hours
- **Data Transfer**: Optimize data synchronization schedules

---

## 📈 Business Continuity Planning

### **👥 Business Impact and Stakeholder Management**

**Stakeholder Communication Plan**:

| Stakeholder Group    | Communication Method | Update Frequency      | Content Focus        |
| -------------------- | -------------------- | --------------------- | -------------------- |
| **Executive Team**   | Email + Phone call   | Major incidents only  | Business impact, ETA |
| **Customer Support** | Slack + Dashboard    | Real-time updates     | Customer messaging   |
| **Engineering Team** | Incident bridge call | Continuous            | Technical details    |
| **Customers**        | Status page + Email  | Hourly during outages | Service status, ETA  |

**Business Continuity Procedures**:

```markdown
## BUSINESS CONTINUITY RESPONSE

### Customer Communication

1. **Status Page Updates**

   - Update within 15 minutes of incident detection
   - Provide realistic ETAs based on RTO targets
   - Include workaround instructions if available

2. **Customer Support Scripts**
   - Pre-approved messaging for different incident types
   - Escalation procedures for enterprise customers
   - Compensation policies for SLA violations

### Revenue Protection

1. **Payment Processing Continuity**

   - Backup payment processor activation
   - Manual order processing procedures
   - Order queue management during recovery

2. **Order Management**
   - Offline order capture capabilities
   - Manual fulfillment processes
   - Customer communication for delayed orders
```

---

## 🔄 Continuous Improvement

### **📈 DR Maturity Evolution**

**Current State Assessment**:

- **Recovery Time**: 45 seconds (DNS) to 1 hour (full service)
- **Recovery Point**: < 5 minutes for critical data
- **Automation Level**: 70% automated response
- **Testing Coverage**: Core services covered, quarterly validation

**Future DR Enhancements**:

- **Multi-AZ Aurora Global Database**: Reduce RTO to < 30 seconds
- **Lambda@Edge**: Improve global performance and resilience
- **Advanced Monitoring**: ML-based failure prediction
- **Chaos Engineering**: Proactive resilience testing

### **📋 Lessons Learned Integration**

**Quarterly DR Reviews**:

- **Incident Post-Mortems**: Root cause analysis and prevention
- **Test Result Analysis**: Performance trends and improvement areas
- **Technology Updates**: New AWS DR features evaluation
- **Process Refinement**: Procedure updates based on experience

---

_This disaster recovery and business continuity strategy ensures CloudShelf maintains high availability and rapid recovery capabilities while protecting customer data and business operations against various failure scenarios._
