# S3 Bucket Setup

This guide provides step-by-step instructions for setting up an S3 bucket for static website hosting, both manually and with CloudFormation.

---

## 📷 Setup Screenshots

### **🪣 S3 Console Overview**

![S3 Console Overview](screenshots/01-s3-console-overview.png)

### **🆕 Create Bucket Configuration**

![Create S3 Bucket](screenshots/02-create-bucket-configuration.png)

### **🌐 Static Website Hosting Setup**

![Static Website Hosting Configuration](screenshots/03-static-website-hosting-setup.png)

### **🔓 Block Public Access Settings**

![Block Public Access Settings](screenshots/04-block-public-access-settings.png)

### **📋 Bucket Policy Configuration**

![Bucket Policy Configuration](screenshots/05-bucket-policy-configuration.png)

### **📁 File Upload Process**

![Upload Website Files](screenshots/06-file-upload-process.png)

### **✅ Website Endpoint Testing**

![Website Endpoint Testing](screenshots/07-website-endpoint-testing.png)

---

## Manual Steps

### Step 1: Create a bucket

1. Sign in to the AWS Management Console and open the Amazon S3 console.
2. Choose "Create bucket".
3. Enter a unique bucket name (e.g., `my-website-bucket`) and select a region.
4. Accept the default settings and choose "Create bucket".

### Step 2: Enable static website hosting

1. In the S3 console, select your bucket.
2. Go to the "Properties" tab.
3. Under "Static website hosting", choose "Edit".
4. Select "Use this bucket to host a website" and enable it.
5. Enter the index document name (e.g., `index.html`).
6. (Optional) Enter an error document name (e.g., `404.html`).
7. Save changes. Note the website endpoint URL provided.

### Step 3: Edit Block Public Access settings

1. Go to the "Permissions" tab of your bucket.
2. Under "Block public access (bucket settings)", choose "Edit".
3. Clear "Block all public access" and save changes. (Warning: This makes your bucket public. For production, consider using CloudFront OAC/OAI instead.)

### Step 4: Add a bucket policy for public read access

1. Under "Permissions", choose "Bucket Policy" and click "Edit".
2. Add the following policy, replacing `Bucket-Name` with your bucket name:
   ```json
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "PublicReadGetObject",
         "Effect": "Allow",
         "Principal": "*",
         "Action": ["s3:GetObject"],
         "Resource": ["arn:aws:s3:::Bucket-Name/*"]
       }
     ]
   }
   ```
3. Save changes.

### Step 5: Upload your website files

1. In the S3 console, select your bucket.
2. Click "Upload" and add your `index.html`, `404.html`, and other website files.
3. Complete the upload process.

### Step 6: Test your website endpoint

1. In the "Properties" tab, under "Static website hosting", find the "Endpoint" URL.
2. Open the endpoint URL in your browser to verify your site is working.

![S3 Bucket Setup Step 1](S3%20Screenshot%201.png)
![S3 Bucket Setup Step 2](S3%20Screenshot%202.png)
![S3 Bucket Setup Step 3](S3%20Screenshot%203.png)
![S3 Bucket Setup Step 4](S3%20Screenshot%204.png)
![S3 Bucket Setup Step 5](S3%20Screenshot%205.png)
![S3 Bucket Setup Step 6](S3%20Screenshot%206.png)

## Notes on Bucket Policy

Bucket policy is needed to give public access otherwise you will get 403 error trying to connect to the website endpoint.

---

## CloudFormation Steps

Refer to the [AWS CloudFormation S3 Bucket documentation](https://docs.aws.amazon.com/AWSCloudFormation/latest/TemplateReference/aws-resource-s3-bucket.html) for automating this setup.
