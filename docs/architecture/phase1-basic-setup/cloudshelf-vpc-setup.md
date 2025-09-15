# 🌐 CloudShelf VPC Network Setup (Phase 1)

> Implementation guide for custom VPC networking foundation (Phase 1, cost-optimized)

This guide provides step-by-step instructions for setting up a minimal, production-aligned custom VPC for the CloudShelf project, following ADR-001 and AWS best practices.

---

## 🔒 Network Security Principles

- **Network Isolation**: Custom VPC for all resources
- **Least-Privilege Security Groups**: Only required access allowed
- **Private Subnets**: Databases and Lambda functions
- **No Direct Database Access**: RDS only accessible from Lambda/jump host

---

## 🏛️ Architecture Overview

- **VPC CIDR**: 10.0.0.0/16
- **Public Subnet**: 10.0.1.0/24 (for future use, NAT, or jump host)
- **Private Subnet**: 10.0.2.0/24 (for RDS, Lambda)
- **Internet Gateway**: For outbound internet access
- **No NAT Gateway (dev only)**: To minimize cost

---

## 🚀 Implementation Guide

### **Step 1: Create VPC**

- CIDR: 10.0.0.0/16
- Enable DNS support and hostnames

![VPC Creation Screenshot](../screenshots/vpc/CloudShelf-VPC-Creation-Step1.png)
_VPC creation with DNS support and hostnames enabled_

### **Step 2: Create Subnets**

- Public Subnet: 10.0.1.0/24 (AZ1)
- Private Subnet: 10.0.2.0/24 (AZ2)

![Subnet Creation Screenshot](../screenshots/vpc/CloudShelf-Subnet-Creation-Step2.png)
_Public and private subnet creation_

### **Step 3: Create and Attach Internet Gateway**

- Attach to VPC

![Internet Gateway Screenshot](../screenshots/vpc/CloudShelf-Internet-Gateway-Step3.png)
_Internet Gateway creation and attachment_

### **Step 4: Configure Route Tables**

- Public Route Table: 0.0.0.0/0 → IGW, associate with public subnet
- Private Route Table: local only, associate with private subnet

![Route Table Screenshot](../screenshots/vpc/CloudShelf-Route-Table-Step4.png)
_Route table configuration and subnet association_

### **Step 5: Security Groups**

- Create empty security groups for RDS and Lambda (see ADR-007)
- Add rules after both exist:
  - RDS SG: allow 5432 from Lambda SG only
  - Lambda SG: allow 5432 to RDS SG, 443 to 0.0.0.0/0

![Security Groups Screenshot](../screenshots/vpc/CloudShelf-Security-Groups-Step5.png)
_Security group creation and rule configuration_

### **Step 6: (Optional) Jump Host**

- Place in public subnet for admin access (see jump host guide)
  ![Jump Host Screenshot](../screenshots/vpc/CloudShelf-Jump-Host-Step6.png)
  _Jump host EC2 instance creation (optional)_

---

## 💡 Cost Optimization Tips

- No NAT Gateway for dev: Use public subnet for jump host, Lambda with no outbound internet
- Use smallest subnets and free tier resources
- Document and monitor usage

---

## 📋 Next Steps

- Deploy RDS in private subnet
- Create jump host in public subnet ([Jump Host Setup Guide](cloudshelf-jump-host-setup.md))
- Configure Lambda with VPC access
- Complete IAM and security setup

---

_This guide replaces the default VPC setup. All new deployments should use this custom VPC foundation._
