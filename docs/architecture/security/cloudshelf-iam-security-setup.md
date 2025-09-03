# 🔐 IAM Security Setup - CloudShelf Architecture

> Implementation guide for AWS Identity and Access Management (IAM) roles, policies, and security configuration

This guide provides step-by-step instructions for setting up IAM security for the CloudShelf online bookstore serverless architecture, implementing security-first design principles with least privilege access.

---

## 🏛️ Architecture Overview

IAM security provides the foundation for all CloudShelf services with:

- **🔒 Least Privilege Access** - Minimal permissions for each service
- **🏗️ Service-Specific Roles** - Dedicated roles for Lambda, RDS, API Gateway
- **⚡ Cross-Service Access** - Secure communication between AWS services
- **📈 Scalable Security** - Role-based access that scales with architecture

**Security Principle**: Zero-trust model with explicit permissions for each service interaction.

---

## 📷 Security Screenshots

### **🔧 IAM Console Overview**

![IAM Console Dashboard](screenshots/01-iam-console-overview.png)

### **🔐 Role Creation Process**

![IAM Role Creation](screenshots/02-iam-role-creation.png)

---

## 🚀 Manual Setup Steps

### Step 1: Lambda Execution Roles

#### Book Catalog Lambda Role

1. **Open IAM Console**

   - Sign in to AWS Management Console
   - Navigate to IAM service
   - Choose "Roles" from left navigation
   - Click "Create role"

   ![IAM Create Role](screenshots/03-create-lambda-role.png)

2. **Configure Lambda Role**

   - **Trusted entity**: AWS service
   - **Use case**: Lambda
   - **Role name**: `cloudshelf-book-catalog-lambda-role`
   - **Description**: "Execution role for CloudShelf book catalog Lambda function"

   ![Lambda Role Configuration](screenshots/04-lambda-role-config.png)

3. **Attach Base Policies**

   - **AWSLambdaVPCAccessExecutionRole** - VPC access for Lambda
   - **CloudWatchLogsFullAccess** - Logging permissions

   ![Lambda Base Policies](screenshots/05-lambda-base-policies.png)

4. **Create Custom RDS Policy**

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

   ![RDS Access Policy](screenshots/06-rds-access-policy.png)

#### Shopping Cart Lambda Role

1. **Create Cart Lambda Role**

   - **Role name**: `cloudshelf-shopping-cart-lambda-role`
   - **Description**: "Execution role for CloudShelf shopping cart Lambda function"

2. **Attach Policies**

   - **AWSLambdaVPCAccessExecutionRole** - VPC access
   - **CloudWatchLogsFullAccess** - Logging permissions

3. **Create Custom DynamoDB Policy**

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

   ![DynamoDB Access Policy](screenshots/07-dynamodb-access-policy.png)

### Step 2: API Gateway IAM Roles

#### API Gateway Execution Role

1. **Create API Gateway Role**

   - **Trusted entity**: API Gateway
   - **Role name**: `cloudshelf-apigateway-execution-role`
   - **Description**: "Execution role for CloudShelf API Gateway"

   ![API Gateway Role](screenshots/08-apigateway-role.png)

2. **Attach Lambda Invoke Policy**

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

   ![Lambda Invoke Policy](screenshots/09-lambda-invoke-policy.png)

### Step 3: RDS Security Configuration

#### Database Access Role

1. **Create RDS Access Role**

   - **Role name**: `cloudshelf-rds-access-role`
   - **Description**: "Database access role for CloudShelf application"

2. **Configure Database Authentication**

   - Enable **IAM database authentication** on RDS instance
   - Create database user with IAM authentication

   ```sql
   -- Connect as master user
   CREATE USER book_catalog_user;
   GRANT rds_iam TO book_catalog_user;
   GRANT SELECT, INSERT, UPDATE, DELETE ON books.* TO book_catalog_user;
   ```

   ![RDS IAM Authentication](screenshots/10-rds-iam-auth.png)

### Step 4: S3 Security Configuration

#### S3 Bucket Policies

