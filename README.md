#  CloudShelf Online Bookstore

> A modern, cloud-native online bookstore built with AWS serverless architecture

CloudShelf is a scalable web application that provides a seamless online book shopping experience. Built using AWS managed services, it delivers high performance, security, and cost-effectiveness while maintaining excellent user experience for both customers and administrators.

This project demonstrates **Solutions Architect** principles and best practices through comprehensive documentation and Infrastructure as Code.

---

##  Quick Links

### ** Project Management**
-  [**Issues**](https://github.com/msjackiebrown/cloudshelf-online-bookstore/issues) - Current development tasks
-  [**Discussions**](https://github.com/msjackiebrown/cloudshelf-online-bookstore/discussions) - Community discussions

### ** Architecture & Infrastructure** 
-  [**System Architecture**](docs/architecture/system-architecture.md) - High-level system design and components
-  [**Integration Patterns**](docs/architecture/integration-patterns.md) - Service integration and API patterns
-  [**Data Architecture**](docs/architecture/data-architecture.md) - Data modeling and storage strategy
-  [**Security Architecture**](docs/architecture/security-architecture.md) - Security design and implementation
-  [**Performance & Scaling**](docs/architecture/performance-scaling-strategy.md) - Performance optimization strategy
-  [**Cost Optimization**](docs/architecture/cost-optimization-strategy.md) - Cost management and optimization
-  [**Monitoring & Observability**](docs/architecture/monitoring-observability.md) - Monitoring and logging strategy
-  [**Deployment Strategy**](docs/architecture/environment-deployment-strategy.md) - Multi-environment deployment
-  [**Disaster Recovery**](docs/architecture/disaster-recovery-business-continuity.md) - Business continuity planning
-  [**API Documentation**](docs/architecture/api-documentation.md) - Comprehensive API specifications

### ** Service Setup Guides**
-  [**VPC Setup**](docs/architecture/vpc/setup-vpc-reference.md) - Network infrastructure guide
-  [**RDS Setup**](docs/architecture/rds/setup-rds.md) - Database architecture and configuration
-  [**DynamoDB Setup**](docs/architecture/dynamodb/setup-dynamodb.md) - NoSQL database configuration
-  [**Lambda Functions**](docs/architecture/lambda/setup-lambda.md) - Serverless compute setup
-  [**API Gateway**](docs/architecture/apigateway/setup-api-gateway.md) - API configuration guide
-  [**CloudFront CDN**](docs/architecture/cloudfront/setup-cloudfront.md) - Content delivery setup
-  [**S3 Hosting**](docs/architecture/s3/setup-s3.md) - Static website hosting

### ** Requirements & Documentation**
-  [**Business Requirements**](docs/requirements/business-requirements.md) - Business goals and context
-  [**Software Requirements**](docs/requirements/cloudshelf-srs.md) - Technical specifications
-  [**Glossary**](docs/requirements/cloudshelf-glossary.md) - Key terms and definitions
-  [**Use Cases**](docs/requirements/cloudshelf-use-cases.md) - User interactions and behavior
-  [**User Stories**](docs/requirements/cloudshelf-user-stories.md) - Feature descriptions
-  [**Project Roles & Deliverables**](docs/project-roles-deliverables.md) - Professional role boundaries

---

##  Features

###  **Customer Experience**
- ** Book Catalog** - Browse comprehensive book collection with advanced filtering
- ** Smart Search** - Find books by title, author, genre, or keywords
- ** Book Details** - Detailed descriptions, reviews, and ratings
- ** Shopping Cart** - Seamless cart management with real-time updates
- ** User Accounts** - Secure registration, authentication, and profile management
- ** Order Management** - Easy checkout process and order history tracking

###  **Admin Features**
- ** Inventory Management** - Add, update, and organize book catalog
- ** Order Processing** - Monitor and manage customer orders
- ** User Administration** - Manage customer accounts and permissions
- ** Analytics Dashboard** - Track sales, popular books, and user engagement

---

##  Architecture Overview

CloudShelf implements a modern **serverless architecture** using AWS managed services:

### **Core AWS Services**
- ** CloudFront** - Global content delivery network
- ** S3** - Static website hosting and asset storage
- ** API Gateway** - RESTful API management and routing
- ** Lambda** - Serverless compute for business logic
- ** RDS PostgreSQL** - Relational database for complex queries
- ** DynamoDB** - NoSQL database for high-performance operations
- ** Cognito** - User authentication and authorization
- ** CloudWatch** - Monitoring, logging, and alerting

### **Design Principles**
- **Serverless-First** - Minimize operational overhead and enable auto-scaling
- **Security by Design** - Implement defense-in-depth security patterns
- **Cost Optimization** - Pay-per-use pricing with intelligent resource management
- **High Availability** - Multi-AZ deployment with automated failover
- **Performance** - Sub-2-second page loads with global CDN

---

##  Project Structure

`
cloudshelf-online-bookstore/
  README.md                           # Project overview (this file)
  .gitignore                          # Git ignore patterns
  docs/                               # Comprehensive documentation
     requirements/                   # Business and technical requirements
        business-requirements.md    # Business goals and context
        cloudshelf-srs.md          # Software requirements specification
        cloudshelf-glossary.md     # Key terms and definitions
        cloudshelf-use-cases.md    # User interactions and behavior
        cloudshelf-user-stories.md # User-focused feature descriptions
     architecture/                   # Technical architecture documentation
         system-architecture.md      # High-level system design
         integration-patterns.md     # API and service integration
         data-architecture.md        # Data modeling and storage
         security-architecture.md    # Security design patterns
         performance-scaling-strategy.md # Performance optimization
         cost-optimization-strategy.md # Cost management
         monitoring-observability.md # Monitoring and logging
         environment-deployment-strategy.md # Deployment strategy
         disaster-recovery-business-continuity.md # DR planning
         api-documentation.md        # API specifications
         vpc/                        # VPC setup guides and diagrams
         rds/                        # RDS architecture and data model
         dynamodb/                   # DynamoDB setup and configuration
         lambda/                     # Lambda functions templates
         apigateway/                 # API Gateway configuration
         cloudfront/                 # CloudFront distribution setup
         s3/                         # S3 bucket configuration
  src/                                # Source code and templates
      lambda/                         # Lambda function JAR files
          book-catalog/               # Book catalog service
          shopping-cart/              # Shopping cart service
`

---

##  Getting Started

### **Prerequisites**
- AWS Account with appropriate permissions
- AWS CLI configured
- Java 11+ for Lambda development
- Maven for build management

### **Quick Deploy**
1. **Clone the repository**
   `powershell
   git clone https://github.com/msjackiebrown/cloudshelf-online-bookstore.git
   cd cloudshelf-online-bookstore
   `

2. **Follow the setup guides**
   - Start with [VPC Setup](docs/architecture/vpc/setup-vpc-reference.md)
   - Continue with [RDS Setup](docs/architecture/rds/setup-rds.md)
   - Deploy [Lambda Functions](docs/architecture/lambda/setup-lambda.md)
   - Configure [API Gateway](docs/architecture/apigateway/setup-api-gateway.md)

3. **Deploy Lambda functions**
   `powershell
   cd src/lambda/book-catalog
   mvn clean package
   `

---

##  Team Responsibilities

### ** Solutions Architect**
- **Technology Decisions**: Architecture Decision Records (ADRs) and technology stack choices
- **System Design**: High-level architecture, integration patterns, and component interactions
- **Standards**: Development guidelines, coding standards, and project structure templates
- **Documentation**: Architecture diagrams, technical specifications, and design patterns
- **Cost Optimization**: Infrastructure cost analysis and optimization strategies

### ** Developer**  
- **Implementation**: Business logic, API endpoints, data access layers, and feature development
- **Testing**: Unit tests, integration tests, code coverage, and quality assurance
- **Code Quality**: Following established patterns and maintaining technical standards

### ** DevOps Engineer**
- **Infrastructure**: Terraform/CloudFormation deployment automation
- **CI/CD**: Build pipelines, automated testing, and deployment processes
- **Monitoring**: Production monitoring, alerting, and operational procedures

---

##  Project Metrics

### **Business Objectives**
- **Target**: $2M revenue (Year 1)  $46M revenue (Year 3)
- **Users**: 10,000  85,000 active customers
- **Performance**: <2 second page loads, 99.9% uptime
- **Cost**: Infrastructure costs <0.5% of revenue

### **Technical Standards**
- **Scalability**: 1,000  50,000 concurrent users
- **Security**: PCI DSS compliance, GDPR compliance
- **Availability**: 99.9% uptime (max 8.7 hours downtime/year)
- **Performance**: Sub-2-second response times globally

---

##  AWS Solutions Architect Demonstration

This project showcases key **AWS Solutions Architect** competencies:

### ** Technical Skills**
- **Serverless Architecture Design** - Lambda, API Gateway, DynamoDB
- **Database Architecture** - RDS PostgreSQL with read replicas
- **Security Implementation** - IAM, Cognito, encryption at rest/transit
- **Performance Optimization** - CloudFront CDN, auto-scaling patterns
- **Cost Optimization** - Right-sizing, auto-scaling, reserved capacity

### ** Documentation Excellence**
- **Business Requirements Translation** - BRD to technical specifications
- **Architecture Decision Records** - Technology choice justification
- **Comprehensive Setup Guides** - Step-by-step implementation
- **Professional Communication** - Multi-stakeholder documentation

### ** Enterprise Patterns**
- **Multi-Environment Strategy** - Dev/Staging/Production deployment
- **Disaster Recovery Planning** - RTO/RPO analysis and procedures
- **Monitoring & Observability** - CloudWatch, alerting, dashboards
- **Compliance & Governance** - Security standards and audit trails

---

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

** Ready to explore AWS Solutions Architecture?** Start with the [Business Requirements](docs/requirements/business-requirements.md) to understand the business context, then dive into the [System Architecture](docs/architecture/system-architecture.md) to see how technology delivers business value!
