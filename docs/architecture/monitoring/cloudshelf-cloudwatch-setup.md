# CloudWatch Monitoring Setup - CloudShelf Architecture

This guide provides step-by-step instructions for setting up CloudWatch monitoring and observability for the CloudShelf online bookstore serverless architecture.

## Architecture Overview

CloudWatch provides comprehensive monitoring for all CloudShelf services:

- **Lambda Functions**: Performance metrics, error rates, duration tracking
- **API Gateway**: Request volume, latency, error rates
- **RDS PostgreSQL**: Database performance and connection metrics
- **DynamoDB**: Read/write capacity, throttling, item-level metrics
- **CloudFront**: Cache hit ratios, origin request metrics
- **Application Logs**: Centralized logging with structured log analysis

## Manual Setup Steps

### Step 1: Create CloudWatch Dashboard

1. **Open CloudWatch Console**

   - Sign in to AWS Management Console
   - Navigate to CloudWatch service
   - Choose "Dashboards" from left navigation
   - Click "Create dashboard"

   ![CloudWatch Console - Create Dashboard](screenshots/01-cloudwatch-dashboard-create.png)

2. **Configure Dashboard**

   - Dashboard name: `CloudShelf-Production-Dashboard`
   - Choose "Blank dashboard"
   - Click "Create dashboard"

   ![CloudWatch Dashboard Configuration](screenshots/02-cloudwatch-dashboard-config.png)

### Step 2: Lambda Function Monitoring

#### Set Up Lambda Metrics

1. **Add Lambda Widget**

   - Click "Add widget"
   - Select "Line" chart type
   - Choose "Lambda" as data source

   ![Lambda Metrics Widget](screenshots/03-lambda-metrics-widget.png)

2. **Configure Lambda Metrics**

   - **Invocations**: Track function execution count
   - **Duration**: Monitor function execution time
   - **Errors**: Track function failures
   - **Throttles**: Monitor concurrency limits
   - **Dead Letter Errors**: Track failed async invocations

   ![Lambda Function Metrics](screenshots/04-lambda-function-metrics.png)

#### Lambda Log Groups

1. **Configure Log Retention**

   - Navigate to CloudWatch Logs
   - Find Lambda log groups: `/aws/lambda/cloudshelf-book-catalog`
   - Set retention period: **30 days** (production), **7 days** (development)

   ![Lambda Log Groups](screenshots/05-lambda-log-groups.png)

2. **Create Log Insights Queries**

   ```sql
   # Error Analysis Query
   fields @timestamp, @message
   | filter @message like /ERROR/
   | sort @timestamp desc
   | limit 100
   ```

   ![CloudWatch Log Insights](screenshots/06-log-insights-queries.png)

### Step 3: API Gateway Monitoring

#### API Gateway Metrics

1. **Add API Gateway Widget**

   - Add new widget to dashboard
   - Select "API Gateway" metrics
   - Configure key metrics:

   ![API Gateway Metrics](screenshots/07-api-gateway-metrics.png)

2. **Key API Gateway Metrics**

   - **Count**: Total API requests
   - **Latency**: Response time (50th, 95th, 99th percentiles)
   - **4XXError**: Client errors
   - **5XXError**: Server errors
   - **CacheHitCount**: API caching effectiveness

   ![API Gateway Performance Metrics](screenshots/08-api-performance-metrics.png)

### Step 4: Database Monitoring

#### RDS PostgreSQL Metrics

1. **Add RDS Widget**

   - Select RDS metrics for PostgreSQL instance
   - Key metrics to monitor:

   ![RDS PostgreSQL Metrics](screenshots/09-rds-metrics.png)

2. **RDS Key Metrics**

   - **CPUUtilization**: Database CPU usage
   - **DatabaseConnections**: Active connections
   - **FreeableMemory**: Available memory
   - **ReadLatency/WriteLatency**: Query performance
   - **ReadIOPS/WriteIOPS**: Disk I/O operations

   ![RDS Performance Dashboard](screenshots/10-rds-performance.png)

#### DynamoDB Metrics

1. **Add DynamoDB Widget**

   - Select DynamoDB table metrics
   - Monitor shopping cart table performance:

   ![DynamoDB Metrics](screenshots/11-dynamodb-metrics.png)

2. **DynamoDB Key Metrics**

   - **ConsumedReadCapacityUnits**: Read usage
   - **ConsumedWriteCapacityUnits**: Write usage
   - **ThrottledRequests**: Capacity exceeded events
   - **SuccessfulRequestLatency**: Response times

### Step 5: CloudFront CDN Monitoring

#### CloudFront Metrics

1. **Add CloudFront Widget**

   - Monitor content delivery performance
   - Track global edge location metrics:

   ![CloudFront Metrics](screenshots/12-cloudfront-metrics.png)

2. **CloudFront Key Metrics**

   - **Requests**: Total requests to CloudFront
   - **BytesDownloaded**: Data transfer volume
   - **CacheHitRate**: Cache effectiveness
   - **OriginLatency**: Backend response time

### Step 6: Custom Alarms Setup

#### Lambda Function Alarms

1. **Create Error Rate Alarm**

   - Metric: Lambda Errors
   - Threshold: > 5% error rate
   - Period: 5 minutes
   - Action: SNS notification

   ![Lambda Error Alarm](screenshots/13-lambda-error-alarm.png)

