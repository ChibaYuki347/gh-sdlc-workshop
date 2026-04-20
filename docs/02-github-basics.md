# セッション2: 環境セットアップと GitHub Flow の基礎

**時間**: 09:45 - 10:30（45分）

---

## 🎯 セッションのゴール

- 開発環境が正しくセットアップされていることを確認する
- GitHub の基本操作（Issue、Branch、Pull Request）を体験する
- Spec Kit の初期化を行い、午前のメインセッションに備える

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
□ Node.js がインストールされている
□ リポジトリがクローンされている
```

#### アプリケーションのビルド確認

```bash
cd app
mvn clean compile
```

> **期待結果**: `BUILD SUCCESS` と表示される

### 2. GitHub Flow の基本操作（20分）

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

# 変更をコミット・プッシュ
git add .
git commit -m "チームX: 環境確認完了"
git push origin feature/team-X-setup
```

GitHub で Pull Request を作成し、他チームのメンバーを Reviewer に追加してください。

> 💡 この「Issue → Branch → PR → Review → Merge」がプラットフォーム開発の基本パターンです。
> セッション3では、このフローを **Coding Agent が自動で実行** します。

### 3. Spec Kit の初期化と Copilot Chat 動作確認（15分）

#### Spec Kit の初期化

```bash
# プロジェクトルートに戻る
cd ..

# Spec Kit の初期化
npx @github/specify init
```

初期化後の構成を確認:
```
.specify/              ← 仕様・計画・タスクの格納先
.github/
├── prompts/           ← Copilot Chat スラッシュコマンド
└── copilot-instructions.md  ← プロジェクトのコンテキスト
```

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
- Spec Kit 初期化が失敗する場合は手動で `.specify/` を作成して進行
- 「この GitHub Flow が自動化の基盤」というメッセージを繰り返す
- 時間が押している場合は PR 作成まで進めなくてもOK（Issue + Branch まで）
