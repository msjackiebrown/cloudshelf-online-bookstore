# 🔐 CloudShelf IAM Security Setup

> Implementation guide for AWS Identity and Access Management (IAM) policies, roles, and security configuration

This guide provides step-by-step instructions for setting up IAM security for the CloudShelf online bookstore serverless architecture, implementing security-first design principles with least privilege access.

---

## 🔒 Security Essentials

### **✅ Security Checklist**

Before implementing CloudShelf, ensure these security fundamentals:

- [ ] **No Hardcoded Credentials** - Use IAM roles, never embed API keys
- [ ] **Least Privilege Access** - Grant only necessary permissions
- [ ] **Network Isolation** - Database in private subnets only
- [ ] **Encryption at Rest** - Enable RDS encryption
- [ ] **Secure Communication** - HTTPS only for all endpoints
- [ ] **Security Groups** - Restrict access between services

### **🛡️ Key Security Principles**

**Defense in Depth**:

- **Network Layer**: VPC security groups and private subnets
- **Application Layer**: IAM roles with minimal permissions
- **Data Layer**: Database encryption and access control

**Zero Trust Model**:

- Every service interaction requires explicit permission
- No default trust relationships between AWS services
- Regular review and rotation of access permissions

---

## 🏛️ Architecture Overview

IAM security provides the foundation for all CloudShelf services with:

- **🔒 Least Privilege Access** - Minimal permissions for each service
- **🏗️ Service-Specific Roles** - Dedicated roles for Lambda, RDS, API Gateway
- **⚡ Cross-Service Access** - Secure communication between AWS services
- **📈 Scalable Security** - Role-based access that scales with architecture

**IAM Security Strategy**: CloudShelf implements a role-based security model with customer-managed policies for governance, least-privilege access principles, and network-level security groups for defense in depth. The security model follows AWS Well-Architected Framework principles with explicit trust relationships between services.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         CloudShelf IAM Security Architecture                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                            IAM Roles & Policies                         │   │
│  │                                                                         │   │
│  │  ┌─────────────────┐                 ┌─────────────────┐               │   │
│  │  │   Lambda Role   │                 │   Lambda Role   │               │   │
│  │  │  (Book Catalog) │                 │ (Shopping Cart) │               │   │
│  │  │                 │                 │                 │               │   │
│  │  │ ┌─────────────┐ │                 │ ┌─────────────┐ │               │   │
│  │  │ │AWS Managed: │ │                 │ │AWS Managed: │ │               │   │
│  │  │ │VPC Execution│ │                 │ │VPC Execution│ │               │   │
│  │  │ │CloudWatch   │ │                 │ │CloudWatch   │ │               │   │
│  │  │ └─────────────┘ │                 │ └─────────────┘ │               │   │
│  │  │ ┌─────────────┐ │                 │ ┌─────────────┐ │               │   │
│  │  │ │Custom:      │ │                 │ │Custom:      │ │               │   │
│  │  │ │RDS-BookCata-│ │                 │ │DynamoDB-    │ │               │   │
│  │  │ │log-Access   │ │                 │ │ShoppingCart │ │               │   │
│  │  │ └─────────────┘ │                 │ └─────────────┘ │               │   │
│  │  └─────────────────┘                 └─────────────────┘               │   │
│  │           │                                   │                         │   │
│  │           ▼                                   ▼                         │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│              │                                   │                             │
│              ▼                                   ▼                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        AWS Services Access                              │   │
│  │                                                                         │   │
│  │  ┌─────────────────┐                 ┌─────────────────┐               │   │
│  │  │   RDS PostgreSQL│                 │    DynamoDB     │               │   │
│  │  │                 │                 │                 │               │   │
│  │  │ • IAM DB Auth   │                 │ • Item-level    │               │   │
│  │  │ • VPC Private   │                 │   permissions   │               │   │
│  │  │ • Encryption    │                 │ • Encryption    │               │   │
│  │  │ • CloudWatch    │                 │ • CloudWatch    │               │   │
│  │  │   Logs          │                 │   Logs          │               │   │
│  │  └─────────────────┘                 └─────────────────┘               │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  Security Principles:                    Access Controls:                      │
│  • Least Privilege Access               • Network Security Groups              │
│  • Customer-Managed Policies            • VPC Private Subnets                  │
│  • Service-Specific Roles               • Resource-Level Permissions           │
│  • No Hardcoded Credentials             • HTTPS-Only Communication             │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                          Policy Structure                               │   │
│  │                                                                         │   │
│  │  Customer-Managed Policies:                                            │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐       │   │
│  │  │CloudShelf-RDS-  │  │CloudShelf-      │  │CloudShelf-S3-   │       │   │
│  │  │BookCatalog-     │  │DynamoDB-        │  │Assets-Access    │       │   │
│  │  │Access           │  │ShoppingCart-    │  │                 │       │   │
│  │  │                 │  │Access           │  │                 │       │   │
│  │  │• RDS Describe   │  │• Get/Put/Update │  │• GetObject      │       │   │
│  │  │• DB Connect     │  │• Delete/Query   │  │• PutObject      │       │   │
│  │  │                 │  │• Scan           │  │                 │       │   │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────┘       │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

_**Placeholder for cloudshelf-iam-security-architecture-diagram** - Complete IAM role relationships, policy attachments, and service access patterns_

---

## 🚀 Implementation Steps

### Step 1: Create Custom IAM Policies

