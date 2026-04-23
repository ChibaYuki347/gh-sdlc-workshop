# 機能仕様書テンプレート — 保険CRM

> spec-kit 用のテンプレートです。`/speckit.specify` コマンドで使用されます。

## 機能名: {feature_name}

### 概要
{brief_description}

### ユーザーストーリー
```
As a {role},
I want {goal},
So that {benefit}.
```

### 機能要件

#### 必須要件（Must Have）
- [ ] {requirement_1}
- [ ] {requirement_2}

#### 推奨要件（Should Have）
- [ ] {requirement_1}

### API 設計

#### エンドポイント
```
{HTTP_METHOD} /api/{resource}
```

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| {param} | {type} | {required} | {description} |

#### レスポンス
```json
{response_example}
```

### UI 設計

#### 画面: {screen_name} (`{url}`)
- **目的**: {purpose}
- **表示要素**: {elements}
- **ユーザー操作**: {actions}

### データモデル

既存モデルとの関連:
```
Customer (1) --- (*) Policy
    │
    └── {new_relation}
```

### エッジケース

| # | ケース | 期待される動作 |
|---|--------|--------------|
| 1 | {edge_case} | {expected_behavior} |

### 受け入れ条件
- [ ] {acceptance_criteria_1}
- [ ] ユニットテストが作成され、全テストがパスする
- [ ] API の動作確認が完了している
- [ ] `specs/api-spec.md` が更新されている
- [ ] `specs/ui-spec.md` が更新されている

### 変更対象ファイル
- `app/src/main/java/com/example/crm/controller/{file}` - {change}
- `app/src/main/java/com/example/crm/service/{file}` - {change}
- `app/src/main/resources/templates/{file}` - {change}
