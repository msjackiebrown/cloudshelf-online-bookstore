--

# 🏗️ CloudShelf EC2 Jump Host (Bastion) Setup Guide

> **Purpose:**
> This guide provides step-by-step instructions for creating and automating a secure EC2 jump host (bastion) for accessing your RDS instance in both development and production environments, following AWS best practices.

---

## 🚀 Step 1: Create Jump Host Security Group

1. **Navigate to EC2 → Security Groups → Create Security Group**

   - **Name**: `cloudshelf-jump-sg-phase1`
   - **Description**: `Jump host (bastion) for RDS access`
   - **VPC**: Select your custom VPC (e.g., `cloudshelf-vpc`)

2. **Configure Inbound Rules**

   - **Type**: SSH (22)
   - **Source**: Your office IP, VPN CIDR, or trusted developer IPs (never 0.0.0.0/0 in prod)
   - **Description**: SSH access for admins/developers

3. **Configure Outbound Rules**

   - **Type**: PostgreSQL (5432)
   - **Destination**: `cloudshelf-rds-sg`
   - **Description**: Allow jump host to connect to RDS

   ![Jump Host Security Group Creation Screenshot](../screenshots/jump-host/Jump-Host-Security-Group.png)
   _Create and configure the security group for the jump host (allow SSH from trusted IPs only)_

---

## 🚀 Step 2: Launch EC2 Instance as Jump Host

1. **Navigate to EC2 → Instances → Launch Instance**

   - **Name**: `cloudshelf-jump-host-phase1`
   - **AMI**: Amazon Linux 2 (or latest Amazon Linux)
   - **Instance Type**: t3.micro (free tier eligible)
   - **Network**: Custom VPC
   - **Subnet**: Private subnet (preferred) or public subnet (with Elastic IP)
   - **Auto-assign Public IP**: Enable only if using public subnet (dev only)
   - **Security Group**: `cloudshelf-jump-sg-phase1`

2. **Key Pair**: Create or use an existing key pair for SSH access

---

## 🚀 Step 3: Automate Jump Host Setup with EC2 UserData

As a Solutions Architect, you should use EC2 UserData to automate the initial configuration of your jump host. This ensures consistency, security, and reduces manual effort.

**Sample UserData Script (Amazon Linux 2):**

```bash
#!/bin/bash
# Update system and install PostgreSQL 17 client
yum update -y
amazon-linux-extras install epel -y
yum install -y postgresql17

# (Optional) Install AWS SSM Agent for Session Manager (usually preinstalled)
yum install -y amazon-ssm-agent
systemctl enable amazon-ssm-agent
systemctl start amazon-ssm-agent

# (Optional) Harden SSH: disable root login, allow only ec2-user
sed -i 's/^PermitRootLogin.*/PermitRootLogin no/' /etc/ssh/sshd_config
systemctl restart sshd

# (Optional) Install CloudWatch Agent for logging
# yum install -y amazon-cloudwatch-agent
# ...configure as needed...

echo "Jump host bootstrap complete." > /etc/motd
```

**How to use:**

- Paste this script into the "User data" field when launching your EC2 instance (see above).
- Adjust as needed for your OS, security, and monitoring requirements.

**Benefits:**

- Ensures every jump host is identically and securely configured
- Supports infrastructure-as-code and automation best practices
- Reduces manual setup errors and improves auditability

---

## 🚀 Step 4: Harden Jump Host Access

- **Restrict SSH**: Only allow trusted IPs/networks in the security group
- **Enable Session Logging**: Use EC2 Instance Connect or AWS Systems Manager Session Manager for auditable access (preferred over SSH)
- **IAM Role**: Attach an IAM role with minimal permissions (e.g., SSM access only)
- **Disable root login**: Use ec2-user or SSM for access

---

## 🚀 Step 5: Connect to RDS from Jump Host

1. **SSH or SSM into the jump host**

   - SSH: `ssh -i your-key.pem ec2-user@jump-host-public-ip`
   - SSM: `aws ssm start-session --target <instance-id>`

2. **Install PostgreSQL Client**

   ```bash
   sudo yum install postgresql17 -y
   # or
   sudo apt-get install postgresql-client-17 -y
   ```

3. **Connect to RDS**
   ```bash
   psql -h your-rds-endpoint -U cloudshelf_admin -d cloudshelf
   ```

---

## 🚀 Step 6: Best Practices for Jump Host

- **Use SSM Session Manager**: No need for public IP or SSH keys; all sessions are logged in CloudTrail
- **Rotate SSH keys regularly** if using SSH
- **Enable CloudWatch Logs** for session activity
- **Remove developer group from jump host SG in prod**; only allow DBAs/SREs
- **Terminate jump host when not needed in dev/test** to save costs

---

## 🚀 Step 7: Clean Up

- **Terminate jump host** when not needed
- **Delete unused security groups and key pairs**

---

_You now have a secure, auditable jump host for RDS access that mirrors real-world AWS best practices!_
