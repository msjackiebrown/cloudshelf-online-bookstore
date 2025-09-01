# 📚 CloudShelf Online Bookstore

> A modern, cloud-native online bookstore built with AWS serverless architecture

CloudShelf is a scalable web application that provides a seamless online book shopping experience. Built using AWS managed services, it delivers high performance, security, and cost-effectiveness while maintaining excellent user experience for both customers and administrators.

---

## ✨ Features

### 🛍️ **Customer Experience**

- **📖 Book Catalog** - Browse comprehensive book collection with advanced filtering
- **🔍 Smart Search** - Find books by title, author, genre, or keywords
- **📋 Book Details** - Detailed descriptions, reviews, and ratings
- **🛒 Shopping Cart** - Seamless cart management with real-time updates
- **👤 User Accounts** - Secure registration, authentication, and profile management
- **📦 Order Management** - Easy checkout process and order history tracking

### 🔧 **Admin Features**

- **📚 Inventory Management** - Add, update, and organize book catalog
- **📊 Order Processing** - Monitor and manage customer orders
- **👥 User Administration** - Manage customer accounts and permissions
- **📈 Analytics Dashboard** - Track sales, popular books, and user engagement

---

## 🏗️ Architecture

CloudShelf uses a **serverless-first architecture** with AWS managed services:

### **Technology Stack**

- **Frontend**: Static HTML/CSS/JavaScript hosted on S3 + CloudFront CDN
- **API Layer**: AWS API Gateway + Lambda functions
- **Databases**: RDS PostgreSQL (catalog) + DynamoDB (carts/sessions)
- **Authentication**: AWS Cognito for secure user management
- **Infrastructure**: CloudFormation for Infrastructure as Code

### **Key Design Principles**

- ✅ **Serverless-first** - Pay only for what you use
- ✅ **Security by design** - Multi-layered security approach
- ✅ **High availability** - Multi-AZ deployment across regions
- ✅ **Cost-optimized** - Right-sized resources with auto-scaling

---

## 📖 Documentation

### **Requirements & Design**

- 📝 [**Glossary**](docs/requirements/cloudshelf-glossary.md) - Key terms and definitions
- 📋 [**Software Requirements Specification**](docs/requirements/cloudshelf-srs.md) - Detailed system requirements
- 🎯 [**Use Cases**](docs/requirements/cloudshelf-use-cases.md) - User interactions and system behavior
- 📖 [**User Stories**](docs/requirements/cloudshelf-user-stories.md) - Feature descriptions from user perspective

### **UI/UX Design**

- 🎨 [**Book Catalog Page**](docs/requirements/wireframes/book-catalog-page.md) - Main browsing interface
- 📄 [**Book Details Page**](docs/requirements/wireframes/book-details-page.md) - Individual book view
- 🛒 [**Shopping Cart Page**](docs/requirements/wireframes/shopping-cart-page.md) - Cart management interface

### **Architecture & Infrastructure**

- 🏛️ [**Architecture Decision Records**](docs/architecture/architecture-decisions.md) - Key technical decisions and rationale
- 🌐 [**VPC Setup Guide**](docs/architecture/vpc/setup-vpc-reference.md) - Complete networking configuration
- 📊 VPC Architecture Diagrams and Screenshots

---

## 🚀 Getting Started

### **Prerequisites**

- AWS Account with appropriate permissions
- Git for version control
- Basic understanding of AWS services

### **Quick Start**

1. **📥 Clone the Repository**

   ```bash
   git clone https://github.com/msjackiebrown/cloudshelf-online-bookstore.git
   cd cloudshelf-online-bookstore
   ```

2. **📚 Review Documentation**

   ```bash
   # Start with architecture decisions
   cat docs/architecture/architecture-decisions.md

   # Understand the requirements
   cat docs/requirements/cloudshelf-srs.md
   ```

3. **🏗️ Infrastructure Setup**

   ```bash
   # Follow the VPC setup guide first
   open docs/architecture/vpc/setup-vpc-reference.md
   ```

4. **🔧 Development Workflow**
   - This project uses **GitFlow** branching strategy
   - `main` branch: Production-ready code
   - `develop` branch: Integration and testing
   - `feature/*` branches: Individual features

---

## 🗂️ Project Structure

```
cloudshelf-online-bookstore/
├── 📄 README.md                    # Project overview (this file)
├── 📁 docs/                        # Comprehensive documentation
│   ├── 📁 requirements/           # Business requirements and design
│   │   ├── 📁 wireframes/         # UI mockups and designs
│   │   └── 📁 issues/             # GitHub issues and import tools
│   └── 📁 architecture/           # Technical architecture
│       ├── 📄 architecture-decisions.md  # ADRs and design choices
│       └── 📁 vpc/                # VPC setup guides and diagrams
├── 📁 .github/                     # GitHub configuration
│   └── 📁 ISSUE_TEMPLATE/         # Issue templates for project management
└── 📄 .gitignore                  # Git ignore patterns
```

---

## 🤝 Contributing

We welcome contributions! Here's how to get involved:

### **Development Process**

1. **📖 Read the Documentation** - Understand requirements and architecture
2. **🌿 Create Feature Branch** - Follow GitFlow branching strategy
3. **✅ Follow Best Practices** - Reference our architecture decisions
4. **🧪 Test Your Changes** - Ensure functionality and security
5. **📝 Update Documentation** - Keep docs current with changes

### **Issue Management**

- Use GitHub issue templates for consistent reporting
- Reference [Issue Import Tools](docs/requirements/issues/README.md) for bulk operations
- Check [GitHub Issues](https://github.com/msjackiebrown/cloudshelf-online-bookstore/issues) for current work

### **Code Standards**

- Follow AWS Well-Architected Framework principles
- Implement security best practices from day one
- Write clear, documented, and testable code
- Use Infrastructure as Code for all AWS resources

---

## 📊 Project Status

### **Current Phase**: Architecture & Foundation

- ✅ Requirements gathering and documentation complete
- ✅ Architecture decisions documented
- ✅ VPC and networking guides complete
- ✅ GitHub workflow and issue management setup
- 🔄 Infrastructure implementation in progress

### **Next Steps**

1. VPC and security group setup
2. Database provisioning (RDS + DynamoDB)
3. Lambda function development
4. API Gateway configuration
5. Frontend development and S3 hosting

---

## 📄 License

This project is part of a learning initiative. See individual files for specific licensing information.

---

## 🔗 Quick Links

- 🏛️ [**Architecture Decisions**](docs/architecture/architecture-decisions.md) - Why we made key technical choices
- 🌐 [**VPC Setup**](docs/architecture/vpc/setup-vpc-reference.md) - Complete infrastructure guide
- 📋 [**Requirements**](docs/requirements/cloudshelf-srs.md) - What we're building and why
- 🎯 [**Issues**](https://github.com/msjackiebrown/cloudshelf-online-bookstore/issues) - Current development tasks
- 💬 [**Discussions**](https://github.com/msjackiebrown/cloudshelf-online-bookstore/discussions) - Community discussions

---

_Last updated: September 1, 2025_
