# 🔒 Security Architecture Deep Dive

> Comprehensive security strategy for CloudShelf online bookstore ensuring data protection, access control, and compliance with industry standards

This document defines the security architecture, policies, and implementation guidelines for CloudShelf, covering all aspects from network security to data protection and compliance requirements.

---

## 🏛️ Security Architecture Overview

CloudShelf implements defense-in-depth security architecture with multiple layers of protection:

- **🌐 Network Security** - VPC isolation, security groups, and network ACLs
- **🔐 Identity & Access Management** - IAM roles, policies, and principle of least privilege
- **🛡️ Data Protection** - Encryption at rest and in transit, key management
- **🔍 Monitoring & Compliance** - Security logging, auditing, and compliance validation
- **🚨 Threat Detection** - AWS GuardDuty, CloudTrail, and security automation

---

## 📷 Setup Screenshots

### **🔒 IAM Roles and Policies**

![IAM Roles Configuration](screenshots/01-iam-roles-policies.png)

### **🔐 KMS Key Management**

![KMS Key Management Setup](screenshots/02-kms-key-management.png)

### **🌐 VPC Security Groups**

![VPC Security Groups Configuration](screenshots/03-vpc-security-groups.png)

### **🛡️ WAF Configuration**

![AWS WAF Rules Configuration](screenshots/04-waf-configuration.png)

### **🔍 CloudTrail Setup**

![CloudTrail Logging Configuration](screenshots/05-cloudtrail-setup.png)

### **🚨 GuardDuty Findings**

![GuardDuty Security Findings](screenshots/06-guardduty-findings.png)

---

## 🌐 Network Security Architecture

### **🏗️ VPC Security Design**

**Network Isolation Strategy**:

```
CloudShelf VPC (10.0.0.0/16)
├── Public Subnets (10.0.1.0/24, 10.0.2.0/24)
│   ├── ALB (if needed)
│   ├── NAT Gateway
│   └── Bastion Host (emergency access)
├── Private Subnets (10.0.10.0/24, 10.0.11.0/24)
│   ├── Lambda Functions
│   ├── RDS Database
│   └── ElastiCache (if implemented)
└── Database Subnets (10.0.20.0/24, 10.0.21.0/24)
    └── RDS PostgreSQL (multi-AZ)
```

### **🔥 Security Groups Configuration**

**Lambda Security Group**:

```json
{
  "GroupName": "cloudshelf-lambda-sg",
  "Description": "Security group for CloudShelf Lambda functions",
  "VpcId": "vpc-cloudshelf",
  "SecurityGroupRules": [
    {
      "IpPermissions": [
        {
          "IpProtocol": "tcp",
          "FromPort": 443,
          "ToPort": 443,
          "IpRanges": [
            { "CidrIp": "0.0.0.0/0", "Description": "HTTPS outbound" }
          ]
        },
        {
          "IpProtocol": "tcp",
          "FromPort": 5432,
          "ToPort": 5432,
          "UserIdGroupPairs": [
            { "GroupId": "sg-rds-postgres", "Description": "RDS access" }
          ]
        }
      ]
    }
  ]
}
```

**RDS Security Group**:

```json
{
  "GroupName": "cloudshelf-rds-sg",
  "Description": "Security group for CloudShelf RDS PostgreSQL",
  "VpcId": "vpc-cloudshelf",
  "SecurityGroupRules": [
    {
      "IpPermissions": [
        {
          "IpProtocol": "tcp",
          "FromPort": 5432,
          "ToPort": 5432,
          "UserIdGroupPairs": [
            { "GroupId": "sg-lambda", "Description": "Lambda access only" }
          ]
        }
      ]
    }
  ]
}
```

### **🚫 Network ACLs (Additional Layer)**

**Private Subnet NACL**:
| Rule | Type | Protocol | Port Range | Source | Action |
|------|------|----------|------------|--------|--------|
| 100 | Inbound | TCP | 443 | 10.0.0.0/16 | ALLOW |
| 200 | Inbound | TCP | 5432 | 10.0.10.0/24 | ALLOW |
| _ | Inbound | ALL | ALL | 0.0.0.0/0 | DENY |
| 100 | Outbound | TCP | 443 | 0.0.0.0/0 | ALLOW |
| 200 | Outbound | TCP | 1024-65535 | 0.0.0.0/0 | ALLOW |
| _ | Outbound | ALL | ALL | 0.0.0.0/0 | DENY |

---

## 🔐 Identity & Access Management

### **👤 IAM Roles Architecture**

**Lambda Execution Roles**:

**Book Catalog Lambda Role**:

