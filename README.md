# 📚 CloudShelf Online Bookstore

<div align="center">

**AWS Solutions Architect Tutorial Project - Serverless E-commerce Demo**

[![AWS](https://img.shields.io/badge/AWS-Tutorial_Project-orange?logo=aws)](https://aws.amazon.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Serverless-blue)](docs/architecture/)
[![Documentation](https://img.shields.io/badge/Documentation-Complete-green)](LEARNING-GUIDE.md)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

</div>

## 🎯 Project Overview

CloudShelf is a **fictional online bookstore** designed as an AWS Solutions Architect learning project that demonstrates modern serverless architecture patterns and best practices.

### Key Learning Objectives

- 🏗️ **Serverless Architecture** - Event-driven design with Lambda, API Gateway, and managed services
- 🔒 **Security Best Practices** - Multi-layer security with VPC isolation and IAM controls
- 🗄️ **Hybrid Data Strategy** - RDS PostgreSQL for catalog + DynamoDB for sessions
- 💰 **Cost Optimization** - Pay-per-use serverless pricing model
- 📚 **Professional Documentation** - Complete technical analysis and decision records

---

## 🏗️ Architecture Overview

<div align="center">

![CloudShelf Architecture Diagram](docs/architecture/diagrams/source/cloudshelf-architecture-diagram.png)

_Serverless e-commerce architecture demonstrating AWS best practices_

</div>

### AWS Services Implementation

| Service               | Purpose                        | Implementation Status |
| --------------------- | ------------------------------ | --------------------- |
| 🚪 **API Gateway**    | REST API management            | ✅ Tutorial Complete  |
| ⚡ **Lambda**         | Serverless compute backend     | ✅ Tutorial Complete  |
| 🗄️ **RDS PostgreSQL** | Book catalog database          | ✅ Tutorial Complete  |
| 🗂️ **DynamoDB**       | Shopping cart sessions         | ✅ Tutorial Complete  |
| 🌐 **CloudFront**     | CDN and performance            | ✅ Tutorial Complete  |
| 🪣 **S3**             | Static website hosting         | ✅ Tutorial Complete  |
| 🔐 **VPC**            | Network security foundation    | ✅ Tutorial Complete  |
| 🛡️ **IAM**            | Access control and permissions | ✅ Tutorial Complete  |

---

## 🚀 Quick Start

### For Portfolio Review

1. **[Technical Analysis](docs/requirements/cloudshelf-technical-analysis.md)** - Architecture decisions and AWS service rationale
2. **[Architecture Decision Records](docs/architecture/cloudshelf-architecture-decisions.md)** - Detailed ADRs for all major decisions
3. **[Business Requirements](docs/requirements/cloudshelf-business-requirements.md)** - Project context and learning objectives

### For Implementation

1. **[Complete Learning Guide](LEARNING-GUIDE.md)** - Step-by-step implementation instructions
2. **[Setup Guides](docs/architecture/)** - Individual service configuration guides
3. **[Source Code](src/)** - Lambda functions and application code

---

## ✨ Implementation Progress

### ✅ Core Features Complete

- **VPC Network Foundation** - Security isolation with public/private subnets
- **IAM Security Configuration** - Least-privilege roles and policies
- **Book Catalog API** - Browse and search functionality (PostgreSQL)
- **Shopping Cart Service** - Session-based cart management (DynamoDB)
- **API Gateway Integration** - RESTful endpoints with CORS support
- **Static Website Hosting** - S3 + CloudFront CDN integration

### 🔄 Advanced Extensions (Future Phases)

- **User Authentication** - AWS Cognito integration
- **Order Processing** - Complete checkout workflow
- **Enhanced Monitoring** - Custom CloudWatch dashboards
- **CI/CD Pipeline** - Automated deployment workflow

---

## 📊 Skills Demonstrated

This project showcases **AWS Solutions Architect competencies** including:

- **Serverless Design Patterns** - Event-driven architecture with managed services
- **Data Architecture** - Polyglot persistence with SQL and NoSQL databases
- **Security Implementation** - Defense-in-depth with network and application security
- **Cost Management** - Serverless-first approach for cost optimization
- **Technical Documentation** - Professional ADRs and implementation guides
- **Systems Thinking** - End-to-end solution design and integration

---

## 📄 License

This tutorial project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**🎯 Ready to explore AWS Solutions Architecture?**

[Technical Analysis](docs/requirements/cloudshelf-technical-analysis.md) → [Learning Guide](LEARNING-GUIDE.md) → [Implementation](docs/architecture/)

[![GitHub Stars](https://img.shields.io/github/stars/msjackiebrown/cloudshelf-online-bookstore?style=social)](https://github.com/msjackiebrown/cloudshelf-online-bookstore)

</div>
