# セッション2: 環境セットアップと GitHub Flow の基礎

**時間**: 09:45 - 10:30（45分）

---

## 🎯 セッションのゴール

- 開発環境が正しくセットアップされていることを確認する
- GitHub の基本操作（Issue、Branch、Pull Request）を体験する
- Copilot のカスタマイズ機能（copilot-instructions / Prompt Files）を確認する

---

## 📖 アジェンダ

### 1. 環境確認（10分）

#### 確認チェックリスト

各自、以下を確認してください。問題があればメンターに声をかけてください。

```
□ VS Code が起動できる
□ GitHub Copilot 拡張機能がインストールされている
□ GitHub Copilot Chat 拡張機能がインストールされている
□ Copilot アイコン（左下）が有効になっている
□ Java / Maven がインストールされている
□ リポジトリがクローンされている
```

#### アプリケーションのビルド確認

```bash
cd app
mvn clean compile
```

> **期待結果**: `BUILD SUCCESS` と表示される

#### アプリケーションの起動確認

```bash
mvn spring-boot:run
```

> **期待結果**: `Started CrmApplication` と表示され、アプリが起動する

ブラウザで以下にアクセスして動作を確認:

| URL | ページ |
|-----|--------|
| http://localhost:8080/ | ダッシュボード（顧客統計・クイック操作） |
| http://localhost:8080/customers | 顧客一覧（検索・ステータスバッジ付き） |
| http://localhost:8080/customers/1 | 顧客詳細（田中太郎さんの契約情報） |
| http://localhost:8080/premium-calculator | 保険料シミュレーション |
| http://localhost:8080/report | 月次レポート |

> 確認できたら `Ctrl+C` でアプリを停止してください。

### 2. GitHub Flow の基本操作（15分）

#### GitHub Flow — プラットフォーム開発の基盤

```
GitHub Flow はこの後体験する全ての自動化の基盤です:

  Issue（何をするか）
    ↓
  Branch（作業場所）
    ↓
  Commit（変更の記録）
    ↓
  Pull Request（レビュー依頼）
    ↓
  Review + CI（品質チェック）
    ↓
  Merge（本番統合）

→ Coding Agent もこの同じフローに従って自律的に動作します
```

#### Issue の作成

> 📋 **GitHub にアクセスできない場合**: Issue の内容をローカルの `docs/issues/` フォルダに
> Markdown ファイルとして保存してください。フローの理解が目的です。

1. GitHub リポジトリページを開く
2. **Issues** タブ → **New issue** をクリック
3. 以下の内容で Issue を作成:

```markdown
タイトル: [チームX] ワークショップ環境確認完了

## 確認結果
- [x] VS Code + Copilot 動作確認済み
- [x] Java / Maven ビルド確認済み
- [ ] Copilot Chat 動作確認済み

## チームメンバー
- メンバー1（役割）
- メンバー2（役割）
```

#### Branch の作成と Pull Request

```bash
# featureブランチを作成
git checkout -b feature/team-X-setup

# 変更をコミット
git add .
git commit -m "チームX: 環境確認完了"

# プッシュ（リポジトリアカウントで認証）
git push origin feature/team-X-setup
```

> 📋 **push できない場合**: ローカルブランチの作成とコミットまでを実施してください。
> ブランチ運用の概念は同じです。PR 作成は講師デモで確認します。

GitHub で Pull Request を作成し、他チームのメンバーを Reviewer に追加してください。

> 💡 この「Issue → Branch → PR → Review → Merge」がプラットフォーム開発の基本パターンです。
> セッション3では、このフローを **Coding Agent が自動で実行** します。

### 3. Spec Kit の構成を確認する（10分）

> 💡 **ここからが SDD 体験の第一歩です！**
> 本リポジトリには `specify init .` の出力が事前にコミットされています。
> ここでは Spec Kit が生成したファイル構成を確認し、SDD ワークフローの全体像を掴みます。

#### Spec Kit が生成するファイル構成

```
.specify/                          ← Spec Kit の設定・テンプレート
├── memory/constitution.md         ← プロジェクトの原則（カスタマイズ済み）
├── templates/                     ← 仕様書・計画・タスクのテンプレート
├── extensions/                    ← Git 連携などの拡張機能
└── workflows/                     ← SDD ワークフロー定義

.github/
├── agents/                        ← Spec Kit エージェント定義
│   └── speckit.*.agent.md         ← specify, plan, tasks 等のエージェント
└── prompts/
    ├── specify.prompt.md          ← 🏠 ワークショップ独自：仕様作成ガイド
    ├── plan.prompt.md             ← 🏠 ワークショップ独自：実装計画ガイド
    ├── tasks.prompt.md            ← 🏠 ワークショップ独自：タスク分解ガイド
    └── speckit.*.prompt.md        ← 🤖 Spec Kit 生成：SDD コマンド群
```