```json
{
  "RoleName": "CloudShelf-BookCatalog-ExecutionRole",
  "AssumeRolePolicyDocument": {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": { "Service": "lambda.amazonaws.com" },
        "Action": "sts:AssumeRole"
      }
    ]
  },
  "ManagedPolicyArns": [
    "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
  ],
  "InlinePolicies": {
    "RDSAccess": {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Effect": "Allow",
          "Action": ["rds-db:connect"],
          "Resource": [
            "arn:aws:rds-db:*:*:dbuser:cloudshelf-postgres/lambda_user"
          ]
        }
      ]
    },
    "CloudWatchLogs": {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Effect": "Allow",
          "Action": [
            "logs:CreateLogGroup",
            "logs:CreateLogStream",
            "logs:PutLogEvents"
          ],
          "Resource": "arn:aws:logs:*:*:log-group:/aws/lambda/book-catalog*"
        }
      ]
    }
  }
}
```

**Shopping Cart Lambda Role**:

```json
{
  "RoleName": "CloudShelf-ShoppingCart-ExecutionRole",
  "InlinePolicies": {
    "DynamoDBAccess": {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Effect": "Allow",
          "Action": [
            "dynamodb:GetItem",
            "dynamodb:PutItem",
            "dynamodb:UpdateItem",
            "dynamodb:DeleteItem",
            "dynamodb:Query"
          ],
          "Resource": [
            "arn:aws:dynamodb:*:*:table/CloudShelf-ShoppingCart",
            "arn:aws:dynamodb:*:*:table/CloudShelf-ShoppingCart/index/*"
          ],
          "Condition": {
            "ForAllValues:StringEquals": {
              "dynamodb:Attributes": [
                "userId",
                "cartId",
                "items",
                "lastModified",
                "ttl"
              ]
            }
          }
        }
      ]
    }
  }
}
```

### **🎯 Principle of Least Privilege**

**Access Control Matrix**:

| Role                     | RDS          | DynamoDB           | S3           | CloudWatch   | API Gateway      |
| ------------------------ | ------------ | ------------------ | ------------ | ------------ | ---------------- |
| **Book Catalog Lambda**  | ✅ Read Only | ❌ No Access       | ❌ No Access | ✅ Logs Only | ❌ No Direct     |
| **Shopping Cart Lambda** | ❌ No Access | ✅ Cart Table Only | ❌ No Access | ✅ Logs Only | ❌ No Direct     |
| **API Gateway**          | ❌ No Access | ❌ No Access       | ❌ No Access | ✅ Logs Only | ✅ Invoke Lambda |
| **CloudFront**           | ❌ No Access | ❌ No Access       | ✅ Read Only | ✅ Logs Only | ❌ No Direct     |

---

## 🛡️ Data Protection & Encryption

### **🔐 Encryption at Rest**

**RDS PostgreSQL Encryption**:

```json
{
  "DBInstanceIdentifier": "cloudshelf-postgres",
  "StorageEncrypted": true,
  "KmsKeyId": "arn:aws:kms:us-east-1:ACCOUNT:key/cloudshelf-rds-key",
  "BackupRetentionPeriod": 7,
  "DeletionProtection": true,
  "PerformanceInsightsEnabled": true,
  "PerformanceInsightsKMSKeyId": "arn:aws:kms:us-east-1:ACCOUNT:key/cloudshelf-rds-key"
}
```

**DynamoDB Encryption**:

```json
{
  "TableName": "CloudShelf-ShoppingCart",
  "SSESpecification": {
    "Enabled": true,
    "SSEType": "KMS",
    "KMSMasterKeyId": "arn:aws:kms:us-east-1:ACCOUNT:key/cloudshelf-dynamodb-key"
  },
  "PointInTimeRecoverySpecification": {
    "PointInTimeRecoveryEnabled": true
  }
}
```

**S3 Bucket Encryption**:

```json
{
  "BucketName": "cloudshelf-static-assets",
  "PublicAccessBlockConfiguration": {
    "BlockPublicAcls": true,
    "IgnorePublicAcls": true,
    "BlockPublicPolicy": true,
    "RestrictPublicBuckets": true
  },
  "BucketEncryption": {
    "ServerSideEncryptionConfiguration": [
      {
        "ServerSideEncryptionByDefault": {
          "SSEAlgorithm": "aws:kms",
          "KMSMasterKeyID": "arn:aws:kms:us-east-1:ACCOUNT:key/cloudshelf-s3-key"
        },
        "BucketKeyEnabled": true
      }
    ]
  }
}
```

### **🔑 KMS Key Management Strategy**

**Key Hierarchy**:

