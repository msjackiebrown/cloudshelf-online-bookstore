# 📊 Phase 1: Basic CloudWatch Monitoring

> Essential monitoring setup for CloudShelf Phase 1 - Simple troubleshooting and cost tracking

This guide provides basic CloudWatch monitoring for Phase 1 CloudShelf deployment. Focus on essential monitoring without complexity - perfect for debugging and basic operational awareness.

---

## 🎯 Phase 1 Monitoring Goals

### **✅ Essential Monitoring Objectives**

- 🔍 **Basic troubleshooting** - View Lambda logs and API errors
- 📊 **Simple metrics** - Track function invocations and errors
- 💰 **Cost awareness** - Basic billing alerts to avoid surprises
- 🚨 **Error alerting** - Get notified when something breaks
- 📋 **Quick debugging** - Fast access to logs when issues occur

### **⏱️ Implementation Time**

- **Total**: 15-20 minutes
- **Setup**: 10 minutes
- **Testing**: 5-10 minutes

---

## 📊 What You'll Set Up

### **🔧 Automatic Monitoring (Already Enabled)**

AWS automatically provides basic monitoring for all Phase 1 services:

| Service         | Automatic Metrics             | What You Get                  |
| --------------- | ----------------------------- | ----------------------------- |
| **Lambda**      | Invocations, Duration, Errors | Function performance tracking |
| **API Gateway** | Count, Latency, Errors        | API performance monitoring    |
| **DynamoDB**    | Consumed Capacity, Throttles  | Database performance          |
| **S3**          | Requests, Data Transfer       | Storage metrics               |
| **CloudFront**  | Requests, Bytes Downloaded    | CDN performance               |

### **➕ What We'll Add**

- 📋 **Lambda log access** - Easy debugging interface
- 🚨 **Basic error alarms** - Get notified when functions fail
- 💰 **Billing alerts** - Track AWS costs
- 📊 **Simple dashboard** - Quick health overview

---

## 🚀 Implementation Steps

### **1️⃣ Set Up Lambda Log Monitoring (5 minutes)**

#### **Access Lambda Logs**

1. **Navigate to CloudWatch Logs**

   - AWS Console → CloudWatch → Logs → Log groups
   - Find your Lambda log groups: `/aws/lambda/cloudshelf-*`

2. **Set Log Retention (Optional)**
   ```bash
   # Keep logs for 7 days (saves costs)
   aws logs put-retention-policy \
     --log-group-name /aws/lambda/cloudshelf-book-catalog \
     --retention-in-days 7
   ```

#### **Basic Log Searching**

Use CloudWatch Logs Insights for simple debugging:

```sql
# Find errors in the last hour
fields @timestamp, @message
| filter @message like /ERROR/
| sort @timestamp desc
| limit 20
```

```sql
# Find slow Lambda executions
fields @timestamp, @duration
| filter @duration > 5000
| sort @timestamp desc
| limit 10
```

### **2️⃣ Create Basic Error Alarms (5 minutes)**

#### **Lambda Error Alarm**

1. **Navigate to CloudWatch Alarms**

   - AWS Console → CloudWatch → Alarms → Create alarm

2. **Configure Error Alarm**

   ```json
   {
     "AlarmName": "CloudShelf-Lambda-Errors",
     "MetricName": "Errors",
     "Namespace": "AWS/Lambda",
     "Statistic": "Sum",
     "Period": 300,
     "EvaluationPeriods": 1,
     "Threshold": 5,
     "ComparisonOperator": "GreaterThanOrEqualToThreshold"
   }
   ```

3. **Add SNS Notification (Optional)**
   - Create SNS topic: `cloudshelf-alerts`
   - Subscribe with your email
   - Get notified when errors occur

#### **API Gateway Error Alarm**

```json
{
  "AlarmName": "CloudShelf-API-Errors",
  "MetricName": "5XXError",
  "Namespace": "AWS/ApiGateway",
  "Statistic": "Sum",
  "Period": 300,
  "EvaluationPeriods": 2,
  "Threshold": 10,
  "ComparisonOperator": "GreaterThanThreshold"
}
```

### **3️⃣ Set Up Billing Alerts (3 minutes)**

#### **Enable Billing Alerts**

1. **Go to Billing Preferences**

   - AWS Console → Billing → Billing preferences
   - Enable "Receive Billing Alerts"

2. **Create Cost Alarm**
   ```json
   {
     "AlarmName": "CloudShelf-Monthly-Cost",
     "MetricName": "EstimatedCharges",
     "Namespace": "AWS/Billing",
     "Statistic": "Maximum",
     "Period": 86400,
     "EvaluationPeriods": 1,
     "Threshold": 10.0,
     "ComparisonOperator": "GreaterThanThreshold"
   }
   ```

### **4️⃣ Create Simple Dashboard (5 minutes)**

#### **Basic CloudShelf Dashboard**

1. **Create Dashboard**

   - CloudWatch → Dashboards → Create dashboard
   - Name: `CloudShelf-Phase1-Overview`

2. **Add Key Widgets**

**Lambda Invocations Widget:**

```json
{
  "type": "metric",
  "properties": {
    "metrics": [
      ["AWS/Lambda", "Invocations", "FunctionName", "cloudshelf-book-catalog"],
      [".", "Errors", ".", "."],
      [".", "Duration", ".", "."]
    ],
    "period": 300,
    "stat": "Sum",
    "region": "us-east-1",
    "title": "Lambda Functions"
  }
}
```