1. **Static Website Bucket Policy**

   ```json
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "PublicReadGetObject",
         "Effect": "Allow",
         "Principal": "*",
         "Action": "s3:GetObject",
         "Resource": "arn:aws:s3:::cloudshelf-static-website/*"
       },
       {
         "Sid": "CloudFrontOriginAccess",
         "Effect": "Allow",
         "Principal": {
           "AWS": "arn:aws:iam::cloudfront:user/CloudFront Origin Access Identity"
         },
         "Action": "s3:GetObject",
         "Resource": "arn:aws:s3:::cloudshelf-static-website/*"
       }
     ]
   }
   ```

   ![S3 Bucket Policy](screenshots/11-s3-bucket-policy.png)

2. **S3 Access Role for Lambda**

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

### Step 5: CloudWatch and Monitoring Permissions

#### CloudWatch Access Role

1. **Create Monitoring Role**

   - **Role name**: `cloudshelf-monitoring-role`
   - **Description**: "CloudWatch monitoring and logging role"

2. **CloudWatch Permissions Policy**

   ```json
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Effect": "Allow",
         "Action": [
           "logs:CreateLogGroup",
           "logs:CreateLogStream",
           "logs:PutLogEvents",
           "logs:DescribeLogStreams",
           "logs:DescribeLogGroups"
         ],
         "Resource": "arn:aws:logs:*:*:log-group:/aws/lambda/cloudshelf-*"
       },
       {
         "Effect": "Allow",
         "Action": [
           "cloudwatch:PutMetricData",
           "cloudwatch:GetMetricStatistics",
           "cloudwatch:ListMetrics"
         ],
         "Resource": "*"
       }
     ]
   }
   ```

   ![CloudWatch Permissions](screenshots/12-cloudwatch-permissions.png)

### Step 6: Cross-Service Security

#### Security Group Configuration

1. **Lambda Security Group**

   - **Name**: `cloudshelf-lambda-sg`
   - **Description**: "Security group for Lambda functions"
   - **VPC**: CloudShelf VPC
   - **Outbound rules**:
     - HTTPS (443) to 0.0.0.0/0 (AWS API calls)
     - PostgreSQL (5432) to RDS security group
     - HTTPS (443) to DynamoDB VPC endpoint

   ![Lambda Security Group](screenshots/13-lambda-security-group.png)

2. **RDS Security Group**

   - **Name**: `cloudshelf-rds-sg`
   - **Description**: "Security group for RDS PostgreSQL"
   - **Inbound rules**:
     - PostgreSQL (5432) from Lambda security group only
     - No public access

   ![RDS Security Group](screenshots/14-rds-security-group.png)

### Step 7: Resource-Based Policies

#### Lambda Resource Policies

1. **Book Catalog Lambda Permission**

   ```bash
   aws lambda add-permission \
     --function-name cloudshelf-book-catalog \
     --statement-id apigateway-invoke \
     --action lambda:InvokeFunction \
     --principal apigateway.amazonaws.com \
     --source-arn "arn:aws:execute-api:*:*:*/*/GET/books/*"
   ```

2. **Shopping Cart Lambda Permission**

   ```bash
   aws lambda add-permission \
     --function-name cloudshelf-shopping-cart \
     --statement-id apigateway-invoke \
     --action lambda:InvokeFunction \
     --principal apigateway.amazonaws.com \
     --source-arn "arn:aws:execute-api:*:*:*/*/POST/cart/*"
   ```

   ![Lambda Resource Policies](screenshots/15-lambda-resource-policies.png)

## 🔒 Security Best Practices

### Principle of Least Privilege

- **Service-Specific Roles**: Each service has only required permissions
- **Resource-Level Permissions**: Policies specify exact resources when possible
- **Time-Bound Access**: Use temporary credentials where applicable

### Access Patterns

- **No Hardcoded Credentials**: All access through IAM roles
- **VPC Isolation**: Database access only from within VPC
- **API Authentication**: Future implementation of Cognito integration

