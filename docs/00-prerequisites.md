# 事前準備ガイド

## 1. アカウントとライセンス

### 必須
- [ ] GitHub Enterprise アカウントが有効であること
- [ ] GitHub Copilot ライセンスが付与されていること

### 確認方法
1. ブラウザで [github.com](https://github.com) にログイン
2. 右上のアイコン → Settings → Copilot で「Active」と表示されること

## 2. 開発環境のセットアップ

### Visual Studio Code
1. [VS Codeダウンロード](https://code.visualstudio.com/) から最新版をインストール
2. 以下の拡張機能をインストール:
   - **GitHub Copilot** (`GitHub.copilot`)
   - **GitHub Copilot Chat** (`GitHub.copilot-chat`)
   - **Extension Pack for Java** (`vscjava.vscode-java-pack`)

### Java開発環境
- **JDK 17以上** がインストールされていること
  ```bash
  java -version
  # java version "17.x.x" 以上が表示されること
  ```
- **Apache Maven 3.8以上** がインストールされていること
  ```bash
  mvn -version
  # Apache Maven 3.8.x 以上が表示されること
  ```

### Node.js（Spec Kit 用）
- **Node.js 18以上** がインストールされていること
  ```bash
  node -version
  # v18.x.x 以上が表示されること
  ```
  > Spec Kit の初期化（`npx @github/specify init`）に必要です

### Git
- **Git** がインストールされていること
  ```bash
  git --version
  ```

## 3. ネットワーク・セキュリティの確認

以下のドメインへのアクセスが許可されていることを確認してください:

| ドメイン | 用途 |
|---------|------|
| `github.com` | GitHub リポジトリ |
| `api.github.com` | GitHub API |
| `copilot-proxy.githubusercontent.com` | Copilot |
| `repo.maven.apache.org` | Maven Central |

### SSL証明書検証テスト
```bash
# GitHub への接続テスト
curl -I https://github.com

# Maven Central への接続テスト
curl -I https://repo.maven.apache.org/maven2/
```

## 4. リポジトリのクローン

ワークショップ当日に使用するリポジトリを事前にクローンしておいてください:

```bash
git clone <ワークショップ用リポジトリURL>
cd github-sdlc-workshop
```

## 5. アプリケーションのビルド確認

```bash
cd app
mvn clean compile
```

エラーなくビルドが完了すれば準備完了です。

## ⚠️ トラブルシューティング

### Copilotが反応しない場合
1. VS Code 左下の Copilot アイコンが有効（✓）になっているか確認
2. VS Code を再起動
3. `Ctrl+Shift+P` → 「GitHub Copilot: Sign In」でサインインし直す

### SSL証明書エラーの場合
社内プロキシのCA証明書が原因の可能性があります。IT部門にお問い合わせください。

### Mavenビルドが失敗する場合
プロキシ設定が必要な場合は `~/.m2/settings.xml` にプロキシ設定を追加してください。
