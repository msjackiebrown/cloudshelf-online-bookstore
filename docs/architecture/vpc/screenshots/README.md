# VPC Setup Screenshots

This directory contains screenshots for the CloudShelf VPC setup guide.

## Screenshot Requirements

### 01-vpc-architecture-overview.png

- High-level diagram showing complete VPC architecture
- Public and private subnets clearly labeled
- Internet gateway, NAT gateway, route tables shown
- Security groups and their relationships
- Database and Lambda placement indicators

### 02-vpc-creation-console.png

- AWS VPC console "Create VPC" screen
- VPC settings with CIDR block 10.0.0.0/16
- Name tag: "cloudshelf-vpc"
- Tenancy: Default
- "Create VPC" button visible

### 03-internet-gateway-setup.png

- Internet Gateway creation screen
- IGW attached to cloudshelf-vpc
- Name: "cloudshelf-igw"
- Attachment status showing "Attached"

### 04-public-subnet-creation.png

- Create subnet dialog for public subnet
- Subnet name: "cloudshelf-public-subnet-1a"
- Availability Zone: us-east-1a
- CIDR: 10.0.1.0/24
- Route table association to public routes

### 05-private-subnet-creation.png

- Create subnet dialog for private subnet
- Subnet name: "cloudshelf-private-subnet-1a"
- Availability Zone: us-east-1a
- CIDR: 10.0.10.0/24
- Route table association to private routes

### 06-security-groups-overview.png

- Security Groups console showing all CloudShelf security groups
- Web tier, application tier, and database tier groups
- Inbound/outbound rules summary
- Group descriptions and VPC associations

## Manual Setup Screenshots Needed

### 07-route-table-public.png

- Public route table configuration
- Route to 0.0.0.0/0 via Internet Gateway
- Subnet associations showing public subnets

### 08-route-table-private.png

- Private route table configuration
- Routes for internal traffic
- NAT Gateway routes (if applicable)

### 09-nat-gateway-setup.png

- NAT Gateway creation (if using private Lambda)
- Elastic IP allocation
- Subnet placement in public subnet

### 10-security-group-web.png

- Web tier security group rules
- Inbound: HTTP (80), HTTPS (443) from 0.0.0.0/0
- Outbound: All traffic allowed

### 11-security-group-app.png

- Application tier security group
- Inbound: Port 443 from web tier security group
- Lambda execution permissions

### 12-security-group-db.png

- Database tier security group
- Inbound: PostgreSQL (5432) from application tier
- No outbound internet access

## Notes for Screenshot Capture

- Use consistent AWS console theme (light mode recommended)
- Ensure resource names follow CloudShelf naming convention
- Show security group rule details clearly
- Include any success messages or confirmations
- Capture complete VPC dashboard showing all components
