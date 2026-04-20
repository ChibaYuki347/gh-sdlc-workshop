# セッション4: GitHub Copilotによる実装とリファクタリング

**時間**: 13:00 - 14:30（90分）

---

## 🎯 セッションのゴール

- Copilot を使って仕様書からコードを生成する体験をする
- レガシーコードの理解と段階的なリファクタリングを体験する
- AI エージェントモードでの協働開発を体験する

---

## 📖 アジェンダ

### 1. Copilot によるコード生成（30分）

#### 午前中に作成した仕様書からの実装

午前中に Spec Kit で作成した仕様書（`.specify/spec.md`）を元に、Copilot を使って実装します。

#### Step 1: Copilot Agent Mode で実装を依頼する

VS Code の Copilot Chat で Agent Mode を使用:

```
プロンプト:
「.specify/spec.md の仕様に基づいて、契約更新通知機能を実装してください。

以下のファイルを作成・修正してください:
1. RenewalNotificationService.java - 更新通知のビジネスロジック
2. RenewalNotificationController.java - REST APIエンドポイント
3. 既存の Customer/Policy モデルとの連携

Spring Boot の既存コード構成に従ってください。」
```

#### Step 2: 生成されたコードを確認する

Copilot が生成したコードに対して:

```
確認ポイント:
□ 仕様書の要件を満たしているか
□ 既存コードとの整合性はあるか
□ エラーハンドリングは適切か
□ エッジケースは考慮されているか
```

#### Step 3: 対話的に修正する

```
プロンプト例:
「このコードに以下の改善をしてください:
- 通知対象日数をパラメータ化（デフォルト30日）
- 既に通知済みの顧客を除外するロジック
- ページネーション対応」
```

### 2. レガシーコードのリファクタリング（40分）

#### CustomerService の段階的改善

現在の `CustomerService.java` には多くの設計上の問題があります。Copilot と協力して段階的にリファクタリングします。

#### Phase 1: コード理解（10分）

```
プロンプト:
「CustomerService.java を分析して、以下を整理してください:
1. このクラスが持つ全ての責務の一覧
2. 各メソッドの問題点
3. リファクタリングの優先順位（影響度・改善効果）」
```

#### Phase 2: 保険料計算ロジックの分離（15分）

```
プロンプト:
「calculatePremium メソッドをリファクタリングしてください:
1. PremiumCalculatorService として独立したサービスクラスに分離
2. マジックナンバーを定数または設定値に置き換え
3. ポリシータイプごとの計算ロジックを Strategy パターンで実装
4. 入力バリデーションを追加」
```

**期待される成果物**:
```
新規作成:
  - PremiumCalculatorService.java（計算サービス）
  - PremiumStrategy.java（計算戦略インターフェース）
  - LifePremiumStrategy.java
  - MedicalPremiumStrategy.java
  - AutoPremiumStrategy.java
  - FirePremiumStrategy.java

修正:
  - CustomerService.java（calculatePremium の委譲）
  - CustomerController.java（新サービスの利用）
```

#### Phase 3: ステータス管理の改善（15分）

```
プロンプト:
「policyStatus のマジックナンバー（0,1,2,3）を改善してください:
1. PolicyStatus enum を作成
2. 不正なステータス遷移を防ぐバリデーション追加
   - 例: 失効(2) → 有効(1) への直接遷移は不可
3. Customer エンティティの policyStatus を enum に変更」
```

### 3. AI エージェントとの協働のコツ（20分）

#### 効果的なプロンプトの書き方

| パターン | 良い例 | 悪い例 |
|---------|-------|-------|
| **具体的な指示** | 「CustomerServiceのsearchCustomersメソッドをJPA Specificationを使って書き直して」 | 「検索を改善して」 |
| **コンテキスト提供** | 「Spring Boot 3.2 + JPA環境で、既存のCustomerRepositoryを拡張して」 | 「検索APIを作って」 |
| **段階的な指示** | 「まずインターフェースを定義し、次に実装クラスを作成して」 | 「全部作って」 |
| **制約の明示** | 「既存のAPIの互換性を保ちながら内部実装を変更して」 | 「リファクタリングして」 |

#### やってはいけないこと

- 生成されたコードを理解せずにそのまま採用する
- 一度に大量の変更を依頼する（段階的に進める）
- エラーメッセージを無視する

---

## ✅ 演習

→ [演習3: Copilot実装演習](../exercises/exercise-03-copilot-impl.md)
→ [演習4: リファクタリング演習](../exercises/exercise-04-refactoring.md)

---

## 💡 講師向けメモ

- リファクタリングは全てを完了する必要はない。プロセスを体験することが目的
- Copilot の出力は毎回異なる。参加者ごとに結果が違うことを事前に説明
- エラーが出た場合は「Copilot にエラーメッセージを貼って修正を依頼する」デモが効果的
- ビジネスメンバーにはリファクタリング前後のコードの可読性の違いに注目してもらう
