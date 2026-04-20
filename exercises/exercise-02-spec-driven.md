# 演習2: 仕様駆動開発（SDD）演習 — Spec Kit を使う

**対応セッション**: セッション3 - AIを活用した要件定義と仕様駆動開発
**所要時間**: 約45分

---

## 🎯 演習のゴール

- GitHub Spec Kit の 4 フェーズ（Specify → Plan → Tasks → Implement）を体験する
- ビジネスメンバーとITメンバーが協働して仕様を作成・レビューする
- 仕様書・技術計画・タスク一覧がリポジトリで管理される流れを理解する

---

## 📋 シナリオ

> **あなたのチームは保険CRMシステムの開発チームです。**
> 営業部門から以下の要望が届きました:
>
> 「保険契約の更新日が近づいている顧客を自動でリストアップして、営業担当者に通知できるようにしてほしい。更新漏れによる契約失効を防ぎたい。」

---

## 📝 演習手順

### Phase 1: Spec Kit の初期化（5分）

#### Spec Kit をプロジェクトにセットアップ

```bash
# Spec Kit の初期化
npx @github/specify init
```

初期化が完了すると、以下のディレクトリが生成されます:

```
.specify/              ← 仕様・計画・タスクの格納先
.github/
├── prompts/           ← Copilot Chat スラッシュコマンド
└── copilot-instructions.md  ← プロジェクトのコンテキスト
```

> ⚠️ 初期化がうまくいかない場合は、手動で `.specify/` ディレクトリを作成してください:
> ```bash
> mkdir -p .specify
> ```

### Phase 2: Specify — 仕様の作成（15分）

**全員で実施**

VS Code の Copilot Chat で `/speckit.specify` コマンドを実行:

```
/speckit.specify

保険CRMアプリケーションに「契約更新通知機能」を追加したいです。

【背景】
- 保険契約には更新日（endDate）がある
- 更新日を過ぎると契約が失効する
- 現在は営業担当者が手動で更新日を確認している
- 更新漏れによる失効が月に数件発生している

【既存システム情報】
- Java/Spring Boot のWebアプリケーション
- 顧客(Customer)と保険契約(Policy)のテーブルがある
- REST API として提供

【要望】
- 更新日が30日以内の顧客をリストアップするAPI
- 営業担当者別にフィルタできる機能
- 通知対象の一覧を CSV でエクスポートできると嬉しい
```

> Spec Kit が `.specify/spec.md` に構造化された仕様書を生成します

#### チームで仕様書をレビュー

生成された `.specify/spec.md` を開いて、チーム内で以下を確認:

- **ビジネスメンバー**: 要件が正確か？ビジネス価値が反映されているか？受け入れ条件は十分か？
- **ITメンバー**: 技術的に実現可能か？既存アーキテクチャとの整合性は？エッジケースは網羅されているか？

必要に応じて仕様書を直接編集してください。

### Phase 3: Plan — 技術計画の立案（10分）

仕様書が確定したら、Copilot Chat で技術計画を作成:

```
/speckit.plan
```

> Spec Kit が `.specify/plan.md` に技術計画を生成します

#### 計画の確認ポイント

```
□ 使用する技術スタック（Spring Boot + JPA で既存と整合するか）
□ データモデルの設計（Customer / Policy との関連）
□ API の設計方針（RESTful、エラーハンドリング）
□ パフォーマンスの考慮（大量データへの対応）
□ セキュリティの考慮（認証・認可）
```

### Phase 4: Tasks — タスク分解（10分）

技術計画が確定したら、実装タスクに分解:

```
/speckit.tasks
```

> Spec Kit が `.specify/tasks.md` にタスク一覧を生成します

#### タスクを GitHub Issues に登録

生成されたタスクを確認し、GitHub Issues として登録してください:

1. GitHub リポジトリで **Issues** タブを開く
2. 各タスクを Issue として作成
3. ラベル（`feat`, `refactor`, `test` 等）を付与
4. チームメンバーにアサイン

### Phase 5: 成果物のコミット（5分）

```bash
# Spec Kit の成果物をコミット
git add .specify/ .github/
git commit -m "docs: 契約更新通知機能の仕様・計画・タスクを追加（Spec Kit）"
git push origin feature/team-X-renewal-notification
```

---

## ✅ 完了条件

- [ ] Spec Kit が初期化されている（`.specify/` ディレクトリが存在）
- [ ] `.specify/spec.md`（仕様書）が作成されている
- [ ] `.specify/plan.md`（技術計画）が作成されている
- [ ] `.specify/tasks.md`（タスク一覧）が作成されている
- [ ] 実装タスクが GitHub Issues に登録されている
- [ ] 成果物がコミットされている

---

## 💬 ディスカッションポイント

演習後、チーム内で以下を議論してください:

1. Spec Kit の Specify → Plan → Tasks の流れで、何が一番有用だと感じたか？
2. ビジネスメンバーとITメンバーの役割分担は適切だったか？
3. 仕様書がリポジトリで管理されるメリットは何か？
4. この仕様書があれば、実装の手戻りはどの程度防げそうか？
