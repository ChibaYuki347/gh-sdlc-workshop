# セッション3: Spec Kit + Copilot Coding Agent — 仕様駆動の自動開発ループ

**時間**: 10:45 - 12:00（75分）

---

## 🎯 セッションのゴール

- Spec Kit の 4 フェーズ（Specify → Plan → Tasks → Implement）を体験する
- GitHub Issue に Copilot Coding Agent をアサインし、自律的に PR が作成される体験をする
- **仕様 → Issue → 自動実装 → PR** の一気通貫フローを理解する

---

## 📖 アジェンダ

### 1. Spec Kit + Coding Agent の概要（10分）

#### プラットフォームが実現する自動開発ループ

```
人間の作業                    GitHub Platform の自動化
─────────────────────         ──────────────────────────────
① 仕様を書く（Specify）
② 技術計画を立てる（Plan）
③ タスクに分解する（Tasks）
④ Issue を作成する
                              ⑤ Coding Agent が Issue を受け取る
                              ⑥ ブランチを自動作成
                              ⑦ コードを実装
                              ⑧ テストを作成・実行
                              ⑨ PR を自動作成
⑩ PR をレビュー・承認
                              ⑪ マージ
```

> 💡 **ポイント**: 人間は「何を作るか」を決め、Coding Agent が「どう作るか」を実行する。
> このループが GitHub Platform 上で完結するため、トレーサビリティが保証される。

#### Copilot Coding Agent とは

| 項目 | 説明 |
|------|------|
| **何ができるか** | GitHub Issue をアサインすると、自律的にブランチ作成・実装・PR作成 |
| **動作環境** | GitHub クラウド上（ローカル環境不要） |
| **対応範囲** | 新機能追加、バグ修正、リファクタリング、テスト作成 |
| **人間の役割** | Issue の記述（仕様）、PR のレビュー・承認 |

### 2. デモ: Coding Agent の動作確認（10分）

> 📺 **講師デモ**: 事前に準備した Issue → Coding Agent → PR の3段階を見せる

#### 事前準備された3つの状態

| 状態 | Issue | Coding Agent | PR |
|------|-------|-------------|-----|
| **完了済み** | ✅ クローズ | ✅ 実装済み | ✅ PR がオープン（レビュー待ち） |
| **実行中** | ✅ アサイン済み | 🔄 実装中 | ⏳ まだ作成されていない |
| **未着手** | ✅ 作成済み | ⏳ 待機中 | − |

**完了済みの PR を見てみましょう**:
- Coding Agent が作成したブランチ
- 自動生成されたコード（diff を確認）
- PR の説明文（Agent が自動記述）
- Issue との関連付け（トレーサビリティ）

### 3. ハンズオン: Spec Kit で仕様を作成する（25分）

#### シナリオ

> 営業部門から「保険契約の更新日が近い顧客をリストアップして通知する機能がほしい」という要望

#### Step 1: Specify — 仕様の作成（10分）

Copilot Chat で `/speckit.specify` コマンドを実行:

```
/speckit.specify

保険CRMアプリケーションに「契約更新通知機能」を追加したいです。

【背景】
- 保険契約には更新日（endDate）がある
- 更新日を過ぎると契約が失効し、営業担当者の手動確認では漏れが発生
- 月に数件の更新漏れによる失効が起きている

【要望】
- 更新日が30日以内の顧客をリストアップするAPI
- 営業担当者別にフィルタできる
- 対象リストを返すREST APIとして提供

【既存システム】
- Java/Spring Boot、Customer/Policyテーブル、REST API
```

> `.specify/spec.md` が生成されます。チームで内容を確認してください。
>
> **ビジネスメンバー**: 要件・受け入れ条件が妥当か確認
> **ITメンバー**: 技術的な実現性・エッジケースを確認

#### Step 2: Plan — 技術計画（5分）

```
/speckit.plan
```

> `.specify/plan.md` が生成されます。アーキテクチャと設計方針を確認。

#### Step 3: Tasks — タスク分解（5分）

```
/speckit.tasks
```

> `.specify/tasks.md` が生成されます。実装タスクの一覧を確認。

#### Step 4: 成果物をコミット（5分）

```bash
git add .specify/
git commit -m "docs: 契約更新通知機能の仕様・計画・タスクを追加"
git push
```

### 4. ハンズオン: Issue 作成 → Coding Agent にアサイン（20分）

#### Step 1: タスクを GitHub Issue に登録

`.specify/tasks.md` のタスクから **1つ** を選んで GitHub Issue を作成:

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

1. 作成した Issue の **Assignees** に `Copilot` を追加
2. Coding Agent が Issue を受け取り、自動的に作業を開始

```
Coding Agent の動作フロー:
  1. Issue の内容を読み取り
  2. リポジトリのコードを分析
  3. 新しいブランチを作成
  4. コードを実装（+ テスト）
  5. PR を作成（Issue への参照付き）
```

> ⏳ Coding Agent の実行には数分かかります。
> 待っている間に、**事前準備済みの完了PR** を確認しましょう。

#### Step 3: 完了した PR を確認する

事前に講師が準備した Coding Agent の PR を確認:

```
確認ポイント:
□ PR のタイトルと説明（Issue との関連）
□ 作成されたコードの品質
□ テストの有無
□ Issue → Branch → PR のトレーサビリティ
```

> 💡 **トレーサビリティの確認**:
> Issue 画面から「この Issue に紐づく PR」が追跡でき、
> PR 画面から「この PR がどの Issue の要件に対応しているか」が確認できます。
> これが Enterprise における監査対応・コンプライアンスの基盤です。

### 5. まとめ: なぜこのフローが強力か（10分）

```
Spec Kit + Coding Agent のフロー:

  仕様書（.specify/spec.md）← 人間が責任を持つ
       ↓
  Issue（要件の単位）      ← 追跡可能な作業チケット
       ↓
  Coding Agent            ← 自律的に実装
       ↓
  Pull Request            ← レビュー可能な変更セット
       ↓
  Review + Merge          ← 人間が品質を担保

★ 全てが GitHub 上で完結 → 監査ログ・承認フロー・ブランチ保護
★ 仕様 → コード → マージの完全なトレーサビリティ
★ これは単体AIツールでは実現できない
```

---

## ✅ 演習

→ [演習2: 仕様駆動開発演習（Spec Kit + Coding Agent）](../exercises/exercise-02-spec-driven.md)

---

## 💡 講師向けメモ

- **事前準備が最重要**: Coding Agent による完了済み PR を必ず1つ以上用意しておく
- Agent の実行待ち時間は「完了済みPRの確認」や「コード理解」で埋める
- `/speckit.specify` の出力は毎回異なる。参加者ごとに結果が違うことを事前に説明
- ビジネスメンバーには「仕様を書いたら自動で PR が上がってくる」体験の価値を強調
- Coding Agent が利用できない環境の場合は、Agent Mode（IDE内）でのデモに切り替え