### Monitoring and Auditing

- **CloudTrail Integration**: Log all IAM actions
- **Access Logging**: Monitor role assumptions and permission usage
- **Regular Reviews**: Quarterly permission audits

## 🛡️ Security Validation

### Role Testing

1. **Lambda Role Validation**

   ```bash
   # Test RDS connection from Lambda
   aws lambda invoke \
     --function-name cloudshelf-book-catalog \
     --payload '{"action":"test-db-connection"}' \
     response.json
   ```

2. **API Gateway Integration Test**

   ```bash
   # Test API Gateway to Lambda integration
   curl -X GET https://your-api-id.execute-api.region.amazonaws.com/prod/books
   ```

### Security Checklist

- [ ] All Lambda functions have dedicated execution roles
- [ ] RDS instance configured with IAM database authentication
- [ ] S3 bucket policies restrict access to CloudFront only
- [ ] Security groups follow least privilege (no 0.0.0.0/0 inbound except HTTPS)
- [ ] DynamoDB access limited to specific tables and operations
- [ ] CloudWatch permissions configured for logging and metrics
- [ ] No AWS credentials stored in code or configuration files
- [ ] All cross-service permissions explicitly defined

## 🚨 Security Monitoring

### CloudWatch Alarms

1. **Failed Authentication Attempts**

   - Monitor CloudTrail for repeated access denied events
   - Alert threshold: >10 failed attempts in 5 minutes

2. **Unusual API Activity**

   - Monitor API Gateway for unusual request patterns
   - Alert on 4xx/5xx error rate spikes

3. **Database Connection Monitoring**

   - Monitor RDS for failed connection attempts
   - Alert on connection count approaching limits

### Security Metrics

- **Authentication Success Rate**: >99.5%
- **Authorization Failures**: <0.1% of requests
- **Security Group Violations**: 0 attempts
- **Credential Exposure**: 0 incidents

## 💰 Cost Considerations

### IAM Pricing

- **IAM Roles and Policies**: No additional cost
- **CloudTrail Logging**: $2.00 per 100,000 events
- **Security Monitoring**: Included in CloudWatch costs

### Cost Optimization

- **Log Retention**: Set appropriate retention periods
- **Metric Filtering**: Only alert on actionable security events
- **Regular Cleanup**: Remove unused roles and policies

## 📋 Troubleshooting

### Common Issues

1. **Lambda Permission Denied**

   - Verify execution role has required policies
   - Check resource ARNs in policy statements
   - Confirm VPC configuration if applicable

2. **RDS Connection Failures**

   - Verify IAM database authentication enabled
   - Check security group rules
   - Confirm database user permissions

3. **API Gateway 403 Errors**

   - Verify Lambda resource-based policies
   - Check API Gateway execution role
   - Confirm CORS configuration

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

## 📚 Related Documentation

- [CloudShelf System Architecture](../cloudshelf-system-architecture.md) - Overall system design
- [VPC Setup Guide](../vpc/cloudshelf-vpc-setup.md) - Network foundation
- [Lambda Setup Guide](../lambda/cloudshelf-lambda-setup.md) - Function deployment
- [API Gateway Setup](../apigateway/cloudshelf-apigateway-setup.md) - API configuration
- [Security Architecture](../cloudshelf-security-architecture.md) - Security strategy

---

**Setup Verification Checklist:**

- [ ] Lambda execution roles created with least privilege permissions
- [ ] RDS IAM authentication configured and tested
- [ ] API Gateway roles and resource policies configured
- [ ] S3 bucket policies restrict access appropriately
- [ ] DynamoDB access policies configured for cart operations
- [ ] Security groups follow principle of least privilege
- [ ] CloudWatch logging permissions configured
- [ ] Cross-service permissions explicitly defined
- [ ] No hardcoded credentials in application code
- [ ] Security monitoring and alerting configured

---

**Document Information:**

- **Created**: September 3, 2025
- **Author**: Solutions Architect
- **Version**: 1.0
- **Last Updated**: September 3, 2025