```
CloudShelf KMS Keys
├── cloudshelf-master-key (CMK)
│   ├── Purpose: Root encryption key
│   ├── Rotation: Annual
│   └── Access: Security team only
├── cloudshelf-rds-key (CMK)
│   ├── Purpose: RDS encryption
│   ├── Rotation: Annual
│   └── Access: Database admins + Lambda
├── cloudshelf-dynamodb-key (CMK)
│   ├── Purpose: DynamoDB encryption
│   ├── Rotation: Annual
│   └── Access: Application services only
└── cloudshelf-s3-key (CMK)
    ├── Purpose: S3 bucket encryption
    ├── Rotation: Annual
    └── Access: CloudFront + authorized users
```

**Key Policy Example**:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "Enable Root Access",
      "Effect": "Allow",
      "Principal": { "AWS": "arn:aws:iam::ACCOUNT:root" },
      "Action": "kms:*",
      "Resource": "*"
    },
    {
      "Sid": "Allow Lambda Service Access",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::ACCOUNT:role/CloudShelf-*-ExecutionRole"
      },
      "Action": ["kms:Decrypt", "kms:GenerateDataKey"],
      "Resource": "*",
      "Condition": {
        "StringEquals": {
          "kms:ViaService": "rds.us-east-1.amazonaws.com"
        }
      }
    }
  ]
}
```

### **🔒 Encryption in Transit**

**TLS/SSL Configuration**:

- **API Gateway**: TLS 1.2+ enforced, custom domain with ACM certificate
- **CloudFront**: TLS 1.2+ enforced, HSTS headers enabled
- **RDS**: SSL/TLS enforced for all database connections
- **Lambda**: HTTPS required for all external API calls

**SSL Certificate Management**:

```json
{
  "DomainName": "api.cloudshelf.com",
  "CertificateArn": "arn:aws:acm:us-east-1:ACCOUNT:certificate/cloudshelf-cert",
  "SecurityPolicy": "TLS_1_2_2021_01",
  "EndpointType": "EDGE"
}
```

---

## 🔍 Security Monitoring & Compliance

### **📋 CloudTrail Configuration**

**Comprehensive Audit Logging**:

```json
{
  "TrailName": "CloudShelf-Security-Trail",
  "S3BucketName": "cloudshelf-security-logs",
  "S3KeyPrefix": "cloudtrail-logs/",
  "IncludeGlobalServiceEvents": true,
  "IsMultiRegionTrail": true,
  "EnableLogFileValidation": true,
  "KMSKeyId": "arn:aws:kms:us-east-1:ACCOUNT:key/cloudshelf-logging-key",
  "EventSelectors": [
    {
      "ReadWriteType": "All",
      "IncludeManagementEvents": true,
      "DataResources": [
        {
          "Type": "AWS::S3::Object",
          "Values": ["arn:aws:s3:::cloudshelf-*/*"]
        },
        {
          "Type": "AWS::DynamoDB::Table",
          "Values": ["arn:aws:dynamodb:*:*:table/CloudShelf-*"]
        }
      ]
    }
  ]
}
```

### **🚨 GuardDuty Threat Detection**

**Security Findings Categories**:

| Finding Type             | Severity | Response Action         | Automation     |
| ------------------------ | -------- | ----------------------- | -------------- |
| **UnauthorizedAPICall**  | Medium   | Investigate user        | Alert SOC      |
| **MaliciousIPCaller**    | High     | Block IP, investigate   | Auto-block     |
| **DataExfiltration**     | Critical | Immediate investigation | Page on-call   |
| **CryptocurrencyMining** | High     | Terminate instances     | Auto-remediate |
| **BruteForce**           | Medium   | Rate limiting           | Auto-throttle  |

**GuardDuty Integration**:

```json
{
  "DetectorId": "cloudshelf-guardduty",
  "Enable": true,
  "DataSources": {
    "S3Logs": { "Enable": true },
    "DNSLogs": { "Enable": true },
    "FlowLogs": { "Enable": true },
    "KubernetesAuditLogs": { "Enable": false },
    "MalwareProtection": { "Enable": true }
  },
  "FindingPublishingFrequency": "FIFTEEN_MINUTES"
}
```

### **📊 Security Dashboards**

**Security Metrics to Track**:

| Metric                             | Source        | Threshold        | Alert    |
| ---------------------------------- | ------------- | ---------------- | -------- |
| **Failed Authentication Attempts** | CloudTrail    | > 10/minute      | Warning  |
| **Privilege Escalation Attempts**  | CloudTrail    | > 0              | Critical |
| **Data Access Anomalies**          | GuardDuty     | High confidence  | Critical |
| **Network Traffic Anomalies**      | VPC Flow Logs | Baseline +200%   | Warning  |
| **Encryption Key Usage**           | CloudTrail    | Unusual patterns | Info     |

---

## 🛡️ Web Application Firewall (WAF)

### **🔧 WAF Rules Configuration**

**Core Protection Rules**:

```json
{
  "WebACLName": "CloudShelf-WAF",
  "Scope": "CLOUDFRONT",
  "DefaultAction": { "Allow": {} },
  "Rules": [
    {
      "Name": "AWSManagedRulesCommonRuleSet",
      "Priority": 1,
      "Statement": {
        "ManagedRuleGroupStatement": {
          "VendorName": "AWS",
          "Name": "AWSManagedRulesCommonRuleSet"
        }
      },
      "Action": { "Block": {} },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "CommonRuleSetMetric"
      }
    },
    {
      "Name": "AWSManagedRulesKnownBadInputsRuleSet",
      "Priority": 2,
      "Statement": {
        "ManagedRuleGroupStatement": {
          "VendorName": "AWS",
          "Name": "AWSManagedRulesKnownBadInputsRuleSet"
        }
      },
      "Action": { "Block": {} }
    },
    {
      "Name": "RateLimitRule",
      "Priority": 3,
      "Statement": {
        "RateBasedStatement": {
          "Limit": 2000,
          "AggregateKeyType": "IP"
        }
      },
      "Action": { "Block": {} }
    }
  ]
}
```

**Custom Security Rules**:

- **SQL Injection Protection**: Block common SQL injection patterns
- **XSS Protection**: Filter cross-site scripting attempts
- **Rate Limiting**: 2000 requests per 5-minute window per IP
- **Geo-blocking**: Block traffic from high-risk countries (if required)
- **Bot Protection**: AWS Bot Control for automated traffic

---

## 🔒 API Security

### **🛡️ API Gateway Security Configuration**

**Authorization Strategy**:

```json
{
  "RestApiId": "cloudshelf-api",
  "AuthorizationType": "AWS_IAM",
  "AuthorizationScopes": ["cloudshelf:read", "cloudshelf:write"],
  "ResourcePolicy": {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": "*",
        "Action": "execute-api:Invoke",
        "Resource": "arn:aws:execute-api:*:*:*/*/GET/*",
        "Condition": {
          "StringEquals": {
            "aws:sourceVpce": "vpce-cloudshelf"
          }
        }
      },
      {
        "Effect": "Deny",
        "Principal": "*",
        "Action": "execute-api:Invoke",
        "Resource": "*",
        "Condition": {
          "StringNotEquals": {
            "aws:sourceVpce": "vpce-cloudshelf"
          }
        }
      }
    ]
  }
}
```

**Request Validation**:

```json
{
  "RequestValidatorName": "CloudShelf-Request-Validator",
  "ValidateRequestBody": true,
  "ValidateRequestParameters": true,
  "RequestTemplates": {
    "application/json": {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "type": "object",
      "required": ["searchTerm"],
      "properties": {
        "searchTerm": {
          "type": "string",
          "minLength": 1,
          "maxLength": 100,
          "pattern": "^[a-zA-Z0-9\\s\\-\\.]+$"
        }
      }
    }
  }
}
```

**CORS Security Configuration**:

```json
{
  "CorsConfiguration": {
    "AllowCredentials": false,
    "AllowHeaders": ["Content-Type", "X-Amz-Date", "Authorization"],
    "AllowMethods": ["GET", "POST", "OPTIONS"],
    "AllowOrigins": ["https://cloudshelf.com", "https://www.cloudshelf.com"],
    "ExposeHeaders": ["X-Request-ID"],
    "MaxAge": 86400
  }
}
```

---

## 🔐 Database Security

### **🗃️ RDS Security Configuration**

**Database-Level Security**:

```sql
-- Create application user with limited privileges
CREATE USER lambda_user WITH PASSWORD 'secure_random_password';

