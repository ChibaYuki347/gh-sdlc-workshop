# 输出模板

用于生成棕地项目开发者技能文件的模板。

## 目录

- [SKILL.md 模板](#skillmd-模板)
- [architecture.md 模板](#architecturemd-模板)
- [tech-stack.md 模板](#tech-stackmd-模板)
- [coding-conventions.md 模板](#coding-conventionsmd-模板)
- [module-structure.md 模板](#module-structuremd-模板)
- [development-patterns.md 模板](#development-patternsmd-模板)

---

## SKILL.md 模板

```markdown
---
name: brownfield-developer-[project-name]
description: >-
  [PROJECT_NAME] 高级开发专家技能。提供对技术栈、架构、编码规范和开发模式的深入理解。
  在 [PROJECT_NAME] 项目中工作时使用此技能，确保代码一致性和质量。
---

# [PROJECT_NAME] 开发者技能

## 技能概述

此技能使 Claude Code 成为 **[PROJECT_NAME]** 的**高级开发工程师**。

## 核心技能

### 1. 技术栈掌握
- **主要语言**: [语言和版本]
- **核心框架**: [框架列表]
- **详情**: 参见 [references/tech-stack.md](references/tech-stack.md)

### 2. 架构理解
- **风格**: [模式]
- **分层**: [描述]
- **详情**: 参见 [references/architecture.md](references/architecture.md)

### 3. 编码规范
- **命名**: [主要约定]
- **风格**: [工具/配置]
- **详情**: 参见 [references/coding-conventions.md](references/coding-conventions.md)

### 4. 模块结构
- **类型**: [单体/多模块/微服务]
- **数量**: [N 个模块]
- **详情**: 参见 [references/module-structure.md](references/module-structure.md)

### 5. 开发模式
- **模式**: [常用模式]
- **实践**: [关键实践]
- **详情**: 参见 [references/development-patterns.md](references/development-patterns.md)

## 棕地原则

1. **尊重现有架构** - 遵循，而非"改进"
2. **优先代码复用** - 先搜索再创建
3. **保持向前兼容** - 不破坏现有功能
4. **按需重构** - 仅在必要时进行
5. **风格一致性** - 匹配现有代码

## 参考文件

| 文件 | 描述 |
|------|------|
| [architecture.md](references/architecture.md) | 架构和分层 |
| [tech-stack.md](references/tech-stack.md) | 技术栈和约束 |
| [coding-conventions.md](references/coding-conventions.md) | 编码标准 |
| [module-structure.md](references/module-structure.md) | 模块职责 |
| [development-patterns.md](references/development-patterns.md) | 开发模式 |
```

---

## architecture.md 模板

```markdown
# [PROJECT_NAME] 架构

## 架构风格

**模式**: [分层/洋葱/六边形/微服务/...]

## 分层结构

### [层 1]
- **职责**: [描述]
- **位置**: [路径]
- **组件**: [类型]

### [层 2]
...

## 依赖规则

### 允许
- [上层] → [下层]

### 禁止
- [下层] → [上层]
- 循环依赖

## 扩展指南

### 添加功能
1. [步骤]
2. [步骤]

### 添加模块
1. [步骤]
2. [步骤]
```

---

## tech-stack.md 模板

```markdown
# [PROJECT_NAME] 技术栈

## 核心技术栈 (锁定)

| 类别 | 技术 | 版本 | 备注 |
|------|------|------|------|
| 语言 | [语言] | [版本] | |
| 运行时 | [运行时] | [版本] | |
| 框架 | [框架] | [版本] | |
| 构建 | [工具] | [版本] | |
| 测试 | [框架] | [版本] | |

## 依赖

### 生产环境
| 依赖 | 版本 | 用途 |
|------|------|------|

### 开发环境
| 依赖 | 版本 | 用途 |
|------|------|------|

## 数据存储

| 类型 | 技术 | 用途 |
|------|------|------|

## 基础设施

| 类别 | 技术 | 配置 |
|------|------|------|
```

---

## coding-conventions.md 模板

```markdown
# [PROJECT_NAME] 编码规范

## 命名

### 文件
| 类型 | 约定 | 示例 |
|------|------|------|

### 代码
| 类型 | 约定 | 示例 |
|------|------|------|

## 格式化

- **工具**: [名称]
- **配置**: [路径]
- **缩进**: [空格/制表符]
- **行长度**: [最大值]
- **引号**: [单引号/双引号]

## 导入组织
1. [分组 1]
2. [分组 2]
3. [分组 3]

## Git 约定

### 提交格式
[模板]

### 分支命名
- 功能: [格式]
- 修复: [格式]
```

---

## module-structure.md 模板

```markdown
# [PROJECT_NAME] 模块结构

## 项目类型
**结构**: [单体/多模块/Monorepo]
**模块数**: [N]

## 模块列表

| 模块 | 路径 | 类别 | 职责 |
|------|------|------|------|

## 依赖关系

```
[依赖关系图]
```

## 代码放置指南

| 代码类型 | 模块 | 路径 |
|----------|------|------|
| REST 端点 | | |
| Service 接口 | | |
| Service 实现 | | |
| Entity | | |
| DTO/VO | | |
| 工具类 | | |
```

---

## development-patterns.md 模板

```markdown
# [PROJECT_NAME] 开发模式

## 设计模式

| 模式 | 用途 | 示例 |
|------|------|------|

## 错误处理

### 异常层次
```
BaseException
├── BusinessException
└── SystemException
```

### 错误响应格式
[示例]

## 日志

- **框架**: [名称]
- **配置**: [路径]

## 测试

### 结构
[目录结构]

### 单元测试模式
[示例]

### 集成测试模式
[示例]
```
