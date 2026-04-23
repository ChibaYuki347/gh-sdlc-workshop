---
name: brownfield-ears
description: >-
  Convert natural language requirements into EARS (Easy Approach to Requirements Syntax) 
  format structured documents. Use when users need to clarify ambiguous requirements, 
  formalize feature requests, or prepare high-quality input for SDD workflow. 
  Triggers on "EARS", "requirements format", "convert requirements", "structure requirements".
---

# EARS Requirements Conversion

Convert natural language requirements into **EARS format** structured documents.

## What is EARS?

EARS (Easy Approach to Requirements Syntax) uses fixed sentence templates to:
- Eliminate ambiguity in requirements
- Ensure testability
- Improve AI comprehension accuracy

## Quick Start

1. Provide a natural language requirement description
2. This skill converts it to EARS format
3. Output is saved to `.docs/EARS/[NNN]-[feature-name].md`
4. Use the EARS document with `/speckit.specify` for better specs

## EARS Patterns

| Pattern | Template | Use Case |
|---------|----------|----------|
| **Ubiquitous** | `The system shall <function>` | Always-required functionality |
| **Event-Driven** | `When <trigger>, the system shall <response>` | Triggered actions |
| **State-Driven** | `If <state>, then the system shall <behavior>` | State-dependent behavior |
| **Optional** | `Where <condition>, the user can <operation>` | User-optional features |
| **Complex** | `When <trigger>, and <state>, the system shall <response>` | Multi-condition triggers |

## Output Location

```
[Project]/
├── .docs/EARS/              # EARS documents (NOT in .specify/)
│   ├── 001-user-auth.md
│   └── 002-payment.md
├── .specify/                # SDD config (separate)
└── specs/                   # Feature specs (separate)
```

## Workflow

```
[Natural Language] → /brownfield-ears → [EARS Doc] → /speckit.specify → [Spec]
```

## Conversion Process

1. **Parse input** - Extract actors, actions, conditions, data
2. **Categorize** - Map to EARS patterns
3. **Number** - Assign IDs: `[PREFIX]-[TYPE]-[SEQ]` (e.g., AUTH-EV-001)
4. **Generate** - Create structured document

## Reference

- [references/conversion-guide.md](references/conversion-guide.md) - Detailed conversion rules
- [references/document-template.md](references/document-template.md) - Output template
- [references/examples.md](references/examples.md) - Conversion examples