-- Grant minimal required permissions
GRANT CONNECT ON DATABASE cloudshelf TO lambda_user;
GRANT USAGE ON SCHEMA public TO lambda_user;
GRANT SELECT ON books, categories, authors TO lambda_user;

-- Create read-only views for sensitive data
CREATE VIEW public_book_info AS
SELECT id, title, author, description, price, category_id
FROM books
WHERE status = 'active';

GRANT SELECT ON public_book_info TO lambda_user;

-- Enable RLS (Row Level Security) if needed
ALTER TABLE books ENABLE ROW LEVEL SECURITY;
```

**Connection Security**:

```javascript
// Lambda database connection with SSL
const { Pool } = require("pg");

const pool = new Pool({
  host: process.env.DB_HOST,
  port: 5432,
  database: process.env.DB_NAME,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  ssl: {
    require: true,
    rejectUnauthorized: true,
    ca: process.env.RDS_CA_CERT,
  },
  max: 10,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000,
});
```

### **⚡ DynamoDB Security**

**Fine-grained Access Control**:

```json
{
  "TableName": "CloudShelf-ShoppingCart",
  "AttributeDefinitions": [
    { "AttributeName": "userId", "AttributeType": "S" },
    { "AttributeName": "cartId", "AttributeType": "S" }
  ],
  "KeySchema": [
    { "AttributeName": "userId", "KeyType": "HASH" },
    { "AttributeName": "cartId", "KeyType": "RANGE" }
  ],
  "BillingMode": "PAY_PER_REQUEST",
  "StreamSpecification": {
    "StreamEnabled": true,
    "StreamViewType": "NEW_AND_OLD_IMAGES"
  },
  "SSESpecification": {
    "Enabled": true,
    "SSEType": "KMS"
  },
  "PointInTimeRecoverySpecification": {
    "PointInTimeRecoveryEnabled": true
  },
  "DeletionProtectionEnabled": true
}
```

---

## 📋 Compliance & Governance

### **📊 Compliance Requirements**

**Security Standards Alignment**:

| Standard          | Requirement        | CloudShelf Implementation         | Status |
| ----------------- | ------------------ | --------------------------------- | ------ |
| **SOC 2 Type II** | Access Controls    | IAM roles, MFA, least privilege   | ✅     |
| **SOC 2 Type II** | Encryption         | KMS encryption at rest/transit    | ✅     |
| **SOC 2 Type II** | Monitoring         | CloudTrail, GuardDuty, CloudWatch | ✅     |
| **PCI DSS**       | Data Protection    | No card data stored               | ✅     |
| **GDPR**          | Data Privacy       | Data encryption, access controls  | ✅     |
| **CCPA**          | Data Access Rights | Data portability capabilities     | 🔄     |

### **🔍 Security Auditing**

**Regular Security Reviews**:

- **Weekly**: Access review, permission audit
- **Monthly**: Vulnerability scanning, dependency updates
- **Quarterly**: Penetration testing, security architecture review
- **Annually**: Full compliance audit, security training

**Automated Compliance Checks**:

```json
{
  "ConfigRuleName": "cloudshelf-security-compliance",
  "ConfigRules": [
    "encrypted-volumes",
    "rds-encrypted",
    "s3-bucket-ssl-requests-only",
    "lambda-function-public-read-prohibited",
    "iam-password-policy",
    "cloudtrail-enabled"
  ]
}
```

---

## 🚨 Incident Response

### **📋 Security Incident Response Plan**

**Incident Classification**:

| Severity     | Examples                                         | Response Time | Team               |
| ------------ | ------------------------------------------------ | ------------- | ------------------ |
| **Critical** | Data breach, system compromise                   | < 1 hour      | Full security team |
| **High**     | Privilege escalation, service disruption         | < 4 hours     | Security + DevOps  |
| **Medium**   | Failed authentication spike, suspicious activity | < 24 hours    | Security team      |
| **Low**      | Policy violations, informational alerts          | < 72 hours    | Security team      |

**Response Procedures**:

1. **Detection** - Automated alerts trigger incident response
2. **Assessment** - Severity determination and impact analysis
3. **Containment** - Isolate affected systems and stop further damage
4. **Investigation** - Root cause analysis and evidence collection
5. **Eradication** - Remove threats and vulnerabilities
6. **Recovery** - Restore systems and validate security
7. **Lessons Learned** - Document improvements and update procedures

---

## 🔄 Security Maintenance

### **📅 Regular Security Tasks**

**Daily**:

- Review GuardDuty findings
- Monitor failed authentication attempts
- Check critical security alerts

**Weekly**:

- Review IAM access patterns
- Audit new security group rules
- Update threat intelligence feeds

**Monthly**:

- Rotate access keys and passwords
- Review and update WAF rules
- Security patch assessment and deployment

**Quarterly**:

- Comprehensive security architecture review
- Penetration testing execution
- Security training and awareness updates

### **🚀 Continuous Security Improvement**

**Security Automation Roadmap**:

- **Phase 1**: Automated threat response for common incidents
- **Phase 2**: Machine learning-based anomaly detection
- **Phase 3**: Zero-trust architecture implementation
- **Phase 4**: Advanced threat hunting capabilities

---

_This security architecture ensures CloudShelf maintains robust protection against threats while enabling business operations and meeting compliance requirements._
