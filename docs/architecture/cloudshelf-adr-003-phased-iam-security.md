# ADR-003: Phased IAM Security Strategy

**Status**: Accepted  
**Date**: 2025-09-06  
**Context**: CloudShelf Phase 1 and Phase 2 Implementation

---

## 📋 Summary

CloudShelf will implement a **phased IAM security approach** that balances learning objectives with security best practices, progressing from simplified AWS managed policies in Phase 1 to production-ready least-privilege custom policies in Phase 2.

---

## 🎯 Decision

### **Phase 1: Learning-Focused IAM (AWS Managed Policies)**

**Implementation**:

- Use AWS managed policies (`AWSLambdaBasicExecutionRole`, `AmazonDynamoDBFullAccess`)
- Single execution role for all Lambda functions
- Simplified permission model for rapid development

**Rationale**:

- ✅ **Faster learning** - Students focus on serverless architecture, not IAM complexity
- ✅ **AWS expertise** - Managed policies are maintained and proven by AWS
- ✅ **Reduced errors** - Less chance of permission misconfigurations blocking progress
- ✅ **Foundation building** - Establishes IAM concepts before advanced implementation

### **Phase 2: Production-Ready IAM (Custom Policies)**

**Implementation**:

- Custom IAM policies with least-privilege access
- Function-specific execution roles
- Resource-level permissions with conditions
- Cross-account roles for multi-environment deployment

**Rationale**:

- 🔒 **Security hardening** - Minimal permissions for production deployment
- 📊 **Compliance readiness** - Meets enterprise security requirements
- 🏢 **Scalability** - Supports complex organizational structures
- 🔍 **Auditability** - Clear permission boundaries for security reviews

---

## 🏛️ Architecture Comparison

### **Phase 1: Simplified Security Model**

```
┌─────────────────────────────────────────────────────────────────┐
│                   Phase 1 IAM Architecture                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  🔐 Single Execution Role                                      │
│  ├── AWSLambdaBasicExecutionRole                               │
│  └── AmazonDynamoDBFullAccess                                  │
│                                                                 │
│  ⚡ All Lambda Functions                                       │
│  ├── Book Catalog Lambda                                       │
│  ├── Shopping Cart Lambda                                      │
│  ├── User Management Lambda                                    │
│  └── Order Processing Lambda                                   │
│                                                                 │
│  📊 Permissions: Broad but functional                         │
│  🎯 Focus: Learning serverless patterns                       │
└─────────────────────────────────────────────────────────────────┘
```

### **Phase 2: Production Security Model**

