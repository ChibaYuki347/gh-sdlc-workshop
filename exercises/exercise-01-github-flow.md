# 演習1: GitHub Flow 演習

**対応セッション**: セッション2 - 環境セットアップとGitHubプラットフォームの基礎
**所要時間**: 約15分

---

## 🎯 演習のゴール

- GitHub Flow（Branch → Commit → PR → Review → Merge）の一連の流れを体験する
- チームでの Issue 管理の基本を理解する

---

## 📝 演習手順

### Step 1: Issue を作成する（5分）

1. GitHub のリポジトリページで **Issues** タブを開く
2. **New issue** をクリック
3. 以下の内容で Issue を作成:

```markdown
タイトル: [チームX] 環境セットアップ確認

## 概要
ワークショップ環境のセットアップが完了したことを確認する。

## 確認項目
- [ ] VS Code + Copilot が動作する
- [ ] Java / Maven のビルドが成功する
- [ ] Git の push / pull ができる
- [ ] Copilot Chat が応答する

## チームメンバー
- @メンバー1
- @メンバー2
- @メンバー3
- @メンバー4
```

4. **Labels** に `setup` を追加
5. **Assignees** にチーム代表者を追加

### Step 2: ブランチを作成して変更する（5分）

```bash
# 1. mainブランチが最新であることを確認
git checkout main
git pull origin main

# 2. チーム用のブランチを作成
#    Xをチーム番号に置き換えてください
git checkout -b feature/team-X-setup-confirmation

# 3. チームの確認結果ファイルを作成
mkdir -p teams
```

`teams/team-X.md` を作成:

```markdown
# チームX セットアップ確認結果

## メンバー
| 名前 | 役割 | 環境確認 |
|------|------|---------|
| メンバー1 | ビジネス | ✅ |
| メンバー2 | IT/開発 | ✅ |
| メンバー3 | IT/インフラ | ✅ |
| メンバー4 | IT/開発 | ✅ |

## 確認結果
- VS Code + Copilot: OK
- Java / Maven: OK
- Git: OK
- Copilot Chat: OK

## 備考
（気づいた点があれば記載）
```

```bash
# 4. 変更をコミット
git add teams/team-X.md
git commit -m "docs: チームXのセットアップ確認結果を追加 #1"

# 5. プッシュ
git push origin feature/team-X-setup-confirmation
```

> 💡 コミットメッセージに `#1` を含めると、Issue #1 に自動リンクされます

### Step 3: Pull Request を作成する（5分）

1. GitHub でリポジトリを開く
2. 「Compare & pull request」ボタンをクリック（またはPull requestsタブから新規作成）
3. 以下を設定:
   - **base**: `main` ← **compare**: `feature/team-X-setup-confirmation`
   - **タイトル**: `docs: チームX セットアップ確認完了`
   - **説明**:
     ```markdown
     ## 変更内容
     チームXのワークショップ環境セットアップ確認結果を追加しました。

     ## 関連Issue
     Closes #X（対応するIssue番号）

     ## 確認項目
     - [x] 全メンバーの環境確認完了
     - [x] Copilot 動作確認完了
     ```
4. **Reviewers** に他チームのメンバーを追加
5. **Create pull request** をクリック

---

## ✅ 完了条件

- [ ] Issue が作成されている
- [ ] ブランチが作成され、変更がプッシュされている
- [ ] Pull Request が作成されている
- [ ] 他チームからのレビューコメントがある（任意）

---

## 📝 追加演習: Spec Kit のファイル構成を理解する

> 💡 本リポジトリには `specify init .` の出力が事前にコミットされています。
> この演習では、Spec Kit が生成したファイルの役割を理解します。

### Step 1: Spec Kit の構成を確認

```bash
# Spec Kit の設定ディレクトリ
find .specify -type f

# Spec Kit が生成したエージェント定義
ls .github/agents/

# Spec Kit が生成したプロンプトファイル
ls .github/prompts/speckit.*.prompt.md
```

### Step 2: カスタムプロンプトと Spec Kit プロンプトの違いを確認

```bash
# ワークショップ独自のプロンプト（🏠 マーク）
cat .github/prompts/specify.prompt.md

# Spec Kit 生成のプロンプト（🤖 マーク）
cat .github/prompts/speckit.specify.prompt.md
```

> 💡 **ポイント**: 独自プロンプトはプロジェクト固有のルールを、
> Spec Kit プロンプトは標準的な SDD ワークフローを提供します。
> 両方を併用することで、より精度の高い仕様駆動開発が可能になります。

### Step 3: （発展）自分のプロジェクトへの導入を試す

自分のプロジェクトで Spec Kit を導入する場合の手順:

```bash
# 新規プロジェクトで実行
cd your-project
specify init .
# → AI アシスタント選択で "copilot" を選択

# Git Flow で管理
git checkout -b feature/speckit-init
git add .specify/ .github/agents/ .github/prompts/speckit.*.prompt.md
git commit -m "feat: Spec Kit SDD ワークフローを初期化"
git push origin feature/speckit-init
```

### ✅ 追加演習の完了条件

- [ ] `.specify/` の構成を確認し、各ファイルの役割を理解した
- [ ] カスタムプロンプトと Spec Kit プロンプトの違いを確認した
- [ ] Copilot Chat で `/specify` と `/speckit.specify` の両方を試した