> 🏠 = このプロジェクト独自のプロンプト、🤖 = `specify init` が自動生成したもの

#### 自分のプロジェクトに Spec Kit を導入するには

新規プロジェクトで Spec Kit を導入する場合は、Git Flow の中で以下を実行します:

```bash
# ブランチを作成
git checkout -b feature/speckit-init

# Spec Kit を初期化（AI アシスタント選択で "copilot" を選ぶ）
specify init .

# 生成されたファイルをコミット＆プッシュ
git add .specify/ .github/agents/ .github/prompts/speckit.*.prompt.md
git commit -m "feat: Spec Kit SDD ワークフローを初期化"
git push origin feature/speckit-init
# → PR を作成してチームレビュー
```

> 💡 **ポイント**: ツールの初期化もコードと同じ Git Flow で管理する。
> これにより「誰が」「いつ」「何のために」変更したかが Pull Request に記録されます。

#### ⚠️ Spec Kit がインストールできない場合

Spec Kit が利用できない環境でも、ネイティブ機能で SDD は実践できます:

| 機能 | Spec Kit あり | ネイティブのみ |
|------|-------------|--------------|
| 仕様書作成 | `/speckit.specify` | Copilot Chat + `/specify` Prompt File |
| 実装計画 | `/speckit.plan` | Copilot Chat + `/plan` Prompt File |
| タスク分解 | `/speckit.tasks` | Copilot Chat + `/tasks` Prompt File |

### 4. Copilot カスタマイズ機能の確認と Chat 動作確認（10分）

#### GitHub ネイティブのカスタマイズ機能

このプロジェクトには、Copilot の動作をカスタマイズする以下のファイルが含まれています:

```
.github/
├── copilot-instructions.md        ← プロジェクトの文脈（Copilot が常に参照）
├── agents/
│   └── speckit.*.agent.md         ← Spec Kit エージェント定義（specify init 生成）
└── prompts/
    ├── specify.prompt.md          ← 🏠 仕様作成ガイド
    ├── plan.prompt.md             ← 🏠 実装計画ガイド
    ├── tasks.prompt.md            ← 🏠 タスク分解ガイド
    └── speckit.*.prompt.md        ← 🤖 Spec Kit SDD コマンド群（specify init 生成）
```

| ファイル | 役割 |
|---------|------|
| `copilot-instructions.md` | プロジェクト固有の技術スタック・規約・注意事項を Copilot に伝える |
| `.github/prompts/*.prompt.md` | Copilot Chat でスラッシュコマンドとして呼び出せる再利用可能なプロンプト |

> 💡 これらは **GitHub のネイティブ機能** です。追加ツールのインストールは不要です。

各ファイルの内容を VS Code で開いて確認してください:
```bash
# プロジェクトルートに戻る
cd ..

# copilot-instructions.md の内容を確認
cat .github/copilot-instructions.md

# Prompt Files の一覧を確認
ls .github/prompts/
```

#### Prompt File の動作確認

VS Code の Copilot Chat で `/` を入力し、以下のコマンドが表示されるか確認:
- `/specify` — 仕様書作成
- `/plan` — 実装計画作成
- `/tasks` — タスク分解

> ⚠️ **Prompt File が表示されない場合**: Copilot Chat 拡張機能のバージョンを確認してください。
> 表示されなくても問題ありません。セッション3では Fallback 手順（直接プロンプト入力）も用意しています。

#### Copilot Chat の動作確認

VS Code で Copilot Chat を開き、以下を試してください:

```
「このプロジェクトの構成を説明してください」
「CustomerService.java の主な機能を教えてください」
```

---

## ✅ 演習

→ [演習1: GitHub Flow演習](../exercises/exercise-01-github-flow.md)

---

## 💡 講師向けメモ

- 環境トラブルが多い想定。メンターは各チームを巡回
- Prompt File が Copilot Chat で認識されない場合は、VS Code の再起動を試す
- 「この GitHub Flow が自動化の基盤」というメッセージを繰り返す
- 時間が押している場合は PR 作成まで進めなくてもOK（Issue + Branch まで）
- **デュアルアカウント環境**: 詳細は [講師ガイド: デュアルアカウント環境の運用](instructor-guide-dual-account.md) を参照
