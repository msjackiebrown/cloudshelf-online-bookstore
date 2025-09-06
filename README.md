# 📚 CloudShelf Online Bookstore

<div align="center">

**AWS Solutions Architect Tutorial Project - Progressive Learning with Serverless Architecture**

[![AWS](https://img.shields.io/badge/AWS-Tutorial_Project-orange?logo=aws)](https://aws.amazon.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Phase--Based_Learning-blue)](docs/architecture/)
[![Documentation](https://img.shields.io/badge/Documentation-Complete-green)](docs/architecture/phase1-basic-setup/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

</div>

## 🎯 Project Overview

CloudShelf is a **hands-on AWS learning project** designed as a progressive tutorial that takes you from basic serverless concepts to production-ready architecture. Build a complete online bookstore while mastering AWS Solutions Architect skills through **two distinct learning phases**.

### 🚀 Why Phase-Based Learning?

- **🎓 Beginner-Friendly Start** - Get a working app in 3-4 hours without VPC complexity
- **📈 Progressive Difficulty** - Master basics before advanced concepts
- **🛠️ Real Implementation** - Build genuine functionality, not toy examples
- **💼 Portfolio Ready** - Both phases demonstrate professional AWS skills
- **🧭 Clear Learning Path** - Structured progression from simple to sophisticated

---

## 🏗️ Learning Architecture

<div align="center">

```
┌─────────────────────────────────────────────────────────────────┐
│                  CloudShelf Learning Progression               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  📚 PHASE 1 (3-4 hours)                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Simple Serverless: DynamoDB → Lambda → API Gateway    │   │
│  │  Static Website: S3 + CloudFront                       │   │
│  │  Basic Security: IAM + CloudWatch                      │   │
│  └─────────────────────────────────────────────────────────┘   │
│                            ⬇️ Migration                        │
│  🏢 PHASE 2 (1-2 days)                                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Production VPC: Private Subnets + Security Groups     │   │
│  │  Enterprise DB: RDS PostgreSQL + DynamoDB              │   │
│  │  Advanced Security: Custom IAM + Monitoring            │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

_Two-phase learning progression: Serverless-first → Production-ready_

</div>

### 📚 **Phase 1: Serverless Fundamentals** (3-4 hours)

> **Perfect for**: AWS beginners, rapid prototyping, learning core serverless patterns

**🎯 What You'll Build:**

- Complete serverless bookstore with full functionality
- DynamoDB-only data storage (books + shopping cart)
- Public API Gateway endpoints
- S3 static website hosting with CloudFront CDN
- Basic IAM security and CloudWatch monitoring

**✅ Skills You'll Master:**

- Serverless architecture patterns
- DynamoDB design and operations
- Lambda function development
- API Gateway configuration
- Basic security best practices

### 🏢 **Phase 2: Production Architecture** (1-2 days)

> **Perfect for**: Production deployment, enterprise requirements, advanced AWS features

**🎯 What You'll Add:**

- VPC networking with private subnets
- RDS PostgreSQL for complex book catalog
- Advanced IAM security policies
- Comprehensive monitoring and logging
- High availability and disaster recovery

**✅ Skills You'll Master:**

- VPC networking and security groups
- Relational database design
- Advanced IAM and security patterns
- Enterprise monitoring and observability
- Production deployment strategies

---

## 🚀 Quick Start Guide

### 📖 **Choose Your Learning Path**

#### 🌟 **Start with Phase 1** (Recommended for most learners)

```bash
📂 Phase 1 Guide: docs/architecture/phase1-basic-setup/
⏱️  Time Required: 3-4 hours
🎯 Outcome: Working serverless bookstore
```

**[➡️ Start Phase 1 Setup](docs/architecture/phase1-basic-setup/README.md)**

#### 🏢 **Jump to Phase 2** (If you have VPC/RDS experience)

```bash
📂 Phase 2 Guide: docs/architecture/phase2-production-setup/
⏱️  Time Required: 1-2 days
🎯 Outcome: Production-ready architecture
```

**[➡️ Start Phase 2 Setup](docs/architecture/phase2-production-setup/README.md)**

#### � **Migration Path** (Phase 1 → Phase 2)

```bash
📂 Migration Guide: docs/architecture/migration/
⏱️  Time Required: 4-6 hours
🎯 Outcome: Seamless transition to production
```

**[➡️ Migration Guide](docs/architecture/migration/README.md)**

### 🗂️ **For Portfolio Reviewers**

- **[📋 Technical Analysis](docs/requirements/cloudshelf-technical-analysis.md)** - Architecture decisions and AWS service rationale
- **[📖 Architecture Decision Records](docs/architecture/cloudshelf-architecture-decisions.md)** - Detailed ADRs for all major decisions
- **[🎯 Business Requirements](docs/requirements/cloudshelf-business-requirements.md)** - Project context and learning objectives

---

## 🏗️ Architecture Services by Phase

### 📊 **Phase 1: Serverless Foundation**

| Service            | Purpose                      | Implementation Time |
| ------------------ | ---------------------------- | ------------------- |
| 🗂️ **DynamoDB**    | Unified data storage         | 30 minutes          |
| 🔐 **IAM (Basic)** | Essential Lambda permissions | 15 minutes          |
| ⚡ **Lambda**      | Serverless compute backend   | 45 minutes          |
| 🚪 **API Gateway** | REST API management          | 30 minutes          |
| 🪣 **S3**          | Static website hosting       | 20 minutes          |
| 🌐 **CloudFront**  | CDN and performance          | 15 minutes          |
| 📊 **CloudWatch**  | Basic monitoring & logging   | 15 minutes          |

**Total Phase 1: 3-4 hours** ⚡

### 🏢 **Phase 2: Production Enhancements**

| Service                    | Purpose                      | Implementation Time |
| -------------------------- | ---------------------------- | ------------------- |
| 🌐 **VPC**                 | Network security foundation  | 2-3 hours           |
| 🗄️ **RDS PostgreSQL**      | Advanced book catalog        | 1-2 hours           |
| 🔐 **IAM (Advanced)**      | Enterprise security policies | 1 hour              |
| 📈 **Advanced Monitoring** | Production observability     | 1 hour              |
| 🔄 **Migration Tools**     | Phase 1 → Phase 2 transition | 1-2 hours           |

**Total Phase 2: 6-9 hours** 🏢

---

## ✨ Implementation Features

### ✅ **Phase 1 - Complete Functionality**

- **📚 Book Catalog** - Browse, search, and view book details
- **🛒 Shopping Cart** - Add/remove items, persistent sessions
- **🌐 Public API** - RESTful endpoints with CORS support
- **🎨 Static Website** - Responsive UI with CloudFront CDN
- **🔒 Basic Security** - IAM roles and HTTPS everywhere
- **� Essential Monitoring** - CloudWatch logs and basic alarms
- **💰 Cost Effective** - Pay-per-request serverless pricing

### 🚀 **Phase 2 - Production Ready**

- **🏢 Enterprise Security** - VPC isolation and advanced IAM
- **📊 Advanced Database** - PostgreSQL with complex relationships
- **🔍 Enhanced Monitoring** - Custom dashboards and X-Ray tracing
- **⚡ High Availability** - Multi-AZ deployment and disaster recovery
- **🔄 Seamless Migration** - Automated transition from Phase 1
- **📈 Scalability** - Auto-scaling and performance optimization

---

## � Skills Demonstrated by Phase

### 🎓 **Phase 1 Skills**

**Perfect for**: Entry-level positions, serverless specialization, rapid prototyping

- **Serverless Architecture** - Event-driven design patterns
- **NoSQL Database Design** - DynamoDB best practices
- **API Development** - REST endpoint creation and management
- **Static Website Hosting** - S3 and CloudFront integration
- **Basic Security** - IAM roles and least-privilege access
- **Monitoring Fundamentals** - CloudWatch logs and alarms

### 🏢 **Phase 2 Skills**

**Perfect for**: Senior positions, enterprise environments, production deployments

- **Network Architecture** - VPC design and security groups
- **Relational Database** - PostgreSQL design and optimization
- **Advanced Security** - Defense-in-depth strategies
- **Enterprise Monitoring** - Comprehensive observability
- **Migration Strategies** - Seamless architecture transitions
- **Production Operations** - High availability and disaster recovery

---

## 🎯 Learning Outcomes

### 🎓 **For AWS Beginners**

Start with **Phase 1** to master:

- Core AWS serverless services
- Basic security and monitoring
- Rapid application development
- Cost-effective architecture patterns

### 🏢 **For Experienced Developers**

Complete **both phases** to demonstrate:

- Full-stack AWS architecture skills
- Production deployment expertise
- Security and compliance knowledge
- Enterprise-grade system design

---

## 📄 License

This tutorial project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**🎯 Ready to start your AWS Solutions Architecture journey?**

**[🌟 Begin with Phase 1](docs/architecture/phase1-basic-setup/README.md)** → **[🏢 Advance to Phase 2](docs/architecture/phase2-production-setup/README.md)** → **[🔄 Migration Guide](docs/architecture/migration/README.md)**

_Choose your starting point based on your AWS experience level_

[![GitHub Stars](https://img.shields.io/github/stars/msjackiebrown/cloudshelf-online-bookstore?style=social)](https://github.com/msjackiebrown/cloudshelf-online-bookstore)

</div>
