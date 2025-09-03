# 📚 CloudShelf Online Bookstore

<div align="center">

**A modern, cloud-native online bookstore built with AWS serverless architecture**

[![AWS](https://img.shields.io/badge/AWS-Serverless-orange?logo=aws)](https://aws.amazon.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Solutions_Architect-blue)](docs/architecture/)
[![Documentation](https://img.shields.io/badge/Documentation-Complete-green)](docs/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

</div>

## 🎯 Overview

CloudShelf is a **scalable e-commerce platform** that demonstrates AWS Solutions Architect best practices through:

- 🏗️ **Serverless Architecture** - Lambda, API Gateway, DynamoDB
- 📊 **Enterprise Documentation** - Comprehensive technical specifications
- 🔒 **Security by Design** - Multi-layer security (Authentication in Phase 2)
- 💰 **Cost Optimization** - Pay-per-use serverless pricing model

**Current Status**: Full-stack implementation with frontend hosting, backend services, and comprehensive documentation. Only user authentication (Cognito) is planned for Phase 2.

---

## 🚀 Quick Start

| Step | Action                          | Link                                                                           |
| ---- | ------------------------------- | ------------------------------------------------------------------------------ |
| 1    | **Understand Business Context** | [Business Requirements](docs/requirements/cloudshelf-business-requirements.md) |
| 2    | **Review Technical Specs**      | [Software Requirements](docs/requirements/cloudshelf-srs.md)                   |
| 3    | **Explore Architecture**        | [System Architecture](docs/architecture/cloudshelf-system-architecture.md)     |
| 4    | **Setup Instructions**          | [Getting Started](#️-getting-started)                                          |

---

## 🏗️ Architecture Overview

<div align="center">

![CloudShelf Architecture Diagram](docs/architecture/cloudshelf-architecture-diagram.png)

</div>

### Current Implementation Status

- ✅ **Phase 1 (Current)**: Book Catalog & Shopping Cart Services + Frontend Hosting
- 🔄 **Phase 2 (Planned)**: User Authentication, Order Processing, Admin Portal

### Core AWS Services

| Service               | Purpose            | Benefits                       | Status     |
| --------------------- | ------------------ | ------------------------------ | ---------- |
| 🚪 **API Gateway**    | RESTful APIs       | Automatic scaling & throttling | ✅ Current |
| ⚡ **Lambda**         | Serverless compute | Pay-per-execution pricing      | ✅ Current |
| 🗄️ **RDS PostgreSQL** | Catalog database   | ACID compliance & performance  | ✅ Current |
| 🗂️ **DynamoDB**       | Shopping carts     | Single-digit ms latency        | ✅ Current |
| 🌐 **CloudFront**     | Global CDN         | Sub-2s page loads worldwide    | ✅ Current |
| 🪣 **S3**             | Static hosting     | 99.999999999% durability       | ✅ Current |
| 🔐 **Cognito**        | Authentication     | Enterprise-grade security      | 🔄 Phase 2 |

---

## ✨ Features & Capabilities

### 🛍️ Customer Experience

- 📖 **Book Catalog** - Advanced filtering and search _(MVP Implementation)_
- 🛒 **Shopping Cart** - Real-time cart management _(MVP Implementation)_
- 👤 **User Accounts** - Secure authentication _(Planned - Phase 2)_
- 📦 **Order Processing** - Streamlined checkout _(Planned - Phase 2)_

### 🔧 Admin Portal

- 📚 **Inventory Management** - Book catalog administration _(Planned - Phase 2)_
- 📊 **Order Dashboard** - Sales and customer analytics _(Planned - Phase 2)_
- 👥 **User Management** - Customer account administration _(Planned - Phase 2)_

---

## 🗺️ Implementation Roadmap

### ✅ Phase 1 (Current Implementation)

- **Book Catalog Service** - Lambda + RDS PostgreSQL
- **Shopping Cart Service** - Lambda + DynamoDB
- **API Gateway** - RESTful endpoints with CORS
- **Frontend Hosting** - S3 Static Website + CloudFront CDN
- **Core Documentation** - Architecture and requirements

### 🔄 Phase 2 (Planned)

- **User Authentication** - AWS Cognito integration
- **Order Processing** - Complete checkout workflow
- **Admin Portal** - Inventory and user management

### 🚀 Phase 3 (Future)

- **Advanced Features** - Search optimization, recommendations
- **Performance Enhancements** - Caching, read replicas
- **Analytics** - Real-time reporting and insights

---

## 📋 Documentation

<details>
<summary><strong>🏛️ Core Architecture</strong></summary>

| Document                                                                       | Purpose                  | Status      |
| ------------------------------------------------------------------------------ | ------------------------ | ----------- |
| [System Architecture](docs/architecture/cloudshelf-system-architecture.md)     | High-level system design | ✅ Complete |
| [Integration Patterns](docs/architecture/cloudshelf-integration-patterns.md)   | Service integration      | ✅ Complete |
| [Data Architecture](docs/architecture/cloudshelf-data-architecture.md)         | Database design          | ✅ Complete |
| [Security Architecture](docs/architecture/cloudshelf-security-architecture.md) | Security patterns        | ✅ Complete |

</details>

<details>
<summary><strong>⚡ Performance & Operations</strong></summary>

| Document                                                                                   | Purpose             | Status      |
| ------------------------------------------------------------------------------------------ | ------------------- | ----------- |
| [Performance Strategy](docs/architecture/cloudshelf-performance-scaling-strategy.md)       | Scaling approach    | ✅ Complete |
| [Cost Optimization](docs/architecture/cloudshelf-cost-optimization-strategy.md)            | Cost management     | ✅ Complete |
| [Monitoring](docs/architecture/cloudshelf-monitoring-observability.md)                     | Observability       | ✅ Complete |
| [Disaster Recovery](docs/architecture/cloudshelf-disaster-recovery-business-continuity.md) | Business continuity | ✅ Complete |

</details>

<details>
<summary><strong>🔧 Setup Guides</strong></summary>

| Service     | Guide                                                                      | Purpose                |
| ----------- | -------------------------------------------------------------------------- | ---------------------- |
| API Gateway | [Setup Guide](docs/architecture/apigateway/cloudshelf-apigateway-setup.md) | API management         |
| Lambda      | [Setup Guide](docs/architecture/lambda/cloudshelf-lambda-setup.md)         | Serverless compute     |
| RDS         | [Setup Guide](docs/architecture/rds/cloudshelf-rds-setup.md)               | PostgreSQL database    |
| DynamoDB    | [Setup Guide](docs/architecture/dynamodb/cloudshelf-dynamodb-setup.md)     | NoSQL shopping cart    |
| S3          | [Setup Guide](docs/architecture/s3/cloudshelf-s3-setup.md)                 | Static website hosting |
| CloudFront  | [Setup Guide](docs/architecture/cloudfront/cloudshelf-cloudfront-setup.md) | Global CDN             |

</details>

<details>
<summary><strong>📚 Requirements & Planning</strong></summary>

| Document                                                                       | Description                        |
| ------------------------------------------------------------------------------ | ---------------------------------- |
| [Business Requirements](docs/requirements/cloudshelf-business-requirements.md) | Business goals and success metrics |
| [Software Requirements](docs/requirements/cloudshelf-srs.md)                   | Technical specifications and NFRs  |
| [Use Cases](docs/requirements/cloudshelf-use-cases.md)                         | User interaction patterns          |
| [User Stories](docs/requirements/cloudshelf-user-stories.md)                   | Feature descriptions               |
| [Glossary](docs/requirements/cloudshelf-glossary.md)                           | Key terms and definitions          |

</details>

---

## 🛠️ Getting Started

### Prerequisites

```bash
# Required tools
- AWS Account with appropriate permissions
- AWS CLI configured
- Git for version control
- Java 21 JDK (for Lambda development)
- Maven 3.8+ (for build management)
```

### Quick Setup

1. **📁 Clone Repository**

   ```bash
   git clone https://github.com/msjackiebrown/cloudshelf-online-bookstore.git
   cd cloudshelf-online-bookstore
   ```

2. **📖 Review Documentation**

   ```bash
   # Start with business context
   open docs/requirements/cloudshelf-business-requirements.md

   # Then review technical architecture
   open docs/architecture/cloudshelf-system-architecture.md
   ```

3. **🏗️ Deploy Infrastructure (Current Phase 1)**

   ```bash
   # Follow setup guides in order:
   # 1. Frontend Hosting
   open docs/architecture/s3/cloudshelf-s3-setup.md
   open docs/architecture/cloudfront/cloudshelf-cloudfront-setup.md

   # 2. API Gateway Setup
   open docs/architecture/apigateway/cloudshelf-apigateway-setup.md

   # 3. Database Setup (RDS + DynamoDB)
   open docs/architecture/rds/cloudshelf-rds-setup.md
   open docs/architecture/dynamodb/cloudshelf-dynamodb-setup.md

   # 4. Lambda Functions
   open docs/architecture/lambda/cloudshelf-lambda-setup.md
   ```

---

## 📊 Project Metrics

<div align="center">

| Metric                     | Target (Year 1) | Target (Year 3) |
| -------------------------- | --------------- | --------------- |
| 💰 **Revenue**             | $2M             | $46M            |
| 👥 **Active Users**        | 10,000          | 85,000          |
| ⚡ **Page Load Time**      | <2 seconds      | <2 seconds      |
| 📈 **Uptime**              | 99.9%           | 99.95%          |
| 💵 **Infrastructure Cost** | <0.5% revenue   | <0.3% revenue   |

</div>

---

## 👨‍💻 AWS Solutions Architect Portfolio

This project demonstrates key **Solutions Architect competencies**:

### 🎯 Technical Excellence

| Competency                  | Implementation        | Evidence                                                                             |
| --------------------------- | --------------------- | ------------------------------------------------------------------------------------ |
| **Serverless Design**       | Lambda + API Gateway  | [Integration Patterns](docs/architecture/cloudshelf-integration-patterns.md)         |
| **Database Architecture**   | RDS + DynamoDB hybrid | [Data Architecture](docs/architecture/cloudshelf-data-architecture.md)               |
| **Security Implementation** | Multi-layer security  | [Security Architecture](docs/architecture/cloudshelf-security-architecture.md)       |
| **Cost Optimization**       | Right-sizing strategy | [Cost Strategy](docs/architecture/cloudshelf-cost-optimization-strategy.md)          |
| **Performance Design**      | Scalable architecture | [Performance Strategy](docs/architecture/cloudshelf-performance-scaling-strategy.md) |

### 📋 Documentation Standards

- **Business Translation** → Requirements to technical specs
- **Architecture Decisions** → Documented rationale and trade-offs
- **Implementation Guides** → Step-by-step technical procedures
- **Professional Communication** → Multi-stakeholder documentation

---

## 🤝 Contributing

### Development Workflow

1. 📖 **Review Requirements** → [Business Context](docs/requirements/cloudshelf-business-requirements.md)
2. 🏗️ **Understand Architecture** → [System Design](docs/architecture/cloudshelf-system-architecture.md)
3. 🌿 **Create Feature Branch** → Follow GitFlow conventions
4. ✅ **Follow Standards** → Reference architecture guidelines
5. 📝 **Update Documentation** → Keep specs current

### Issue Management

- 🎯 [Current Issues](https://github.com/msjackiebrown/cloudshelf-online-bookstore/issues)
- 💬 [Discussions](https://github.com/msjackiebrown/cloudshelf-online-bookstore/discussions)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**🎯 Ready to explore AWS Solutions Architecture?**

Start with [Business Requirements](docs/requirements/cloudshelf-business-requirements.md) → [System Architecture](docs/architecture/cloudshelf-system-architecture.md) → [Setup Guides](docs/architecture/)

[![GitHub Stars](https://img.shields.io/github/stars/msjackiebrown/cloudshelf-online-bookstore?style=social)](https://github.com/msjackiebrown/cloudshelf-online-bookstore)
[![GitHub Forks](https://img.shields.io/github/forks/msjackiebrown/cloudshelf-online-bookstore?style=social)](https://github.com/msjackiebrown/cloudshelf-online-bookstore)

</div>
