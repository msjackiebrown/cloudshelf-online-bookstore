# 📚 CloudShelf Online Bookstore

<div align="center">

**AWS Solutions Architect Tutorial Project - Progressive Learning with Serverless Architecture**

[![AWS](https://img.shields.io/badge/AWS-Tutorial_Project-orange?logo=aws)](https://aws.amazon.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Phase--Based_Learning-blue)](docs/architecture/)
[![Documentation](https://img.shields.io/badge/Documentation-Complete-green)](docs/architecture/phase1-basic-setup/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

</div>

## 🎯 Project Overview

CloudShelf is a **hands-on AWS learning project** designed as a progressive tutorial that takes you from hybrid database fundamentals to advanced enterprise patterns. Build a complete online bookstore while mastering AWS Solutions Architect skills through **two distinct learning phases**.

### 🚀 Why Phase-Based Learning?

- **🎓 Realistic Foundation** - Start with industry-standard hybrid database patterns
- **📈 Smooth Progression** - Bridge from beginner concepts to advanced enterprise features
- **🛠️ Real Implementation** - Build genuine functionality with professional patterns
- **💼 Portfolio Ready** - Both phases demonstrate production-ready AWS skills
- **🧭 Clear Learning Path** - Structured progression from hybrid databases to custom VPC

---

## 🗯️ Learning Architecture

<div align="center">

```
┌─────────────────────────────────────────────────────────────────┐
│                  CloudShelf Learning Progression               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  📚 PHASE 1 (4-6 hours)                                       │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Hybrid Database: PostgreSQL RDS + DynamoDB            │   │
│  │  Default VPC: Lambda VPC integration                   │   │
│  │  Static Website: S3 + CloudFront                       │   │
│  │  Console Setup: Beginner-friendly, no CLI             │   │
│  └─────────────────────────────────────────────────────────┘   │
│                            ⬇️ Enhancement                       │
│  🏢 PHASE 2 (1-2 days)                                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Custom VPC: Private Subnets + Advanced Security       │   │
│  │  Advanced Patterns: GSI, Streams, Multi-AZ             │   │
│  │  Enterprise Features: Monitoring + Disaster Recovery   │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

_Two-phase learning progression: Serverless-first → Production-ready_

</div>

### 📚 **Phase 1: Hybrid Architecture** (4-6 hours)

> **Perfect for**: AWS beginners ready for realistic patterns, industry-standard database design

**🎯 What You'll Build:**

- Complete hybrid database bookstore with professional patterns
- PostgreSQL RDS for relational data (books, users, orders)
- DynamoDB for high-performance operations (cart, sessions)
- Default VPC introduction without complexity
- API Gateway with unified endpoints
- S3 static website hosting with CloudFront CDN
- Console-focused setup (no CLI complexity)

**✅ Skills You'll Master:**

- Hybrid database architecture patterns
- PostgreSQL and DynamoDB integration
- Default VPC and basic networking
- Lambda VPC connectivity
- Industry-standard data modeling
- Console-based AWS operations

### 🏢 **Phase 2: Custom VPC & Advanced Patterns** (1-2 days)

> **Perfect for**: Production deployment, enterprise requirements, advanced AWS patterns

**🎯 What You'll Add:**

- Custom VPC with private/public subnets
- Advanced DynamoDB patterns (GSI, streams, cross-region)
- Multi-AZ RDS deployment for high availability
- Advanced IAM security policies and roles
- Comprehensive monitoring with X-Ray tracing
- Infrastructure as Code (CloudFormation/CDK)

**✅ Skills You'll Master:**

- Custom VPC networking and security groups
- Advanced DynamoDB and PostgreSQL patterns
- High availability and disaster recovery
- Enterprise monitoring and observability
- Infrastructure automation
- Production deployment strategies

---

## 🚀 Quick Start Guide

### �️ **Multi-Role Implementation Schedule**

> **For comprehensive AWS portfolio development across Solutions Architect, DevOps Engineer, and Developer roles**

```bash
📂 Complete Timeline: docs/architecture/cloudshelf-project-timeline.md
⏱️  Total Duration: 4 weeks (20 working days)
🎯 Outcome: Portfolio demonstrating all three AWS specializations
🗓️ Schedule: September 9 - October 3, 2025
```

**[➡️ View Complete Implementation Timeline](docs/architecture/cloudshelf-project-timeline.md)**

### �📖 **Choose Your Learning Path**

#### 🌟 **Start with Phase 1** (Recommended for most learners)

```bash
📂 Phase 1 Guide: docs/architecture/phase1-basic-setup/
⏱️  Time Required: 4-6 hours
🎯 Outcome: Hybrid database bookstore with realistic patterns
💰 Cost: $15-25/month (industry-standard expectations)
```

**[➡️ Start Phase 1 Setup](docs/architecture/phase1-basic-setup/README.md)**

#### 🏢 **Jump to Phase 2** (If you have VPC/advanced AWS experience)

```bash
📂 Phase 2 Guide: docs/architecture/phase2-production-setup/
⏱️  Time Required: 1-2 days
🎯 Outcome: Custom VPC with advanced enterprise patterns
```

**[➡️ Start Phase 2 Setup](docs/architecture/phase2-production-setup/README.md)**

#### 📈 **Enhancement Path** (Phase 1 → Phase 2)

```bash
📂 Enhancement Guide: docs/architecture/phase2-enhancements/
⏱️  Time Required: 4-6 hours
🎯 Outcome: Custom VPC and advanced AWS patterns
```

**[➡️ Phase 2 Enhancement Guide](docs/architecture/phase2-enhancements/README.md)**

### 🗂️ **For Portfolio Reviewers**

- **[📋 Technical Analysis](docs/requirements/cloudshelf-technical-analysis.md)** - Architecture decisions and AWS service rationale
- **[📖 Architecture Decision Records](docs/architecture/cloudshelf-architecture-decisions.md)** - Detailed ADRs for all major decisions
- **[🎯 Business Requirements](docs/requirements/cloudshelf-business-requirements.md)** - Project context and learning objectives

---

## 🗯️ Architecture Services by Phase

### 📊 **Phase 1: Hybrid Foundation**

| Service               | Purpose                           | Implementation Time |
| --------------------- | --------------------------------- | ------------------- |
| 🗯️ **PostgreSQL RDS** | Relational data (books, users)    | 45 minutes          |
| 🗂️ **DynamoDB**       | High-performance (cart, sessions) | 30 minutes          |
| 🔒 **IAM (Hybrid)**   | Database access permissions       | 20 minutes          |
| ⚡ **Lambda (VPC)**   | Hybrid database functions         | 60 minutes          |
| 🚪 **API Gateway**    | Unified REST API endpoints        | 30 minutes          |
| 🪣 **S3**             | Static website hosting            | 20 minutes          |
| 🌐 **CloudFront**     | CDN and performance               | 15 minutes          |
| 📊 **CloudWatch**     | Monitoring & logging              | 15 minutes          |

**Total Phase 1: 4-6 hours** ⚡

### 🏢 **Phase 2: Advanced Patterns & Custom VPC**

| Service                       | Purpose                      | Implementation Time |
| ----------------------------- | ---------------------------- | ------------------- |
| 🌐 **Custom VPC**             | Private subnets & security   | 2-3 hours           |
| 📈 **Advanced DynamoDB**      | GSI, streams, cross-region   | 1-2 hours           |
| 🗄️ **Multi-AZ RDS**           | High availability PostgreSQL | 1-2 hours           |
| 🔒 **Enterprise IAM**         | Advanced security policies   | 1 hour              |
| 🔍 **X-Ray & Monitoring**     | Distributed tracing          | 1 hour              |
| 🗯️ **Infrastructure as Code** | CloudFormation/CDK templates | 1-2 hours           |

**Total Phase 2: 7-11 hours** 🏢

---

## ✨ Implementation Features

### ✅ **Phase 1 - Hybrid Database Foundation**

- **📚 Book Catalog** - PostgreSQL with complex queries and relationships
- **👤 User Management** - PostgreSQL with ACID compliance and data integrity
- **🛒 Shopping Cart** - DynamoDB with single-digit millisecond performance
- **🖥️ Session Management** - DynamoDB with TTL auto-cleanup
- **🔗 Default VPC** - Introduction to VPC concepts without complexity
- **🌐 Unified API** - RESTful endpoints with hybrid database access
- **🎨 Static Website** - Responsive UI with CloudFront CDN
- **🔒 Security** - IAM roles for hybrid database access
- **📊 Monitoring** - CloudWatch logs and basic alarms
- **💰 Realistic Costs** - Industry-standard $15-25/month expectations

### 🚀 **Phase 2 - Advanced Enterprise Patterns**

- **🏢 Custom VPC** - Private subnets with advanced networking
- **📊 Advanced DynamoDB** - Global Secondary Indexes and DynamoDB Streams
- **🗄️ Multi-AZ RDS** - High availability and automatic failover
- **🔍 Distributed Tracing** - X-Ray for comprehensive observability
- **⚡ Auto-scaling** - Dynamic scaling based on demand
- **🔄 Blue/Green Deployment** - Zero-downtime deployment strategies
- **🗯️ Infrastructure as Code** - Automated provisioning and updates
- **📈 Custom Dashboards** - Advanced monitoring and alerting

---

## 🎓 Skills Demonstrated by Phase

### 🎓 **Phase 1 Skills**

**Perfect for**: AWS beginners ready for realistic patterns, industry-standard architecture

- **Hybrid Database Architecture** - PostgreSQL + DynamoDB integration patterns
- **Relational Database Design** - PostgreSQL schema design and optimization
- **NoSQL Performance Patterns** - DynamoDB for high-speed operations
- **Default VPC Networking** - Introduction to VPC concepts
- **Lambda VPC Integration** - Database connectivity in VPC environment
- **API Development** - Unified REST endpoints across databases
- **Console Mastery** - Professional AWS Console navigation
- **Security Fundamentals** - IAM roles for hybrid database access

### 🏢 **Phase 2 Skills**

**Perfect for**: Senior positions, enterprise environments, production deployments

- **Custom VPC Architecture** - Private subnet design and security groups
- **Advanced Database Patterns** - Multi-AZ, read replicas, and performance tuning
- **DynamoDB Advanced Features** - GSI, streams, cross-region replication
- **Enterprise Security** - Defense-in-depth and compliance strategies
- **Infrastructure as Code** - CloudFormation and CDK automation
- **Production Operations** - Monitoring, logging, and disaster recovery
- **Performance Optimization** - Scaling strategies and cost optimization
- **DevOps Integration** - CI/CD pipelines and automated deployments

---

## 🎯 Learning Outcomes

### 🎓 **For AWS Beginners**

Start with **Phase 1** to master:

- Hybrid database architecture patterns
- PostgreSQL and DynamoDB integration
- Default VPC and basic networking concepts
- Console-based AWS operations
- Industry-standard data modeling
- Realistic cost expectations ($15-25/month)

### 🏢 **For Experienced Developers**

Complete **both phases** to demonstrate:

- Full-stack hybrid database expertise
- Custom VPC and advanced networking
- Enterprise security and compliance
- Production deployment automation
- Advanced monitoring and observability
- Infrastructure as Code proficiency

---

## 📄 License

This tutorial project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**🎯 Ready to start your AWS Solutions Architecture journey?**

**[🌟 Begin with Phase 1](docs/architecture/phase1-basic-setup/README.md)** → **[🏢 Advance to Phase 2](docs/architecture/phase2-production-setup/README.md)** → **[🚀 Enhancement Guide](docs/architecture/phase2-enhancements/README.md)**

_Start with realistic hybrid database patterns, then advance to custom VPC and enterprise features_

[![GitHub Stars](https://img.shields.io/github/stars/msjackiebrown/cloudshelf-online-bookstore?style=social)](https://github.com/msjackiebrown/cloudshelf-online-bookstore)

</div>
