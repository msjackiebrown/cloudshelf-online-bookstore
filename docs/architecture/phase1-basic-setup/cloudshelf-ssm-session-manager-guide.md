# Optional Guide: Using AWS Systems Manager (SSM) Session Manager for Secure EC2 Access

> **Purpose:**
> This guide explains how to use AWS SSM Session Manager to securely access your EC2 jump host (bastion) in a private or public subnet, without requiring a public IP or SSH access. This is the recommended approach for both learning and production environments.

---

## 🚀 Prerequisites

- **EC2 instance (jump host):** Launched in your VPC (can be in a public or private subnet)
- **SSM Agent:** Installed and running (Amazon Linux 2 AMI includes it by default)
- **IAM role:** Attach the `AmazonSSMManagedInstanceCore` policy to your EC2 instance
- **Outbound internet access:** Required for SSM to function (via IGW, NAT Gateway, or SSM VPC endpoints)

---

## 🔒 Why Use SSM Session Manager?

- No need for public or Elastic IPs
- No open SSH ports or key management
- All sessions are logged in AWS CloudTrail
- Works in both public and private subnets
- Free for basic usage

---

## 📝 Step-by-Step: Connect to EC2 with SSM

1. **Verify SSM Agent is running**

   - For Amazon Linux 2: SSM Agent is preinstalled and enabled by default.
   - To check manually:
     ```bash
     sudo systemctl status amazon-ssm-agent
     ```

2. **Attach IAM Role**

   - Attach a role with the `AmazonSSMManagedInstanceCore` policy to your EC2 instance.

   ![Attach IAM Role Screenshot](../screenshots/jump-host/Jump-Host-SSM-IAM-Role.png)
   _Attach IAM role with SSM permissions to the EC2 instance_

3. **Ensure Outbound Access**

   - The instance must have outbound internet access (via IGW, NAT, or SSM VPC endpoints).

4. **Start a Session from AWS Console**

   - Go to EC2 → Instances → Select your instance → Connect → Session Manager tab → Click "Connect"

   ![SSM Console Connect Screenshot](../screenshots/jump-host/Jump-Host-SSM-Console-Connect.png)
   _Connect to the instance using Session Manager from the AWS Console_

5. **Start a Session from AWS CLI**

   - Install and configure the AWS CLI on your local machine.
   - Run:
     ```bash
     aws ssm start-session --target <instance-id>
     ```

   ![SSM CLI Connect Screenshot](../screenshots/jump-host/Jump-Host-SSM-CLI-Connect.png)
   _Connect to the instance using Session Manager from the AWS CLI_

---

## 🛡️ Security & Audit

- All SSM sessions are logged in AWS CloudTrail.
- You can enable session logging to S3 or CloudWatch for full command history.

---

## 🧑‍💻 Next Steps

- Use the SSM session to install tools, connect to RDS, or perform admin tasks on the jump host.
- For more advanced SSM features (port forwarding, session logging), see the [AWS SSM documentation](https://docs.aws.amazon.com/systems-manager/latest/userguide/session-manager.html).

---

_This guide demonstrates how to use AWS SSM Session Manager for secure, auditable, and cost-effective EC2 access—no public IP or SSH required!_
