# 演習4: リファクタリング演習

**対応セッション**: セッション4 - GitHub Copilotによる実装とリファクタリング
**所要時間**: 約30分

---

## 🎯 演習のゴール

- レガシーコードの問題点を Copilot で特定する
- 段階的なリファクタリングのアプローチを体験する
- リファクタリング前後の品質の違いを実感する

---

## 📋 リファクタリング対象

`app/src/main/java/com/zurich/crm/service/CustomerService.java`

このクラスには以下の設計上の問題があります:
- **God Class**: 顧客管理・保険料計算・レポート・通知など複数の責務
- **マジックナンバー**: ステータスコード（0,1,2,3）、保険料率のハードコーディング
- **入力バリデーション不足**: null チェック、範囲チェックの欠如
- **非効率な検索**: 全件取得してからフィルタリング

---

## 📝 演習手順

### Task 1: 保険料計算の分離（15分）

#### Step 1: Copilot に分析を依頼

```
CustomerService.java の calculatePremium メソッドを分析してください。
以下を整理してください:
1. 現在の問題点
2. リファクタリング方針
3. 作成すべきクラス/インターフェース
```

#### Step 2: リファクタリングを実行

```
calculatePremium メソッドを以下の方針でリファクタリングしてください:

1. PremiumCalculatorService を新規作成
   - 場所: app/src/main/java/com/zurich/crm/service/PremiumCalculatorService.java

2. マジックナンバーを定数に置き換え
   - 基本保険料、年齢係数、喫煙者割増率を定数化
   - 消費税率を定数化

3. 各保険タイプの計算を個別メソッドに分離
   - calculateLifePremium()
   - calculateMedicalPremium()
   - calculateAutoPremium()
   - calculateFirePremium()

4. 入力バリデーション追加
   - policyType が null または未知の場合は IllegalArgumentException
   - age が 0未満または150超の場合は IllegalArgumentException

5. CustomerService の calculatePremium は PremiumCalculatorService に委譲
```

#### Step 3: コンパイル確認

```bash
cd app
mvn compile
```

### Task 2: ステータス管理の改善（10分）

#### Step 1: Enum の作成

```
policyStatus のマジックナンバー（0=申請中, 1=有効, 2=失効, 3=解約）を
Java Enum に変換してください。

1. PolicyStatus enum を作成
   - 場所: app/src/main/java/com/zurich/crm/model/PolicyStatus.java
   - 各ステータスに日本語ラベルと数値コードを持たせる

2. ステータス遷移のバリデーションメソッドを追加
   - 有効な遷移:
     - 申請中(PENDING) → 有効(ACTIVE)
     - 有効(ACTIVE) → 失効(EXPIRED) / 解約(CANCELLED)
   - 無効な遷移:
     - 失効(EXPIRED) → 有効(ACTIVE) は不可
     - 解約(CANCELLED) → いずれのステータスにも変更不可
```

#### Step 2: 既存コードへの適用

```
作成した PolicyStatus enum を既存のコードに適用してください:
1. Customer.java の policyStatus フィールドを int から PolicyStatus に変更
2. CustomerService の changePolicyStatus にバリデーション追加
3. getPolicyStatusText メソッドを enum の機能で置き換え
```

### Task 3: 検索の改善（5分、時間がある場合）

```
CustomerService の searchCustomers メソッドを改善してください:
1. 全件取得ではなく、JPA の @Query アノテーションでDB側で検索
2. CustomerRepository にカスタムクエリを追加
3. LIKE 検索で firstName, lastName, email, policyNumber を検索
```

---

## ✅ 完了条件

- [ ] PremiumCalculatorService.java が作成されている
- [ ] マジックナンバーが定数に置き換えられている
- [ ] PolicyStatus enum が作成されている
- [ ] `mvn compile` が成功する
- [ ] 変更がコミットされている

---

## 📊 ビフォー・アフター

### Before（リファクタリング前）
```java
// マジックナンバーだらけ、テスト困難
if (policyType.equals("LIFE")) {
    basePremium = 5000;
    if (age > 60) {
        basePremium = basePremium * 2.5;
    }
}
```

### After（リファクタリング後）
```java
// 定数化、メソッド分離、テスト容易
private static final double LIFE_BASE_PREMIUM = 5000.0;
private static final double SENIOR_AGE_FACTOR = 2.5;

public double calculateLifePremium(int age, boolean isSmoker) {
    double premium = LIFE_BASE_PREMIUM;
    premium *= getAgeFactor(age);
    if (isSmoker) premium *= SMOKER_SURCHARGE;
    return applyTax(premium);
}
```

> 💡 **ポイント**: リファクタリングの目的は「動作を変えずに構造を改善する」こと。
> Copilot に依頼する際も「既存のAPIの振る舞いは変更しないで」と伝えることが重要です。
