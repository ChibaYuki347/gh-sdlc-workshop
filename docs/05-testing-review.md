# セッション5: PR を中心とした品質ゲート — レビュー・テスト・セキュリティ

**時間**: 14:45 - 16:00（75分）

---

## 🎯 セッションのゴール

- **Pull Request がプラットフォームの品質ゲート**であることを体験する
- Copilot Code Review による自動レビューを体験する
- テスト自動生成 → CI → セキュリティチェック → マージの統合フローを理解する

---

## 📖 アジェンダ

### 1. PR = プラットフォームの品質ゲート（10分）

#### なぜ PR が重要か

```
単体AIツールの場合:
  AIがコード生成 → 開発者が手動でレビュー → 手動でテスト → 手動でデプロイ
  └ 品質チェックのプロセスが属人的

GitHub Platform の場合:
  PR作成
    ↓ 自動: Copilot Code Review（AIがレビューコメント）
    ↓ 自動: GitHub Actions（CI/テスト実行）
    ↓ 自動: Advanced Security（脆弱性・シークレットスキャン）
    ↓ 人間: レビュー・承認
    ↓ マージ（ブランチ保護ルールで品質を強制）
```

> 💡 PR が開かれた瞬間に、**AIレビュー + CI + セキュリティチェック** が自動で走る。
> これが「プラットフォームによる品質の強制」です。

### 2. Copilot Code Review の体験（20分）

#### Step 1: PR を作成する

セッション4のリファクタリング結果を PR にします:

```bash
git push origin feature/team-X-refactoring
```

GitHub で Pull Request を作成:
- **タイトル**: `refactor: 保険料計算の分離とステータス管理の改善`
- **説明**: Copilot Chat に PR の説明文を生成してもらう:

```
プロンプト:
「このブランチの変更内容に基づいて、Pull Request の説明文を日本語で作成してください。
変更の目的、内容、影響範囲を含めてください。」
```

#### Step 2: Copilot をレビュアーに追加

1. PR 画面の **Reviewers** で `Copilot` を追加
2. Copilot Code Review が自動でコード分析を開始
3. 数分後、PR のコード差分にレビューコメントが付く

> 📺 **講師デモ**: 事前に用意した PR で Copilot Code Review のコメントを確認

#### Copilot Code Review の特徴

| 機能 | 説明 |
|------|------|
| **コード品質** | 可読性、設計パターン、重複コードの指摘 |
| **バグ検出** | null ポインタ、オフバイワン、リソースリークの警告 |
| **セキュリティ** | 入力バリデーション不足、インジェクションリスクの指摘 |
| **提案** | 具体的なコード改善案をインラインで提示 |

#### Step 3: レビューコメントを確認・対応

Copilot Code Review のコメントを確認し、重要なものに対応:

```bash
# レビュー指摘を修正
# VS Code で Copilot にコメント対応を依頼:
# 「Copilot Code Review の指摘を修正してください: (...指摘内容...)」

git add .
git commit -m "fix: Code Review指摘の対応"
git push
```

### 3. テストの自動生成と CI（25分）

#### Step 1: Copilot でテストを生成

```
プロンプト:
「PremiumCalculatorService（リファクタリング後）のユニットテストを作成してください。

テストクラスの場所:
app/src/test/java/com/example/crm/service/PremiumCalculatorServiceTest.java

以下を含めてください:
- 各保険タイプの基本計算テスト
- 年齢による料率変動のテスト
- 境界値テスト（年齢 25, 30, 40, 60, 65）
- 入力バリデーションのテスト（不正な保険タイプ、不正な年齢）」
```

#### Step 2: テストを実行

```bash
cd app
mvn test
```

テストが失敗した場合は Copilot にエラーメッセージを貼って修正を依頼。

#### Step 3: テスト結果をコミット → CI で確認

```bash
git add .
git commit -m "test: PremiumCalculatorService のユニットテストを追加"
git push
```

> GitHub Actions が設定されている場合、PR 上で CI の結果が表示されます。
> テストがパスすると PR に ✅ チェックマークが付きます。

### 4. セキュリティチェック（10分）

#### GitHub Advanced Security の概要

| 機能 | 説明 | PR での表示 |
|------|------|------------|
| **Code Scanning (CodeQL)** | コード内のセキュリティ脆弱性を検出 | PR にアラートが表示 |
| **Secret Scanning** | パスワード、APIキーなどの混入を検出 | プッシュ時にブロック |
| **Dependabot** | 依存ライブラリの脆弱性を検出 | 自動で PR を作成 |

#### Copilot でセキュリティレビュー

```
プロンプト:
「CustomerController.java のセキュリティ上の問題点を
OWASP Top 10 の観点から分析してください。
リスクレベル（高/中/低）と修正方法を示してください。」
```

**発見すべき典型的な問題**:
- 入力バリデーション不足（インジェクションリスク）
- エラー情報の過剰公開（スタックトレースの露出）
- 認証・認可の欠如

### 5. マージ — 品質ゲートを通過（10分）

#### PR 上の品質チェック結果を確認

```
PR の品質ゲート:
  ✅ Copilot Code Review  → レビューコメント対応済み
  ✅ CI / テスト          → 全テストパス
  ✅ セキュリティ          → 重大な脆弱性なし
  ✅ 人間のレビュー        → チームメンバーが承認
```

#### マージ

全てのチェックがパスしたら、PR をマージ:

1. **Squash and merge** を選択（コミット履歴をきれいに）
2. マージ完了 → Issue が自動クローズ（PR 説明に `Closes #XX` がある場合）

> 💡 **トレーサビリティの完成**:
> ```
> 仕様（.specify/spec.md）
>   → Issue #XX（要件の単位）
>     → Branch（作業場所）
>       → PR #YY（変更セット）
>         → Copilot Review + CI + Security（品質チェック）
>           → Merge（本番統合）
> ```
> この一連のフローが全て GitHub 上で追跡可能。監査・コンプライアンス対応の基盤。

---

## ✅ 演習

→ [演習5: テスト生成演習](../exercises/exercise-05-testing.md)
→ [演習6: コードレビュー演習](../exercises/exercise-06-code-review.md)

---

## 💡 講師向けメモ

- **Copilot Code Review** がこのセッションの最大の見せ場。事前にデモ用 PR を準備
- 時間が足りない場合はセキュリティセクションを講師デモに切り替え
- テスト生成は参加者のスキルレベルに合わせて難易度調整
- 「PR = 品質ゲート」のメッセージを一貫して伝える
- ブランチ保護ルール（必須レビュー、CI パス必須）のデモも効果的
