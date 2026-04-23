# タスク分解テンプレート — 保険CRM

> spec-kit 用のテンプレートです。`/speckit.tasks` コマンドで使用されます。

## 機能: {feature_name}

### タスク一覧

| # | タスク | ファイル | 推定規模 | 依存 |
|---|--------|---------|---------|------|
| 1 | {task_description} | {file_path} | S/M/L | - |
| 2 | {task_description} | {file_path} | S/M/L | #1 |

### タスク詳細

#### Task 1: {task_name}
- **ファイル**: `{file_path}`
- **内容**: {detailed_description}
- **受け入れ条件**: {acceptance_criteria}

### 実装順序
```
Task 1 (Model) → Task 2 (Repository) → Task 3 (Service)
                                             ↓
                            Task 4 (Controller) → Task 5 (Template)
                                                       ↓
                                                  Task 6 (Test)
```
