# 📷 RDS PostgreSQL Setup Screenshots

## **🎯 Purpose**

This directory contains screenshot placeholders for the RDS PostgreSQL setup documentation. These images provide visual guidance for AWS console operations that complement the written instructions.

---

## **📸 Required Screenshots**

### **01-rds-console-overview.png**

- **View**: AWS RDS Console homepage
- **Focus**: "Create database" button and RDS service overview
- **Purpose**: Show starting point for RDS database creation

### **02-postgresql-engine-selection.png**

- **View**: Database engine selection page
- **Focus**: PostgreSQL engine option selected
- **Purpose**: Confirm PostgreSQL engine choice per ADR-002

### **03-database-instance-configuration.png**

- **View**: Database configuration settings page
- **Focus**: Instance class, storage, database name settings
- **Purpose**: Show proper instance sizing and database naming

### **04-vpc-security-configuration.png**

- **View**: VPC and security group configuration section
- **Focus**: VPC selection, subnet groups, security groups
- **Purpose**: Demonstrate network isolation setup per ADR-001

### **05-monitoring-backup-configuration.png**

- **View**: Additional configuration section
- **Focus**: Backup retention, monitoring, maintenance window settings
- **Purpose**: Show production-ready backup and monitoring setup

### **06-database-instance-launch.png**

- **View**: Final review and launch page
- **Focus**: Summary of configuration before creation
- **Purpose**: Final validation before database creation

### **07-rds-instance-created.png**

- **View**: RDS instances list showing new database
- **Focus**: Successfully created PostgreSQL instance with "Available" status
- **Purpose**: Confirm successful database creation

---

## **📋 Screenshot Guidelines**

- **Resolution**: Minimum 1920x1080 for clear detail
- **Format**: PNG for crisp UI elements
- **Annotations**: Highlight important buttons, selections, and configuration values
- **Consistency**: Use consistent browser/interface theme across all screenshots
- **Security**: Ensure no sensitive information (passwords, ARNs) is visible

---

## **🔄 Maintenance**

- Update screenshots when AWS console UI changes significantly
- Verify screenshots match current CloudShelf architecture decisions
- Ensure all screenshots reflect the VPC and security configuration from ADR-001
