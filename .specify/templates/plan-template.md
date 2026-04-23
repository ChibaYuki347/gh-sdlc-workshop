# 実装計画テンプレート — 保険CRM

> spec-kit 用のテンプレートです。`/speckit.plan` コマンドで使用されます。

## 機能: {feature_name}

### 実装アプローチ
{approach_description}

### フェーズ分割

#### Phase 1: データモデル＆リポジトリ
- [ ] エンティティクラスの作成/更新（`app/src/main/java/com/example/crm/model/`）
- [ ] リポジトリインターフェースの作成/更新（`app/src/main/java/com/example/crm/repository/`）
- [ ] テストデータの追加（`app/src/main/resources/data.sql`）

#### Phase 2: ビジネスロジック
- [ ] Service クラスの作成/更新（`app/src/main/java/com/example/crm/service/`）
- [ ] ユニットテストの作成（`app/src/test/java/com/example/crm/service/`）

#### Phase 3: API エンドポイント
- [ ] REST Controller の作成/更新（`app/src/main/java/com/example/crm/controller/`）
- [ ] API テストの作成

#### Phase 4: UI 画面
- [ ] Thymeleaf テンプレートの作成（`app/src/main/resources/templates/`）
- [ ] WebController のルート追加
- [ ] ナビゲーションへのリンク追加（`layout.html`）

#### Phase 5: 仕様書更新
- [ ] `specs/api-spec.md` の更新
- [ ] `specs/ui-spec.md` の更新

### 依存関係
- {dependency_notes}

### リスクと注意事項
- 既存 API との後方互換性を維持すること
- `pom.xml` への新規依存追加が必要な場合は明記すること
