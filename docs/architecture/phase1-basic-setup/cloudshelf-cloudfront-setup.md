# 🌐 CloudShelf CloudFront Distribution Setup

Complete guide for configuring AWS CloudFront CDN for static website delivery with optimal performance and security.

---

## 🏗️ Architecture Overview

CloudFront serves as the global CDN layer for CloudShelf's static web assets, providing:

- **Global Edge Locations**: Low latency content delivery worldwide
- **S3 Origin Integration**: Secure access to static website files
- **HTTPS Termination**: SSL/TLS encryption for all client connections
- **Caching Strategy**: Optimized content delivery and reduced origin load

| Component                 | Configuration        | Purpose                                    |
| ------------------------- | -------------------- | ------------------------------------------ |
| **Distribution**          | Global edge network  | Content delivery acceleration              |
| **Origin Access Control** | S3 bucket security   | Private bucket with CloudFront-only access |
| **Custom Domain**         | Route 53 integration | Professional domain presentation           |
| **SSL Certificate**       | ACM certificate      | HTTPS security and trust                   |

![CloudShelf CloudFront Architecture](CloudFront-CDN-Architecture-Diagram.png)

---

## 🚀 Implementation Steps

### 1. Create CloudFront Distribution

Navigate to **CloudFront Console → Create Distribution**

![Create CloudFront Distribution](../screenshots/cloudfront/CloudFront%20Distribution%20Screenshot%201.png)

### 2. Configure Origin Settings

**Origin Domain**: Select your S3 bucket endpoint  
**Origin Access**: Create new Origin Access Control (OAC)

```json
{
  "originAccessControlConfig": {
    "name": "cloudshelf-s3-oac",
    "description": "OAC for CloudShelf static website",
    "originAccessControlOriginType": "s3",
    "signingBehavior": "always",
    "signingProtocol": "sigv4"
  }
}
```

![Origin Settings Configuration](../screenshots/cloudfront/CloudFront%20Distribution%20Screenshot%202.png)

### 3. Configure Distribution Settings

**Default Root Object**: `index.html`  
**Viewer Protocol Policy**: Redirect HTTP to HTTPS  
**Allowed HTTP Methods**: GET, HEAD, OPTIONS

![Distribution Settings](../screenshots/cloudfront/CloudFront%20Distribution%20Screenshot%203.png)

### 4. Update S3 Bucket Policy

Replace `YOUR-BUCKET-NAME` and `YOUR-DISTRIBUTION-ID` with actual values:

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
      "Resource": "arn:aws:s3:::YOUR-BUCKET-NAME/*",
      "Condition": {
        "StringEquals": {
          "AWS:SourceArn": "arn:aws:cloudfront::ACCOUNT-ID:distribution/YOUR-DISTRIBUTION-ID"
        }
      }
    }
  ]
}
```

![S3 Bucket Policy Update](../screenshots/cloudfront/CloudFront%20Distribution%20Screenshot%204.png)

### 5. Verify Distribution Deployment

Monitor deployment status and test distribution domain:

```bash
# Test CloudFront domain
curl -I https://d1234567890abc.cloudfront.net

# Verify cache headers
curl -H "Cache-Control: no-cache" https://d1234567890abc.cloudfront.net
```

![Distribution Verification](../screenshots/cloudfront/CloudFront%20Distribution%20Screenshot%205.png)

---

## 🔧 Best Practices & Optimization

<details>
<summary><strong>📋 CloudFront Performance Optimization</strong></summary>

### Caching Strategy

- **TTL Configuration**: Set appropriate cache TTLs for different content types
- **Query String Handling**: Configure query string forwarding based on application needs
- **Compression**: Enable automatic compression for text-based content
- **HTTP/2 Support**: Leverage HTTP/2 for improved performance

### Content Optimization

- **Origin Request Policy**: Create custom policies for optimal origin requests
- **Response Headers Policy**: Configure security and caching headers
- **Real-time Logs**: Enable for detailed analytics and debugging
- **Edge Locations**: Monitor hit ratios and optimize cache behavior

### Cost Optimization

- **Price Class**: Choose appropriate price class based on global reach requirements
- **Origin Shield**: Enable for origins with multiple CloudFront distributions
- **Transfer Acceleration**: Use S3 Transfer Acceleration for upload-heavy workloads

</details>

<details>
<summary><strong>🔒 Security & Access Control</strong></summary>

### Security Headers

- **Content Security Policy**: Implement CSP headers for XSS protection
- **HSTS Headers**: Enforce HTTPS with Strict-Transport-Security
- **X-Content-Type-Options**: Prevent MIME type sniffing attacks
- **Referrer Policy**: Control referrer information leakage

### Access Control

- **Signed URLs**: Implement for time-limited access to private content
- **Signed Cookies**: Use for session-based access control
- **Geographic Restrictions**: Configure geo-blocking if required
- **AWS WAF Integration**: Add web application firewall protection

### SSL/TLS Configuration

- **Custom SSL Certificate**: Use ACM certificates for custom domains
- **TLS Versions**: Enforce minimum TLS 1.2 for security compliance
- **OCSP Stapling**: Enable for improved SSL handshake performance

</details>

<details>
<summary><strong>💡 Troubleshooting & Monitoring</strong></summary>

### Common Issues & Solutions

- **403 Access Denied**: Verify OAC configuration and S3 bucket policy
- **504 Gateway Timeout**: Check origin response times and timeout settings
- **Cache Misses**: Analyze cache hit ratios and optimize cache behavior
- **SSL Certificate Issues**: Validate certificate deployment and domain validation

### Monitoring & Alerts

- **CloudWatch Metrics**: Monitor origin latency, error rates, and cache statistics
- **Real-time Monitoring**: Set up dashboards for key performance indicators
- **Error Analysis**: Configure alarms for 4xx/5xx error rate thresholds
- **Cache Performance**: Track cache hit ratios and optimize accordingly

### Deployment Best Practices

- **Staged Rollouts**: Use multiple distributions for staged deployments
- **Invalidation Strategy**: Plan cache invalidations for content updates
- **Blue-Green Deployments**: Implement with weighted routing policies
- **Rollback Procedures**: Maintain ability to quickly revert to previous versions

</details>

---

## 📚 Additional Resources

- [📖 CloudShelf Architecture Decisions](../cloudshelf-architecture-decisions.md) - Complete ADR documentation
- [🗄️ S3 Setup Guide](../s3/cloudshelf-s3-setup.md) - Origin bucket configuration
- � [**IAM Security Setup**](../security/cloudshelf-iam-security-setup.md) - Security roles and policies
- 📊 [**CloudWatch Setup**](../monitoring/cloudshelf-cloudwatch-setup.md) - Performance monitoring
- [🌐 Route 53 Configuration](../cloudshelf-system-architecture.md) - Custom domain setup

---

_📋 **Documentation Status**: Complete | ✅ **Client Ready**: Yes | 🔄 **Last Updated**: Implementation Phase_  
_🏗️ **Architecture Phase**: Content Delivery | 👥 **Team**: Solutions Architecture | 📋 **Next**: S3 Storage Setup_
