# セッション2: 環境セットアップとGitHubプラットフォームの基礎

**時間**: 09:45 - 10:30（45分）

---

## 🎯 セッションのゴール

- 開発環境が正しくセットアップされていることを確認する
- GitHub の基本操作（Issue、Branch、Pull Request）を体験する
- チームでの GitHub Flow を理解する

---

## 📖 アジェンダ

### 1. 環境確認（15分）

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

### 2. GitHub の基本操作（15分）

#### Issue の作成

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

#### Branch の作成

```bash
# featureブランチを作成
git checkout -b feature/team-X-setup

# 変更をコミット
git add .
git commit -m "チームX: 環境確認完了"

# プッシュ
git push origin feature/team-X-setup
```

#### Pull Request の作成

1. GitHub でリポジトリを開く
2. **Pull requests** タブ → **New pull request**
3. `feature/team-X-setup` → `main` を選択
4. タイトルと説明を記入して作成

### 3. GitHub Flow の理解（10分）

```
main ブランチ（本番）
  │
  ├── feature/add-search ← ブランチを作成
  │     │
  │     ├── commit 1: 検索機能の実装
  │     ├── commit 2: テストの追加
  │     └── commit 3: レビュー指摘の修正
  │           │
  │           └── Pull Request → レビュー → マージ
  │
  ├── feature/fix-bug-123 ← 別のブランチ
  │     └── ...
  │
  └── main に統合
```

#### GitHub Flow のポイント

| ステップ | 説明 |
|---------|------|
| 1. Branch | mainから機能ブランチを作成 |
| 2. Commit | 小さな単位で変更をコミット |
| 3. Pull Request | 変更をレビューに出す |
| 4. Review | チームメンバーがコードレビュー |
| 5. Merge | 承認後、mainにマージ |

### 4. Copilot Chat の動作確認（5分）

VS Code で Copilot Chat を開き、以下を試してください:

```
質問例:
「このプロジェクトの構成を説明してください」
「CustomerService.java の主な機能を教えてください」
```

---

## ✅ 演習

→ [演習1: GitHub Flow演習](../exercises/exercise-01-github-flow.md)

---

## 💡 講師向けメモ

- 環境トラブルが多い想定。メンターは各チームを巡回
- Git 操作に不慣れな参加者にはGUIクライアントでの操作も案内
- Copilot Chat が動作しない場合のトラブルシュートを準備
