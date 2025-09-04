# 🗄️ CloudShelf S3 Storage Setup

Complete guide for configuring AWS S3 buckets for static website hosting and content storage with security best practices.

---

## 🏗️ Architecture Overview

S3 provides scalable object storage for CloudShelf's static assets and content delivery:

- **Static Website Hosting**: HTML, CSS, JavaScript files for the frontend
- **Asset Storage**: Images, documents, and media files
- **CloudFront Integration**: Secure origin for global content delivery
- **Version Control**: Object versioning for content management

| Component              | Configuration               | Purpose                       |
| ---------------------- | --------------------------- | ----------------------------- |
| **Website Bucket**     | Static hosting enabled      | Frontend application delivery |
| **Assets Bucket**      | Private with CloudFront OAC | Secure content storage        |
| **Backup Bucket**      | Cross-region replication    | Disaster recovery             |
| **Lifecycle Policies** | Automated transitions       | Cost optimization             |

![CloudShelf S3 Architecture](../cloudshelf-detailed-architecture-diagrams.md#7-s3-storage-architecture)

---

## 🚀 Implementation Steps

### 1. Create Primary Website Bucket

Navigate to **S3 Console → Create Bucket**

```bash
# Bucket naming convention
cloudshelf-website-[environment]-[region]
# Example: cloudshelf-website-prod-us-east-1
```

![Create S3 Bucket](screenshots/S3%20Screenshot%201.png)

### 2. Configure Static Website Hosting

**Properties Tab → Static Website Hosting → Edit**

```json
{
  "indexDocument": "index.html",
  "errorDocument": "error.html",
  "redirectAllRequestsTo": null,
  "routingRules": []
}
```

![Static Website Hosting](screenshots/S3%20Screenshot%202.png)

### 3. Set Bucket Permissions (CloudFront Integration)

**Keep bucket private and use CloudFront OAC for security:**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowCloudFrontServicePrincipal",
      "Effect": "Allow",
      "Principal": {
        "Service": "cloudfront.amazonaws.com"
      },
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::cloudshelf-website-prod/*",
      "Condition": {
        "StringEquals": {
          "AWS:SourceArn": "arn:aws:cloudfront::ACCOUNT:distribution/DISTRIBUTION-ID"
        }
      }
    }
  ]
}
```

![Bucket Policy Configuration](screenshots/S3%20Screenshot%203.png)

### 4. Upload Website Content

**Objects Tab → Upload → Add Files/Folders**

```bash
# Typical structure
index.html
error.html
assets/
  ├── css/
  ├── js/
  ├── images/
  └── fonts/
```

![Upload Website Files](screenshots/S3%20Screenshot%204.png)

### 5. Configure Lifecycle Policies

**Management Tab → Lifecycle Configuration → Create Rule**

```json
{
  "rules": [
    {
      "id": "WebsiteContentTransition",
      "status": "Enabled",
      "transitions": [
        {
          "days": 30,
          "storageClass": "STANDARD_IA"
        },
        {
          "days": 90,
          "storageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

![Lifecycle Configuration](screenshots/S3%20Screenshot%205.png)

### 6. Verify Website Endpoint

Test both direct S3 endpoint and CloudFront distribution:

```bash
# S3 website endpoint
curl -I http://cloudshelf-website-prod.s3-website-us-east-1.amazonaws.com

# CloudFront distribution
curl -I https://d1234567890abc.cloudfront.net
```

![Website Testing](screenshots/S3%20Screenshot%206.png)

---

## 🔧 Best Practices & Optimization

<details>
<summary><strong>📋 Storage Optimization</strong></summary>

### Storage Classes

- **Standard**: Frequently accessed content (HTML, CSS, JS)
- **Standard-IA**: Infrequently accessed assets (images, documents)
- **Glacier**: Long-term archival (old versions, backups)
- **Intelligent-Tiering**: Automatic cost optimization

### Performance Optimization

- **Multipart Upload**: Use for files larger than 100MB
- **Transfer Acceleration**: Enable for global upload performance
- **Request Rate Optimization**: Distribute object keys to avoid hot spotting
- **Compression**: Compress text-based content before upload

### Cost Management

- **Lifecycle Policies**: Automate transitions to lower-cost storage classes
- **Delete Markers**: Clean up incomplete multipart uploads
- **Storage Analytics**: Monitor access patterns for optimization
- **Requester Pays**: Consider for third-party access scenarios

</details>

<details>
<summary><strong>🔒 Security & Access Control</strong></summary>

### Bucket Security

- **Block Public Access**: Keep enabled for production buckets
- **Bucket Encryption**: Enable default encryption with KMS
- **Versioning**: Enable for content change tracking
- **MFA Delete**: Require MFA for object deletion in critical buckets

### Access Control

- **CloudFront OAC**: Use instead of public bucket policies
- **IAM Policies**: Implement least-privilege access
- **Pre-signed URLs**: Provide time-limited access to private content
- **CORS Configuration**: Configure for cross-origin web requests

### Monitoring & Compliance

- **CloudTrail Integration**: Log all S3 API calls
- **Access Logging**: Enable bucket access logging
- **VPC Endpoints**: Use for private network access
- **Object Lock**: Implement for regulatory compliance

</details>

<details>
<summary><strong>💡 Troubleshooting & Maintenance</strong></summary>

### Common Issues & Solutions

- **403 Forbidden**: Check bucket policy and CloudFront OAC configuration
- **CORS Errors**: Verify CORS configuration for web applications
- **Slow Upload/Download**: Enable Transfer Acceleration or use CloudFront
- **High Costs**: Review storage classes and implement lifecycle policies

### Maintenance Tasks

- **Storage Analytics**: Review monthly storage patterns and costs
- **Access Pattern Analysis**: Optimize storage classes based on usage
- **Security Audits**: Regularly review bucket policies and access controls
- **Content Cleanup**: Remove unused objects and old versions

### Disaster Recovery

- **Cross-Region Replication**: Set up for critical content
- **Backup Strategy**: Implement automated backup procedures
- **Version Recovery**: Maintain recovery procedures for accidental deletions
- **Regional Failover**: Plan for region-specific outages

</details>

---

## 📚 Additional Resources

- [📖 CloudShelf Architecture Decisions](../cloudshelf-architecture-decisions.md) - Complete ADR documentation
- [🌐 CloudFront Setup Guide](../cloudfront/cloudshelf-cloudfront-setup.md) - CDN integration
- � [**IAM Security Setup**](../security/cloudshelf-iam-security-setup.md) - Security roles and policies
- [📊 Monitoring Setup](../monitoring/cloudshelf-monitoring-observability.md) - Performance monitoring
- [💰 Cost Optimization](../cloudshelf-cost-optimization-strategy.md) - Storage cost management

---

_📋 **Documentation Status**: Complete | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Implementation Phase_  
_🏗️ **Architecture Phase**: Storage Layer | 👥 **Team**: Solutions Architecture | 📋 **Next**: CloudWatch Monitoring_
