---
# ADR-00X: Use AWS SSM Session Manager for Jump Host Access

## Status
Accepted

## Context

Traditionally, a jump host (bastion) in AWS is provisioned with a public IP or Elastic IP to allow secure SSH access for administrators. However, Elastic IPs are not included in the AWS Free Tier and incur a cost when allocated but not associated, or when the instance is stopped. Additionally, exposing SSH to the internet, even with restricted security groups, increases the attack surface.

AWS Systems Manager (SSM) Session Manager provides a secure, auditable, and cost-effective way to access EC2 instances without requiring a public IP, Elastic IP, or open SSH ports. SSM is included at no additional cost for basic usage and is ideal for study, learning, and production environments.

## Decision

- All administrative access to the jump host EC2 instance will use AWS SSM Session Manager instead of SSH via public/Elastic IP.
- The jump host EC2 instance will be launched in a private or public subnet, but will NOT require a public or Elastic IP.
- The security group for the jump host will NOT require inbound SSH (22) from the internet.
- SSM Agent will be installed and enabled on the jump host (Amazon Linux 2 AMI includes it by default).
- IAM role with SSM permissions will be attached to the jump host EC2 instance.

## Consequences

- **Cost Savings:** No Elastic IP charges; remains within AWS Free Tier for EC2.
- **Security:** No public IP or open SSH port required; all access is via SSM and logged in CloudTrail.
- **Simplicity:** No need to manage SSH keys or IP whitelists.
- **Learning Value:** Demonstrates modern, secure AWS operational practices.

## Implementation Notes

- To connect: `aws ssm start-session --target <instance-id>`
- Ensure the EC2 instance profile includes the `AmazonSSMManagedInstanceCore` policy.
- Outbound internet access (via NAT or IGW) is required for SSM to function, or use VPC endpoints for SSM in private subnets.

---
