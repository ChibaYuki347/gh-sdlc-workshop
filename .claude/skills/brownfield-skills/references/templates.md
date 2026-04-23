# Output Templates

Templates for generating brownfield developer Skills files.

## Table of Contents

- [SKILL.md Template](#skillmd-template)
- [architecture.md Template](#architecturemd-template)
- [tech-stack.md Template](#tech-stackmd-template)
- [coding-conventions.md Template](#coding-conventionsmd-template)
- [module-structure.md Template](#module-structuremd-template)
- [development-patterns.md Template](#development-patternsmd-template)

---

## SKILL.md Template

```markdown
---
name: brownfield-developer-[project-name]
description: >-
  Senior developer expertise for [PROJECT_NAME]. Provides deep understanding of 
  tech stack, architecture, coding conventions, and development patterns. 
  Use when working on [PROJECT_NAME] to ensure code consistency and quality.
---

# [PROJECT_NAME] Developer Skills

## Skill Overview

This skill empowers Claude Code as a **senior development engineer** for **[PROJECT_NAME]**.

## Key Skills

### 1. Tech Stack Mastery
- **Primary Language**: [language and version]
- **Core Frameworks**: [framework list]
- **Details**: See [references/tech-stack.md](references/tech-stack.md)

### 2. Architecture Understanding
- **Style**: [pattern]
- **Layering**: [description]
- **Details**: See [references/architecture.md](references/architecture.md)

### 3. Coding Conventions
- **Naming**: [main conventions]
- **Style**: [tools/config]
- **Details**: See [references/coding-conventions.md](references/coding-conventions.md)

### 4. Module Structure
- **Type**: [monolith/multi-module/microservices]
- **Count**: [N modules]
- **Details**: See [references/module-structure.md](references/module-structure.md)

### 5. Development Patterns
- **Patterns**: [common patterns]
- **Practices**: [key practices]
- **Details**: See [references/development-patterns.md](references/development-patterns.md)

## Brownfield Principles

1. **Respect existing architecture** - Follow, don't "improve"
2. **Code reuse first** - Search before creating
3. **Forward compatibility** - Don't break existing
4. **Refactor on-demand** - Only when necessary
5. **Style consistency** - Match existing code

## Reference Files

| File | Description |
|------|-------------|
| [architecture.md](references/architecture.md) | Architecture and layering |
| [tech-stack.md](references/tech-stack.md) | Tech stack and constraints |
| [coding-conventions.md](references/coding-conventions.md) | Coding standards |
| [module-structure.md](references/module-structure.md) | Module responsibilities |
| [development-patterns.md](references/development-patterns.md) | Development patterns |
```

---

## architecture.md Template

```markdown
# [PROJECT_NAME] Architecture

## Architecture Style

**Pattern**: [Layered/Onion/Hexagonal/Microservices/...]

## Layer Structure

### [Layer 1]
- **Responsibility**: [description]
- **Location**: [path]
- **Components**: [types]

### [Layer 2]
...

## Dependency Rules

### Allowed
- [Upper] → [Lower]

### Forbidden
- [Lower] → [Upper]
- Circular dependencies

## Extension Guidelines

### Adding Features
1. [Step]
2. [Step]

### Adding Modules
1. [Step]
2. [Step]
```

---

## tech-stack.md Template

```markdown
# [PROJECT_NAME] Tech Stack

## Core Stack (Locked)

| Category | Technology | Version | Notes |
|----------|------------|---------|-------|
| Language | [lang] | [ver] | |
| Runtime | [runtime] | [ver] | |
| Framework | [fw] | [ver] | |
| Build | [tool] | [ver] | |
| Test | [fw] | [ver] | |

## Dependencies

### Production
| Dependency | Version | Purpose |
|------------|---------|---------|

### Development
| Dependency | Version | Purpose |
|------------|---------|---------|

## Data Storage

| Type | Technology | Purpose |
|------|------------|---------|

## Infrastructure

| Category | Technology | Config |
|----------|------------|--------|
```

---

## coding-conventions.md Template

```markdown
# [PROJECT_NAME] Coding Conventions

## Naming

### Files
| Type | Convention | Example |
|------|------------|---------|

### Code
| Type | Convention | Example |
|------|------------|---------|

## Formatting

- **Tool**: [name]
- **Config**: [path]
- **Indentation**: [spaces/tabs]
- **Line length**: [max]
- **Quotes**: [single/double]

## Import Organization
1. [Group 1]
2. [Group 2]
3. [Group 3]

## Git Conventions

### Commit Format
[template]

### Branch Naming
- Feature: [format]
- Fix: [format]
```

---

## module-structure.md Template

```markdown
# [PROJECT_NAME] Module Structure

## Project Type
**Structure**: [Monolith/Multi-module/Monorepo]
**Modules**: [N]

## Module List

| Module | Path | Category | Responsibility |
|--------|------|----------|----------------|

## Dependencies

```
[dependency graph]
```

## Code Placement Guide

| Code Type | Module | Path |
|-----------|--------|------|
| REST endpoint | | |
| Service interface | | |
| Service impl | | |
| Entity | | |
| DTO/VO | | |
| Utility | | |
```

---

## development-patterns.md Template

```markdown
# [PROJECT_NAME] Development Patterns

## Design Patterns

| Pattern | Purpose | Example |
|---------|---------|---------|

## Error Handling

### Exception Hierarchy
```
BaseException
├── BusinessException
└── SystemException
```

### Error Response Format
[example]

## Logging

- **Framework**: [name]
- **Config**: [path]

## Testing

### Structure
[directory structure]

### Unit Test Pattern
[example]

### Integration Test Pattern
[example]
```
