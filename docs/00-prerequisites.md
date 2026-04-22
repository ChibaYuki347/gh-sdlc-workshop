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

### Python + uv（Spec Kit 使用時のみ・オプション）
- **Python 3.11以上** がインストールされていること
  ```bash
  python3 --version
  # Python 3.11.x 以上が表示されること
  ```
- **uv**（Python パッケージマネージャ）がインストールされていること
  ```bash
  uv --version
  ```
  > Spec Kit を使用する場合に必要です（`uv tool install specify-cli`）。
  > ネイティブ機能のみで SDD を行う場合は不要です。

  **uv のインストール方法（環境に応じて選択）**:
  ```bash
  # 方法1: 公式インストーラ（推奨）
  # Windows (PowerShell)
  powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"

  # 方法2: pip 経由（方法1がブロックされる場合）
  pip install uv

  # 方法3: インストール済みの Python 環境がある場合
  python -m pip install uv
  ```

  > ⚠️ 社内ネットワークでインストールがブロックされる場合は、IT部門にご相談ください。

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

## 5. アプリケーションのビルドと起動確認

```bash
cd app
mvn clean compile
```

エラーなくビルドが完了したら、アプリケーションを起動します:

```bash
mvn spring-boot:run
```

起動後、ブラウザで http://localhost:8080/ にアクセスしてダッシュボードが表示されれば準備完了です。

| URL | ページ | 確認ポイント |
|-----|--------|-------------|
| http://localhost:8080/ | ダッシュボード | 顧客数・契約統計が表示される |
| http://localhost:8080/customers | 顧客一覧 | 7件の顧客データが表示される |
| http://localhost:8080/premium-calculator | 保険料計算 | 種別・年齢を入力して計算できる |

確認できたら `Ctrl+C` でアプリを停止してください。

## ⚠️ トラブルシューティング

### Copilotが反応しない場合
1. VS Code 左下の Copilot アイコンが有効（✓）になっているか確認
2. VS Code を再起動
3. `Ctrl+Shift+P` → 「GitHub Copilot: Sign In」でサインインし直す

### SSL証明書エラーの場合
社内プロキシのCA証明書が原因の可能性があります。IT部門にお問い合わせください。

### Mavenビルドが失敗する場合
プロキシ設定が必要な場合は `~/.m2/settings.xml` にプロキシ設定を追加してください。