> **📝 Best Practice**: Following **[ADR-008: IAM Policy Creation Strategy](../cloudshelf-architecture-decisions.md#adr-008)**, create customer-managed policies first, then attach them to roles for better governance and reusability.

#### CloudShelf RDS Access Policy

**Policy Name**: `CloudShelf-RDS-BookCatalog-Access`  
**Description**: "RDS access policy for CloudShelf book catalog service"

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["rds:DescribeDBInstances", "rds:DescribeDBClusters"],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": ["rds-db:connect"],
      "Resource": "arn:aws:rds-db:*:*:dbuser:cloudshelf-db/book_catalog_user"
    }
  ]
}
```

#### CloudShelf DynamoDB Access Policy

**Policy Name**: `CloudShelf-DynamoDB-ShoppingCart-Access`  
**Description**: "DynamoDB access policy for CloudShelf shopping cart service"

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem",
        "dynamodb:Query",
        "dynamodb:Scan"
      ],
      "Resource": [
        "arn:aws:dynamodb:*:*:table/cloudshelf-shopping-cart",
        "arn:aws:dynamodb:*:*:table/cloudshelf-shopping-cart/index/*"
      ]
    }
  ]
}
```

#### CloudShelf Lambda Invoke Policy

> **📝 Note**: This policy is for future use cases where other AWS services might need to invoke Lambda functions programmatically. API Gateway uses resource-based policies instead.

**Policy Name**: `CloudShelf-Lambda-Invoke-Access`  
**Description**: "Lambda invocation policy for programmatic access (future use)"

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["lambda:InvokeFunction"],
      "Resource": [
        "arn:aws:lambda:*:*:function:cloudshelf-book-catalog",
        "arn:aws:lambda:*:*:function:cloudshelf-shopping-cart"
      ]
    }
  ]
}
```

#### CloudShelf S3 Access Policy

**Policy Name**: `CloudShelf-S3-Assets-Access`  
**Description**: "S3 access policy for CloudShelf asset management"

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject", "s3:PutObject"],
      "Resource": [
        "arn:aws:s3:::cloudshelf-assets/*",
        "arn:aws:s3:::cloudshelf-logs/*"
      ]
    }
  ]
}
```

---

### Step 2: Create IAM Roles

Now create roles and attach the custom policies created in Step 1.

#### Book Catalog Lambda Role

**Configuration Requirements:**

- **Trusted entity**: AWS service (Lambda)
- **Role name**: `cloudshelf-book-catalog-lambda-role`
- **Description**: "Execution role for CloudShelf book catalog Lambda function"

**Attach Policies:**

- **AWS Managed**: `AWSLambdaVPCAccessExecutionRole`
- **AWS Managed**: `CloudWatchLogsFullAccess`
- **Custom**: `CloudShelf-RDS-BookCatalog-Access`

![Book Catalog Lambda Role Configuration](screenshots/lambda-book-catalog-role.png)

#### Shopping Cart Lambda Role

**Configuration Requirements:**

- **Trusted entity**: AWS service (Lambda)
- **Role name**: `cloudshelf-shopping-cart-lambda-role`
- **Description**: "Execution role for CloudShelf shopping cart Lambda function"

**Attach Policies:**

- **AWS Managed**: `AWSLambdaVPCAccessExecutionRole`
- **AWS Managed**: `CloudWatchLogsFullAccess`
- **Custom**: `CloudShelf-DynamoDB-ShoppingCart-Access`

![Shopping Cart Lambda Role Configuration](screenshots/lambda-shopping-cart-role.png)

---

## 🔧 Best Practices & Security

<details>
<summary><strong>🔒 IAM Security Best Practices</strong></summary>

### Principle of Least Privilege

- **Service-Specific Roles**: Each service has only required permissions
- **Resource-Level Permissions**: Policies specify exact resources when possible
- **Time-Bound Access**: Use temporary credentials where applicable

### Policy Management

- **Customer-Managed Policies**: Use for reusability and governance
- **Policy Versioning**: Implement versioning for policy changes
- **Regular Audits**: Quarterly permission reviews and cleanup

### Access Patterns

- **No Hardcoded Credentials**: All access through IAM roles
- **VPC Isolation**: Database access only from within VPC
- **API Authentication**: Future implementation of Cognito integration

</details>

---

**Next Step**: Complete the [🗃️ RDS Database Setup Guide](../rds/cloudshelf-rds-setup.md) to deploy the PostgreSQL database that will use the IAM roles and policies configured in this guide.

---

## 📚 Additional Resources

- [📖 CloudShelf Architecture Decisions](../cloudshelf-architecture-decisions.md) - Complete ADR documentation
- [🌐 VPC Setup Guide](../vpc/cloudshelf-vpc-setup.md) - Network foundation
- [⚡ Lambda Setup Guide](../lambda/cloudshelf-lambda-setup.md) - Function deployment
- [🔌 API Gateway Setup](../apigateway/cloudshelf-apigateway-setup.md) - API configuration
- 🏛️ [**All Architecture Decisions**](../cloudshelf-architecture-decisions.md) - Context for security architecture choices

---

## 📋 Security Checklist

- [ ] Custom IAM policies created with least privilege permissions
- [ ] IAM roles created and policies attached correctly
- [ ] RDS IAM authentication configured and tested
- [ ] S3 bucket policies restrict access appropriately
- [ ] DynamoDB access policies configured for cart operations
- [ ] CloudWatch logging permissions configured
- [ ] Cross-service permissions explicitly defined
- [ ] No hardcoded credentials in application code
- [ ] Security monitoring and alerting configured
- [ ] Regular security audits scheduled

---

_📋 **Documentation Status**: Complete | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Implementation Phase_  
_🏗️ **Architecture Phase**: Security Foundation | 👥 **Team**: Solutions Architecture | 📋 **Next**: Application Deployment_