**API Gateway Widget:**

```json
{
  "type": "metric",
  "properties": {
    "metrics": [
      ["AWS/ApiGateway", "Count", "ApiName", "cloudshelf-api"],
      [".", "Latency", ".", "."],
      [".", "4XXError", ".", "."],
      [".", "5XXError", ".", "."]
    ],
    "period": 300,
    "stat": "Average",
    "region": "us-east-1",
    "title": "API Gateway"
  }
}
```

---

## 🧪 Testing Your Monitoring

### **📋 Verification Checklist**

#### **Test Log Access**

- [ ] **Lambda logs visible** - Can see function execution logs
- [ ] **Error logs searchable** - Can find errors using Logs Insights
- [ ] **Log retention set** - Logs kept for appropriate time

#### **Test Alarms**

- [ ] **Error alarm created** - Lambda error notifications configured
- [ ] **API alarm created** - API Gateway error monitoring
- [ ] **Billing alert active** - Cost monitoring enabled
- [ ] **Notifications working** - Test SNS alerts (if configured)

#### **Test Dashboard**

- [ ] **Dashboard accessible** - Can view Phase 1 overview
- [ ] **Metrics displaying** - All widgets showing data
- [ ] **Real-time updates** - Metrics update as you use the application

### **🧪 Quick Test Commands**

#### **Generate Test Data**

```bash
# Test your book catalog API to generate metrics
curl https://your-api-gateway-url/books

# Check Lambda logs
aws logs describe-log-groups --log-group-name-prefix /aws/lambda/cloudshelf

# View recent log events
aws logs get-log-events \
  --log-group-name /aws/lambda/cloudshelf-book-catalog \
  --log-stream-name $(aws logs describe-log-streams \
    --log-group-name /aws/lambda/cloudshelf-book-catalog \
    --order-by LastEventTime \
    --descending \
    --limit 1 \
    --query 'logStreams[0].logStreamName' \
    --output text) \
  --limit 10
```

#### **Verify Alarms**

```bash
# Check alarm status
aws cloudwatch describe-alarms --alarm-names CloudShelf-Lambda-Errors

# Test billing alarm
aws cloudwatch describe-alarms --alarm-name-prefix CloudShelf-Monthly-Cost
```

---

## 💡 Phase 1 Monitoring Best Practices

### **🔍 Debugging Workflow**

1. **Check dashboard** - Quick health overview
2. **Review alarms** - Any active alerts?
3. **Search logs** - Use Logs Insights for specific errors
4. **Check metrics** - Function duration, error rates
5. **Test functionality** - Reproduce issues if needed

### **📊 Key Metrics to Watch**

- **Lambda Duration** - Functions should complete quickly
- **Error Rate** - Keep under 1% for good user experience
- **API Latency** - Responses should be fast
- **Costs** - Monitor to avoid surprises

### **🚨 When to Investigate**

- **High error rates** - More than 5% failures
- **Slow responses** - API latency over 1 second
- **Missing data** - No metrics appearing
- **Cost spikes** - Unexpected billing increases

---

## 🚀 What's Next?

### **📈 Phase 1 → Phase 2 Monitoring Evolution**

When you're ready for production (Phase 2), you'll upgrade to:

- 📊 **Custom dashboards** - Comprehensive business metrics
- 🔍 **X-Ray tracing** - Detailed request flow analysis
- 📋 **Advanced log analysis** - Complex queries and insights
- 🚨 **Sophisticated alerting** - Multi-level alarm strategies
- 🔒 **Security monitoring** - GuardDuty and security metrics

### **📚 Related Documentation**

- 🏢 [**Phase 2 Monitoring**](../phase2-production-setup/monitoring-and-logging.md) - Advanced observability
- 📊 [**Cost Optimization**](../cloudshelf-cost-optimization-strategy.md) - Managing AWS costs
- 🔧 [**Troubleshooting Guide**](../cloudshelf-troubleshooting-guide.md) - Common issues and solutions

---

## 🎯 Success Criteria

### **✅ Phase 1 Monitoring Complete When...**

- [ ] **Lambda logs accessible** - Can view function execution logs
- [ ] **Basic alarms configured** - Error notifications set up
- [ ] **Dashboard created** - Simple overview of system health
- [ ] **Billing alerts active** - Cost monitoring enabled
- [ ] **Test scenarios work** - Can debug issues using logs and metrics

### **🔧 Troubleshooting Common Issues**

#### **Logs Not Appearing**

- Check log group names match function names
- Verify Lambda has CloudWatch Logs permissions
- Wait a few minutes for initial log creation

#### **Alarms Not Triggering**

- Verify metric names and namespaces
- Check alarm thresholds are appropriate
- Test with actual error conditions

#### **Dashboard Empty**

- Ensure metric names match your resources
- Check region settings in widget configuration
- Verify resources are generating metrics

---

**📊 Basic monitoring complete! Your Phase 1 CloudShelf now has essential observability.**

_📋 **Guide Status**: Phase 1 Complete | ✅ **Monitoring Level**: Basic | 🔄 **Last Updated**: Phase 1 Organization_  
_🎯 **Phase**: Basic Setup | 👥 **Audience**: Beginners | 📋 **Duration**: 15-20 minutes_
