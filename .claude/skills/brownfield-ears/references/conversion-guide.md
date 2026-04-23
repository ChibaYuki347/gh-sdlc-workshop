# EARS Conversion Guide

Detailed procedures for converting natural language to EARS format.

## Step 1: Requirement Decomposition

Extract these elements from user description:

### Actor Identification
- **Primary User**: Who is using this feature?
- **Secondary User**: Who else is affected?
- **System Role**: What role does the system play?

### Action Identification
- **User Actions**: What operations will users perform?
- **System Responses**: How should the system respond?
- **Post-Actions**: What happens after completion?

### Condition Identification
- **Preconditions**: Under what circumstances can this be executed?
- **Trigger Conditions**: What events trigger this feature?
- **Exception Conditions**: Under what circumstances will it fail?

### Data Identification
- **Input Data**: What inputs are needed?
- **Output Data**: What outputs are produced?
- **State Changes**: What data will be modified?

---

## Step 2: Pattern Mapping

### Main Flow → Event-Driven (EV)
User's core operation flow.

Format: `When <user performs action>, the system shall <response>`

### Preconditions → State-Driven (ST)
Prerequisite states for feature availability.

Format: `If <condition>, then the system shall <allow/execute>`

### Exception Handling → Complex (CX)
Handling of exceptional situations.

Format: `When <action>, and <exception>, the system shall <error handling>`

### Universal Constraints → Ubiquitous (UB)
Constraints the system must always satisfy.

Format: `The system shall <constraint>`

### Optional Features → Optional (OP)
Features users can choose to use.

Format: `Where <condition>, the user can <operation>`

---

## Step 3: Requirement Numbering

```
[Feature Prefix]-[Type]-[Sequence]
```

| Component | Description | Example |
|-----------|-------------|---------|
| Prefix | 2-4 uppercase letters | AUTH, PAY |
| Type | Pattern code | EV, ST, CX, UB, OP |
| Sequence | Three digits | 001, 002 |

Examples: `AUTH-EV-001`, `PAY-ST-002`

---

## Writing Best Practices

1. **One requirement, one thing** - Each describes only one functional point
2. **Testability** - Each must be verifiable
3. **No ambiguity** - Avoid "maybe", "generally", "etc."
4. **Active voice** - Use "shall" not "should"
5. **Specific values** - Replace "fast" with actual numbers

---

## Mapping to Spec Sections

| EARS Content | Spec Section |
|--------------|--------------|
| Event-Driven | User Scenarios |
| State-Driven | Edge Cases |
| Business Rules | Functional Requirements |
| Data Requirements | Key Entities |
| Non-Functional | Success Criteria |
