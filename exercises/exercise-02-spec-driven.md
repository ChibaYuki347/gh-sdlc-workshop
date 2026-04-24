# 演習2: 仕様駆動開発 — ネイティブ機能で仕様からIssueまで

**対応セッション**: セッション3 - 仕様駆動開発（SDD）
**所要時間**: 約45分

---

## 🎯 演習のゴール

- GitHub ネイティブ機能（Copilot Chat + Prompt Files）で SDD を体験する
- 仕様 → 計画 → タスク分解 → Issue 作成の流れを実践する
- Copilot Cloud Agent にアサインし、自動実装の体験をする

---

## 📋 シナリオ

> **あなたのチームは保険CRMシステムの開発チームです。**
> 営業部門から以下の要望が届きました:
>
> 「保険契約の更新日が近づいている顧客を自動でリストアップして、営業担当者に通知できるようにしてほしい。更新漏れによる契約失効を防ぎたい。」

---

## 📝 演習手順

### Phase 1: 仕様を作成する（15分）

#### Step 1: Specify — 仕様の作成

Copilot Chat を開き、以下のいずれかの方法で仕様を作成します。

**方法A: Prompt File を使う場合**（推奨）

> VS Code の Copilot Chat で `/specify` と入力すると、
> `.github/prompts/specify.prompt.md` がスラッシュコマンドとして利用できます。

```
/specify

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

**方法B: 直接プロンプトで作成する場合**（Fallback）

```
@workspace このプロジェクトのアーキテクチャを踏まえて、
以下の要件の機能仕様書をMarkdownで作成してください。

概要、ユーザーストーリー、機能要件、API設計、
データモデル、エッジケース、受け入れ条件を含めてください。

要件: 保険契約の更新日が30日以内に迫っている顧客を
リストアップするREST APIを追加する。
営業担当者別のフィルタ機能も必要。
```

#### Step 2: チームでレビュー

- **ビジネスメンバー**: 要件・受け入れ条件が妥当か確認
- **ITメンバー**: 技術的な実現性・エッジケースを確認

必要に応じて出力内容を修正してください。

#### Step 3: ファイルに保存

Copilot Chat の出力を `Open in Editor` でファイルに保存:

```bash
# ディレクトリ作成
mkdir -p specs/renewal-notification

# Copilot Chat の出力を以下に保存
# specs/renewal-notification/spec.md
```

#### Step 4: Plan → Tasks

**Plan（実装計画）**:
```
/plan

specs/renewal-notification/spec.md を読んで実装計画を作成してください。
変更対象ファイル、実装ステップ、テスト戦略を含めてください。
```
> 出力を `specs/renewal-notification/plan.md` に保存

**Tasks（タスク分解）**:
```
/tasks

specs/renewal-notification/plan.md を読んでタスク一覧に分解してください。
各タスクは GitHub Issue として登録できる粒度にしてください。
```
> 出力を `specs/renewal-notification/tasks.md` に保存

#### Step 5: コミット

```bash
git add specs/renewal-notification/
git commit -m "docs: 契約更新通知機能の仕様・計画・タスクを追加"
git push  # push できない場合はコミットまででOK
```

### Phase 2: Issue 作成 → 実装（15分）

> ⚠️ **環境に応じた実施方法**:
> - **パスA（Cloud Agent 利用可能）**: Issue に Copilot をアサインし、自動実装を体験
> - **パスB（Cloud Agent 利用不可 / GitHub 操作不可）**: ローカルの Agent Mode で実装

#### Step 1: タスクを整理する

`specs/renewal-notification/tasks.md` から **1つ** のタスクを選び、以下の形式で整理します:

> 📋 **GitHub にアクセスできる場合**: 以下の内容で GitHub Issue を作成してください。
> **アクセスできない場合**: ローカルに `specs/renewal-notification/issue-draft.md` として保存してください。

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
- 詳細仕様: specs/renewal-notification/spec.md
```

#### Step 2: 実装

**パスA: Cloud Agent にアサイン（利用可能な場合）**

1. Issue の **Assignees** に `Copilot` を追加
2. Cloud Agent が自動で実行を開始

> ⏳ Agent の実行中は次の Phase に進みましょう

**パスB: ローカル Agent Mode で実装（Cloud Agent が利用不可の場合）**

仕様の内容を使って、VS Code の Copilot Agent Mode で実装します:

```
プロンプト:
「以下の仕様に基づいて実装してください。
まず計画を提示し、承認後に実装を開始してください。

[Issue / issue-draft.md の内容を貼り付け]」
```

> 💡 パスBでも仕様を Issue 形式で整理するステップは重要です。
> 「何を作るか」を明確に定義してから AI に渡すことで、精度の高い実装が得られます。

### Phase 3: 完了 PR の確認（15分）

#### 事前準備済みの PR を確認

講師が事前に用意した Cloud Agent の完了 PR を確認します:

```
確認チェックリスト:
□ PR のタイトルと説明は Issue の要件を反映しているか
□ 作成されたコードは仕様に準拠しているか
□ テストコードが含まれているか
□ Issue → Branch → PR のトレーサビリティが確認できるか
```

#### 自チームの成果物を確認

**パスA**: 自チームがアサインした Issue の Cloud Agent PR が完成していれば:

1. PR の内容を確認
2. 仕様との整合性をチームで検証
3. ビジネスメンバーが受け入れ条件をチェック

**パスB**: ローカルで実装した場合:

1. 実装結果をコミット・プッシュし、PR を作成（`Closes #XX` を含める）
2. 仕様との整合性をチームで検証
3. ビジネスメンバーが受け入れ条件をチェック

---

## ✅ 完了条件

- [ ] `specs/renewal-notification/spec.md`（仕様書）が作成されている
- [ ] `specs/renewal-notification/plan.md`（技術計画）が作成されている
- [ ] `specs/renewal-notification/tasks.md`（タスク一覧）が作成されている
- [ ] GitHub Issue が作成されている
- [ ] Cloud Agent PR または ローカル実装の PR を確認済み

---

## 💬 ディスカッションポイント

1. 仕様 → Issue → 実装 → PR のフローで、一番価値を感じたのはどこか？
2. AIに「任せられること」と「人間が判断すべきこと」の境界はどこか？
3. このフローを自社の開発プロセスに適用する場合、何が変わるか？
4. 仕様 → コード → マージのトレーサビリティは、監査・コンプライアンスにどう役立つか？
5. Spec Kit のようなツールを導入するメリット・デメリットは何か？
