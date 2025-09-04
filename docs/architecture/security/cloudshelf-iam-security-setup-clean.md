# 🔐 CloudShelf IAM Security Setup

> Implementation guide for AWS Identity and Access Management (IAM) policies, roles, and security configuration

This guide provides step-by-step instructions for setting up IAM security for the CloudShelf online bookstore serverless architecture, implementing security-first design principles with least privilege access.

---

## 🏛️ Architecture Overview

IAM security provides the foundation for all CloudShelf services with:

- **🔒 Least Privilege Access** - Minimal permissions for each service
- **🏗️ Service-Specific Roles** - Dedicated roles for Lambda, RDS, API Gateway
- **⚡ Cross-Service Access** - Secure communication between AWS services
- **📈 Scalable Security** - Role-based access that scales with architecture

**Security Principle**: Zero-trust model with explicit permissions for each service interaction.

![CloudShelf IAM Security Architecture](screenshots/cloudshelf-iam-security-architecture.png)

---

## 🚀 Implementation Steps

### Step 1: Create Custom IAM Policies

> **📝 Best Practice**: Create customer-managed policies first, then attach them to roles for better governance and reusability.

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

**Policy Name**: `CloudShelf-Lambda-Invoke-Access`  
**Description**: "Lambda invocation policy for CloudShelf API Gateway"

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

#### API Gateway Execution Role

**Configuration Requirements:**

- **Trusted entity**: API Gateway
- **Role name**: `cloudshelf-apigateway-execution-role`
- **Description**: "Execution role for CloudShelf API Gateway"

**Attach Policies:**

- **Custom**: `CloudShelf-Lambda-Invoke-Access`

![API Gateway Execution Role Configuration](screenshots/apigateway-execution-role.png)

---

### Step 3: Database Security Configuration

#### RDS IAM Authentication Setup

**Configuration Requirements:**

- **Role name**: `cloudshelf-rds-access-role`
- **Description**: "Database access role for CloudShelf application"

**Database User Setup:**

```sql
-- Connect as master user
CREATE USER book_catalog_user;
GRANT rds_iam TO book_catalog_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON books.* TO book_catalog_user;
```

Enable **IAM database authentication** on RDS instance.

---

### Step 4: S3 Security Configuration

#### Static Website Bucket Policy

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "CloudFrontOriginAccess",
      "Effect": "Allow",
      "Principal": {
        "Service": "cloudfront.amazonaws.com"
      },
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::cloudshelf-static-website/*",
      "Condition": {
        "StringEquals": {
          "AWS:SourceArn": "arn:aws:cloudfront::ACCOUNT:distribution/DISTRIBUTION-ID"
        }
      }
    }
  ]
}
```

---

### Step 5: Security Groups Configuration

#### Lambda Security Group

**Configuration Requirements:**

- **Name**: `cloudshelf-lambda-sg`
- **Description**: "Security group for Lambda functions"
- **VPC**: CloudShelf VPC

**Rules:**

- **Outbound**: HTTPS (443) to 0.0.0.0/0 (AWS API calls)
- **Outbound**: PostgreSQL (5432) to RDS security group
- **Outbound**: HTTPS (443) to DynamoDB VPC endpoint

![Lambda Security Group Configuration](screenshots/lambda-security-group.png)

#### RDS Security Group

**Configuration Requirements:**

- **Name**: `cloudshelf-rds-sg`
- **Description**: "Security group for RDS PostgreSQL"

**Rules:**

- **Inbound**: PostgreSQL (5432) from Lambda security group only
- **No public access**

![RDS Security Group Configuration](screenshots/rds-security-group.png)

---

### Step 6: Resource-Based Policies

#### Lambda Resource Policies

**Book Catalog Lambda Permission:**

```bash
aws lambda add-permission \
  --function-name cloudshelf-book-catalog \
  --statement-id apigateway-invoke \
  --action lambda:InvokeFunction \
  --principal apigateway.amazonaws.com \
  --source-arn "arn:aws:execute-api:*:*:*/*/GET/books/*"
```

**Shopping Cart Lambda Permission:**

```bash
aws lambda add-permission \
  --function-name cloudshelf-shopping-cart \
  --statement-id apigateway-invoke \
  --action lambda:InvokeFunction \
  --principal apigateway.amazonaws.com \
  --source-arn "arn:aws:execute-api:*:*:*/*/POST/cart/*"
```

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

<details>
<summary><strong>🛡️ Security Validation & Testing</strong></summary>

### Role Testing Commands

**Lambda Role Validation:**

```bash
# Test RDS connection from Lambda
aws lambda invoke \
  --function-name cloudshelf-book-catalog \
  --payload '{"action":"test-db-connection"}' \
  response.json
```

**API Gateway Integration Test:**

```bash
# Test API Gateway to Lambda integration
curl -X GET https://your-api-id.execute-api.region.amazonaws.com/prod/books
```

### Debug Commands

```bash
# Check role permissions
aws iam simulate-principal-policy \
  --policy-source-arn arn:aws:iam::account:role/cloudshelf-lambda-role \
  --action-names rds-db:connect \
  --resource-arns arn:aws:rds-db:region:account:dbuser:instance/username

# Test Lambda execution
aws lambda invoke \
  --function-name cloudshelf-book-catalog \
  --log-type Tail \
  --payload '{}' \
  output.json
```

</details>

<details>
<summary><strong>🚨 Security Monitoring</strong></summary>

### CloudWatch Alarms

- **Failed Authentication Attempts**: >10 failed attempts in 5 minutes
- **Unusual API Activity**: 4xx/5xx error rate spikes
- **Database Connection Monitoring**: Connection count approaching limits

### Security Metrics

- **Authentication Success Rate**: >99.5%
- **Authorization Failures**: <0.1% of requests
- **Security Group Violations**: 0 attempts
- **Credential Exposure**: 0 incidents

### Troubleshooting Common Issues

**Lambda Permission Denied:**

- Verify execution role has required policies
- Check resource ARNs in policy statements
- Confirm VPC configuration if applicable

**RDS Connection Failures:**

- Verify IAM database authentication enabled
- Check security group rules
- Confirm database user permissions

**API Gateway 403 Errors:**

- Verify Lambda resource-based policies
- Check API Gateway execution role
- Confirm CORS configuration

</details>

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
- [ ] API Gateway roles and resource policies configured
- [ ] S3 bucket policies restrict access appropriately
- [ ] DynamoDB access policies configured for cart operations
- [ ] Security groups follow principle of least privilege
- [ ] CloudWatch logging permissions configured
- [ ] Cross-service permissions explicitly defined
- [ ] No hardcoded credentials in application code
- [ ] Security monitoring and alerting configured
- [ ] Regular security audits scheduled

---

_📋 **Documentation Status**: Complete | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Implementation Phase_  
_🏗️ **Architecture Phase**: Security Foundation | 👥 **Team**: Solutions Architecture | 📋 **Next**: Application Deployment_
