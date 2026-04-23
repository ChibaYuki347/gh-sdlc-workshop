# Analysis Guide

Detailed procedures for analyzing brownfield projects.

## Table of Contents

- [Tech Stack Identification](#tech-stack)
- [Architecture Recognition](#architecture)
- [Conventions Extraction](#conventions)
- [Module Analysis](#modules)
- [Pattern Recognition](#patterns)

---

## Tech Stack

### Primary Programming Languages

1. Identify file extension distribution (.java, .ts, .py, .go, .rs, etc.)
2. Determine language version constraints (package.json engines, pom.xml properties)
3. Identify if multi-language project

### Frameworks and Runtimes

| Category | Examples |
|----------|----------|
| Web frameworks | Spring Boot, Express, Django, Gin, Actix |
| Frontend frameworks | React, Vue, Angular, Svelte |
| Runtimes | Node.js, JVM, Python, Go |

### Build Tools and Package Managers

- Build: Maven, Gradle, npm/yarn/pnpm, cargo, go build
- Dependencies: Config files and lock files

### Testing Frameworks

| Type | Examples |
|------|----------|
| Unit | JUnit, Jest, pytest, go test |
| Integration | Testcontainers, Supertest |
| E2E | Playwright, Cypress, Selenium |

### Data Storage

- Databases: MySQL, PostgreSQL, MongoDB, Redis
- ORM/ODM: JPA, Prisma, SQLAlchemy, GORM

### Infrastructure

- Containerization: Docker, docker-compose
- CI/CD: GitHub Actions, GitLab CI, Jenkins
- Cloud: AWS, GCP, Azure

---

## Architecture

### Pattern Recognition

| Pattern | Recognition Features | Key Directories |
|---------|---------------------|-----------------|
| Layered | controller/service/repository | src/main/java/.../controller |
| Onion | domain/application/infrastructure | src/domain, src/infrastructure |
| Hexagonal | ports/adapters | src/adapters, src/ports |
| Microservices | Independent service directories | services/, apps/ |
| Monorepo | Multiple packages coexist | packages/, apps/, libs/ |
| Modular Monolith | Feature-based modules | modules/, features/ |

### Code Organization

- **Layer-first**: controllers/, services/, models/
- **Feature-first**: users/, orders/, payments/
- **Hybrid**: Identify primary and secondary organization

---

## Conventions

### Naming Conventions

| Type | Options |
|------|---------|
| File naming | kebab-case, PascalCase, snake_case |
| Variables | camelCase, snake_case |
| Classes | PascalCase, I-prefix for interfaces |
| Constants | UPPER_SNAKE_CASE |

### Code Style

Extract from config files:
- Indentation (spaces/tabs, count)
- Quotes (single/double)
- Semicolons (yes/no)
- Line length limit
- Import organization

### Comment Standards

- Doc format: JSDoc, Javadoc, docstring
- Inline style
- TODO/FIXME conventions

### Git Conventions

- Commit format: Conventional Commits or custom
- Branch naming strategy
- PR/MR templates

---

## Modules

### For Multi-Module Projects

1. **Enumerate all modules**:

| Module | Path | Type | Responsibility |
|--------|------|------|----------------|
| [name] | [path] | api/service/dao/common | [description] |

2. **Analyze responsibilities**:
   - API/Interface layer
   - Service implementation layer
   - Data access layer
   - Common utilities layer

3. **Map dependencies**:
   - Dependency graph
   - Allowed/forbidden directions
   - Cross-module communication

---

## Patterns

### Design Patterns to Identify

- Repository Pattern
- Service Layer Pattern
- Factory Pattern
- Strategy Pattern

### Code Organization Patterns

- DTO/VO/Entity separation
- Interface/implementation separation
- Configuration externalization
- Dependency injection

### Error Handling

- Exception hierarchy
- Unified error response format
- Error code standards

### Logging and Observability

- Logging framework and format
- Distributed tracing
- Metrics collection
