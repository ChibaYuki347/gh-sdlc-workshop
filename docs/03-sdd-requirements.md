# セッション3: 仕様駆動開発（SDD）— 仕様からコードへの自動開発ループ

**時間**: 10:45 - 12:00（75分）

---

## 🎯 セッションのゴール

- **仕様駆動開発（SDD）** が特定ツールではなく「方法論」であることを理解する
- GitHub のネイティブ機能（Copilot Chat・Prompt Files・Plan Mode）だけで SDD を実践する
- GitHub Issue に Copilot Coding Agent をアサインし、自律的に PR が作成される体験をする
- **仕様 → Issue → 自動実装 → PR** の一気通貫フローを理解する

---

## 📖 アジェンダ

### 1. 仕様駆動開発（SDD）とは何か（10分）

#### SDD = 方法論であり、特定ツールではない

```
仕様駆動開発（Spec-Driven Development）の本質:

  ① 「何を作るか」を仕様として明文化する     ← 人間の責任
  ② 仕様を実装計画に変換する                ← AI が支援
  ③ 計画をタスクに分解する                  ← AI が支援
  ④ タスクを実装する                       ← AI が自律実行
  ⑤ 仕様に照らしてレビューする              ← 人間 + AI が協働
```

> 💡 **重要**: SDD は Copilot Chat のネイティブ機能だけで実現可能です。
> 専用ツール（Spec Kit 等）は、この方法論を **標準化・高速化するアクセラレータ** です。

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

> 人間は「何を作るか」を決め、Coding Agent が「どう作るか」を実行する。
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
>
> ⚠️ **Coding Agent が参加者環境で利用できない場合**:
> このセクションは講師デモのみとなります。参加者は Step 3 の「完了した PR の確認」で
> Coding Agent が生成した成果物を確認する形で体験します。

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

### 3. ハンズオン: ネイティブ機能で SDD を実践する（30分）

#### SDD を支える GitHub ネイティブ機能

```
.github/
├── copilot-instructions.md   ← プロジェクトの文脈（Copilot が常に参照）
└── prompts/                   ← 再利用可能なプロンプトファイル
    ├── specify.prompt.md      ← 仕様作成ガイド
    ├── plan.prompt.md         ← 実装計画ガイド
    └── tasks.prompt.md        ← タスク分解ガイド
```

> これらは **GitHub ネイティブの機能** です。Copilot Chat で
> スラッシュコマンドとして利用できます（例: `/specify`）。

#### シナリオ

> 営業部門から「保険契約の更新日が近い顧客をリストアップして通知する機能がほしい」という要望

#### Step 1: Specify — 仕様の作成（10分）

Copilot Chat を開き、Prompt File を使って仕様を作成します:

**方法A: Prompt File を使う場合**
```
/specify

保険CRMアプリケーションに「契約更新通知機能」を追加したいです。

【背景】
- 保険契約には更新日（endDate）がある
- 更新日を過ぎると契約が失効し、営業担当者の手動確認では漏れが発生
- 月に数件の更新漏れによる失効が起きている

【要望】
- 更新日が30日以内の顧客をリストアップするAPI
- 営業担当者別にフィルタできる
- 対象リストを返すREST APIとして提供
```

**方法B: Prompt File が利用できない場合（Fallback）**
```
@workspace このプロジェクトのアーキテクチャを踏まえて、
以下の要件の機能仕様書をMarkdownで作成してください。

概要、ユーザーストーリー、機能要件、API設計、
データモデル、エッジケース、受け入れ条件を含めてください。

要件: 保険契約の更新日が30日以内に迫っている顧客を
リストアップするREST APIを追加する。
営業担当者別のフィルタ機能も必要。
```

> Copilot が仕様書を生成します。チームで内容を確認してください。
>
> **ビジネスメンバー**: 要件・受け入れ条件が妥当か確認
> **ITメンバー**: 技術的な実現性・エッジケースを確認

#### Step 2: 仕様書をファイルに保存（3分）

Copilot Chat の出力を `Open in Editor` でファイルに保存:

```
specs/renewal-notification/spec.md
```

> 💡 **なぜファイルに保存するか**: チャットで終わらせず **artifact として残す** ことで、
> 後から参照可能・レビュー可能・トレーサブルになります。これが SDD の本質です。

#### Step 3: Plan — 技術計画の作成（5分）

```
/plan

specs/renewal-notification/spec.md を読んで実装計画を作成してください。
変更対象ファイル、実装ステップ、テスト戦略を含めてください。
```

出力を `specs/renewal-notification/plan.md` に保存。

> **Fallback**: Copilot Chat の Agent Mode（または Plan Mode）を使い、
> 「この仕様を実装するための計画を立ててください」と依頼しても同様の結果が得られます。

#### Step 4: Tasks — タスク分解（5分）

```
/tasks

specs/renewal-notification/plan.md を読んでタスク一覧に分解してください。
各タスクは GitHub Issue として登録できる粒度にしてください。
```

出力を `specs/renewal-notification/tasks.md` に保存。

#### Step 5: 成果物をコミット（2分）