```
┌─────────────────────────────────────────────────────────────────┐
│                   Phase 2 IAM Architecture                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  🔐 Function-Specific Roles                                   │
│  ├── BookCatalog-Role (DynamoDB books table read-only)         │
│  ├── ShoppingCart-Role (DynamoDB carts table read/write)       │
│  ├── UserMgmt-Role (DynamoDB users table + Cognito)           │
│  └── OrderProcess-Role (Multiple tables + SES)                 │
│                                                                 │
│  🏢 Environment-Specific Roles                                │
│  ├── Dev-Environment-Role                                      │
│  ├── Staging-Environment-Role                                  │
│  └── Prod-Environment-Role                                     │
│                                                                 │
│  📊 Permissions: Least-privilege, auditable                   │
│  🎯 Focus: Production security and compliance                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎓 Learning Progression

### **Phase 1 Learning Objectives**

**IAM Concepts Students Learn**:

- ✅ **Execution roles** - How Lambda functions get permissions
- ✅ **Trust policies** - Service-to-service authentication
- ✅ **AWS managed policies** - Leveraging AWS expertise
- ✅ **CloudWatch integration** - Logging permissions
- ✅ **Basic troubleshooting** - Permission error resolution

**Skills Developed**:

- Reading IAM policy JSON
- Attaching policies to roles
- Understanding service permissions
- Basic security troubleshooting

### **Phase 2 Learning Objectives**

**Advanced IAM Concepts**:

- 🔒 **Custom policy creation** - Writing least-privilege policies
- 📋 **Resource-level permissions** - Table and action-specific access
- 🏢 **Cross-account roles** - Multi-environment security
- 📊 **Condition-based policies** - Context-aware permissions
- 🔍 **Security auditing** - IAM Access Analyzer usage

**Skills Developed**:

- Enterprise IAM architecture
- Security compliance frameworks
- Advanced troubleshooting
- Policy optimization

---

## 💡 Alternatives Considered

### **Alternative 1: Least-Privilege from Start**

**Approach**: Begin with custom, minimal permissions in Phase 1

**Rejected Because**:

- 🚫 **Steep learning curve** - Students struggle with IAM complexity before understanding serverless
- 🚫 **Development friction** - Permission errors block learning of core concepts
- 🚫 **Premature optimization** - Security details overshadow architectural understanding
- 🚫 **Error-prone** - High chance of misconfiguration in learning environment

### **Alternative 2: Single Admin Role**

**Approach**: Use administrator access for simplicity

**Rejected Because**:

- 🚫 **Security anti-pattern** - Teaches bad security habits
- 🚫 **No learning value** - Students don't understand permission boundaries
- 🚫 **Production unusable** - Cannot migrate to real environments
- 🚫 **Compliance issues** - Fails enterprise security requirements

### **Alternative 3: Role per Function from Start**

**Approach**: Create separate roles for each function in Phase 1

**Rejected Because**:

- 🚫 **Complexity overload** - Too many IAM concepts before core learning
- 🚫 **Time consuming** - Setup time detracts from serverless learning
- 🚫 **Error amplification** - Multiple roles = multiple points of failure
- 🚫 **Premature complexity** - Advanced patterns before fundamentals

---

## 📊 Impact Analysis

### **Learning Impact**

| Aspect              | Phase 1 (Managed)   | Phase 2 (Custom)      |
| ------------------- | ------------------- | --------------------- |
| **Setup Time**      | 15 minutes          | 2-3 hours             |
| **Error Rate**      | Low                 | Medium                |
| **Learning Focus**  | Serverless patterns | Security architecture |
| **Troubleshooting** | Simple              | Advanced              |
| **Real-world Prep** | Basic understanding | Production-ready      |

### **Security Impact**

| Aspect              | Phase 1 Risk           | Phase 2 Mitigation    |
| ------------------- | ---------------------- | --------------------- |
| **Over-privileged** | Medium (learning only) | Low (least-privilege) |
| **Audit Trail**     | Basic                  | Comprehensive         |
| **Compliance**      | Not suitable           | Enterprise-ready      |
| **Attack Surface**  | Larger                 | Minimal               |

---

## 🔄 Migration Strategy

### **Phase 1 to Phase 2 Transition**

**Step 1: Audit Current Permissions**

- Review Phase 1 role usage
- Identify actual permissions needed
- Document access patterns

**Step 2: Create Custom Policies**

- Write least-privilege policies for each function
- Use IAM Access Analyzer recommendations
- Test policies in development environment

**Step 3: Gradual Migration**

- Create new roles with custom policies
- Update functions one at a time
- Monitor for permission errors

**Step 4: Cleanup**

- Remove old managed policy roles
- Verify no functions using old permissions
- Document new security model

---

## 🎯 Success Criteria

### **Phase 1 Success Metrics**

- ✅ **Learning completion** - Students finish Phase 1 in target time (3-4 hours)
- ✅ **Low error rate** - Minimal IAM-related setup issues
- ✅ **Understanding demonstration** - Students can explain role purposes
- ✅ **Working application** - All Lambda functions execute successfully

### **Phase 2 Success Metrics**

- 🔒 **Security compliance** - Passes enterprise security reviews
- 📊 **Minimal permissions** - Functions have only necessary access
- 🔍 **Audit readiness** - Clear permission documentation and trails
- ⚡ **Performance maintained** - No performance impact from security changes

---

## 📚 References

### **AWS Documentation**

- [IAM Best Practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)
- [Lambda Execution Roles](https://docs.aws.amazon.com/lambda/latest/dg/lambda-intro-execution-role.html)
- [AWS Managed Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-vs-inline.html)

### **Security Frameworks**

- [AWS Well-Architected Security Pillar](https://docs.aws.amazon.com/wellarchitected/latest/security-pillar/)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)
- [CIS AWS Foundations Benchmark](https://www.cisecurity.org/benchmark/amazon_web_services)

### **Learning Resources**

- [AWS IAM Workshop](https://identity-and-access-management-for-aws.workshop.aws/)
- [AWS Security Best Practices](https://aws.amazon.com/architecture/security-identity-compliance/)

---

## 🔄 Review Schedule

- **Quarterly Review**: Assess Phase 1 learning outcomes and error rates
- **Annual Review**: Evaluate security landscape changes and policy updates
- **Migration Assessment**: Monitor Phase 1 to Phase 2 transition success

---

_This ADR establishes the foundation for CloudShelf's progressive security approach, ensuring both effective learning and production readiness._
