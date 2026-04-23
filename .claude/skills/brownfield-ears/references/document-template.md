# EARS Document Template

```markdown
# EARS Requirement Document: [Feature Name]

**Document Number**: [NNN]
**Created**: [YYYY-MM-DD]
**Status**: Draft | Confirmed | Converted
**Original Requirement**: 
> [User's original requirement description]

---

## 1. Requirement Overview

### 1.1 Background
[Feature background and business value]

### 1.2 Objectives
[Main objectives]

### 1.3 Scope
- **In Scope**: [Included]
- **Out of Scope**: [Excluded]

---

## 2. Actors

| Actor | Type | Description |
|-------|------|-------------|
| [Actor 1] | Primary | [Description] |
| [Actor 2] | Secondary | [Description] |
| System | System | [Role] |

---

## 3. EARS Requirements

### 3.1 Ubiquitous Requirements

| ID | Description | Verification |
|----|-------------|--------------|
| [XX-UB-001] | The system shall [function] | [Method] |

### 3.2 Event-Driven Requirements

| ID | Trigger | Response | Verification |
|----|---------|----------|--------------|
| [XX-EV-001] | When [trigger] | shall [response] | [Method] |

### 3.3 State-Driven Requirements

| ID | State | Behavior | Verification |
|----|-------|----------|--------------|
| [XX-ST-001] | If [state] | shall [behavior] | [Method] |

### 3.4 Optional Requirements

| ID | Condition | Operation | Verification |
|----|-----------|-----------|--------------|
| [XX-OP-001] | Where [cond] | can [operation] | [Method] |

### 3.5 Complex Requirements

| ID | Trigger | State | Response | Verification |
|----|---------|-------|----------|--------------|
| [XX-CX-001] | When [trigger] | And [state] | shall [response] | [Method] |

---

## 4. Business Rules

| Rule ID | Description | Requirements |
|---------|-------------|--------------|
| BR-001 | [Rule] | [IDs] |

---

## 5. Data Requirements

### Input Data

| Data | Type | Required | Constraints |
|------|------|----------|-------------|

### Output Data

| Data | Type | Notes |
|------|------|-------|

---

## 6. Non-Functional Requirements

### Performance
- [Requirements]

### Security
- [Requirements]

### Usability
- [Requirements]

---

## 7. Items to Clarify

| ID | Question | Impact | Options |
|----|----------|--------|---------|
| Q1 | [Question] | [Reqs] | A: / B: |

---

## 8. Revision History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | [Date] | Initial | AI |

---

## Next Steps

**If modifications needed**: Describe adjustments.

**If confirmed**: Continue with:
```bash
/speckit.specify .docs/EARS/[document-name]
```
```
