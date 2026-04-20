# 演習2: Spec Kit + Coding Agent による仕様駆動開発

**対応セッション**: セッション3 - Spec Kit + Copilot Coding Agent
**所要時間**: 約45分

---

## 🎯 演習のゴール

- Spec Kit の Specify → Plan → Tasks フローを体験する
- GitHub Issue を作成し、Copilot Coding Agent にアサインする
- **仕様 → Issue → 自動実装 → PR** の一気通貫フローを体験する

---

## 📋 シナリオ

> **あなたのチームは保険CRMシステムの開発チームです。**
> 営業部門から以下の要望が届きました:
>
> 「保険契約の更新日が近づいている顧客を自動でリストアップして、営業担当者に通知できるようにしてほしい。更新漏れによる契約失効を防ぎたい。」

---

## 📝 演習手順

### Phase 1: Spec Kit で仕様を作成する（15分）

#### Step 1: Specify — 仕様の作成

Copilot Chat で `/speckit.specify` コマンドを実行:

```
/speckit.specify

保険CRMアプリケーションに「契約更新通知機能」を追加したいです。

【背景】
- 保険契約には更新日（endDate）がある
- 更新日を過ぎると契約が失効し、営業担当者の手動確認では漏れが発生
- 月に数件の更新漏れによる失効が起きている

【既存システム情報】
- Java/Spring Boot のWebアプリケーション
- 顧客(Customer)と保険契約(Policy)のテーブルがある
- REST API として提供

【要望】
- 更新日が30日以内の顧客をリストアップするAPI
- 営業担当者別にフィルタできる
- 対象リストを返すREST APIとして提供
```

> `.specify/spec.md` が生成されます

#### Step 2: チームでレビュー

- **ビジネスメンバー**: 要件・受け入れ条件が妥当か確認
- **ITメンバー**: 技術的な実現性・エッジケースを確認

必要に応じて `.specify/spec.md` を直接編集してください。

#### Step 3: Plan → Tasks

```
/speckit.plan
```
> `.specify/plan.md` が生成されます

```
/speckit.tasks
```
> `.specify/tasks.md` が生成されます

#### Step 4: コミット

```bash
git add .specify/
git commit -m "docs: 契約更新通知機能の仕様・計画・タスクを追加"
git push
```

### Phase 2: Issue 作成 → Coding Agent にアサイン（15分）

#### Step 1: タスクを Issue に登録

`.specify/tasks.md` から **1つ** のタスクを選び、GitHub Issue を作成:

```markdown
タイトル: feat: 契約更新通知APIの実装

## 概要
保険契約の更新日が指定日数以内に迫っている顧客をリストアップする
REST API エンドポイントを実装する。

## 仕様
- エンドポイント: GET /api/notifications/renewals
- パラメータ: daysBeforeExpiry (デフォルト: 30)
- ステータスが有効(ACTIVE)の契約のみ対象
- endDate が null の契約は除外
- 結果を更新日の昇順でソート

## 受け入れ条件
- [ ] エンドポイントが正しく動作する
- [ ] テストコードが含まれている
- [ ] 既存のコードに影響がない

## 技術情報
- 既存の Customer, Policy モデルと Repository を使用
- Spring Boot の規約に従う
- .specify/spec.md の仕様に準拠
```

#### Step 2: Coding Agent にアサイン

1. Issue の **Assignees** に `Copilot` を追加
2. Coding Agent が自動で実行を開始

> ⏳ Agent の実行中は次の Phase に進みましょう

### Phase 3: 完了 PR の確認（15分）

#### 事前準備済みの PR を確認

講師が事前に用意した Coding Agent の完了 PR を確認します:

```
確認チェックリスト:
□ PR のタイトルと説明は Issue の要件を反映しているか
□ 作成されたコードは仕様（.specify/spec.md）に準拠しているか
□ テストコードが含まれているか
□ Issue → Branch → PR のトレーサビリティが確認できるか
```

#### 自チームの Agent PR（完成していれば）

自チームがアサインした Issue の PR が完成していれば:

1. PR の内容を確認
2. 仕様との整合性をチームで検証
3. ビジネスメンバーが受け入れ条件をチェック

---

## ✅ 完了条件

- [ ] `.specify/spec.md`（仕様書）が作成されている
- [ ] `.specify/plan.md`（技術計画）が作成されている
- [ ] `.specify/tasks.md`（タスク一覧）が作成されている
- [ ] GitHub Issue が作成され、Copilot がアサインされている
- [ ] 事前準備済み or 自チームの Coding Agent PR を確認済み

---

## 💬 ディスカッションポイント

1. 仕様 → Issue → 自動実装 → PR のフローで、一番価値を感じたのはどこか？
2. Coding Agent に「任せられること」と「人間が判断すべきこと」の境界はどこか？
3. このフローを自社の開発プロセスに適用する場合、何が変わるか？
4. 仕様 → コード → マージのトレーサビリティは、監査・コンプライアンスにどう役立つか？
