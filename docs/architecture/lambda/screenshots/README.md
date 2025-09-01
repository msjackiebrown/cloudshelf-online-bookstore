# 📷 Lambda Functions Setup Screenshots

## **🎯 Purpose**

This directory contains screenshot placeholders for the Lambda functions setup documentation. These images provide visual guidance for AWS console operations and JAR file deployment that complement the written instructions.

---

## **📸 Required Screenshots**

### **01-lambda-console-overview.png**

- **View**: AWS Lambda Console homepage
- **Focus**: "Create function" button and Lambda service overview
- **Purpose**: Show starting point for Lambda function creation

### **02-create-lambda-function.png**

- **View**: Create function configuration page
- **Focus**: Function name, runtime selection (Java 21), basic settings
- **Purpose**: Show proper function naming and runtime configuration

### **03-upload-jar-file.png**

- **View**: Function code upload section
- **Focus**: JAR file upload interface, deployment package selection
- **Purpose**: Demonstrate JAR file deployment process for compiled Lambda functions

### **04-function-configuration.png**

- **View**: Function configuration settings page
- **Focus**: Handler, memory, timeout, environment variables
- **Purpose**: Show proper function configuration for CloudShelf operations

### **05-vpc-security-configuration.png**

- **View**: VPC configuration section
- **Focus**: VPC selection, subnets, security groups for database access
- **Purpose**: Demonstrate network configuration for RDS/DynamoDB access

### **06-api-gateway-integration.png**

- **View**: API Gateway trigger configuration
- **Focus**: API Gateway trigger setup and endpoint configuration
- **Purpose**: Show integration between Lambda and API Gateway

### **07-lambda-function-deployed.png**

- **View**: Lambda function details page showing successful deployment
- **Focus**: Function status, recent deployments, monitoring metrics
- **Purpose**: Confirm successful function deployment and operational status

---

## **📋 Screenshot Guidelines**

- **Resolution**: Minimum 1920x1080 for clear detail
- **Format**: PNG for crisp UI elements
- **Annotations**: Highlight important configuration settings and deployment steps
- **Consistency**: Use consistent browser/interface theme across all screenshots
- **Security**: Ensure no sensitive information (function ARNs, account details) is visible

---

## **🔄 Maintenance**

- Update screenshots when AWS console UI changes significantly
- Verify screenshots match current CloudShelf Lambda architecture per ADR-004
- Ensure all deployment screenshots reflect the JAR-based deployment process
- Update integration screenshots when API Gateway patterns change
