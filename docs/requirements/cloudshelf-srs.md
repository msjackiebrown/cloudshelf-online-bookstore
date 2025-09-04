# Software Requirements Specification (SRS)

**Project:** CloudShelf Online Bookstore (AWS Tutorial Project)  
**Date:** September 4, 2025

---

## 1. Introduction

### 1.1 Purpose

This Software Requirements Specification (SRS) describes the functional and non-functional requirements for the **CloudShelf Online Bookstore tutorial project**. It serves as a learning example for AWS Solutions Architects to understand how business requirements translate into technical specifications.

### 1.2 Scope

CloudShelf is a **fictional online bookstore** designed as an AWS serverless architecture demonstration. The system showcases modern cloud-native patterns using AWS Lambda, API Gateway, DynamoDB, S3, and other managed services.

**Tutorial Learning Objectives:**

- Serverless architecture implementation
- AWS service integration patterns
- Security best practices
- Performance optimization techniques
- Cost-effective infrastructure design

### 1.3 Definitions, Acronyms, and Abbreviations

- **AWS:** Amazon Web Services
- **API:** Application Programming Interface
- **S3:** Simple Storage Service (Object Storage)
- **DynamoDB:** AWS NoSQL Database Service
- **Lambda:** AWS Serverless Compute Service
- **API Gateway:** AWS API Management Service
- **IAM:** Identity and Access Management
- **CloudFront:** AWS Content Delivery Network
- **SRS:** Software Requirements Specification

### 1.4 References

- [CloudShelf Business Requirements Document](./cloudshelf-business-requirements.md) - Tutorial business context
- [CloudShelf Use Cases](./cloudshelf-use-cases.md) - User interaction scenarios
- [CloudShelf User Stories](./cloudshelf-user-stories.md) - Agile development stories

### 1.5 Tutorial Context

## This SRS demonstrates how to translate business requirements into technical specifications for a real-world e-commerce scenario while maintaining tutorial project scope.

## 2. System Overview

### 2.1 Architecture Approach

CloudShelf demonstrates **serverless-first architecture** using AWS managed services:

- **Frontend**: Static website hosted on S3 with CloudFront CDN
- **API Layer**: API Gateway with Lambda functions
- **Data Storage**: DynamoDB for shopping cart, RDS for book catalog
- **Security**: IAM roles, VPC isolation, encryption at rest/transit
- **Monitoring**: CloudWatch for logging and metrics

### 2.2 Core Functionality

**Phase 1 Features (Tutorial Scope):**

- Book catalog browsing and search
- Shopping cart management
- Product detail pages
- Basic user interface

**Phase 2 Features (Extended Learning):**

- User authentication
- Order processing
- Admin functionality
- Enhanced search

### 2.3 User Types

- **Customers**: Browse books, manage shopping cart
- **Administrators**: Manage catalog, view metrics (future)
- **Anonymous Users**: Browse catalog without account

### 2.4 Technology Stack

- **Frontend**: HTML/CSS/JavaScript (Static Website)
- **Backend**: AWS Lambda (Java)
- **Database**: PostgreSQL (RDS) + DynamoDB
- **Storage**: S3 for static content and book images
- **CDN**: CloudFront for global distribution
- **API**: REST APIs via API Gateway

---

## 3. Functional Requirements

### 3.1 Book Catalog Management

**FR-1: Book Display**

- System shall display book information (title, author, price, description)
- System shall show book cover images
- System shall organize books by categories

**FR-2: Search and Browse**

- System shall allow searching books by title or author
- System shall support category-based browsing
- System shall display search results with pagination

**FR-3: Book Details**

- System shall show detailed book information on dedicated pages
- System shall display book availability status
- System shall show pricing information

### 3.2 Shopping Cart

**FR-4: Cart Management**

- System shall allow adding books to shopping cart
- System shall allow removing books from cart
- System shall allow updating quantities in cart
- System shall persist cart contents across browser sessions

**FR-5: Cart Display**

- System shall show cart contents with book details
- System shall calculate and display total price
- System shall show quantity and line totals

### 3.3 User Interface

**FR-6: Responsive Design**

- System shall work on desktop and mobile devices
- System shall provide intuitive navigation
- System shall load quickly on all device types

---

## 4. Non-Functional Requirements

### 4.1 Performance Requirements

**NFR-1: Page Load Performance**

- Pages shall load in under 2 seconds for tutorial demonstrations
- API responses shall complete in under 500ms for good user experience
- Shopping cart operations shall be responsive (< 200ms)

**NFR-2: Scalability**

- System shall auto-scale with AWS Lambda to handle traffic spikes
- DynamoDB shall auto-scale based on demand
- CloudFront shall provide global performance optimization

### 4.2 Availability Requirements

**NFR-3: System Reliability**

- System shall target 99.9% uptime using AWS managed services
- System shall handle failures gracefully with error messages
- System shall use AWS service redundancy for high availability

### 4.3 Security Requirements

**NFR-4: Data Protection**

- System shall encrypt data at rest (RDS encryption, DynamoDB encryption)
- System shall use HTTPS for all communication (TLS 1.2+)
- System shall follow AWS security best practices

**NFR-5: Access Control**

- System shall use IAM roles for AWS service access
- System shall implement least-privilege access principles
- System shall isolate resources using VPC when appropriate

### 4.4 Operational Requirements

**NFR-6: Cost Optimization**

- System shall use serverless pricing models (pay-per-use)
- System shall leverage AWS free tier where applicable
- System shall implement cost monitoring with CloudWatch

**NFR-7: Monitoring and Logging**

- System shall log all API requests for debugging
- System shall monitor performance metrics with CloudWatch
- System shall provide dashboards for system health

---

## 5. External Interface Requirements

### 5.1 User Interfaces

- **Web Interface**: Responsive HTML/CSS/JavaScript frontend
- **REST APIs**: JSON-based APIs for frontend-backend communication

### 5.2 System Interfaces

- **AWS Lambda**: Serverless compute functions
- **API Gateway**: REST API management and routing
- **RDS PostgreSQL**: Relational database for book catalog
- **DynamoDB**: NoSQL database for shopping cart data
- **S3**: Object storage for static content and images
- **CloudFront**: Content delivery network

---

## 6. Tutorial Learning Outcomes

### 6.1 AWS Services Demonstrated

Students will learn to implement:

- **Serverless Architecture**: Lambda functions, API Gateway
- **Data Storage**: RDS, DynamoDB, S3 integration patterns
- **Security**: IAM, VPC, encryption best practices
- **Performance**: CloudFront CDN, caching strategies
- **Monitoring**: CloudWatch logs and metrics

### 6.2 Architecture Patterns

- **Microservices**: Separate Lambda functions per feature
- **Event-Driven**: Asynchronous processing patterns
- **JAMstack**: JavaScript, APIs, and Markup approach
- **Infrastructure as Code**: CloudFormation templates

---

## 7. Project Scope and Constraints

### 7.1 Included in Tutorial

✅ **Core E-commerce Features**

- Book catalog with search
- Shopping cart functionality
- Responsive web interface
- AWS serverless implementation

### 7.2 Not Included (Future Phases)

❌ **Advanced Features**

- Real payment processing
- Complex user authentication
- Advanced analytics
- Third-party integrations

### 7.3 Tutorial Constraints

- **Scope**: Learning project, not production system
- **Cost**: Focus on AWS free tier and low-cost services
- **Complexity**: Appropriate for Solutions Architect learning
- **Timeline**: Designed for structured learning progression

---

_Tutorial project documentation for AWS Solutions Architect learning_  
_CloudShelf: Fictional online bookstore demonstration_
