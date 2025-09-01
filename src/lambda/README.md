# Lambda Deployment JARs - Solutions Architect Guide

## Overview

This document provides the **DevOps Engineer** with the JAR files and deployment information needed to create AWS Lambda functions.

## Available JAR Files

### 📚 Book Catalog Service

- **Location**: `src/lambda/book-catalog/target/book-catalog-lambda-1.0-SNAPSHOT.jar`
- **Size**: ~3.4 MB (includes all dependencies)
- **Handler**: `com.cloudshelf.bookcatalog.BookCatalogHandler::handleRequest`
- **Runtime**: Java 21
- **Memory**: 512 MB (recommended starting point)
- **Timeout**: 30 seconds (recommended starting point)

### 🛒 Shopping Cart Service

- **Location**: `src/lambda/shopping-cart/target/shopping-cart-lambda-1.0-SNAPSHOT.jar`
- **Size**: ~15 MB (includes AWS SDK and DynamoDB dependencies)
- **Handler**: `com.cloudshelf.shoppingcart.ShoppingCartHandler::handleRequest`
- **Runtime**: Java 21
- **Memory**: 512 MB (recommended starting point)
- **Timeout**: 30 seconds (recommended starting point)

## Build Commands

### Prerequisites

- Java 21 installed
- Maven 3.8+ installed

### Building JARs

```powershell
# Book Catalog Service
cd "src/lambda/book-catalog"
mvn clean compile package

# Shopping Cart Service
cd "../shopping-cart"
mvn clean compile package
```

## AWS Lambda Configuration

### Environment Variables (DevOps to Configure)

#### Book Catalog Lambda

```bash
# Database configuration
DB_ENDPOINT=your-rds-endpoint.region.rds.amazonaws.com
DB_NAME=cloudshelf_books
DB_PORT=5432

# Application configuration
LOG_LEVEL=INFO
MAX_CONNECTIONS=10
```

#### Shopping Cart Lambda

```bash
# DynamoDB configuration
DYNAMODB_TABLE_NAME=cloudshelf-shopping-carts
AWS_REGION=us-east-1

# Application configuration
LOG_LEVEL=INFO
```

### IAM Permissions Required

#### Book Catalog Lambda Role

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["rds-db:connect"],
      "Resource": "arn:aws:rds-db:region:account:dbuser:db-instance/db-user-name"
    },
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    }
  ]
}
```

#### Shopping Cart Lambda Role

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
      "Resource": "arn:aws:dynamodb:region:account:table/cloudshelf-shopping-carts*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    }
  ]
}
```

## API Gateway Integration

### Book Catalog Endpoints

- **Base Path**: `/books`
- **Methods**: GET, POST, PUT, DELETE
- **Integration Type**: Lambda Proxy Integration
- **CORS**: Enabled

### Shopping Cart Endpoints

- **Base Path**: `/cart`
- **Methods**: GET, POST, PUT, DELETE
- **Integration Type**: Lambda Proxy Integration
- **CORS**: Enabled

## Testing the JARs

### Local Testing (Optional)

The JARs contain basic template handlers that return:

#### Book Catalog Response

```json
{
  "service": "book-catalog",
  "status": "template-ready",
  "message": "Solutions Architect template - Developer implementation required",
  "path": "/books",
  "method": "GET"
}
```

#### Shopping Cart Response

```json
{
  "service": "shopping-cart",
  "status": "template-ready",
  "message": "Solutions Architect template - Developer implementation required",
  "path": "/cart",
  "method": "GET"
}
```

## Deployment Notes

### For DevOps Engineer

1. **Upload JARs** to Lambda functions
2. **Configure environment variables** as specified above
3. **Attach IAM roles** with proper permissions
4. **Connect to API Gateway** with proxy integration
5. **Configure VPC** (if RDS is in private subnet)
6. **Set up monitoring** with CloudWatch

### Architecture Validation

Once deployed, the Lambda functions will respond with template messages indicating they are ready for **Developer** implementation. The JARs are fully functional and can be deployed to AWS immediately.

### Next Steps

- **Developer Team**: Implement business logic based on architecture documentation
- **DevOps Team**: Set up infrastructure, monitoring, and CI/CD pipelines

## Related Documentation

- [System Architecture](../../docs/architecture/cloudshelf-system-architecture.md)
- [Integration Patterns](../../docs/architecture/cloudshelf-integration-patterns.md)
- [Development Guidelines](../../docs/architecture/cloudshelf-development-guidelines.md)