```bash
git add specs/renewal-notification/
git commit -m "docs: 契約更新通知機能の仕様・計画・タスクを追加"
git push
```

#### Step 6: チームでレビュー（5分）

```
確認ポイント:
□ 仕様書の要件は妥当か（ビジネスメンバーが判断）
□ 技術計画は実現可能か（ITメンバーが判断）
□ タスクの粒度は適切か
□ 受け入れ条件はテスト可能か
```

### 4. ハンズオン: Issue 作成 → 実装（15分）

> ⚠️ **環境に応じた実施方法**:
> - **パスA（Coding Agent 利用可能）**: Issue に Copilot をアサインし、自動実装を体験
> - **パスB（Coding Agent 利用不可 / GitHub 操作不可）**: ローカルの Agent Mode で実装

#### Step 1: タスクを仕様として整理する

`specs/renewal-notification/tasks.md` から **1つ** を選び、以下の形式で整理します:

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

#### Step 2: 実装の実行

**パスA: Coding Agent にアサイン（利用可能な場合）**

1. 作成した Issue の **Assignees** に `Copilot` を追加
2. Coding Agent が Issue を受け取り、自動的に作業を開始

> ⏳ Coding Agent の実行には数分かかります。
> 待っている間に、**事前準備済みの完了PR** を確認しましょう。

**パスB: ローカル Agent Mode で実装（Coding Agent が利用不可の場合）**

VS Code の Copilot Agent Mode を使って、仕様をベースに実装します:

```
プロンプト:
「以下の仕様に基づいて実装してください。

[Issue / issue-draft.md の内容を貼り付け]

まず実装計画を提示してから、承認後に実装してください。」
```

> 💡 パスBでも仕様を Issue 形式で整理するステップは重要です。
> 「何を作るか」を明確に定義してから AI に渡すことで、精度の高い実装が得られます。

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

### 5. デモ: Spec Kit によるSDD高速化（5分）

> 📺 **講師デモ**: Spec Kit を使うと同じ SDD フローがどう加速されるかを見せる

#### Spec Kit = SDD のアクセラレータ

```
Native SDD:                      Spec Kit:
───────────────                  ──────────
仕様作成（Prompt File）          /speckit.specify → .specify/spec.md
   ↓ 手動保存                       ↓ 自動生成・保存
計画作成（Prompt File）          /speckit.plan → .specify/plan.md
   ↓ 手動保存                       ↓ 自動生成・保存
タスク分解（Prompt File）        /speckit.tasks → .specify/tasks.md
   ↓ 手動Issue作成                   ↓ 自動生成・保存
Issue → Coding Agent             Issue → Coding Agent
```

#### 比較まとめ

| 観点 | Native SDD | Spec Kit |
|------|-----------|----------|
| **始めやすさ** | ◎ 追加ツール不要 | △ Python + uv + 初期化が必要 |
| **標準化** | △ チーム内で合意が必要 | ◎ フォーマットが統一される |
| **再現性** | △ プロンプト次第 | ◎ 毎回同じフローで実行 |
| **カスタマイズ性** | ◎ 自由にプロンプト設計可能 | △ Spec Kit の形式に依存 |
| **速度** | ○ | ◎ ワンコマンドで完了 |

> 💡 **結論**: SDD の本質は方法論です。
> Native 機能で基礎を理解した上で、チームの成熟度に応じて Spec Kit のような
> アクセラレータを導入すると効果的です。

### 6. まとめ: なぜこのフローが強力か（5分）

```
仕様駆動開発のフロー:

  仕様書（specs/...）         ← 人間が責任を持つ
       ↓
  Issue（要件の単位）          ← 追跡可能な作業チケット
       ↓
  Coding Agent               ← 自律的に実装
       ↓
  Pull Request               ← レビュー可能な変更セット
       ↓
  Review + Merge             ← 人間が品質を担保

★ 全てが GitHub 上で完結 → 監査ログ・承認フロー・ブランチ保護
★ 仕様 → コード → マージの完全なトレーサビリティ
★ これは単体AIツールでは実現できない
```

---

## ✅ 演習

→ [演習2: 仕様駆動開発演習](../exercises/exercise-02-spec-driven.md)

---

## 💡 講師向けメモ

- **事前準備が最重要**: Coding Agent による完了済み PR を必ず1つ以上用意しておく
- Agent の実行待ち時間は「完了済みPRの確認」や「コード理解」で埋める
- Prompt File（`.github/prompts/`）が参加者環境で利用可能か事前確認すること
  - 利用不可の場合は Fallback 手順（直接プロンプト入力）を案内
- Copilot Chat の出力は毎回異なる。参加者ごとに結果が違うことを事前に説明
- ビジネスメンバーには「仕様を書いたら自動で PR が上がってくる」体験の価値を強調
- **Coding Agent が利用できない環境の場合**:
  - デモは講師環境で実施、参加者はローカル Agent Mode で実装を体験
  - Issue 作成 → Agent Mode 実装 → PR 作成の流れでトレーサビリティは確保
  - 「Coding Agent が有効であればこの手動ステップが自動化される」ことを説明
- **Spec Kit デモは講師のみ実行**。参加者には Native フローをハンズオンさせる
