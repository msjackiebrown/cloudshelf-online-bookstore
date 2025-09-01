# CloudFront Distribution Setup

This guide provides step-by-step instructions for setting up a CloudFront distribution for your static website, both manually and with CloudFormation.

---

## 📷 Setup Screenshots

### **🌐 CloudFront Console Overview**

![CloudFront Console Overview](screenshots/01-cloudfront-console-overview.png)

### **🚀 Create Distribution Configuration**

![Create CloudFront Distribution](screenshots/02-create-distribution-configuration.png)

### **📍 Origin Settings Configuration**

![Origin Settings Configuration](screenshots/03-origin-settings-configuration.png)

### **🔒 Security and Access Configuration**

![Security and Access Settings](screenshots/04-security-access-configuration.png)

### **⚙️ Distribution Settings**

![Distribution Settings Configuration](screenshots/05-distribution-settings.png)

### **✅ Distribution Created Successfully**

![CloudFront Distribution Created](screenshots/06-distribution-created.png)

### **🔧 Origin Access Control Setup**

![Origin Access Control Configuration](screenshots/07-origin-access-control.png)

### **📋 S3 Bucket Policy Update**

![S3 Bucket Policy Configuration](screenshots/08-s3-bucket-policy-update.png)

---

## Manual Steps

### Step 1: Create a CloudFront distribution

1. Sign in to the AWS Management Console and open the CloudFront console.
2. Choose "Create Distribution".
3. Select "Web" and click "Get Started".

### Step 2: Configure the distribution settings

1. In the "Origin Settings", for "Origin Domain Name", select your S3 bucket (the website endpoint).
2. Set "Viewer Protocol Policy" to "Redirect HTTP to HTTPS".
3. (Optional) Set "Default Root Object" to `index.html`.
4. Leave other settings as default and choose "Create Distribution".

### Step 3: Update the S3 bucket policy

1. Go to the S3 console and select your bucket.
2. Under "Permissions", choose "Bucket Policy" and click "Edit".
3. Add the following policy, replacing `CloudFront-Distribution-ID` with your CloudFront distribution ID:
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
         "Resource": "arn:aws:s3:::Bucket-Name/*",
         "Condition": {
           "StringEquals": {
             "AWS:SourceArn": "arn:aws:cloudfront::Account-ID:distribution/CloudFront-Distribution-ID"
           }
         }
       }
     ]
   }
   ```
4. Save changes.

### Step 4: Test your CloudFront distribution

1. In the CloudFront console, find your distribution and note the "Domain Name".
2. Open the CloudFront domain name in your browser to verify your site is working.

![CloudFront Distribution Step 5](CloudFront%20Distribution%20Screenshot%205.png)

## Notes on CloudFront Access Denied

If you are hosting a static website in S3 and using CloudFront, you may get Access Denied errors unless you set up the S3 website endpoint as the CloudFront origin. The standard S3 REST endpoint enforces bucket/object permissions, but the website endpoint serves public content as a static site. For static website hosting, always use the S3 website endpoint in your CloudFront distribution settings.

![CloudFront Access Denied Screenshot 1](CloudFront%20Access%20Denied%20Screenshot%201.png)
![CloudFront Access Denied Screenshot 2](CloudFront%20Access%20Denied%20Screenshot%202.png)

## Notes on CloudFront and S3 Bucket Access

If you are using CloudFront with an S3 origin, you do not need to make your S3 bucket public. Instead, follow these steps for a secure setup:

1. **Restrict S3 Bucket Access:**
   - Keep your S3 bucket private (Block Public Access ON).
2. **Use Origin Access Identity (OAI) or Origin Access Control (OAC):**
   - Configure CloudFront to use an OAI (legacy) or OAC (recommended) to access your S3 bucket.
   - Update your S3 bucket policy to allow access only from your CloudFront OAI/OAC.
3. **CloudFront Fetches Content:**
   - When a user requests content, CloudFront fetches it from S3 using the OAI/OAC, even though the bucket is private.

**Step-by-step:**

1. Create your S3 bucket and upload your static website files.
2. Block all public access on the S3 bucket.
3. Create a CloudFront distribution with your S3 bucket as the origin.
4. In CloudFront, set up an OAI or OAC.
5. Update your S3 bucket policy to grant read access to the OAI/OAC only.
6. Set the Default Root Object in CloudFront (e.g., `index.html`).
7. Access your site via the CloudFront URL.

**Summary:**
With CloudFront + OAI/OAC, your S3 bucket stays private, and only CloudFront can access your content. This is more secure and is the recommended approach for production static sites.

---

## CloudFormation Steps

Refer to the [AWS CloudFormation CloudFront Distribution documentation](https://docs.aws.amazon.com/AWSCloudFormation/latest/TemplateReference/aws-resource-cloudfront-distribution.html) for automating this setup.
