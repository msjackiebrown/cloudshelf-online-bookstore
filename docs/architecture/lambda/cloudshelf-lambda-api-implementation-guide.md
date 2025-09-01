# Lambda and API Gateway Implementation Guide

This guide covers the practical steps for implementing Lambda functions and API Gateway endpoints for the CloudShelf Online Bookstore backend.

---

## 1. Lambda Function Implementation

- **Write Lambda Handlers:**
  - Implement Java handler classes for Books and Shopping Cart features.
  - Use the AWS SDK for Java to interact with RDS (for books) and DynamoDB (for cart).
- **Example Handler Signatures:**
  - `com.cloudshelf.lambda.BookFunction::handleRequest`
  - `com.cloudshelf.lambda.CartFunction::handleRequest`
- **Best Practices:**
  - Use environment variables for DB connection info.
  - Handle exceptions and return meaningful error messages.

## 2. Using AWS SDKs in Lambda

- **RDS Access:**
  - Use JDBC (e.g., PostgreSQL or MySQL driver) to connect to RDS from Lambda.
  - Store DB credentials securely (e.g., Secrets Manager or environment variables).
- **DynamoDB Access:**
  - Use the AWS SDK's DynamoDB client to read/write cart data.
  - Example: `AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();`

## 3. API Gateway Endpoint Mapping

- **Define RESTful Endpoints:**
  - `GET /books` - Fetch all books (calls BookFunction)
  - `GET /books/{id}` - Fetch book details (calls BookFunction)
  - `POST /cart` - Add a book to the cart (calls CartFunction)
  - `GET /cart` - View the cart (calls CartFunction)
- **Integration:**
  - Use Lambda Proxy integration for flexible request/response mapping.
  - Map HTTP methods and paths to the correct Lambda function and handler.

## 4. Testing

- Use the Lambda console's test feature to invoke handlers with sample events.
- Use Postman or curl to test API Gateway endpoints end-to-end.

---

For setup and deployment, see the [Lambda Setup Guide](./setup-lambda.md) and [API Gateway Setup Guide](./setup-api-gateway.md).
