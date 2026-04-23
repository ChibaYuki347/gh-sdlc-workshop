---
name: brownfield-skills
description: >-
  Generate "Senior Developer Expert in This Project" Skills for brownfield (existing) projects. 
  Use when users ask to generate project skills, create developer expertise, analyze project 
  conventions, or help AI understand an existing codebase. Triggers on "generate skills", 
  "project skills", "developer expertise", "analyze conventions", "brownfield skills".
---

# Brownfield Developer Skills Generator

Generate Claude Code Project Skills for **brownfield (existing) projects**.

## What This Skill Does

Analyze an existing project and generate structured Skills files that enable AI to:

1. **Master the project** - Deep understanding of tech stack, architecture, and code style
2. **Follow conventions** - Work according to existing coding patterns
3. **Ensure compatibility** - Maintain backward compatibility when extending
4. **Reuse code first** - Prioritize existing implementations over new code

## Quick Start

1. Run this skill in your project root
2. Generated Skills are placed in `.claude/skills/brownfield-developer-[project-name]/`
3. Claude Code automatically loads these Skills

## Output Structure

```
.claude/skills/brownfield-developer-[project-name]/
├── SKILL.md                    # Entry point with project overview
└── references/
    ├── architecture.md         # Architecture and layering
    ├── tech-stack.md           # Tech stack and versions
    ├── coding-conventions.md   # Coding standards
    ├── module-structure.md     # Module responsibilities
    └── development-patterns.md # Development patterns
```

## Execution Process

### Phase 0: Deep Project Analysis

Analyze the project like a senior engineer taking over:

1. **Tech Stack Identification** - See [references/analysis-guide.md](references/analysis-guide.md#tech-stack)
2. **Architecture Recognition** - See [references/analysis-guide.md](references/analysis-guide.md#architecture)
3. **Conventions Extraction** - See [references/analysis-guide.md](references/analysis-guide.md#conventions)
4. **Module Analysis** - See [references/analysis-guide.md](references/analysis-guide.md#modules)
5. **Pattern Recognition** - See [references/analysis-guide.md](references/analysis-guide.md#patterns)

### Phase 1: Generate Skills Files

Transform analysis into structured Skills using templates in [references/templates.md](references/templates.md).

### Phase 2: Validation

Verify generated Skills:
- Directory structure is correct
- All 5 reference files exist
- SKILL.md entry is valid
- Reference links work

## Pre-Output Checklist

| Check | Correct | Wrong |
|-------|---------|-------|
| Skills root | `.claude/skills/` | `skills/` |
| Skill dir | `brownfield-developer-[name]` | Other names |
| Entry file | `SKILL.md` | `skill.md` |
| References | `references/` | `reference/` |

## Brownfield Principles

1. **Respect existing architecture** - Don't "improve", follow accurately
2. **Code reuse first** - Search existing code before creating new
3. **Forward compatibility** - New features must not break existing
4. **Refactor on-demand** - Only when necessary

## Reference Files

- [references/analysis-guide.md](references/analysis-guide.md) - Detailed analysis procedures
- [references/templates.md](references/templates.md) - Output file templates
- [references/templates-cn.md](references/templates-cn.md) - Chinese templates

## Integration

Works alongside Repomix:
1. Run `repomix --skill-generate` for code snapshots
2. Run this skill for development expertise
3. Together they provide complete project mastery