2. **Create Duration Alarm**

   - Metric: Lambda Duration
   - Threshold: > 10 seconds (95th percentile)
   - Period: 5 minutes

   ![Lambda Duration Alarm](screenshots/14-lambda-duration-alarm.png)

#### API Gateway Alarms

1. **Create Latency Alarm**

   - Metric: API Gateway Latency
   - Threshold: > 2 seconds (95th percentile)
   - Period: 5 minutes

   ![API Gateway Latency Alarm](screenshots/15-api-latency-alarm.png)

2. **Create Error Rate Alarm**

   - Metric: 5XXError rate
   - Threshold: > 1% of total requests
   - Period: 5 minutes

#### Database Alarms

1. **RDS Connection Alarm**

   - Metric: DatabaseConnections
   - Threshold: > 80% of max connections
   - Action: Scale up alert

   ![RDS Connection Alarm](screenshots/16-rds-connection-alarm.png)

2. **DynamoDB Throttle Alarm**

   - Metric: ThrottledRequests
   - Threshold: > 0 throttled requests
   - Action: Capacity scaling alert

### Step 7: Log Analysis Setup

#### Structured Logging

1. **Configure Application Logs**

   - Implement structured JSON logging in Lambda functions
   - Include correlation IDs for request tracing
   - Add business metrics (book searches, cart operations)

   ![Structured Logging Format](screenshots/17-structured-logging.png)

2. **Log Insights Dashboards**

   - Create saved queries for common troubleshooting scenarios
   - Business metrics queries (popular books, cart abandonment)

   ```sql
   # Business Metrics: Popular Books
   fields @timestamp, book_id, search_term
   | filter @message like /book_search/
   | stats count(*) as searches by book_id
   | sort searches desc
   | limit 10
   ```

### Step 8: X-Ray Distributed Tracing

#### Enable X-Ray Tracing

1. **Lambda Function Tracing**

   - Enable X-Ray tracing for all Lambda functions
   - Add X-Ray SDK to application code

   ![X-Ray Lambda Configuration](screenshots/18-xray-lambda-config.png)

2. **API Gateway Tracing**

   - Enable X-Ray tracing on API Gateway
   - Configure sampling rules for production

   ![X-Ray API Gateway](screenshots/19-xray-api-gateway.png)

3. **Service Map Analysis**

   - View complete request flow across services
   - Identify performance bottlenecks

   ![X-Ray Service Map](screenshots/20-xray-service-map.png)

## Monitoring Best Practices

### Dashboard Organization

- **Executive Dashboard**: High-level business and system health metrics
- **Operations Dashboard**: Detailed technical metrics for troubleshooting
- **Service-Specific Dashboards**: Deep-dive metrics per service

### Alerting Strategy

- **Critical Alerts**: Immediate response required (system down, high error rates)
- **Warning Alerts**: Investigation needed (performance degradation)
- **Info Alerts**: Trend monitoring (capacity planning)

### Log Management

- **Retention Policies**: Balance cost vs. compliance requirements
- **Log Levels**: ERROR/WARN for production alerting, INFO/DEBUG for development
- **Sensitive Data**: Ensure no PII in logs, mask sensitive information

## Cost Optimization

### CloudWatch Costs

- **Log Retention**: Set appropriate retention periods per environment
- **Custom Metrics**: Monitor custom metric usage to control costs
- **Dashboard Optimization**: Combine widgets to reduce API calls

### Monitoring Budget

- **Development**: $10-20/month for basic monitoring
- **Production**: $50-100/month for comprehensive monitoring
- **Enterprise**: $200+/month with custom metrics and extended retention

## Compliance and Security

### Data Protection

- **Log Encryption**: Enable encryption at rest for CloudWatch Logs
- **Access Control**: Use IAM roles for CloudWatch access
- **Audit Trail**: Monitor who accesses monitoring data

### Compliance Requirements

- **Data Retention**: Meet regulatory requirements for log retention
- **Monitoring Coverage**: Ensure all critical systems are monitored
- **Incident Response**: Integrate with incident management systems

## Related Documentation

- [CloudShelf System Architecture](../cloudshelf-system-architecture.md) - Overall system design
- [Security Architecture](../security/) - Security monitoring integration
- [Performance Optimization](../cloudshelf-performance-scaling-strategy.md) - Performance tuning
- [Disaster Recovery](../cloudshelf-disaster-recovery-business-continuity.md) - Monitoring in DR scenarios

---

**Setup Verification Checklist:**

- [ ] CloudWatch dashboard created with all service metrics
- [ ] Lambda function monitoring configured
- [ ] API Gateway metrics and alarms set up
- [ ] Database monitoring (RDS + DynamoDB) active
- [ ] CloudFront CDN metrics configured
- [ ] Critical alarms created with SNS notifications
- [ ] Log retention policies configured
- [ ] X-Ray tracing enabled for distributed tracing
- [ ] Cost monitoring and budgets configured
- [ ] Team access and permissions configured

---

**Document Information:**

- **Created**: September 3, 2025
- **Author**: Solutions Architect
- **Version**: 1.0
- **Last Updated**: September 3, 2025
