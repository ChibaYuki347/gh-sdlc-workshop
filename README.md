# GitHub Enterprise + Copilot 開発ライフサイクル体験ワークショップ

## 📋 ワークショップ概要

本ワークショップでは、GitHub Enterprise と GitHub Copilot を活用した次世代のソフトウェア開発ライフサイクル（SDLC）全体を体験します。要件定義から実装、テスト、セキュアなコラボレーションまで、AI エージェントとの協働による開発の進め方を実践的なハンズオンを通じて学びます。

### ハンズオンシナリオ

**既存改修シナリオ（メイン）**：擬似保険CRMアプリケーション（Java/Spring Boot）を題材に、GitHub Platform の統合開発ループ（仕様策定 → Issue → Coding Agent → PR → Copilot Review → Merge）を体験します。

## 🕐 タイムテーブル

| 時間 | セッション | 内容 |
|------|-----------|------|
| 09:00 - 09:45 | [1. キックオフ](docs/01-kickoff.md) | GitHub Platform が実現する AI 開発ライフサイクル |
| 09:45 - 10:30 | [2. 環境セットアップ](docs/02-github-basics.md) | GitHub Flow の基礎と Spec Kit 初期化 |
| 10:30 - 10:45 | 休憩 | |
| 10:45 - 12:00 | [3. 仕様駆動開発（SDD）](docs/03-sdd-requirements.md) | ネイティブ機能 + Coding Agent で仕様 → Issue → 自動実装 → PR |
| 12:00 - 13:00 | 昼休憩 | |
| 13:00 - 14:30 | [4. IDE × Copilot 協働開発](docs/04-implementation.md) | レガシーコード理解・リファクタリング・人間とAIの役割分担 |
| 14:30 - 14:45 | 休憩 | |
| 14:45 - 16:00 | [5. PR を中心とした品質ゲート](docs/05-testing-review.md) | Copilot Code Review・テスト自動生成・セキュリティチェック・マージ |
| 16:00 - 17:00 | [6. ラップアップ](docs/06-wrapup.md) | 振り返り・導入ロードマップ・質疑応答 |

## 🛠️ 事前準備

詳細は [事前準備ガイド](docs/00-prerequisites.md) を参照してください。

### 必要なアカウント・ライセンス
- GitHub Enterprise アカウント
- GitHub Copilot ライセンス

### 開発環境
- Visual Studio Code（最新版）
- VS Code 拡張機能：GitHub Copilot / GitHub Copilot Chat
- Java Development Kit (JDK) 17以上
- Apache Maven 3.8以上
- Python 3.11以上 + uv（Spec Kit 使用時のみ・オプション）

### ネットワーク要件
- GitHub（github.com）へのアクセス
- Maven Central リポジトリへのアクセス
- SSL証明書検証が正常に動作すること

## 📁 リポジトリ構成

```
├── README.md                 # 本ファイル
├── .github/
│   ├── copilot-instructions.md  # Copilot プロジェクト設定
│   └── prompts/                 # 再利用可能な Prompt Files（SDD用）
├── docs/                     # セッション資料
├── exercises/                # ハンズオン演習手順
├── specs/                    # 仕様書テンプレート・成果物格納先
└── app/                      # 擬似CRMアプリケーション（Java/Spring Boot）
```

## 📝 ハンズオン演習一覧

| # | 演習 | 対応セッション |
|---|------|---------------|
| 1 | [GitHub Flow演習](exercises/exercise-01-github-flow.md) | セッション2 |
| 2 | [仕様駆動開発演習](exercises/exercise-02-spec-driven.md) | セッション3 |
| 3 | [Copilot実装演習](exercises/exercise-03-copilot-impl.md) | セッション4 |
| 4 | [リファクタリング演習](exercises/exercise-04-refactoring.md) | セッション4 |
| 5 | [テスト生成演習](exercises/exercise-05-testing.md) | セッション5 |
| 6 | [コードレビュー演習](exercises/exercise-06-code-review.md) | セッション5 |
