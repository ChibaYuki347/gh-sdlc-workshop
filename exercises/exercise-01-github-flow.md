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
